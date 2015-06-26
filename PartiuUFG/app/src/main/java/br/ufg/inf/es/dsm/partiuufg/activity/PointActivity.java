package br.ufg.inf.es.dsm.partiuufg.activity;

import android.database.sqlite.SQLiteException;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.dbModel.SingleBusStop;
import br.ufg.inf.es.dsm.partiuufg.fragment.NextPointBusTimeFragment;
import br.ufg.inf.es.dsm.partiuufg.http.EasyBusService;
import br.ufg.inf.es.dsm.partiuufg.http.RestBusServiceFactory;
import br.ufg.inf.es.dsm.partiuufg.model.CompleteBusStop;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PointActivity extends AbstractActivity {
    private Integer pointNumber;
    private CompleteBusStop completeBusStop;
    private NextPointBusTimeFragment fragment;

    public void updatePointViewInformation() {
        TextView address = (TextView) findViewById(R.id.tvAddress);
        address.setText(completeBusStop.getAddress());
        TextView searchTime = (TextView) findViewById(R.id.tvSearchTime);
        searchTime.setText(getString(R.string.last_search_time) + " " + completeBusStop.getSearchDateFormatted());
    }

    public void increasePointAccess() {
        List<SingleBusStop> accessList;
        try {
            accessList = SingleBusStop.find(SingleBusStop.class,
                    "number = ?", pointNumber.toString());
        } catch(SQLiteException e) {
            accessList = new ArrayList<>();
        }

        if(accessList.size() > 0 ) {
            for (SingleBusStop access : accessList) {
                access.setAddress(completeBusStop.getAddress());
                access.setReference(completeBusStop.getReferenceLocation());
                access.setLastSearchDate(completeBusStop.getSearchDate());
                access.setAccessCount(access.getAccessCount() + 1);
                access.save();
            }
        } else {
            SingleBusStop access = new SingleBusStop(pointNumber, completeBusStop.getAddress(),
                    completeBusStop.getReferenceLocation(), completeBusStop.getSearchDate(), (long) 1);
            access.save();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pointNumber = getIntent().getIntExtra("pointId", -1);

        if(savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            fragment = new NextPointBusTimeFragment();
            ft.add(R.id.linhas, fragment);
            ft.commit();

            EasyBusService service = RestBusServiceFactory.getAdapter();
            service.getPoint(pointNumber.toString(), new Callback<CompleteBusStop>() {
                @Override
                public void success(CompleteBusStop vCompleteBusStop, Response response) {
                    completeBusStop = vCompleteBusStop;
                    increasePointAccess();
                    updatePointViewInformation();
                    fragment.setCompleteBusStop(completeBusStop);
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast toast = Toast.makeText(getBaseContext(),
                            "Ponto não encontrado", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        } else {
            try {
                completeBusStop = (CompleteBusStop) savedInstanceState.getSerializable("completeBusStop");
                updatePointViewInformation();
            } catch( NullPointerException e) {}
        }
    }

    @Override
    protected void setActivityContentView() {
        setContentView(R.layout.activity_point);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("completeBusStop", completeBusStop);
        super.onSaveInstanceState(outState);
    }
}
