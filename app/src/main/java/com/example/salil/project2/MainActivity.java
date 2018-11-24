package com.example.salil.project2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.CAMERA},9);
            }
        }
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//
//        }
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//
//        }

        Button front = (Button) findViewById(R.id.Front);
        Button back = (Button) findViewById(R.id.Back);

        // Capture button clicks
//        normal.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View arg0) {
//
//                // Start NewActivity.class
//                Intent myIntent = new Intent(MainActivity.this,
//                        Main2Activity.class);
//                myIntent.putExtra("Choice", 1);
//                startActivity(myIntent);
//            }
//        });
//
//        brighness.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View arg0) {
//
//                // Start NewActivity.class
//                Intent myIntent = new Intent(MainActivity.this,
//                        Main2Activity.class);
//                myIntent.putExtra("Choice", 2);
//                startActivity(myIntent);
//            }
//        });
//
//        keypoints.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View arg0) {
//
//                // Start NewActivity.class
//                Intent myIntent = new Intent(MainActivity.this,
//                        Main2Activity.class);
//                myIntent.putExtra("Choice", 3);
//                startActivity(myIntent);
//            }
//        });
//
//        canny.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View arg0) {
//
//                // Start NewActivity.class
//                Intent myIntent = new Intent(MainActivity.this,
//                        Main2Activity.class);
//                myIntent.putExtra("Choice", 4);
//                startActivity(myIntent);
//            }
//        });
//
//        activity3.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View arg0) {
//
//                // Start NewActivity.class
//                Intent myIntent = new Intent(MainActivity.this,
//                        Main3Activity.class);
//                myIntent.putExtra("Choice", 5);
//                startActivity(myIntent);
//            }
//        });

        front.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(MainActivity.this,
                        Main2Activity.class);
                myIntent.putExtra("camChoice", 1);
                startActivity(myIntent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(MainActivity.this,
                        Main2Activity.class);
                myIntent.putExtra("camChoice", 0);
                startActivity(myIntent);
            }
        });

    }
}
