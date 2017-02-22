package com.scorpio.takingimageofaview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ImageView imgHello,imgApril;
    Button scButton,mdButton;
    LinearLayout mainLayout;
    File imgFile;
    MediaProjectionManager mediaProjectionManager;
    MediaProjection mediaProjection;
    static final int REQUEST_SCREENSHOT = 7077;
    int DISPLAY_WIDTH;
    int DISPLAY_HEIGHT;
    public int disp_mets;
    Image img;
    ImageReader imageReader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaProjectionManager = (MediaProjectionManager)getSystemService(MEDIA_PROJECTION_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        disp_mets = displayMetrics.densityDpi;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        DISPLAY_WIDTH = size.x;
        DISPLAY_HEIGHT = size.y;

        mainLayout = (LinearLayout)findViewById(R.id.main_layout);
        imgHello = (ImageView)findViewById(R.id.imgHello);
        imgApril = (ImageView)findViewById(R.id.imgApril);

        scButton = (Button)findViewById(R.id.bScreen);
        scButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //screenShot(imgHello);
                //screenShot(imgApril);
                //screenShot(scButton);
                screenShot(mainLayout);

            }
        });

        mdButton = (Button)findViewById(R.id.bMediaScreen);
        mdButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(),REQUEST_SCREENSHOT);

            }
        });
        /*mdButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int i = motionEvent.getAction();
                if(i==MotionEvent.ACTION_DOWN)
                    System.out.println("ACTION DOWN");
                if(i==MotionEvent.ACTION_UP)
                    System.out.println("ATION UP");
                return false;
            }
        });
*/

    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int flags = DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY | DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC;

        mediaProjection = mediaProjectionManager.getMediaProjection(resultCode,data);
        ImageReader.OnImageAvailableListener imageAvailableListener = new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader imgReader) {
                imageReader = imgReader;
                img = imageReader.acquireLatestImage();
                if(img==null){
                    System.out.println("NULL");
                }

            }
        };
        imageReader.setOnImageAvailableListener(imageAvailableListener,null);
        mediaProjection.createVirtualDisplay("myVirtualDisplay",DISPLAY_WIDTH,DISPLAY_HEIGHT,disp_mets, flags,imageReader.getSurface(),null,null);


        /*final Image.Plane[] planes = img.getPlanes();
        final ByteBuffer buffer = planes[0].getBuffer();
        int offset = 0;
        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * DISPLAY_WIDTH;
// create bitmap
        Bitmap bmp = Bitmap.createBitmap(DISPLAY_WIDTH+rowPadding/pixelStride, DISPLAY_HEIGHT, Bitmap.Config.RGB_565);
        bmp.copyPixelsFromBuffer(buffer);
        img.close();
        System.out.println(bmp.toString());*/
    }

    /***CAPTURING SCREENSHOT OF A VIEW***/
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

