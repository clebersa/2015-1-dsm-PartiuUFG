package br.ufg.inf.es.dsm.partiuufg.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Bruno on 20/06/2015.
 */
public class BusLine implements Serializable {
    private Integer number;
    private String name;

    public BusLine(String jsonIn) {
        try {
            JSONObject reader = new JSONObject(jsonIn);
            this.number = reader.getInt("number");
            this.name = reader.getString("name");
        } catch (JSONException e) {
            Log.d(this.getClass().getSimpleName(), e.getMessage());
        }
    }
}
