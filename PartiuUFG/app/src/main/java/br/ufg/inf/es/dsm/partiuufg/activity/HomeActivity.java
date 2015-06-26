package br.ufg.inf.es.dsm.partiuufg.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.HashMap;
import java.util.List;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.dbModel.Campus;
import br.ufg.inf.es.dsm.partiuufg.dbModel.SingleBusLine;
import br.ufg.inf.es.dsm.partiuufg.fragment.BusStopListFragment;
import br.ufg.inf.es.dsm.partiuufg.service.GCMServer;
import br.ufg.inf.es.dsm.partiuufg.service.RegistrationIntentService;


public class HomeActivity extends AbstractActivity {
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private final String CAMPUS_SAMAMBAIA_NAME = "Campus Samambaia";
    private final String CAMPUS_COLEMAR_NAME = "Campus Colemar Natal e Silva";
    private HashMap<String, Campus> campi;

    public void gcmTokenReceived() {
        Intent intent = new Intent(getBaseContext(), GCMServer.class);
        startService(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.
                getDefaultSharedPreferences(getBaseContext());
        String gcmToken = sharedPreferences.getString("gcmToken", null);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                String gcmToken = sharedPreferences.getString("gcmToken", null);

                if(gcmToken != null) {
                    gcmTokenReceived();
                }
            }
        };

        if (checkPlayServices(false)) {
            if(gcmToken == null) {
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            } else {
                gcmTokenReceived();
            }
        }

        if(savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = new BusStopListFragment();
            Bundle b = new Bundle();
            b.putInt("mode", BusStopListFragment.DATABASE_MODE);
            fragment.setArguments(b);
            ft.add(R.id.most_visited_stop_bus, fragment);
            ft.commit();
        }

        List<Campus> campiList = Campus.listAll(Campus.class);

        boolean hasSamambaia = false;
        boolean hasColemar = false;
        campi = new HashMap<>();
        for( Campus campus : campiList ) {
            if(CAMPUS_SAMAMBAIA_NAME.equals(campus.getName())) {
                campi.put(CAMPUS_SAMAMBAIA_NAME, campus);
                hasSamambaia = true;
            } else if (CAMPUS_COLEMAR_NAME.equals(campus.getName())) {
                campi.put(CAMPUS_COLEMAR_NAME, campus);
                hasColemar = true;
            }
        }

        if(!hasSamambaia) initCampusSamambaia();
        if(!hasColemar) initCampusColemar();

    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(getString(R.string.registrationCompleteIntentName)));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
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

    private void initCampusSamambaia(){
        Campus campusSamambaia = new Campus(CAMPUS_SAMAMBAIA_NAME);
        campusSamambaia.save();
        campi.put(CAMPUS_SAMAMBAIA_NAME, campusSamambaia);

        initCampus(campusSamambaia, new int[]{105, 270, 174, 263, 268, 269, 302});
    }

    private void initCampusColemar(){
        Campus campusColemar = new Campus(CAMPUS_COLEMAR_NAME);
        campusColemar.save();
        campi.put(CAMPUS_COLEMAR_NAME, campusColemar);

        initCampus(campusColemar, new int[]{19, 26,302});
    }

    private void initCampus(Campus campus, int[] lineNumbers){
        SingleBusLine singleBusLine;
        List<SingleBusLine> singleBusLineList;
        for(Integer lineNumber: lineNumbers){
            singleBusLineList = SingleBusLine.find(SingleBusLine.class, "number = ?", lineNumber.toString());

            if(singleBusLineList == null || singleBusLineList.size() == 0){
                singleBusLine = new SingleBusLine(lineNumber);
                singleBusLine.setCampus(campus);
                singleBusLine.save();
            }else{
                boolean hasCampus = false;
                for(SingleBusLine sbl: singleBusLineList){
                    if(sbl.getCampus().getId() == campus.getId()){
                        hasCampus = true;
                        break;
                    }
                }
                if(!hasCampus){
                    singleBusLine = new SingleBusLine(lineNumber);
                    singleBusLine.setCampus(campus);
                    singleBusLine.save();
                }
            }
        }
    }

    public void loadSamambaiaBusLines(View v){
        loadBusLines(campi.get(CAMPUS_SAMAMBAIA_NAME));
    }

    public void loadColemarBusLines(View v){
        loadBusLines(campi.get(CAMPUS_COLEMAR_NAME));
    }

    public void loadBusLines(Campus campus){
        Intent intent = new Intent(this, CampusActivity.class);
        Campus c2 = Campus.findById(Campus.class, campus.getId());
        intent.putExtra("campusId", campus.getId());
        this.startActivity(intent);
    }
}
