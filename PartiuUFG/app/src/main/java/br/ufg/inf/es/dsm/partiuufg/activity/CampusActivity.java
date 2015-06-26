package br.ufg.inf.es.dsm.partiuufg.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.dbModel.SingleBusLine;
import br.ufg.inf.es.dsm.partiuufg.fragment.CampusBusLinesFragment;
import br.ufg.inf.es.dsm.partiuufg.http.EasyBusService;
import br.ufg.inf.es.dsm.partiuufg.http.RestBusServiceFactory;
import br.ufg.inf.es.dsm.partiuufg.dbModel.Campus;
import br.ufg.inf.es.dsm.partiuufg.model.BusLine;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Cleber on 25/06/2015.
 */
public class CampusActivity extends AbstractActivity{
    private final String TAG = this.getClass().getName();
    private Campus campus;
    private CampusBusLinesFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            fragment = new CampusBusLinesFragment();
            Bundle b = new Bundle();
            b.putLong("campusId", getIntent().getLongExtra("campusId", 0));
            fragment.setArguments(b);
            ft.add(R.id.bus_line_lines, fragment);
            ft.commit();
        }
    }

    @Override
    protected void setActivityContentView() {
        setContentView(R.layout.activity_campus);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        super.onQueryTextSubmit(query);
        finish();
        return false;
    }
}
