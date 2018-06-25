package com.shindygo.shindy.utils;

import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shindygo.shindy.Api;
import com.shindygo.shindy.R;

import java.io.File;

public class GlideImage {
    static RequestOptions options = new RequestOptions()
                //.placeholder(R.drawable.your_placeholder_image)
                .error(R.mipmap.no_image);
    public static void load(String url, ImageView imageView){
        Glide.with(Api.getContext()).load(url).apply(options).into(imageView);
    }
    static void load(String path, boolean validate , ImageView imageView){

        File file = new File(path);
        if(file.exists()){
            Uri uri = Uri.fromFile(new File(path));
            Glide.with(Api.getContext()).load(uri).apply(options).into(imageView);

        }else{
            Glide.with(Api.getContext()).load(path).apply(options).into(imageView);

        }

    }
    static void loadFromPath(String path, ImageView imageView){
        Glide.with(Api.getContext()).load(Uri.fromFile(new File(path))).apply(options).into(imageView);
    }


/*
Glide.with(this)
        .load("url here") // image url
        .placeholder(R.drawable.placeholder) // any placeholder to load at start
        .error(R.drawable.imagenotfound)  // any image in case of error
        .override(200, 200); // resizing
        .centerCrop();
        .into(imageView);  // imageview object*/
}
