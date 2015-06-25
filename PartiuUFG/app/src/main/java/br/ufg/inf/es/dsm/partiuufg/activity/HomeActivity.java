package br.ufg.inf.es.dsm.partiuufg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import br.ufg.inf.es.dsm.partiuufg.R;


public class HomeActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setActivityContentView() {
        setContentView(R.layout.activity_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void loadSamambaiaBusLines(View v){
        Intent intent = new Intent(this, CampusActivity.class);
        intent.putExtra("campusId", 1);
        this.startActivity(intent);
    }

    public void loadColemarBusLines(View v){
        Intent intent = new Intent(this, CampusActivity.class);
        intent.putExtra("campusId", 2);
        this.startActivity(intent);
    }
}
