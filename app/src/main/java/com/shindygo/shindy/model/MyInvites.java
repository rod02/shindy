package com.shindygo.shindy.model;

/**
 * Created by Anton Kyrychenko on 019 19.04.18.
 */
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyInvites implements Parcelable
{

    @SerializedName("my_invites")
    @Expose
    public List<MyInvitesUser> myInvites = null;
    @SerializedName("others_invite")
    @Expose
    public List<MyInvitesUser> othersInvite = null;
    public final static Parcelable.Creator<MyInvites> CREATOR = new Creator<MyInvites>() {


        @SuppressWarnings({
                "unchecked"
        })
        public MyInvites createFromParcel(Parcel in) {
            return new MyInvites(in);
        }

        public MyInvites[] newArray(int size) {
            return (new MyInvites[size]);
        }

    }
            ;

    protected MyInvites(Parcel in) {
        in.readList(this.myInvites, (MyInvitesUser.class.getClassLoader()));
        in.readList(this.othersInvite, (MyInvitesUser.class.getClassLoader()));
    }

    public MyInvites() {
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(myInvites);
        dest.writeList(othersInvite);
    }

    public int describeContents() {
        return 0;
    }

}
