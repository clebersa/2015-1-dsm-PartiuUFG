package br.ufg.inf.es.dsm.partiuufg.model;

/**
 * Created by bruno on 6/26/15.
 */
public class GCMMessage {
    private String to;
    private GCMMessageData data;

    public GCMMessage(String to, GCMMessageData data) {
        this.to = to;
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public GCMMessageData getData() {
        return data;
    }

    public void setData(GCMMessageData data) {
        this.data = data;
    }
}
