package br.ufg.inf.es.dsm.partiuufg.activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.devspark.progressfragment.ProgressFragment;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.assyncTask.PointDataAssyncTask;
import br.ufg.inf.es.dsm.partiuufg.fragment.NextPointBusTimeFragment;
import br.ufg.inf.es.dsm.partiuufg.interfaces.WebServiceConsumer;
import br.ufg.inf.es.dsm.partiuufg.model.Point;
import br.ufg.inf.es.dsm.partiuufg.model.WebServiceResponse;

public class PointActivity extends AbstractActivity implements WebServiceConsumer {
    NextPointBusTimeFragment fragment;

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
            Point point = new Point(response.getBody());
            fragment.setPoint(point);
        } else {
            Toast toast = Toast.makeText(this, "Ponto n�o encontrado", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        super.onQueryTextSubmit(query);
        finish();
        return false;
    }
}
