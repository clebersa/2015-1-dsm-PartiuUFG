package br.ufg.inf.es.dsm.partiuufg.dbModel;

import com.orm.SugarRecord;

/**
 * Created by bruno on 6/25/15.
 */
public class SingleBusStop extends SugarRecord<SingleBusStop> {
    Integer number;
    String address;
    String reference;
    String lastSearchDate;
    Long accessCount;

    public SingleBusStop() {
    }

    public SingleBusStop(Integer number, String address, String reference, String lastSearchDate,
                         Long accessCount) {
        this.number = number;
        this.address = address;
        this.reference = reference;
        this.lastSearchDate = lastSearchDate;
        this.accessCount = accessCount;
    }

    @Override
    public String toString() {
        return "SingleBusStop{" +
                "number=" + number +
                ", address='" + address + '\'' +
                ", reference='" + reference + '\'' +
                ", lastSearchDate='" + lastSearchDate + '\'' +
                ", accessCount=" + accessCount +
                '}';
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getLastSearchDate() {
        return lastSearchDate;
    }

    public void setLastSearchDate(String lastSearchDate) {
        this.lastSearchDate = lastSearchDate;
    }

    public Long getAccessCount() {
        return accessCount;
    }

    public void setAccessCount(Long accessCount) {
        this.accessCount = accessCount;
    }
}
