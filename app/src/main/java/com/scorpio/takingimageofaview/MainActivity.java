package com.scorpio.takingimageofaview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ImageView imgHello,imgApril;
    Button scButton;
    LinearLayout mainLayout;
    File imgFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLayout = (LinearLayout)findViewById(R.id.main_layout);
        imgHello = (ImageView)findViewById(R.id.imgHello);
        imgApril = (ImageView)findViewById(R.id.imgApril);
        scButton = (Button)findViewById(R.id.bScreen);
        scButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //screenShot(imgHello);
                screenShot(imgApril);
                //screenShot(scButton);
                //screenShot(mainLayout);

            }
        });

    }

    public void screenShot(View v){
        saveImage(v);

    }

    public Bitmap createViewBitmap(View v)
    {
        Bitmap viewBitmap = Bitmap.createBitmap(v.getWidth(),v.getHeight(),Bitmap.Config.RGB_565);
        Canvas viewCanvas = new Canvas(viewBitmap);
        Drawable backgroundDrawable = v.getBackground();

        if(backgroundDrawable!=null){
            backgroundDrawable.draw(viewCanvas);
        }
        else{
            viewCanvas.drawColor(Color.GREEN);
            v.draw(viewCanvas);
        }
        return viewBitmap;
    }

    public void saveImage(View v )
    {
        Bitmap b = createViewBitmap(v);
        String fileStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        OutputStream outputStream = null;
        try{
            imgFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),fileStamp+".png");
            Log.d("IMG_PATH",imgFile.getAbsolutePath());
            outputStream = new FileOutputStream(imgFile);
            b.compress(Bitmap.CompressFormat.PNG,40,outputStream);
            outputStream.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

}

