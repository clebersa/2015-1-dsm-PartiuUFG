package br.ufg.inf.es.dsm.partiuufg.model;

import android.os.Parcel;
import android.os.Parcelable;

import br.ufg.inf.es.dsm.partiuufg.dbModel.SingleBusStop;

/**
 * Created by Bruno on 05/07/2015.
 */
public class BusStopWithLine extends SingleBusStop implements Parcelable {
    private CompleteBusStop completeBusStop;
    private Boolean loaded = false;

    public BusStopWithLine(Integer number, String address, String reference,
                           String lastSearchDate, Long accessCount,
                           CompleteBusStop completeBusStop) {
        super(number, address, reference, lastSearchDate, accessCount);
        this.completeBusStop = completeBusStop;
    }

    public BusStopWithLine(Integer number, String address, String reference,
                           String lastSearchDate, Long accessCount) {
        this(number, address, reference, lastSearchDate, accessCount, null);
    }

    public BusStopWithLine(CompleteBusStop simpleCompleteBusStop){
        this(simpleCompleteBusStop.getNumber(), simpleCompleteBusStop.getAddress(),
                simpleCompleteBusStop.getReferenceLocation(), simpleCompleteBusStop.getSearchDate(),
                (long)0);
    }

    public CompleteBusStop getCompleteBusStop() {
        return completeBusStop;
    }

    public void setCompleteBusStop(CompleteBusStop completeBusStop) {
        this.completeBusStop = completeBusStop;
    }

    public Boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(Boolean loaded) {
        this.loaded = loaded;
    }

    private BusStopWithLine(Parcel in) {
        this.number = in.readInt();
        this.address = in.readString();
        this.reference = in.readString();
        this.lastSearchDate = in.readString();
        this.accessCount = in.readLong();
        this.completeBusStop = in.readParcelable(CompleteBusStop.class.getClassLoader());
        this.loaded = (in.readByte() != 0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(number);
        dest.writeString(address);
        dest.writeString(reference);
        dest.writeString(lastSearchDate);
        dest.writeLong(accessCount);
        dest.writeParcelable(completeBusStop, flags);
        dest.writeByte((byte) (loaded ? 1 : 0));
    }

    public static final Parcelable.Creator<BusStopWithLine> CREATOR = new Parcelable.Creator<BusStopWithLine>() {
        public BusStopWithLine createFromParcel(Parcel in) {
            return new BusStopWithLine(in);
        }
        public BusStopWithLine[] newArray(int size) {
            return new BusStopWithLine[size];
        }
    };

    public static BusStopWithLine createBySingle(SingleBusStop single) {
        return new BusStopWithLine(single.getNumber(), single.getAddress(), single.getReference(),
                single.getLastSearchDate(), single.getAccessCount());
    }
}
