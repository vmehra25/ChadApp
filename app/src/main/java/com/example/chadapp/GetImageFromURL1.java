package com.example.chadapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

class GetImageFromURL1 extends AsyncTask<String,Void, Bitmap> {


    ImageView imv;
    String Url;
    Bitmap bitmap;

    GetImageFromURL1(String Url,ImageView imv)
    {
        this.imv = imv;
        this.Url = Url;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        bitmap = null;
        try{
            //   Toast.makeText(getContext(), "Downloading", Toast.LENGTH_SHORT).show();
            InputStream inputStream = new java.net.URL(Url).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            // Toast.makeText(getContext(), "Image available", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d("DownloadingImage",e.getMessage());
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        //Toast.makeText(getContext(), "Image Downloaded", Toast.LENGTH_SHORT).show();
        imv.setImageBitmap(bitmap);
        ChatFragment.dialog.dismiss();
    }
}
