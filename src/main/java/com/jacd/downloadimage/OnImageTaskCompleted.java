package com.jacd.downloadimage;

import android.graphics.drawable.Drawable;

import org.json.JSONException;

/**
 * Created by Jesus C on 01/09/20.
 */
public interface OnImageTaskCompleted {
    void OnDrawableTaskCompleted(String response) throws Exception;
}
