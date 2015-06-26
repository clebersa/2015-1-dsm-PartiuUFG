package br.ufg.inf.es.dsm.partiuufg.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by bruno on 6/26/15.
 */
public class GCMResult {
    @SerializedName("multicast_id")
    private long multicastId;
    @SerializedName("success")
    private Integer success;
    @SerializedName("failure")
    private Integer failure;
    @SerializedName("canonical_ids")
    private Integer canonicalIds;
    @SerializedName("results")
    private List<SendedMessage> results;

    public class SendedMessage {
        @SerializedName("message_id")
        String messageId;

        public SendedMessage(){

        }

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }
    }

    public GCMResult() {

    }

    @Override
    public String toString() {
        return "GCMResult{" +
                "multicastId=" + multicastId +
                ", success=" + success +
                ", failure=" + failure +
                ", canonicalIds=" + canonicalIds +
                ", results=" + results +
                '}';
    }

    public long getMulticastId() {
        return multicastId;
    }

    public void setMulticastId(long multicastId) {
        this.multicastId = multicastId;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getFailure() {
        return failure;
    }

    public void setFailure(Integer failure) {
        this.failure = failure;
    }

    public Integer getCanonicalIds() {
        return canonicalIds;
    }

    public void setCanonicalIds(Integer canonicalIds) {
        this.canonicalIds = canonicalIds;
    }

    public List<SendedMessage> getResults() {
        return results;
    }

    public void setResults(List<SendedMessage> results) {
        this.results = results;
    }
}