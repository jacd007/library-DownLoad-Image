package com.jacd.downloadimage;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoadSvgImage extends AsyncTask<Void, Void, Drawable> {
    private OnImageTaskCompleted taskComplete;
    private Context context;
    private URL url;

    public LoadSvgImage(@NonNull Context context, @NonNull String fromURL)  {
        this.context = context;
        //this.url = new URL("http://upload.wikimedia.org/wikipedia/commons/e/e8/Svg_example3.svg");
        try {
            this.url = new URL(fromURL);
            execute();
            System.out.println("Start HttpImageRequestTask");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Drawable doInBackground(Void... params) {
        try {
            System.out.println("in Progress HttpImageRequestTask");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            SVG svg = SVGParser. getSVGFromInputStream(inputStream);
            Drawable drawable = svg.createPictureDrawable();
            return drawable;
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Drawable drawable) {
        System.out.println("finish HttpImageRequestTask");
        try {
           String response = UtilsImage.drawableToB64(drawable);
            taskComplete.OnDrawableTaskCompleted(response);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                LoadOtherImage loadOtherImage = new LoadOtherImage(context, url.toString());
                loadOtherImage.setTaskComplete(response -> {
                    taskComplete.OnDrawableTaskCompleted(response);
                });
            }catch (Exception er){
                Toast.makeText(context, "Error en Respuesta del Servidor...", Toast.LENGTH_SHORT).show();
            }


        }
    }

    public OnImageTaskCompleted getTaskComplete() {
        return taskComplete;
    }

    public void setTaskComplete(OnImageTaskCompleted taskComplete) {
        this.taskComplete = taskComplete;
    }
}
