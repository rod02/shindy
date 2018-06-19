package com.shindygo.shindy.model;
import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("image_path")
    @Expose
    public String imagePath;
    public final static Parcelable.Creator<Image> CREATOR = new Creator<Image>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        public Image[] newArray(int size) {
            return (new Image[size]);
        }

    }
            ;
    private final static long serialVersionUID = 2682572668050527533L;

    protected Image(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.imagePath = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Image() {
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(imagePath);
    }

    public int describeContents() {
        return 0;
    }

}