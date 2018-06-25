package com.shindygo.shindy.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;

/**
 * Created by Anton Kyrychenko on 019 19.03.18.
 */

public class Filter implements Parcelable {

    private int ageTo;
    private int ageFrom;
    private int distancePos;
    private int religionPos;
    private int genderPos;
    private int genderPref;


    public Filter(int ageTo, int ageFrom, int distancePos, int religionPos, int genderPos, int genderPref) {
        this.ageTo = ageTo;
        this.ageFrom = ageFrom;
        this.distancePos = distancePos;
        this.religionPos = religionPos;
        this.genderPos = genderPos;
        this.genderPref = genderPref;
    }
    public boolean isClear(){
        return ageTo == 0 &&
               ageFrom == 0 && distancePos == 0 &&
                religionPos == 0 && genderPos == 0 && genderPref == 0;

    }
    public Filter(Editable ageTo, Editable timeFrom, int distancePos, int religionPos, int genderPos, int genderPref) {

        if (ageTo.length()<1)
            this.ageTo=0;
        else
            this.ageTo= Integer.parseInt(ageTo.toString());

        if (timeFrom.length()<1)
            this.ageFrom=0;
        else
            this.ageFrom= Integer.parseInt(timeFrom.toString());



        this.distancePos = distancePos;
        this.religionPos = religionPos;
        this.genderPos = genderPos;
        this.genderPref = genderPref;
    }

    public Filter() {

    }

    protected Filter(Parcel in) {
        ageTo = in.readInt();
        ageFrom = in.readInt();
        distancePos = in.readInt();
        religionPos = in.readInt();
        genderPos = in.readInt();
        genderPref = in.readInt();
    }

    public static final Creator<Filter> CREATOR = new Creator<Filter>() {
        @Override
        public Filter createFromParcel(Parcel in) {
            return new Filter(in);
        }

        @Override
        public Filter[] newArray(int size) {
            return new Filter[size];
        }
    };

    public int getAgeTo() {
        return ageTo;
    }

    public int getAgeFrom() {
        return ageFrom;
    }

    public int getDistancePos() {
        return distancePos;
    }

    public int getReligionPos() {
        return religionPos;
    }


    public int getGenderPos() {
        return genderPos;
    }

    public int getGenderPref() {
        return genderPref;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(ageTo);
        parcel.writeInt(ageFrom);
        parcel.writeInt(distancePos);
        parcel.writeInt(religionPos);
        parcel.writeInt(genderPos);
        parcel.writeInt(genderPref);





    }
}
