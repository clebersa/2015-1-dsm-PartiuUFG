package br.ufg.inf.es.dsm.partiuufg.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Bruno on 19/06/2015.
 */
public class Point implements Serializable {
    private Integer number;
    private ArrayList<BusLine> availableLines = new ArrayList<BusLine>();
    private HashMap<Integer, BusTime> busTimes = new HashMap<Integer, BusTime>();
    private String address;
    private String referenceLocation;
    private Boolean isTerminal;
    private Date searchDate;

    public Point(String jsonIn) {
        try {
            JSONObject reader = new JSONObject(jsonIn);
            this.number = reader.getInt("number");
            this.address = reader.getString("address");
            this.referenceLocation = reader.getString("reference-point");
            this.isTerminal = reader.getBoolean("terminal");

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            try {
                this.searchDate = formatter.parse(reader.getString("request-date"));
            } catch (ParseException e) {
                Log.d(this.getClass().getSimpleName(), e.getMessage());
            }

            JSONArray availableLines = reader.getJSONArray("lines-available");
            BusLine tmpBusLine;
            for( int i = 0; i < availableLines.length(); i++ ) {
                tmpBusLine = new BusLine( availableLines.getJSONObject(i).toString() );
                tmpBusLine.addPoint(this);
                this.availableLines.add( tmpBusLine );
            }

            JSONArray nextBuses = reader.getJSONArray("bus-lines");
            BusTime tmpBusTime;
            for( int i = 0; i < nextBuses.length(); i++ ) {
                tmpBusTime = new BusTime( nextBuses.getJSONObject(i).toString() );
                this.busTimes.put( tmpBusTime.getNumber(), tmpBusTime );
            }
        } catch (JSONException e) {
            Log.d(this.getClass().getSimpleName(), e.getMessage());
        }
    }

    public Integer getNumber() {
        return number;
    }

    public ArrayList<BusLine> getAvailableLines() {
        return availableLines;
    }

    public HashMap<Integer,BusTime> getNextBuses() {
        return busTimes;
    }

    public String getAddress() {
        return address;
    }

    public String getReferenceLocation() {
        return referenceLocation;
    }

    public Boolean getIsTerminal() {
        return isTerminal;
    }

    public Date getSearchDate() {
        return searchDate;
    }

    public BusTime getBusTime(Integer busLine) {
        if(busTimes.containsKey(busLine)) {
            return busTimes.get(busLine);
        }

        return null;
    }

    public String getSearchDateFormated() {
        return new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm").format(searchDate);
    }
}
