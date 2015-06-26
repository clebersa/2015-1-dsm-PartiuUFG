package br.ufg.inf.es.dsm.partiuufg.http;

import br.ufg.inf.es.dsm.partiuufg.model.BusLine;
import br.ufg.inf.es.dsm.partiuufg.model.BusTime;
import br.ufg.inf.es.dsm.partiuufg.model.CompleteBusStop;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by bruno on 6/25/15.
 */
public interface EasyBusService {
    @GET("/bus/{lineNumber}")
    void getBusLine(@Path("lineNumber") String lineNumber, Callback<BusLine> callback);

    @GET("/point/{pointNumber}")
    void getPoint(@Path("pointNumber") String pointNumber, Callback<CompleteBusStop> callback);

    @GET("/point/{pointNumber}/bus/{lineNumber}")
    void getBusNextTimeInPoint(@Path("pointNumber") String pointNumber,
                               @Path("lineNumber") String lineNumber,
                               Callback<BusTime> callback);
}
