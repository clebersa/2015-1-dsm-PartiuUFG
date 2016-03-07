package br.ufg.inf.es.dsm.partiuufg.http;

import retrofit.RestAdapter;

/**
 * Created by bruno on 6/25/15.
 */
public class RestBusServiceFactory {
    private static String API_BASE_URL = "http://easybus.ml/api/v1";

    public static EasyBusService getAdapter() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_BASE_URL)
                .build();

        return restAdapter.create(EasyBusService.class);
    }
}
