package br.ufg.inf.es.dsm.partiuufg.assyncTask;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

import br.ufg.inf.es.dsm.partiuufg.activity.CampusActivity;
import br.ufg.inf.es.dsm.partiuufg.interfaces.DBImplementer;
import br.ufg.inf.es.dsm.partiuufg.model.BusLine;
import br.ufg.inf.es.dsm.partiuufg.model.Campus;

/**
 * Created by Cleber on 25/06/2015.
 */
public class DBCampusSelectAsyncTask extends AsyncTask<Integer, Void, Campus> {
    protected Context context;
    private DBImplementer handler;

    public DBCampusSelectAsyncTask(DBImplementer<Campus> handler, Context context) {
        this.handler = handler;
        this.context = context;
    }

    @Override
    protected Campus doInBackground(Integer... params) {
        if(((CampusActivity) handler).getCampusId() == 1){
            ArrayList<BusLine> busLines = new ArrayList<>();
            busLines.add(new BusLine(105, "Campinas"));
            busLines.add(new BusLine(270, "Rodoviária"));
            busLines.add(new BusLine(174, "Fama"));
            busLines.add(new BusLine(263, "Praça da Bíblia"));
            busLines.add(new BusLine(268, "Goiânia 2"));
            busLines.add(new BusLine(269, "Criméia"));
            busLines.add(new BusLine(302, "Universitário"));
            return new Campus(1, "Campus Samambaia", busLines);
        }else if(((CampusActivity) handler).getCampusId() == 2){
            ArrayList<BusLine> busLines = new ArrayList<>();
            busLines.add(new BusLine(302, "Universitário"));
            busLines.add(new BusLine(21, "Flamboyant"));
            busLines.add(new BusLine(19, "Av. 85"));
            return new Campus(2, "Campus Colemar Natal e Silva", busLines);
        }else{
            return null;
        }
    }

    @Override
    protected void onPostExecute(Campus campus) {
        handler.onSelect(campus);
    }
}
