package com.shindygo.shindy.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shindygo.shindy.R;
import com.shindygo.shindy.model.Image;

import java.util.List;

public class PageFragment2 extends Fragment {
    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    List<Image> imageList;
    int pageNumber;
    public static PageFragment2 newInstance(int page,String s) {
        PageFragment2 pageFragment = new PageFragment2();
        Bundle arguments = new Bundle();
        arguments.putString("image",s);
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, null);
        String s = getArguments().getString("image","");
        ImageView imageView = view.findViewById(R.id.imgMain);
        Glide.with(getContext()).load(s).into(imageView);
        return view;
    }

}

