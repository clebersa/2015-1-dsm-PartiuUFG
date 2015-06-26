package br.ufg.inf.es.dsm.partiuufg.dbModel;

import com.orm.SugarRecord;

/**
 * Created by Bruno on 26/06/2015.
 */
public class GCMUser extends SugarRecord<GCMUser> {
    String gcmRegid;
    String createdAt;

    public GCMUser(){
    }

    public GCMUser(String gcmRegid, String createdAt) {
        this.gcmRegid = gcmRegid;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "GCMUser{" +
                "gcmRegid='" + gcmRegid + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }

    public String getGcmRegid() {
        return gcmRegid;
    }

    public void setGcmRegid(String gcmRegid) {
        this.gcmRegid = gcmRegid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
