package br.ufg.inf.es.dsm.partiuufg.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.fragment.CampusBusLinesFragment;
import br.ufg.inf.es.dsm.partiuufg.dbModel.Campus;

/**
 * Created by Cleber on 25/06/2015.
 */
public class CampusActivity extends AbstractActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            CampusBusLinesFragment fragment = new CampusBusLinesFragment();
            Bundle b = new Bundle();
            b.putLong("campusId", getIntent().getLongExtra("campusId", 0));
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
