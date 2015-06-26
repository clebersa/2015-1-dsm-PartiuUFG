package br.ufg.inf.es.dsm.partiuufg.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bruno on 20/06/2015.
 */
public class BusLine implements Serializable {
    @SerializedName("number")
    private Integer number;
    @SerializedName("name")
    private String name;
    @SerializedName("points")
    private List<CompleteBusStop> completeBusStops = new ArrayList<CompleteBusStop>();

    public BusLine() {

    }

    public Integer getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public List<CompleteBusStop> getCompleteBusStops() {
        return completeBusStops;
    }

    public CompleteBusStop getPoint(Integer pointNumber) {
        for( CompleteBusStop completeBusStop : completeBusStops) {
            if(completeBusStop.getNumber().equals(pointNumber)) {
                return completeBusStop;
            }
        }

        return null;
    }
}
