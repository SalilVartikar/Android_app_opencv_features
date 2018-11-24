package com.example.salil.project2;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Main4Activity extends Main2Activity {

    ImageView iv1;
    ImageView iv2;
    TextView tv1;
    TextView tv2;
    public int compare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        iv1 = (ImageView) findViewById(R.id.img1);
        iv2 = (ImageView) findViewById(R.id.img2);
        tv1 = (TextView) findViewById(R.id.text1);
        tv2 = (TextView) findViewById(R.id.text2);

        Button nCanny = (Button) findViewById(R.id.normalCanny);
        Button nSobel = (Button) findViewById(R.id.normalSobel);
        Button edge = (Button) findViewById(R.id.cannySobel);

        nCanny.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                compare = 1;
                displayImages();
            }
        });

        nSobel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                compare = 2;
                displayImages();
            }
        });

        edge.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                compare = 3;
                displayImages();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void displayImages() {
        Mat m = Imgcodecs.imread("/storage/emulated/0/Images/image.jpg");
        Imgproc.cvtColor(m, m, Imgproc.COLOR_BGR2RGB);


        if(compare == 1) {
            Bitmap bm = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(m, bm);

            iv1.setImageBitmap(bm);
            tv1.setText("Normal");

            Mat mCanny = canny(m);
            Bitmap bmCanny = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mCanny, bmCanny);
            iv2.setImageBitmap(bmCanny);
            tv2.setText("Canny");
        }

        else if(compare == 2) {
            Bitmap bm = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(m, bm);

            iv1.setImageBitmap(bm);
            tv1.setText("Normal");


            Mat sobel = sobel(m);
            Bitmap bmSobel = Bitmap.createBitmap(sobel.cols(), sobel.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(sobel, bmSobel);
            iv2.setImageBitmap(bmSobel);
            tv2.setText("Sobel");
        }

        else if(compare == 3) {
            Mat mCanny = canny(m);
            Bitmap bmCanny = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mCanny, bmCanny);
            iv1.setImageBitmap(bmCanny);
            tv1.setText("Canny");

            Mat sobel = sobel(m);
            Bitmap bmSobel = Bitmap.createBitmap(sobel.cols(), sobel.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(sobel, bmSobel);
            iv2.setImageBitmap(bmSobel);
            tv2.setText("Sobel");
        }
    }
}
