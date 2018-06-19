package com.shindygo.shindy.interfaces;

import com.shindygo.shindy.model.User;

/**
 * Created by Anton Kyrychenko on 005 05.04.18.
 */
public interface UserControl {
    void addUser(User user);
    void removeUser(User user);
}
