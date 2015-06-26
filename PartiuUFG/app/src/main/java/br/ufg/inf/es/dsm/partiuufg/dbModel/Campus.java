package br.ufg.inf.es.dsm.partiuufg.dbModel;

import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by Cleber on 25/06/15.
 */
public class Campus extends SugarRecord<Campus> implements Serializable {
    private String name;

    public Campus() {
    }

    public Campus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
