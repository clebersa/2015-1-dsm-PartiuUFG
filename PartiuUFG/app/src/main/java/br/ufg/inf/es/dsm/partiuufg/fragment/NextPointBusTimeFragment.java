package br.ufg.inf.es.dsm.partiuufg.fragment;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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
import tr.xip.errorview.ErrorView;

/**
 * Created by Bruno on 20/06/2015.
 */
public class NextPointBusTimeFragment extends ProgressFragment {
    private static final String TAG = NextPointBusTimeFragment.class.getSimpleName();

    private CompleteBusStop completeBusStop;
    private SuperRecyclerView recList;
    private BusLineAdapter busLineAdapter;
    private Integer busStopNumber;
    private ErrorView errorView;
    private LinearLayout content;
    private Integer showErrorStatus = 0;

    public NextPointBusTimeFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(R.layout.fragment_next_point_bus_time);

        busStopNumber = getArguments().getInt("busStopNumber");

        content = (LinearLayout) getContentView().findViewById(R.id.content);

        errorView = (ErrorView) getContentView().findViewById(R.id.error_view);
        errorView.setOnRetryListener(new ErrorView.RetryListener() {
            @Override
            public void onRetry() {
                refreshBusStop();
            }
        });

        recList = (SuperRecyclerView) getContentView().findViewById(R.id.rec_list);
        LinearLayoutManager layout = new LinearLayoutManager(getActivity());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        layout.scrollToPosition(0);
        recList.setLayoutManager(layout);
        recList.getRecyclerView().setHasFixedSize(true);
        recList.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshBusStop();
            }
        });

        setContentShown(false);

        if(savedInstanceState == null) {
            getStopBusDataFromWeb();
        } else {
            try {
                completeBusStop = (CompleteBusStop) savedInstanceState
                        .getSerializable("completeBusStop");
                busLineAdapter = new BusLineAdapter(completeBusStop, getActivity());
            } catch(NullPointerException e) {
                Log.e(TAG, "Can't load completeBusStop from memory");
            }

            try {
                showErrorStatus = savedInstanceState.getInt("showError");
            } catch(NullPointerException e) {
                Log.e(TAG, "Can't load show error status");
            }

            if(showErrorStatus > 0) {
                showErrorView();
            } else {
                createView();
            }
        }
    }

    public void refreshBusStop() {
        setContentShown(false);
        getStopBusDataFromWeb();
    }

    public void createView() {
        try {
            recList.setAdapter(busLineAdapter);
            TextView address = (TextView) getView().findViewById(R.id.tvAddress);
            address.setText(completeBusStop.getAddress());

            TextView searchTime = (TextView) getView().findViewById(R.id.tvSearchTime);
            searchTime.setText(getString(R.string.last_search_time,
                    completeBusStop.getSearchDateFormatted()));

            content.setVisibility(View.VISIBLE);
            errorView.setVisibility(View.GONE);
            setContentShown(true);
        } catch(NullPointerException | IllegalStateException e) {
            Log.e(TAG, "Can't create nextpoint view: " + e.getMessage());
            getActivity().finish();
        }
    }

    public void showErrorView() {
        errorView.setError(showErrorStatus);
        content.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        setContentShown(true);
    }

    public void getStopBusDataFromWeb() {
        EasyBusService service = RestBusServiceFactory.getAdapter();
        service.getPoint(busStopNumber.toString(), new Callback<CompleteBusStop>() {
            @Override
            public void success(CompleteBusStop vCompleteBusStop, Response response) {
                if (getActivity() == null) {
                    return;
                }

                Log.d(TAG, "Complete bus stop " + vCompleteBusStop.getNumber() + " loaded.");
                completeBusStop = vCompleteBusStop;
                if (busLineAdapter != null) {
                    busLineAdapter.cancelRefreshTimer();
                }
                busLineAdapter = new BusLineAdapter(completeBusStop, getActivity());
                increaseBusStopAccessCounter();
                createView();
            }

            @Override
            public void failure(RetrofitError error) {
                if (getActivity() == null) {
                    return;
                }

                if (error.getResponse() == null) {
                    showErrorStatus = 408;
                } else {
                    showErrorStatus = error.getResponse().getStatus();
                }

                showErrorView();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if( busLineAdapter != null ) {
            busLineAdapter.notifyItemInserted(0);
            busLineAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        if(busLineAdapter != null) {
            busLineAdapter.cancelRefreshTimer();
        }
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("completeBusStop", completeBusStop);
        outState.putInt("showError", showErrorStatus);
        super.onSaveInstanceState(outState);
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