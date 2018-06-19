package com.shindygo.shindy.interfaces;

import com.shindygo.shindy.model.Event;
import com.shindygo.shindy.model.User;

/**
 * Created by User on 17.03.2018.
 */

public interface ClickEvent {
    void Click(Event event);
    void openEvent(Event event);

}
