package com.example.besafe;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;

import java.util.Objects;

public class BackgroundService extends Service{
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    String[] contacts;
    String Emergency_msg;
    MediaPlayer mp;

    FusedLocationProviderClient fusedLocationClient;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("Notify" , "myNotification" ,NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager =  getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }

        contacts = intent.getStringArrayExtra("Contact");
        Emergency_msg = intent.getStringExtra("Msg") ;


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(BackgroundService.this);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        SensorManager mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);


        return START_STICKY;
    }


  private  void addNotification(){

      Intent intent = new Intent(this, exit.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
      PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

      NotificationCompat.Builder builder = new NotificationCompat.Builder(BackgroundService.this , "Notify");
      builder.setContentTitle("ALERT 💀🚨");
      builder.setContentText("Sending Emergency Message...! Tap to cancel ❌");
      builder.setAutoCancel(true);
      builder.setContentIntent(pendingIntent);
      builder.setSmallIcon(R.drawable.icon);
      builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

      mp= MediaPlayer.create(BackgroundService.this, R.raw.vibrate);
      mp.start();
      NotificationManagerCompat notificationManager = NotificationManagerCompat.from(BackgroundService.this);
      notificationManager.notify(1 ,builder.build());


  }



    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if (mAccel > 30) {


                Toast.makeText(BackgroundService.this, "Shake event detected", Toast.LENGTH_SHORT).show();



                if (ActivityCompat.checkSelfPermission(BackgroundService.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(BackgroundService.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(BackgroundService.this, "Give Permission From Setting", Toast.LENGTH_SHORT).show();

                }else{
                    fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, new CancellationToken() {
                                @Override
                                public boolean isCancellationRequested() {
                                    return false;
                                }

                                @NonNull
                                @Override
                                public CancellationToken onCanceledRequested(
                                        @NonNull OnTokenCanceledListener
                                                onTokenCanceledListener) {
                                    return null;
                                }
                            })
                            .addOnSuccessListener( new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    addNotification();
                                    if (location == null) {
                                        Toast.makeText(BackgroundService.this, "Unable To Fetch Location", Toast.LENGTH_SHORT).show();
                                    } else  {

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                SmsManager smsManager = SmsManager.getDefault();
                                                mp.stop();
                                                Toast.makeText(BackgroundService.this, "Sos Message send", Toast.LENGTH_SHORT).show();
                                                NotificationManager notificationManager =
                                                        (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                                                notificationManager.cancel(1);


                                                String message

                                                        = "Hey, I am in DANGER, i need help. Please urgently reach me out. Here are my coordinates.\n "
//                                                    + Emergency_msg
                                                        + " http://maps.google.com/?q="
                                                        + location.getLatitude() + ","
                                                        + location.getLongitude();
                                                for (String num : contacts) {
                                                    smsManager.sendTextMessage(num, null,
                                                            message, null, null);

                                                }


                                            }
                                        }, 10000);



                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(BackgroundService.this, "Sos Not Send", Toast.LENGTH_SHORT).show();
                                }
                            });
                } }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
}
