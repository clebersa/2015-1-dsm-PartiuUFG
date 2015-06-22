package br.ufg.inf.es.dsm.partiuufg.assyncTask;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;

import br.ufg.inf.es.dsm.partiuufg.interfaces.WebServiceConsumer;
import br.ufg.inf.es.dsm.partiuufg.model.WebServiceResponse;

/**
 * Created by alunoinf on 20/06/2015.
 */
public abstract class AbstractAssyncTask<T> extends AsyncTask<T, Void, WebServiceResponse> {
    protected Context context;
    private WebServiceConsumer handler;
    private WebServiceResponse response = new WebServiceResponse();
    protected Uri.Builder uriBuilder;

    public AbstractAssyncTask(WebServiceConsumer handler, Context context) {
        this.handler = handler;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        uriBuilder = new Uri.Builder();
        uriBuilder.scheme("http")
                .authority("easybus.tk")
                .appendPath("api")
                .appendPath("v1");

        appendUriBuilder(uriBuilder);
    }

    @Override
    protected WebServiceResponse doInBackground(T... params) {
        String getUrl = uriBuilder.build().toString();
        Log.d("APIEXECUTE", getUrl);
        HttpRequest response  = HttpRequest.get( getUrl );
        this.response.setResponseCode(response.code());
        this.response.setBody(response.body());

        return this.response;
    }

    @Override
    protected void onPostExecute(WebServiceResponse response) {
        handler.receiveResponse(response);
    }

    abstract protected void appendUriBuilder( Uri.Builder uriBuilder );

    public Context getContext() {
        return context;
    }
}