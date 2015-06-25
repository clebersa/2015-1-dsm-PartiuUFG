package br.ufg.inf.es.dsm.partiuufg.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Bruno on 20/06/2015.
 */
public class BusTime implements Serializable {
    @SerializedName("number")
    private Integer number;
    @SerializedName("name")
    private String destination;
    @SerializedName("next")
    private Integer nextTime;
    @SerializedName("following")
    private Integer followingTime;

    public BusTime() {

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
