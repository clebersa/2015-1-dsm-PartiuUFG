package br.ufg.inf.es.dsm.partiuufg.http;


import br.ufg.inf.es.dsm.partiuufg.model.GCMResult;
import br.ufg.inf.es.dsm.partiuufg.model.GCMMessage;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by bruno on 6/26/15.
 */
public interface GCMHttpService {
    @POST("/send")
    GCMResult send(@Body GCMMessage GCMMessage);
}
