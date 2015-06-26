package br.ufg.inf.es.dsm.partiuufg.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.devspark.progressfragment.ProgressFragment;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.List;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.adapter.CampusSingleBusLinesAdapter;
import br.ufg.inf.es.dsm.partiuufg.dbModel.Campus;
import br.ufg.inf.es.dsm.partiuufg.dbModel.SingleBusLine;
import br.ufg.inf.es.dsm.partiuufg.http.EasyBusService;
import br.ufg.inf.es.dsm.partiuufg.http.RestBusServiceFactory;
import br.ufg.inf.es.dsm.partiuufg.model.BusLine;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Cleber on 25/06/2015.
 */
public class CampusBusLinesFragment extends ProgressFragment {
    private final String TAG = this.getClass().getName();

    private List<SingleBusLine> singleBusLineList;
    private SuperRecyclerView recList;
    private CampusSingleBusLinesAdapter campusSingleBusLinesAdapter;
    private Long campusId;

    public CampusBusLinesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        campusId = getArguments().getLong("campusId");
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

        Campus campus = Campus.findById(Campus.class, campusId);
        if(campus == null) {
            Log.e(TAG, "Campus NOT found.");
            setEmptyText("Campus não encontrado :(");
            setContentEmpty(true);
            Toast toast = Toast.makeText(getActivity(), "Campus não encontrado", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        getActivity().setTitle(campus.getName());
        Log.i(TAG, "Campus '" + campus.getName() + "' found.");
        singleBusLineList = SingleBusLine.find(SingleBusLine.class,
                "campus = ?", campus.getId().toString());
        Log.d(TAG, "Campus '" + campus.getName() + "' single bus lines amount: "
                + singleBusLineList.size());
        campusSingleBusLinesAdapter = new CampusSingleBusLinesAdapter(singleBusLineList, getActivity());
        recList.setAdapter(campusSingleBusLinesAdapter);
        setContentShown(true);

        for(final SingleBusLine singleBusLine: singleBusLineList){
            if(singleBusLine.getName() == null){
                Log.w(TAG, "Name of the bus line '" + singleBusLine.getNumber() + "' NOT found.");
                EasyBusService ebs = RestBusServiceFactory.getAdapter();
                ebs.getBusLine(singleBusLine.getNumber().toString(), new Callback<BusLine>() {
                    @Override
                    public void success(BusLine busLine, Response response) {
                        singleBusLine.setName(busLine.getName());
                        Log.d(TAG, "Name of the bus line '" + singleBusLine.getNumber() + "': "
                                + singleBusLine.getName() + " obtained.");
                        campusSingleBusLinesAdapter.notifyDataSetChanged();
                        Log.d(TAG, "Bus line '" + singleBusLine.getNumber() + "' updated.");
                        singleBusLine.save();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(TAG, "Unable to get the data about the bus line "
                                        + singleBusLine.getNumber());
                    }
                });
            }else{
                Log.d(TAG, "Name of the bus line '" + singleBusLine.getNumber() + "': "
                        + singleBusLine.getName());
            }
        }
        Log.d(TAG, "End of single bus lines reached.");
    }
}