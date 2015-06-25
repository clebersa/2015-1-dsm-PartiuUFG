package br.ufg.inf.es.dsm.partiuufg.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Bruno on 20/06/2015.
 */
public class BusLine implements Serializable {
    private Integer number;
    private String name;
    private HashMap<Integer,Point> points = new HashMap<>();

    public BusLine(String jsonIn) {
        try {
            JSONObject reader = new JSONObject(jsonIn);
            this.number = reader.getInt("number");
            this.name = reader.getString("name");
        } catch (JSONException e) {
            Log.d(this.getClass().getSimpleName(), e.getMessage());
        }
    }

    public BusLine(Integer number, String name){
        this.number = number;
        this.name = name;
    }

    public Integer getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public HashMap<Integer, Point> getPoints() {
        return points;
    }

    public void addPoint(Point point) {
        if(!points.containsKey(point.getNumber())) {
            points.put(point.getNumber(), point);
        }
    }

    public Point getPoint(Integer pointNumber) {
        if(points.containsKey(pointNumber)) {
            return points.get(pointNumber);
        }

        return null;
    }
}
