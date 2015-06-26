package br.ufg.inf.es.dsm.partiuufg.dbModel;

import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by Cleber on 25/06/15.
 */
public class Campus extends SugarRecord<Campus> implements Serializable {
    private String name;
    private SingleBusLine[] singleBusLines;

    public Campus() {
    }

    public Campus(String name, SingleBusLine[] singleBusLines) {
        this.name = name;
        this.singleBusLines = singleBusLines;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SingleBusLine[] getSingleBusLines() {
        return singleBusLines;
    }

    public void setSingleBusLines(SingleBusLine[] singleBusLines) {
        this.singleBusLines = singleBusLines;
    }
}
