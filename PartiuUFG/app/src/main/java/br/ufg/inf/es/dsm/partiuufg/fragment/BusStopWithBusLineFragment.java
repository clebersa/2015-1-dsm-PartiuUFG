package br.ufg.inf.es.dsm.partiuufg.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.devspark.progressfragment.ProgressFragment;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.ArrayList;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.adapter.recyclerView.BusStopAdapter;
import br.ufg.inf.es.dsm.partiuufg.http.EasyBusService;
import br.ufg.inf.es.dsm.partiuufg.http.RestBusServiceFactory;
import br.ufg.inf.es.dsm.partiuufg.model.BusLine;
import br.ufg.inf.es.dsm.partiuufg.model.BusStopWithLine;
import br.ufg.inf.es.dsm.partiuufg.model.CompleteBusStop;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import tr.xip.errorview.ErrorView;

/**
 * Created by Bruno on 20/06/2015.
 */
public class BusStopWithBusLineFragment extends ProgressFragment {
    private final String TAG = BusStopWithBusLineFragment.class.getSimpleName();

    private BusLine busLine;
    private Integer lineNumber;
    private SuperRecyclerView recList;
    private BusStopAdapter<BusStopWithLine> adapter;
    private ArrayList<BusStopWithLine> busStopList;
    private ErrorView errorView;
    private LinearLayout content;
    private Integer errorStatus = 0;

    public BusStopWithBusLineFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lineNumber = getArguments().getInt("lineNumber");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(R.layout.fragment_list);

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
            loadListByWeb();
        } else {
            errorStatus = savedInstanceState.getInt("errorStatus");
            busLine = savedInstanceState.getParcelable("busLine");
            busStopList = savedInstanceState.getParcelableArrayList("busStopList");
            createView();
        }

        adapter = new BusStopAdapter<>(busStopList, lineNumber);
        recList.setAdapter(adapter);
    }

    public void updateTitle() {
        try {
            getActivity().setTitle(getString(R.string.title_activity_bus_line_stops,
                    busLine.getNumber(), busLine.getName()));
        } catch(Exception e) {
            Log.e(TAG, "Could not set title");
        }
    }

    public void refreshList(Boolean fullRefresh) {
        if(fullRefresh) {
            setContentShown(false);
            loadListByWeb();
        } else {
            for(BusStopWithLine busStop : busStopList) {
                busStop.setLoaded(false);
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        if(adapter != null) {
            adapter.cancelRefreshTimer();
        }
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("busStopList", busStopList);
        outState.putInt("errorStatus", errorStatus);
        outState.putParcelable("busLine", busLine);
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
            updateTitle();
        }
        setContentShown(true);
    }

    public void loadListByWeb() {
        EasyBusService service = RestBusServiceFactory.getAdapter();
        service.getBusLine(lineNumber.toString(), new Callback<BusLine>() {
            @Override
            public void success(BusLine tBusLine, Response response) {
                if(getActivity() == null) {
                    return;
                }
                errorStatus = 0;
                busLine = tBusLine;
                busStopList = new ArrayList<>();
                BusStopWithLine tmpBusStopWithLine;
                for (CompleteBusStop completeBusStop : busLine.getCompleteBusStops()) {
                    tmpBusStopWithLine = new BusStopWithLine(completeBusStop);
                    busStopList.add(tmpBusStopWithLine);
                }
                adapter.setBusStops(busStopList);
                createView();
            }

            @Override
            public void failure(RetrofitError error) {
                if(getActivity() == null) {
                    return;
                }

                if( error.getResponse() == null ) {
                    errorStatus = 408;
                } else {
                    errorStatus = error.getResponse().getStatus();
                }
                createView();
            }
        });
    }
}