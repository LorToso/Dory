package com.doryapp.dory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;

/**
 * Created by Lorenzo on 31.07.2016.
 */
public class DownloadImageTask {
    private PostDownloadAction postDownloadAction;

    public DownloadImageTask(PostDownloadAction postDownloadAction) {
        this.postDownloadAction = postDownloadAction;
    }
    public void execute(String url)
    {
        new AsyncTask<String, Void, Bitmap>(){

            @Override
            protected Bitmap doInBackground(String... urls) {
                return downloadImage(urls[0]);
            }
            protected void onPostExecute(Bitmap result) {
                postDownloadAction.onDownloadCompleted(result);
            }

        }.execute(url);
    }

    private Bitmap downloadImage(String url) {
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    public abstract static class PostDownloadAction
    {
        abstract void onDownloadCompleted(Bitmap result);
    }
}