package br.ufg.inf.es.dsm.partiuufg.model;

import android.os.Parcel;
import android.os.Parcelable;

import br.ufg.inf.es.dsm.partiuufg.dbModel.SingleGCMBusStopLine;

/**
 * Created by Bruno on 03/07/2015.
 */
public class CompleteGCMBusStopLine extends SingleGCMBusStopLine implements Parcelable {
    private CompleteBusStop busStop;
    private Boolean loaded = false;

    public CompleteGCMBusStopLine(Integer pointNumber, Integer busLineNumber,
                                  Integer beforeMinutesToAlert, CompleteBusStop busStop) {
        super(pointNumber, busLineNumber, beforeMinutesToAlert);
        this.busStop = busStop;
    }

    public CompleteGCMBusStopLine(Integer pointNumber, Integer busLineNumber,
                                  Integer beforeMinutesToAlert) {
        this(pointNumber, busLineNumber, beforeMinutesToAlert,  null);
    }

    public boolean compareSingle(SingleGCMBusStopLine o) {
        if(pointNumber.equals(o.getPointNumber()) && busLineNumber.equals(o.getBusLineNumber())) {
            setBeforeMinutesToAlert(o.getBeforeMinutesToAlert());
            return true;
        }
        return false;
    }


    public CompleteBusStop getBusStop() {
        return busStop;
    }

    public Boolean isLoaded() {
        return loaded;
    }

    public void setBusStop(CompleteBusStop busStop) {
        this.busStop = busStop;
    }

    public void setLoaded(Boolean loaded) {
        this.loaded = loaded;
    }

    private CompleteGCMBusStopLine(Parcel in) {
        this.pointNumber = in.readInt();
        this.busLineNumber = in.readInt();
        this.beforeMinutesToAlert = in.readInt();
        this.busStop = in.readParcelable(CompleteBusStop.class.getClassLoader());
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
        dest.writeParcelable(busStop, flags);
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
