package br.ufg.inf.es.dsm.partiuufg.fragment;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.devspark.progressfragment.ProgressFragment;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.adapter.BusStopAdapter;
import br.ufg.inf.es.dsm.partiuufg.dbModel.SingleBusStop;

/**
 * Created by Bruno on 20/06/2015.
 */
public class BusStopListFragment extends ProgressFragment {
    private SuperRecyclerView recList;

    public BusStopListFragment() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(R.layout.fragment_bus_stop_list);

        recList = (SuperRecyclerView) getContentView().findViewById(R.id.bus_stop_list);
        LinearLayoutManager layout = new LinearLayoutManager(getActivity());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        layout.scrollToPosition(0);
        recList.setLayoutManager(layout);
        recList.getRecyclerView().setHasFixedSize(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateBusStopList();
    }

    public void updateBusStopList() {
        List<SingleBusStop> busStopList;
        try {
            busStopList = SingleBusStop.find(SingleBusStop.class, null, null, null,
                    "access_count DESC", "10" );
        } catch(SQLiteException e) {
            busStopList = new ArrayList<>();
        }

        BusStopAdapter busStopAdapter = new BusStopAdapter(busStopList, getActivity());
        recList.setAdapter(busStopAdapter);

        setContentEmpty(false);
        setContentShown(true);
    }
}