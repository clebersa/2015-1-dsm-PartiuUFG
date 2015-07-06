package br.ufg.inf.es.dsm.partiuufg.fragment;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devspark.progressfragment.ProgressFragment;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.poliveira.parallaxrecycleradapter.ParallaxRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.adapter.recyclerView.BusLineAdapter;
import br.ufg.inf.es.dsm.partiuufg.dbModel.SingleBusStop;
import br.ufg.inf.es.dsm.partiuufg.http.EasyBusService;
import br.ufg.inf.es.dsm.partiuufg.http.RestBusServiceFactory;
import br.ufg.inf.es.dsm.partiuufg.model.BusLine;
import br.ufg.inf.es.dsm.partiuufg.model.CompleteBusStop;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import tr.xip.errorview.ErrorView;

/**
 * Created by Bruno on 20/06/2015.
 */
public class BusLineWithBusStopFragment extends ProgressFragment {
    private static final String TAG = BusLineWithBusStopFragment.class.getSimpleName();

    private Integer busStopNumber;
    private CompleteBusStop completeBusStop;
    private SuperRecyclerView recList;
    private ParallaxRecyclerAdapter<BusLine> busLineAdapter;
    private BusLineAdapter busLineAdapterMethods;
    private ErrorView errorView;
    private LinearLayout content;
    private Integer errorStatus = 0;

    public BusLineWithBusStopFragment() {
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


        content = (LinearLayout) getContentView().findViewById(R.id.content);
        errorView = (ErrorView) getContentView().findViewById(R.id.error_view);
        errorView.setOnRetryListener(new ErrorView.RetryListener() {
            @Override
            public void onRetry() {
                refreshList(true);
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
                refreshList(false);
            }
        });

        if(savedInstanceState == null) {
            setContentShown(false);
            getStopBusDataFromWeb();
        } else {
            completeBusStop = (CompleteBusStop) savedInstanceState.getSerializable("completeBusStop");
            errorStatus = savedInstanceState.getInt("errorStatus");
            if(completeBusStop == null && errorStatus == 0) {
                setContentShown(false);
                getStopBusDataFromWeb();
            } else {
                createView();
            }
        }

        List<BusLine> data = BusLineAdapter.getCreatedData(completeBusStop);
        busLineAdapter = new ParallaxRecyclerAdapter<>(data);
        busLineAdapterMethods = new BusLineAdapter(busLineAdapter, completeBusStop, getActivity());
        updateParallax();
        recList.setAdapter(busLineAdapter);
    }

    public void updateParallax() {
        if(completeBusStop != null) {
            View v = LayoutInflater.from(getActivity()).inflate(
                    R.layout.bus_stop_list_header, recList.getRecyclerView(), false);

            TextView address = (TextView) v.findViewById(R.id.tvAddress);
            address.setText(completeBusStop.getAddress().trim());

            TextView searchTime = (TextView) v.findViewById(R.id.tvSearchTime);
            searchTime.setText(getString(R.string.last_search_time,
                    completeBusStop.getSearchDateFormatted()));

            busLineAdapter.setParallaxHeader(v, recList.getRecyclerView());
        }
    }

    public void refreshList(Boolean reloadContent) {
        if(reloadContent) {
            setContentShown(false);
        }
        getStopBusDataFromWeb();
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
        if(busLineAdapterMethods != null) {
            busLineAdapterMethods.cancelRefreshTimer();
        }
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("completeBusStop", completeBusStop);
        outState.putInt("errorStatus", errorStatus);
        super.onSaveInstanceState(outState);
    }

    public void createView() {
        if(errorStatus > 0) {
            content.setVisibility(View.GONE);
            errorView.setError(errorStatus);
            errorView.setVisibility(View.VISIBLE);
        } else {
            content.setVisibility(View.VISIBLE);
            errorView.setVisibility(View.GONE);
        }
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

                errorStatus = 0;
                completeBusStop = vCompleteBusStop;

                increaseBusStopAccessCounter();

                List<BusLine> data = BusLineAdapter.getCreatedData(completeBusStop);
                busLineAdapter = new ParallaxRecyclerAdapter<>(data);
                busLineAdapterMethods.setCompleteBusStop(completeBusStop);
                busLineAdapterMethods.setAdapter(busLineAdapter);
                updateParallax();
                recList.setAdapter(busLineAdapter);

                createView();
            }

            @Override
            public void failure(RetrofitError error) {
                if (getActivity() == null) {
                    return;
                }

                if (error.getResponse() == null) {
                    errorStatus = 408;
                } else {
                    errorStatus = error.getResponse().getStatus();
                    if(errorStatus == 400) {
                        Toast toast = Toast.makeText(getActivity(),
                                getString(R.string.bus_stop_not_found),
                                Toast.LENGTH_LONG);
                        toast.show();
                        getActivity().finish();
                        return;
                    }
                }
                createView();
            }
        });
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