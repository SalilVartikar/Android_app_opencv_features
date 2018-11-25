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

        /*Request camera permission*/
        //if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.CAMERA},9);
            }
        //}

        /*Declaring and initializing buttons*/
        Button front = (Button) findViewById(R.id.Front);
        Button back = (Button) findViewById(R.id.Back);

        /*Use front camera*/
        front.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(MainActivity.this,
                        Main2Activity.class);
                myIntent.putExtra("camChoice", 1);
                startActivity(myIntent);
            }
        });

        /*Use back camera*/
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
