package br.ufg.inf.es.dsm.partiuufg.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Bruno on 20/06/2015.
 */
public class BusLine implements Serializable, Parcelable {
    @SerializedName("number")
    private Integer number;
    @SerializedName("name")
    private String name;
    @SerializedName("points")
    private List<CompleteBusStop> completeBusStops = new ArrayList<CompleteBusStop>();

    public BusLine() {

    }

    public Integer getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public List<CompleteBusStop> getCompleteBusStops() {
        return completeBusStops;
    }

    public CompleteBusStop getPoint(Integer pointNumber) {
        for( CompleteBusStop completeBusStop : completeBusStops) {
            if(completeBusStop.getNumber().equals(pointNumber)) {
                return completeBusStop;
            }
        }

        return null;
    }

    private BusLine(Parcel in) {
        this.number = in.readInt();
        this.name = in.readString();
        Parcelable[] parcelableArray = in.readParcelableArray(CompleteBusStop.class.getClassLoader());
        if (parcelableArray != null) {
            CompleteBusStop[] completeBusStopArray = Arrays.copyOf(parcelableArray,
                    parcelableArray.length, CompleteBusStop[].class);
            this.completeBusStops = new ArrayList<>(Arrays.asList(completeBusStopArray));
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(number);
        dest.writeString(name);
        CompleteBusStop[] completeBusStopsArray = new CompleteBusStop[completeBusStops.size()];
        completeBusStopsArray = completeBusStops.toArray(completeBusStopsArray);
        dest.writeParcelableArray(completeBusStopsArray, flags);
    }

    public static final Parcelable.Creator<BusLine> CREATOR = new Parcelable.Creator<BusLine>() {
        public BusLine createFromParcel(Parcel in) {
            return new BusLine(in);
        }
        public BusLine[] newArray(int size) {
            return new BusLine[size];
        }
    };
}
