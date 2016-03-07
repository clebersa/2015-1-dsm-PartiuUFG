package br.ufg.inf.es.dsm.partiuufg.http;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * Created by bruno on 6/26/15.
 */
public class RestGCMServiceFactory {
    private static String API_BASE_URL = "https://gcm-http.googleapis.com/gcm";
    private static final String GOOGLE_API_KEY =  "AIzaSyDWGnFu0qzCiM4dSUVj2IP4hqwZaHYsQR0";

    public static GCMHttpService getAdapter() {
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Authorization", "key=" + GOOGLE_API_KEY);
                request.addHeader("Content-Type", "application/json");
            }
        };

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_BASE_URL)
                .setRequestInterceptor(requestInterceptor)
                .build();

        return restAdapter.create(GCMHttpService.class);
    }
}
