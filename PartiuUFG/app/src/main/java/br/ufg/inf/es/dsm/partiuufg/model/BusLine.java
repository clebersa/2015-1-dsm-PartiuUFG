package br.ufg.inf.es.dsm.partiuufg.model;

import android.util.Log;

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
    private List<Point> points = new ArrayList<Point>();

    public BusLine() {

    }

    public BusLine(Integer number, String name) {
        this.number = number;
        this.name = name;
    }

    public Integer getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public List<Point> getPoints() {
        return points;
    }

    public Point getPoint(Integer pointNumber) {
        for( Point point : points ) {
            if(point.getNumber().equals(pointNumber)) {
                return point;
            }
        }

        return null;
    }
}
