package br.ufg.inf.es.dsm.partiuufg.activity;

import android.app.ActivityManager;
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
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.adapter.fragment.ViewPagerAdapter;
import br.ufg.inf.es.dsm.partiuufg.dbModel.Campus;
import br.ufg.inf.es.dsm.partiuufg.dbModel.SingleBusLine;
import br.ufg.inf.es.dsm.partiuufg.fragment.BusStopListFragment;
import br.ufg.inf.es.dsm.partiuufg.fragment.page.CampiPageFragment;
import br.ufg.inf.es.dsm.partiuufg.service.GCMServer;
import br.ufg.inf.es.dsm.partiuufg.service.RegistrationIntentService;
import br.ufg.inf.es.dsm.partiuufg.view.SlidingTabLayout;


public class HomeActivity extends AbstractActivity {
    private ViewPager pager;
    private ViewPagerAdapter adapter;
    private SlidingTabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<String> titles = new ArrayList<>();
        titles.add("MAIS ACESSADOS");
        titles.add("CAMPI");
        titles.add("MONITORADOS");

        adapter =  new ViewPagerAdapter(getSupportFragmentManager(), titles);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        pager.setCurrentItem(1);

        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);

        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        tabs.setViewPager(pager);

        if (checkPlayServices(false)) {
            if(!isMyServiceRunning(GCMServer.class)) {
                Intent gcmServerIntent = new Intent(this, GCMServer.class);
                startService(gcmServerIntent);
            }

            SharedPreferences sharedPreferences = PreferenceManager.
                    getDefaultSharedPreferences(getBaseContext());
            String gcmToken = sharedPreferences.getString("gcmToken", null);

            if(gcmToken == null) {
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            }
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void setActivityContentView() {
        setContentView(R.layout.activity_home);
    }

    @Override
    protected Boolean haveBackButton() {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
