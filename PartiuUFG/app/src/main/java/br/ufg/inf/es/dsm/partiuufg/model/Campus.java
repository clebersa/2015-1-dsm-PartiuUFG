package br.ufg.inf.es.dsm.partiuufg.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Cleber on 25/06/15.
 */
public class Campus implements Serializable{
    private Integer id;
    private String name;
    private ArrayList<BusLine> busLines;

    public Campus(Integer id, String name, ArrayList<BusLine> busLines) {
        this.id = id;
        this.name = name;
        this.busLines = busLines;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<BusLine> getBusLines() {
        return busLines;
    }

    public void setBusLines(ArrayList<BusLine> busLines) {
        this.busLines = busLines;
    }
}
