package br.ufg.inf.es.dsm.partiuufg.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Bruno on 19/06/2015.
 */
public class Point implements Serializable {
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

    public Point() {

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

    public String getSearchDateFormatted() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            Date searchDate = formatter.parse(this.searchDate);
            return new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm").format(searchDate);
        } catch (Exception e) {
            Log.d(this.getClass().getSimpleName(), e.getMessage());
        }

        return "Sem informação";
    }

    public BusTime getBusTime(Integer busLine) {
        for( BusTime busTime : busTimes) {
            if (busTime.getNumber().equals(busLine)) {
                return busTime;
            }
        }

        return null;
    }
}
