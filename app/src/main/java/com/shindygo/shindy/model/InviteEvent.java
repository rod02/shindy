package com.shindygo.shindy.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anton Kyrychenko on 010 10.04.18.
 */
public class InviteEvent {
    String eventid;
    String attendee_id;
    String invite_by;
    int anonymous_invite;
    int offer_to_pay;


    public InviteEvent(String eventid, String attendee_id,  int anonymous_invite, int offer_to_pay) {
        this.eventid = eventid;
        this.attendee_id = attendee_id;
        this.invite_by = invite_by;
        this.anonymous_invite = anonymous_invite;
        this.offer_to_pay = offer_to_pay;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public void setAttendee_id(String attendee_id) {
        this.attendee_id = attendee_id;
    }

    public void setInvite_by(String invite_by) {
        this.invite_by = invite_by;
    }

    public void setAnonymous_invite(int anonymous_invite) {
        this.anonymous_invite = anonymous_invite;
    }

    public void setOffer_to_pay(int offer_to_pay) {
        this.offer_to_pay = offer_to_pay;
    }


    public Map<String ,Object> toMap () {
        Map<String ,Object> map = new HashMap<>();
        map.put("eventid",eventid);
        map.put("attendee_id",attendee_id);
        map.put("invite_by",invite_by);
        map.put("anonymous_invite",anonymous_invite);
        map.put("offer_to_pay",offer_to_pay);



        return map;
    }
}
