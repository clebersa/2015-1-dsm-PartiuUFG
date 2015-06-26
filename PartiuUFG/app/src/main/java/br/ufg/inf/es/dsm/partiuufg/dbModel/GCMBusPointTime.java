package br.ufg.inf.es.dsm.partiuufg.dbModel;

import com.orm.SugarRecord;

/**
 * Created by bruno on 6/25/15.
 */
public class GCMBusPointTime extends SugarRecord<GCMBusPointTime> {
    private static final Integer DEFAULT_BEFORE_MINUTES_TO_ALERT = 5;
    Integer pointNumber;
    Integer busLineNumber;
    Integer beforeMinutesToAlert;

    public GCMBusPointTime() {
    }

    public GCMBusPointTime(Integer pointNumber, Integer busLineNumber, Integer beforeMinutesToAlert){
        this.pointNumber = pointNumber;
        this.busLineNumber = busLineNumber;
        this.beforeMinutesToAlert = beforeMinutesToAlert;
    }

    public GCMBusPointTime(Integer pointNumber, Integer busLineNumber){
        this(pointNumber, busLineNumber, DEFAULT_BEFORE_MINUTES_TO_ALERT);
    }

    @Override
    public String toString() {
        return "GCMBusPointTime{Bus stop number: " + pointNumber +
                ", Bus line: " + busLineNumber +
                ", Alert in: " + beforeMinutesToAlert + "}";
    }

    public Integer getPointNumber() {
        return pointNumber;
    }

    public void setPointNumber(Integer pointNumber) {
        this.pointNumber = pointNumber;
    }

    public Integer getBusLineNumber() {
        return busLineNumber;
    }

    public void setBusLineNumber(Integer busLineNumber) {
        this.busLineNumber = busLineNumber;
    }

    public Integer getBeforeMinutesToAlert() {
        return beforeMinutesToAlert;
    }

    public void setBeforeMinutesToAlert(Integer beforeMinutesToAlert) {
        this.beforeMinutesToAlert = beforeMinutesToAlert;
    }
}
