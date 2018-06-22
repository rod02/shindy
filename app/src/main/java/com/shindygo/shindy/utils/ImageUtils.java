package com.shindygo.shindy.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.shindygo.shindy.Api;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringReader;

public class ImageUtils {

    public static String img64from(Uri uri) throws FileNotFoundException{
        String s = null;
        final InputStream imageStream = Api.getContext().getContentResolver().openInputStream(uri);
        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.JPEG, 50, baos); //bm is the bitmap object
        s = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        return s;
    }

    public static String img64fromPath(String path) throws FileNotFoundException{
        return img64from(Uri.fromFile(new File(path)));
    }
}
