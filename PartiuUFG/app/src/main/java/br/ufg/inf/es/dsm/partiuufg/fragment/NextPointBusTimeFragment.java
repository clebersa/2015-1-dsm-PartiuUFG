package br.ufg.inf.es.dsm.partiuufg.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.devspark.progressfragment.ProgressFragment;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.adapter.BusLineAdapter;
import br.ufg.inf.es.dsm.partiuufg.model.Point;

/**
 * Created by Bruno on 20/06/2015.
 */
public class NextPointBusTimeFragment extends ProgressFragment {
    private Point point;
    private SuperRecyclerView recList;

    public NextPointBusTimeFragment() {

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
    }

    public void updatePointData() {
        if( getContentView() == null ) {
            return;
        }

        BusLineAdapter busLineAdapter = new BusLineAdapter(point, getActivity());
        recList.setAdapter(busLineAdapter);

        setContentEmpty(false);
        setContentShown(true);
    }

    public void setPoint(Point point) {
        this.point = point;
        updatePointData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("point", point);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState != null) {
            try {
                setPoint((Point) savedInstanceState.getSerializable("point"));
            } catch( NullPointerException e) {}
        }
    }
}