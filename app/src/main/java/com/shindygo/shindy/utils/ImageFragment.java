package com.shindygo.shindy.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shindygo.shindy.R;
import com.shindygo.shindy.model.Image;

import java.util.List;

public class ImageFragment extends Fragment {
    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    public static ImageFragment newInstance(int page, Image s) {
        ImageFragment pageFragment = new ImageFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable("image",s);
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, null);
        Image s = getArguments().getParcelable("image");
        ImageView imageView = view.findViewById(R.id.imgMain);
        String imagepath = "";
        try {
            imagepath = s.imagePath;
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        if(s.id != null && !s.id.equals("0")){
            GlideImage.load(s.imagePath,imageView);
        }else{
            GlideImage.loadFromPath(imagepath,imageView);
        }
        return view;
    }
}

