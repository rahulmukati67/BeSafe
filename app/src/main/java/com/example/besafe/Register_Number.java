package com.example.besafe;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class Register_Number extends AppCompatActivity {
    TextView number;
//    ArrayList<String> num;
    TextView emergency_msg;
    String[] num  ;
    String msg ;


    int i = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_number);
        number = findViewById(R.id.number);
        emergency_msg = findViewById(R.id.emergency_msg);
         num = new String[3];








        // *******************************  Function to Register Number Form the user ***************************************** //

        findViewById(R.id.add_number).setOnClickListener(new View.OnClickListener() {

         @Override
         public void onClick(View view) {
             if(i <3 && number.getText().toString().length() == 10) {
                 num[i] =number.getText().toString();
                 number.setText("");
                 Toast.makeText(Register_Number.this, "Number Added Successfully ✔", Toast.LENGTH_SHORT).show();
                 i++;
             }else{
                 if(number.getText().toString().length()<10){
                     Toast.makeText(Register_Number.this, "Enter Valid Number", Toast.LENGTH_SHORT).show();
                 }else if(i>=3){
                     Toast.makeText(Register_Number.this, "Already 3 number Registered ✔", Toast.LENGTH_SHORT).show();
                 }
             }
//             if(i==2) {
                 Intent intent1 = getIntent();
                 msg = intent1.getStringExtra("Emergency_msg");
                 Intent intent = new Intent(Register_Number.this, BackgroundService.class);
                 intent.putExtra("Contact", num);
                 intent.putExtra("Msg", String.valueOf(emergency_msg));
                 startService(intent);
//             }

         }
     });

        // ********************************  Function to View Registered Number Form the user ***********************************  //

      findViewById(R.id.view_number).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              if ( i<3) {
                  Toast.makeText(Register_Number.this, "Please Register 3 Number First", Toast.LENGTH_SHORT).show();
              } else{
                  Intent intent = new Intent(Register_Number.this, User_Contact.class);
                  intent.putExtra("Contact", num);
                  startActivity(intent);
              }
          }
      });

    }


    private void addNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("Notifications Example")
                        .setContentText("This is a test notification");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }


}