package br.ufg.inf.es.dsm.partiuufg.model;

import android.os.Parcel;
import android.os.Parcelable;

import br.ufg.inf.es.dsm.partiuufg.dbModel.SingleGCMBusStopLine;

/**
 * Created by Bruno on 03/07/2015.
 */
public class CompleteGCMBusStopLine extends SingleGCMBusStopLine implements Parcelable {
    private String name;
    private String address;
    private Integer nextTime;
    private String searchDate;
    private Boolean loaded = false;

    public CompleteGCMBusStopLine(Integer pointNumber, Integer busLineNumber,
                                  Integer beforeMinutesToAlert, String name,
                                  String address, Integer nextTime, String searchDate) {
        super(pointNumber, busLineNumber, beforeMinutesToAlert);
        this.name = name;
        this.address = address;
        this.nextTime = nextTime;
        this.searchDate = searchDate;
    }

    public CompleteGCMBusStopLine(Integer pointNumber, Integer busLineNumber,
                                  Integer beforeMinutesToAlert) {
        this(pointNumber, busLineNumber, beforeMinutesToAlert,  "", "",  -1,  "");
    }

    public boolean compareSingle(SingleGCMBusStopLine o) {
        if(pointNumber.equals(o.getPointNumber()) && busLineNumber.equals(o.getBusLineNumber())) {
            setBeforeMinutesToAlert(o.getBeforeMinutesToAlert());
            return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Integer getNextTime() {
        return nextTime;
    }

    public String getSearchDate() {
        return searchDate;
    }

    public Boolean isLoaded() {
        return loaded;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setNextTime(Integer nextTime) {
        this.nextTime = nextTime;
    }

    public void setSearchDate(String searchDate) {
        this.searchDate = searchDate;
    }

    public void setLoaded(Boolean loaded) {
        this.loaded = loaded;
    }

    private CompleteGCMBusStopLine(Parcel in) {
        this.pointNumber = in.readInt();
        this.busLineNumber = in.readInt();
        this.beforeMinutesToAlert = in.readInt();
        this.name = in.readString();
        this.address = in.readString();
        this.nextTime = in.readInt();
        this.searchDate = in.readString();
        this.loaded = (in.readByte() != 0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pointNumber);
        dest.writeInt(busLineNumber);
        dest.writeInt(beforeMinutesToAlert);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeInt(nextTime);
        dest.writeString(searchDate);
        dest.writeByte((byte) (loaded ? 1 : 0));
    }

    public static final Parcelable.Creator<CompleteGCMBusStopLine> CREATOR = new Parcelable.Creator<CompleteGCMBusStopLine>() {
        public CompleteGCMBusStopLine createFromParcel(Parcel in) {
            return new CompleteGCMBusStopLine(in);
        }
        public CompleteGCMBusStopLine[] newArray(int size) {
            return new CompleteGCMBusStopLine[size];
        }
    };

    public static CompleteGCMBusStopLine createBySingle(SingleGCMBusStopLine single) {
        return new CompleteGCMBusStopLine(single.getPointNumber(), single.getBusLineNumber(),
                single.getBeforeMinutesToAlert());
    }
}
