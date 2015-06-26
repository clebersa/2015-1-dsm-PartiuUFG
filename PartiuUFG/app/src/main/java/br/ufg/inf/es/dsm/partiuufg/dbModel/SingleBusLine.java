package br.ufg.inf.es.dsm.partiuufg.dbModel;

import com.orm.SugarRecord;

/**
 * Created by Cleber on 25/06/15.
 */
public class SingleBusLine extends SugarRecord<SingleBusLine> {
    protected Integer number;
    protected String name;

    public SingleBusLine() {
    }

    public SingleBusLine(Integer number, String name) {
        this.number = number;
        this.name = name;
    }

    public SingleBusLine(Integer number) {
        this.number = number;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SingleBusLine{" +
                "number=" + number +
                ", name='" + name + '\'' +
                '}';
    }
}
