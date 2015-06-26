package br.ufg.inf.es.dsm.partiuufg.model;

/**
 * Created by bruno on 6/26/15.
 */
public class GCMMessageData {
    String message;
    Integer bus_stop;
    Integer bus_line;

    public GCMMessageData(String message, Integer bus_stop, Integer bus_line) {
        this.message = message;
        this.bus_stop = bus_stop;
        this.bus_line = bus_line;
    }

    @Override
    public String toString() {
        return "GCMMessageData{" +
                "message='" + message + '\'' +
                ", bus_stop=" + bus_stop +
                ", bus_line=" + bus_line +
                '}';
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getBus_stop() {
        return bus_stop;
    }

    public void setBus_stop(Integer bus_stop) {
        this.bus_stop = bus_stop;
    }

    public Integer getBus_line() {
        return bus_line;
    }

    public void setBus_line(Integer bus_line) {
        this.bus_line = bus_line;
    }
}
