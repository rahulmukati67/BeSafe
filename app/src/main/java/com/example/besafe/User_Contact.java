package com.example.besafe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class User_Contact extends AppCompatActivity {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_contact);
        listView = findViewById(R.id.listView);
        Intent intent = getIntent();
        String[] Contacts = intent.getStringArrayExtra("Contact");
        ArrayAdapter<String> list = new ArrayAdapter<>(this, com.airbnb.lottie.R.layout.support_simple_spinner_dropdown_item,Contacts);
        listView.setAdapter(list);

    }
}