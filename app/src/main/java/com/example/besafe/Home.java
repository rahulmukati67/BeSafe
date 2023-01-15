package com.example.besafe;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Home extends AppCompatActivity {
    CardView Register_Number, Emergency_msg;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Register_Number = findViewById(R.id.number);
        Emergency_msg = findViewById(R.id.Emergency_msg);

        //   ******************* permission *********************** ///

        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                    Boolean SMS_PERMISSION = result.getOrDefault(
                            Manifest.permission.SEND_SMS, false);

                            if (fineLocationGranted != null && fineLocationGranted) {
                                // Precise location access granted.
                            }
                            else {
                                // No location access granted.
                                Toast.makeText(this, "NO PERMISSION GRANTED", Toast.LENGTH_SHORT).show();
                            }
                        }
                );

        locationPermissionRequest.launch(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.SEND_SMS
        });

        //   ******************* permission *********************** ///

        // ******************** notification *********************//

           Intent intent = getIntent();




        // ******************** notification *********************//


        Register_Number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Register_Number.class);
                startActivity(intent);
            }
        });
        Emergency_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Emergency_msg.class);
                startActivity(intent);

            }
        });


    }
}