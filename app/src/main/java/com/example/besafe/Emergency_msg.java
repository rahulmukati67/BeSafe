package com.example.besafe;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class Emergency_msg extends AppCompatActivity {
    EditText emergency_msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_msg);
        emergency_msg = findViewById(R.id.emergency_msg);
        emergency_msg.setText("Help I am in danger , My current LOCATION is ⬇ ");
        findViewById(R.id.updateMessage).setOnClickListener(view -> {
            String Msg = emergency_msg.getText().toString();
            SharedPreferences sharedPreferences =  getSharedPreferences("EmergencyMessage" , MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString( "Message",Msg);
            edit.apply();

            Toast.makeText(Emergency_msg.this, "Message Saved Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Emergency_msg.this , Home.class);
            startActivity(intent);



        Intent intent1 = new Intent(Emergency_msg.this , Home.class);
        intent.putExtra("Emergency_msg" , String.valueOf(emergency_msg));
        startActivity(intent1);
        finish();
        });
        SharedPreferences sharedPreferences = getSharedPreferences("EmergencyMessage", MODE_PRIVATE);
        String s1 = sharedPreferences.getString("Message","");
        emergency_msg.setText(s1);

    }
}