package com.shindygo.shindy.interfaces;

import android.view.View;

public interface Click<T> {
    void onClick(int id, View view, T t);
}
