package br.ufg.inf.es.dsm.partiuufg.activity;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.fragment.NextPointBusTimeFragment;

public class BusStopActivity extends AbstractActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Integer busStopNumber = getIntent().getIntExtra("pointId", -1);
        setTitle(getString(R.string.title_activity_bus_stop, busStopNumber));

        if(savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            NextPointBusTimeFragment fragment = new NextPointBusTimeFragment();
            Bundle b = new Bundle();
            b.putInt("busStopNumber", busStopNumber);
            fragment.setArguments(b);
            ft.add(R.id.list_lines, fragment);
            ft.commit();
        }
    }

    @Override
    protected void setActivityContentView() {
        setContentView(R.layout.activity_generic_list);
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
