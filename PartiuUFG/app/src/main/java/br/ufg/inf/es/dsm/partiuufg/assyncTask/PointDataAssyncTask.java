package br.ufg.inf.es.dsm.partiuufg.assyncTask;

import android.content.Context;
import android.net.Uri;

import br.ufg.inf.es.dsm.partiuufg.interfaces.WebServiceConsumer;

/**
 * Created by alunoinf on 20/06/2015.
 */
public class PointDataAssyncTask extends AbstractAssyncTask<Void> {
    private Integer point;

    public PointDataAssyncTask(WebServiceConsumer handler, Context context, Integer point) {
        super(handler, context);
        this.point = point;
    }

    @Override
    protected void appendUriBuilder(Uri.Builder uriBuilder) {
        uriBuilder.appendPath("point")
                .appendPath(point.toString());
    }
}
