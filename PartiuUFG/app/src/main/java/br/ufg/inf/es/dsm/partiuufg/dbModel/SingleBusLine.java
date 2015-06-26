package br.ufg.inf.es.dsm.partiuufg.dbModel;

import com.orm.SugarRecord;

/**
 * Created by Cleber on 25/06/15.
 */
public class SingleBusLine extends SugarRecord<SingleBusLine> {
    protected Integer number;
    protected String name;
    protected Campus campus;

    public SingleBusLine() {
    }

    public SingleBusLine(Integer number, String name, Campus campus) {
        this.number = number;
        this.name = name;
        this.campus = campus;
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

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    @Override
    public String toString() {
        return "SingleBusLine{" +
                "number=" + number +
                ", name='" + name + '\'' +
                ", campus=" + campus +
                '}';
    }
}
