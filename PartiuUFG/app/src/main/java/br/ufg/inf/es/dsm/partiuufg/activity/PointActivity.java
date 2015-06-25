package br.ufg.inf.es.dsm.partiuufg.activity;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.assyncTask.PointDataAssyncTask;
import br.ufg.inf.es.dsm.partiuufg.fragment.NextPointBusTimeFragment;
import br.ufg.inf.es.dsm.partiuufg.interfaces.WebServiceConsumer;
import br.ufg.inf.es.dsm.partiuufg.model.Point;
import br.ufg.inf.es.dsm.partiuufg.model.WebServiceResponse;

public class PointActivity extends AbstractActivity implements WebServiceConsumer {
    Point point;
    NextPointBusTimeFragment fragment;

    public void updatePointViewInformation() {
        TextView address = (TextView) findViewById(R.id.tvAddress);
        address.setText(point.getAddress());
        TextView reference = (TextView) findViewById(R.id.tvReference);
        reference.setText(point.getReferenceLocation());
        TextView searchTime = (TextView) findViewById(R.id.tvSearchTime);
        searchTime.setText(getString(R.string.last_search_time) + " " + point.getSearchDateFormated());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null) {
            Integer pointId = getIntent().getIntExtra("pointId", 0);
            PointDataAssyncTask service = new PointDataAssyncTask(this, this, pointId);
            service.execute();

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            fragment = new NextPointBusTimeFragment();
            ft.add(R.id.linhas, fragment);
            ft.commit();
        } else {
            try {
                point = (Point) savedInstanceState.getSerializable("point");
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
    public void receiveResponse(WebServiceResponse response) {
        if( response.isSuccess() ) {
            point = new Point(response.getBody());
            updatePointViewInformation();
            fragment.setPoint(point);
        } else {
            Toast toast = Toast.makeText(this, "Ponto não encontrado", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        super.onQueryTextSubmit(query);
        finish();
        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("point", point);
        super.onSaveInstanceState(outState);
    }
}
