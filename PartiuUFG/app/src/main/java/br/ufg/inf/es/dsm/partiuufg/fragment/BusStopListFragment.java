package br.ufg.inf.es.dsm.partiuufg.fragment;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.devspark.progressfragment.ProgressFragment;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.adapter.BusStopAdapter;
import br.ufg.inf.es.dsm.partiuufg.dbModel.SingleBusStop;
import br.ufg.inf.es.dsm.partiuufg.http.EasyBusService;
import br.ufg.inf.es.dsm.partiuufg.http.RestBusServiceFactory;
import br.ufg.inf.es.dsm.partiuufg.model.BusLine;
import br.ufg.inf.es.dsm.partiuufg.model.CompleteBusStop;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Bruno on 20/06/2015.
 */
public class BusStopListFragment extends ProgressFragment {
    private final String TAG = this.getClass().getName();

    public static final int DATABASE_MODE = 1;
    public static final int WEB_MODE = 2;

    private int mode;
    private Integer lineNumber;
    private SuperRecyclerView recList;
    private BusStopAdapter busStopAdapter;

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
                getActivity().setTitle("Linha " + lineNumber);
                loadByWeb();
                break;
            default:
                List<SingleBusStop> singleBusStopList = new ArrayList<>();
                busStopAdapter = new BusStopAdapter(singleBusStopList, getActivity());
                Log.e(TAG, "Invalid mode in the BusStopListFragment.");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mode == DATABASE_MODE) loadByDatabase();
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
        recList.setAdapter(busStopAdapter);
        setContentEmpty(false);
        setContentShown(true);
    }

    public void loadByWeb(){
        Log.i(TAG, "Loading bus stop list from web...");
        EasyBusService service = RestBusServiceFactory.getAdapter();
        service.getBusLine(lineNumber.toString(), new Callback<BusLine>() {
            @Override
            public void success(BusLine busline, Response response) {
                List<SingleBusStop> singleBusStopList = new ArrayList<>();
                SingleBusStop singleBusStop;
                for (CompleteBusStop completeBusStop : busline.getCompleteBusStops()) {
                    singleBusStop = new SingleBusStop(completeBusStop);
                    singleBusStopList.add(singleBusStop);
                }
                Log.i(TAG, "List size: " + singleBusStopList.size());
                getActivity().setTitle(getActivity().getTitle() + ": " + busline.getName());
                busStopAdapter = new BusStopAdapter(singleBusStopList, getActivity());
                recList.setAdapter(busStopAdapter);
                setContentEmpty(false);
                setContentShown(true);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(getActivity(),
                        "Linha não encontrada.", Toast.LENGTH_SHORT);
                toast.show();
                Log.i(TAG, "Unable to get bus stop list");
                setContentEmpty(false);
                setContentShown(true);
            }
        });
    }
}