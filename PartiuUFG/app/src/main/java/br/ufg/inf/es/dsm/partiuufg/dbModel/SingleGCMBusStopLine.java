package br.ufg.inf.es.dsm.partiuufg.dbModel;

import com.orm.SugarRecord;

/**
 * Created by bruno on 6/25/15.
 */
public class SingleGCMBusStopLine extends SugarRecord<SingleGCMBusStopLine>  {
    protected static final Integer DEFAULT_BEFORE_MINUTES_TO_ALERT = 5;
    protected Integer pointNumber;
    protected Integer busLineNumber;
    protected Integer beforeMinutesToAlert;

    public SingleGCMBusStopLine() {
    }

    public SingleGCMBusStopLine(Integer pointNumber, Integer busLineNumber, Integer beforeMinutesToAlert){
        this.pointNumber = pointNumber;
        this.busLineNumber = busLineNumber;
        this.beforeMinutesToAlert = beforeMinutesToAlert;
    }

    public SingleGCMBusStopLine(Integer pointNumber, Integer busLineNumber){
        this(pointNumber, busLineNumber, DEFAULT_BEFORE_MINUTES_TO_ALERT);
    }

    @Override
    public String toString() {
        return "SingleGCMBusStopLine{Bus stop number: " + pointNumber +
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
