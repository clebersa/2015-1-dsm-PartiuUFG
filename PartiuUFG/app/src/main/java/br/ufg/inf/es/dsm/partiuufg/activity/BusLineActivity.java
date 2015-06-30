package br.ufg.inf.es.dsm.partiuufg.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.fragment.BusStopListFragment;

public class BusLineActivity extends AbstractActivity {
    private Integer lineNumber;
    private BusStopListFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lineNumber = getIntent().getIntExtra("lineNumber", -1);
        Log.d(TAG, "Line number received: " + lineNumber);

        if(savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            fragment = new BusStopListFragment();
            Bundle b = new Bundle();
            b.putInt("mode", BusStopListFragment.WEB_MODE);

            b.putInt("lineNumber", lineNumber);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("busLine", lineNumber);
        super.onSaveInstanceState(outState);
    }
}
