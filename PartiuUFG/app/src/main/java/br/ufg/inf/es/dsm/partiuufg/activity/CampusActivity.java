package br.ufg.inf.es.dsm.partiuufg.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.assyncTask.DBCampusSelectAsyncTask;
import br.ufg.inf.es.dsm.partiuufg.fragment.CampusBusLinesFragment;
import br.ufg.inf.es.dsm.partiuufg.interfaces.DBImplementer;
import br.ufg.inf.es.dsm.partiuufg.model.Campus;

public class CampusActivity extends AbstractActivity implements DBImplementer<Campus>{
    Integer campusId;
    CampusBusLinesFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null) {
            campusId = getIntent().getIntExtra("campusId", 0);
            DBCampusSelectAsyncTask selectAsyncTask = new DBCampusSelectAsyncTask(this, this);
            selectAsyncTask.execute();

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            fragment = new CampusBusLinesFragment();
            ft.add(R.id.bus_line_lines, fragment);
            ft.commit();
        }
    }

    public Integer getCampusId() {
        return campusId;
    }

    public void setCampusId(Integer campusId) {
        this.campusId = campusId;
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

    @Override
    public void onSelect(Campus campus) {
        if(campus != null){
            fragment.setCampus(campus);
        } else{
            Toast toast = Toast.makeText(this, "Campus não encontrado", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
