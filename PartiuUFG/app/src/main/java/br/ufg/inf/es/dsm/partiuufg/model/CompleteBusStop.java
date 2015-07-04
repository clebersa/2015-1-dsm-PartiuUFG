package br.ufg.inf.es.dsm.partiuufg.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Bruno on 19/06/2015.
 */
public class CompleteBusStop implements Serializable, Parcelable {
    @SerializedName("number")
    private Integer number;
    @SerializedName("lines-available")
    private List<BusLine> availableLines = new ArrayList<BusLine>();
    @SerializedName("bus-lines")
    private List<BusTime> busTimes = new ArrayList<BusTime>();
    @SerializedName("address")
    private String address;
    @SerializedName("reference-point")
    private String referenceLocation;
    @SerializedName("terminal")
    private Boolean isTerminal;
    @SerializedName("request-date")
    private String searchDate;

    public CompleteBusStop() {

    }

    public Integer getNumber() {
        return number;
    }

    public List<BusLine> getAvailableLines() {
        return availableLines;
    }

    public List<BusTime> getBusTimes() {
        return busTimes;
    }

    public String getAddress() {
        return address;
    }

    public String getReferenceLocation() {
        return referenceLocation;
    }

    public Boolean isTerminal() {
        return isTerminal;
    }

    public String getSearchDate() {
        return searchDate;
    }

    public long getSearchDateTimestamp() {
        Date searchDate = getDateSearchDate();
        if( searchDate == null ) {
            return 0;
        }

        return searchDate.getTime();
    }

    public Date getDateSearchDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date searchDate = null;
        try {
            searchDate = formatter.parse(this.searchDate);
        } catch (Exception e) {}

        return searchDate;
    }

    public String getSearchDateFormatted() {
        Date searchDate = getDateSearchDate();
        if( searchDate == null ) {
            return "";
        }

        return new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm").format(searchDate);
    }

    public BusLine getBusLine(Integer busLineNumber) {
        for( BusLine busLine : availableLines) {
            if (busLine.getNumber().equals(busLineNumber)) {
                return busLine;
            }
        }

        return null;
    }

    public BusTime getBusTime(Integer busLine) {
        for( BusTime busTime : busTimes) {
            if (busTime.getNumber().equals(busLine)) {
                return busTime;
            }
        }

        return null;
    }

    private CompleteBusStop(Parcel in) {
        this.number = in.readInt();
        Parcelable[] parcelableArray = in.readParcelableArray(BusLine.class.getClassLoader());
        if (parcelableArray != null) {
            BusLine[] busLinesArray = Arrays.copyOf(parcelableArray, parcelableArray.length,
                    BusLine[].class);
            this.availableLines = new ArrayList<>(Arrays.asList(busLinesArray));
        }

        parcelableArray = in.readParcelableArray(BusTime.class.getClassLoader());
        if (parcelableArray != null) {
            BusTime[] busTimesArray = Arrays.copyOf(parcelableArray, parcelableArray.length,
                    BusTime[].class);
            this.busTimes = new ArrayList<>(Arrays.asList(busTimesArray));
        }

        this.address = in.readString();
        this.referenceLocation = in.readString();
        this.isTerminal = (in.readByte() != 0);
        this.searchDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(number);
        BusLine[] busLinesArray = new BusLine[availableLines.size()];
        busLinesArray = availableLines.toArray(busLinesArray);
        dest.writeParcelableArray(busLinesArray, flags);
        BusTime[] busTimesArray = new BusTime[busTimes.size()];
        busTimesArray = busTimes.toArray(busTimesArray);
        dest.writeParcelableArray(busTimesArray, flags);
        dest.writeString(address);
        dest.writeString(referenceLocation);
        dest.writeByte((byte) (isTerminal ? 1 : 0));
        dest.writeString(searchDate);
    }

    public static final Parcelable.Creator<CompleteBusStop> CREATOR = new Parcelable.Creator<CompleteBusStop>() {
        public CompleteBusStop createFromParcel(Parcel in) {
            return new CompleteBusStop(in);
        }
        public CompleteBusStop[] newArray(int size) {
            return new CompleteBusStop[size];
        }
    };
}
