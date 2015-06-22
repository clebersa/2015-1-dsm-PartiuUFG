package br.ufg.inf.es.dsm.partiuufg.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Bruno on 20/06/2015.
 */
public class BusTime implements Serializable {
    private Integer number;
    private String destination;
    private Integer nextTime;
    private Integer followingTime;

    public BusTime(Integer number, String destination, Integer nexTime, Integer followingTime) {
        this.number = number;
        this.destination = destination;
        this.nextTime = nexTime;
        this.followingTime = followingTime;
    }

    public BusTime(String jsonIn) {
        try {
            JSONObject reader = new JSONObject(jsonIn);
            this.number = reader.getInt("number");
            this.destination = reader.getString("name");
            this.nextTime = reader.getInt("next");
            this.followingTime = reader.getInt("following");
        } catch (JSONException e) {
            Log.d(this.getClass().getSimpleName(), e.getMessage());
        }
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
}
