package br.ufg.inf.es.dsm.partiuufg.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Bruno on 20/06/2015.
 */
public class BusTime implements Serializable, Parcelable {
    @SerializedName("number")
    private Integer number;
    @SerializedName("name")
    private String destination;
    @SerializedName("next")
    private Integer nextTime;
    @SerializedName("following")
    private Integer followingTime;

    public BusTime() {

    }

    public Integer getNumber() {
        return number;
    }

    public String getDestination() {
        return destination;
    }

    public Integer getNextTime() {
        return nextTime;
    }

    public Integer getFollowingTime() {
        return followingTime;
    }

    private BusTime(Parcel in) {
        this.number = in.readInt();
        this.destination = in.readString();
        this.nextTime = in.readInt();
        this.followingTime = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(number);
        dest.writeString(destination);
        dest.writeInt(nextTime);
        dest.writeInt(followingTime);
    }

    public static final Parcelable.Creator<BusTime> CREATOR = new Parcelable.Creator<BusTime>() {
        public BusTime createFromParcel(Parcel in) {
            return new BusTime(in);
        }
        public BusTime[] newArray(int size) {
            return new BusTime[size];
        }
    };
}
