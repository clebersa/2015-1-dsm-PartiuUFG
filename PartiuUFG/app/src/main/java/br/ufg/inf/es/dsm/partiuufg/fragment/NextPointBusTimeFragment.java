package br.ufg.inf.es.dsm.partiuufg.fragment;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.devspark.progressfragment.ProgressFragment;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.activity.BusStopActivity;
import br.ufg.inf.es.dsm.partiuufg.adapter.BusLineAdapter;
import br.ufg.inf.es.dsm.partiuufg.dbModel.SingleBusStop;
import br.ufg.inf.es.dsm.partiuufg.http.EasyBusService;
import br.ufg.inf.es.dsm.partiuufg.http.RestBusServiceFactory;
import br.ufg.inf.es.dsm.partiuufg.model.CompleteBusStop;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Bruno on 20/06/2015.
 */
public class NextPointBusTimeFragment extends ProgressFragment {
    private final String TAG = this.getClass().getName();

    private CompleteBusStop completeBusStop;
    private SuperRecyclerView recList;
    private BusLineAdapter busLineAdapter;
    private Integer busStopNumber;

    public NextPointBusTimeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        busStopNumber = getArguments().getInt("busStopNumber");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(R.layout.fragment_next_point_bus_time);

        recList = (SuperRecyclerView) getContentView().findViewById(R.id.rec_list);
        LinearLayoutManager layout = new LinearLayoutManager(getActivity());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        layout.scrollToPosition(0);
        recList.setLayoutManager(layout);
        recList.getRecyclerView().setHasFixedSize(true);

        setContentShown(false);

        EasyBusService service = RestBusServiceFactory.getAdapter();
        service.getPoint(busStopNumber.toString(), new Callback<CompleteBusStop>() {
            @Override
            public void success(CompleteBusStop vCompleteBusStop, Response response) {
                completeBusStop = vCompleteBusStop;
                Log.e(TAG, "Complete bus stop "+completeBusStop.getNumber() +" loaded.");
                busLineAdapter = new BusLineAdapter(completeBusStop, getActivity());
                recList.setAdapter(busLineAdapter);
                increaseBusStopAccessCounter();

                TextView address = (TextView) getView().findViewById(R.id.tvAddress);
                address.setText(completeBusStop.getAddress());
                TextView searchTime = (TextView) getView().findViewById(R.id.tvSearchTime);
                searchTime.setText(getString(R.string.last_search_time) + " "
                        + completeBusStop.getSearchDateFormatted());
                setContentShown(true);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(getActivity(),
                        "Ponto não encontrado", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("completeBusStop", completeBusStop);
        super.onSaveInstanceState(outState);
        Log.d(TAG, "Complete bus stop "+completeBusStop.getNumber() +" saved.");
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState != null) {
            try {
                completeBusStop = ((CompleteBusStop) savedInstanceState.getSerializable("completeBusStop"));
                Log.d(TAG, "Complete bus stop "+completeBusStop.getNumber() +" restored.");
                busLineAdapter.notifyDataSetChanged();
            } catch( NullPointerException e) {}
        }
    }

    public void increaseBusStopAccessCounter() {
        List<SingleBusStop> accessList;
        try {
            accessList = SingleBusStop.find(SingleBusStop.class,
                    "number = ?", busStopNumber.toString());
        } catch(SQLiteException e) {
            accessList = new ArrayList<>();
        }

        if(accessList.size() > 0 ) {
            for (SingleBusStop access : accessList) {
                access.setAddress(completeBusStop.getAddress());
                access.setReference(completeBusStop.getReferenceLocation());
                access.setLastSearchDate(completeBusStop.getSearchDate());
                access.setAccessCount(access.getAccessCount() + 1);
                Log.d(TAG, "Counter " + busStopNumber + " increased: " + access.getAccessCount());
                access.save();
            }
        } else {
            SingleBusStop access = new SingleBusStop(busStopNumber, completeBusStop.getAddress(),
                    completeBusStop.getReferenceLocation(), completeBusStop.getSearchDate(), (long) 1);
            access.save();
            Log.d(TAG, "Counter " + busStopNumber + " increased: " + access.getAccessCount());
        }
    }
}