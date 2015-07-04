package br.ufg.inf.es.dsm.partiuufg.fragment;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devspark.progressfragment.ProgressFragment;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.adapter.recyclerView.BusStopAdapter;
import br.ufg.inf.es.dsm.partiuufg.adapter.recyclerView.GeneralStopBusOnClickListenerFactory;
import br.ufg.inf.es.dsm.partiuufg.adapter.recyclerView.StopBusFromLineOnClickListenerFactory;
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
public class BusStopListFragment extends ProgressFragment {
    private final String TAG = BusStopListFragment.class.getSimpleName();

    public static final int DATABASE_MODE = 1;
    public static final int WEB_MODE = 2;

    private int mode;
    private Integer lineNumber;
    private SuperRecyclerView recList;
    private BusStopAdapter busStopAdapter;
    private ErrorView errorView;
    private LinearLayout content;

    public BusStopListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mode = getArguments().getInt("mode");
        if(WEB_MODE == mode){
            lineNumber = getArguments().getInt("lineNumber");
            Log.d(TAG, "Line number received: " + lineNumber);
        }
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
                refreshWebList();
            }
        });

        recList = (SuperRecyclerView) getContentView().findViewById(R.id.rec_list);
        LinearLayoutManager layout = new LinearLayoutManager(getActivity());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        layout.scrollToPosition(0);
        recList.setLayoutManager(layout);
        recList.getRecyclerView().setHasFixedSize(true);

        setContentShown(false);

        switch(mode){
            case DATABASE_MODE:
                loadByDatabase();
                break;
            case WEB_MODE:
                loadByWeb();
                break;
            default:
                List<SingleBusStop> singleBusStopList = new ArrayList<>();
                busStopAdapter = new BusStopAdapter(singleBusStopList, getActivity());
                Log.e(TAG, "Invalid mode in the BusStopListFragment.");
        }
    }

    public void refreshWebList() {
        setContentShown(false);
        loadByWeb();
    }

    public void refreshDatabaseList() {
        setContentShown(false);
        loadByDatabase();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mode == DATABASE_MODE) {
            refreshDatabaseList();
        }
    }

    public void loadByDatabase(){
        Log.i(TAG, "Loading bus stop list from database...");
        List<SingleBusStop> busStopList;
        try {
            busStopList = SingleBusStop.find(SingleBusStop.class, null, null, null,
                    "access_count DESC", "10" );
        } catch(SQLiteException e) {
            busStopList = new ArrayList<>();
        }

        busStopAdapter = new BusStopAdapter(busStopList, getActivity());
        busStopAdapter.setOnClickListenerFactory(new GeneralStopBusOnClickListenerFactory());
        recList.setAdapter(busStopAdapter);

        TextView noItens = (TextView) getContentView().findViewById(R.id.no_itens);
        noItens.setText(getString(R.string.no_top_access));

        if(busStopAdapter.getBusStops().size() == 0){
            noItens.setVisibility(View.VISIBLE);
            recList.setVisibility(View.GONE);
        } else {
            noItens.setVisibility(View.GONE);
            recList.setVisibility(View.VISIBLE);
        }

        createView();
    }

    public void createView() {
        content.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        setContentShown(true);
    }

    public void loadByWeb(){
        Log.i(TAG, "Loading bus stop list from web...");
        EasyBusService service = RestBusServiceFactory.getAdapter();
        service.getBusLine(lineNumber.toString(), new Callback<BusLine>() {
            @Override
            public void success(BusLine busline, Response response) {
                if(getActivity() == null) {
                    return;
                }

                List<SingleBusStop> singleBusStopList = new ArrayList<>();
                SingleBusStop singleBusStop;
                for (CompleteBusStop completeBusStop : busline.getCompleteBusStops()) {
                    singleBusStop = new SingleBusStop(completeBusStop);
                    singleBusStopList.add(singleBusStop);
                }
                Log.i(TAG, "List size: " + singleBusStopList.size());
                getActivity().setTitle(getString(R.string.title_activity_bus_line_stops,
                        busline.getNumber(), busline.getName()));
                busStopAdapter = new BusStopAdapter(singleBusStopList, getActivity());

                busStopAdapter.setOnClickListenerFactory(new StopBusFromLineOnClickListenerFactory(
                        lineNumber));
                recList.setAdapter(busStopAdapter);
                createView();
            }

            @Override
            public void failure(RetrofitError error) {
                if(getActivity() == null) {
                    return;
                }

                int statusCode;
                if( error.getResponse() == null ) {
                    statusCode = 408;
                } else {
                    statusCode = error.getResponse().getStatus();
                }

                errorView.setError(statusCode);
                content.setVisibility(View.GONE);
                errorView.setVisibility(View.VISIBLE);
                setContentShown(true);
            }
        });
    }
}