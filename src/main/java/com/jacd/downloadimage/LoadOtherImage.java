package com.jacd.downloadimage;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.InputStream;

/**
  Manifest add  <uses-permission android:name="android.permission.INTERNET"/>
  copy class: interface GetImageCompleted
 copy class: ImageUtil
 instance
     LoadImage loadImage = new LoadImage(this, url_string);
     loadImage.setOnImageComplete(response -> {
        //this your code, note: response is Base64_String
     });
 **/

public class LoadOtherImage extends AsyncTask<String, Void, Bitmap> {

    private final String TAG = "LoadImage";
    private boolean hasImageView;
    private String urlLink;
    private Context context;
    private ImageView imageView;
    private OnImageTaskCompleted imageCompleted;

    public LoadOtherImage(Context context, ImageView imageResult, String url) {
        this.context = context;
        this.imageView = imageResult;
        this.hasImageView = true;
        this.urlLink = url;
        execute(urlLink);
    }

    public LoadOtherImage(Context context, String url) {
        this.context = context;
        this.urlLink = url;
        this.hasImageView = false;
        execute(urlLink);
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String urlLink = strings[0];
        Bitmap bitmap = null;

        try {
            InputStream inputStream = new java.net.URL(urlLink).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(hasImageView)
            imageView.setImageBitmap(bitmap);
        else {
            try {
                String response = UtilsImage.convert(bitmap);
                imageCompleted.OnDrawableTaskCompleted(response);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "Error en Respuesta del Servidor...", Toast.LENGTH_SHORT).show();
            }
            //Log.w(TAG,""+response);
        }
    }


    public OnImageTaskCompleted getTaskComplete() {
        return imageCompleted;
    }

    public void setTaskComplete(OnImageTaskCompleted taskComplete) {
        this.imageCompleted = taskComplete;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        Log.e("getBitmap", "getBitmap: 1");
        return bitmap;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static Bitmap getBitmap(Context context, int drawableId) {
        Log.e("getBitmapDrawable", "getBitmap: 2");
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (drawable instanceof BitmapDrawable) {
            return BitmapFactory.decodeResource(context.getResources(), drawableId);
        } else if (drawable instanceof VectorDrawable) {
            return getBitmap((VectorDrawable) drawable);
        } else {
            throw new IllegalArgumentException("unsupported drawable type");
        }
    }

// notificationss
    public void notifSimple(Context context, String CHANNEL_ID, String title, String content, String image, int smallIcon){
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(smallIcon)
                .setContentTitle(""+title)
                .setContentText(""+content)
                .setSound(sound)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);

    }

    public static void notifSetImage(Context context, String CHANNEL_ID, String title, String content, Bitmap myBitmap, int smallIcon){
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(smallIcon)
                .setContentTitle(""+title)
                .setContentText(""+content)
                .setSound(sound)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(myBitmap))
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);

    }

}


