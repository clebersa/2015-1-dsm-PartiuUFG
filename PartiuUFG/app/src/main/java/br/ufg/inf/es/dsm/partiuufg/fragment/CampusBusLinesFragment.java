package br.ufg.inf.es.dsm.partiuufg.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.devspark.progressfragment.ProgressFragment;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.adapter.BusLineAdapter;
import br.ufg.inf.es.dsm.partiuufg.adapter.CampusBusLinesAdapter;
import br.ufg.inf.es.dsm.partiuufg.model.Campus;
import br.ufg.inf.es.dsm.partiuufg.model.Point;

/**
 * Created by Cleber on 25/06/2015.
 */
public class CampusBusLinesFragment extends ProgressFragment {
    private Campus campus;
    private SuperRecyclerView recList;

    public CampusBusLinesFragment() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(R.layout.fragment_bus_line);

        recList = (SuperRecyclerView) getContentView().findViewById(R.id.bus_lines_list);
        LinearLayoutManager layout = new LinearLayoutManager(getActivity());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        layout.scrollToPosition(0);
        recList.setLayoutManager(layout);
        recList.getRecyclerView().setHasFixedSize(true);

        setContentShown(false);
    }

    public void updatePointData() {
        if( getContentView() == null ) {
            return;
        }

        CampusBusLinesAdapter campusBusLinesAdapterbusLineAdapter = new CampusBusLinesAdapter(
                campus.getBusLines(), getActivity());
        recList.setAdapter(campusBusLinesAdapterbusLineAdapter);

        setContentEmpty(false);
        setContentShown(true);
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
        updatePointData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("campus", campus);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState != null) {
            try {
                setCampus((Campus) savedInstanceState.getSerializable("campus"));
            } catch( NullPointerException e) {}
        }
    }
}