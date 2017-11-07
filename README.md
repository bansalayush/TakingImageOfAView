# TakingImageOfAView

If you want to take image of a particular View then follow this code

```
//Define a bitmap with height and width of the View
Bitmap viewBitmap = Bitmap.createBitmap(v.getWidth(),v.getHeight(),Bitmap.Config.RGB_565);

Canvas viewCanvas = new Canvas(viewBitmap);

//get background of canvas
Drawable backgroundDrawable = v.getBackground();

if(backgroundDrawable!=null){
backgroundDrawable.draw(viewCanvas);//draw the background on canvas;
}
else{
viewCanvas.drawColor(Color.GREEN);
//draw on canvas
v.draw(viewCanvas) 
}

//write the above generated bitmap  to a file
String fileStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        OutputStream outputStream = null;
        try{
            imgFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),fileStamp+".png");
            outputStream = new FileOutputStream(imgFile);
            b.compress(Bitmap.CompressFormat.PNG,40,outputStream);
            outputStream.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
```

**Don't forget to add this permission**
```
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```
This is very useful in cases like signature pads, where the user draws something and then wants to save an image of it.
