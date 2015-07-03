package br.ufg.inf.es.dsm.partiuufg.fragment.page;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.adapter.recyclerView.MonitoringBusLineStopAdapter;
import br.ufg.inf.es.dsm.partiuufg.dbModel.SingleGCMBusStopLine;
import br.ufg.inf.es.dsm.partiuufg.model.CompleteGCMBusStopLine;

/**
 * Created by Bruno on 02/07/2015.
 */
public class MonBusStopLinesPageFragment extends Fragment {
    private static final String TAG = MonBusStopLinesPageFragment.class.getSimpleName();
    private SuperRecyclerView recList;
    private MonitoringBusLineStopAdapter adapter;
    private ArrayList<CompleteGCMBusStopLine> monBusStopLines;
    private TextView noItens;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_page_monitoring, container, false);

        noItens = (TextView) v.findViewById(R.id.no_itens);
        noItens.setText(getString(R.string.no_monitoring_bus_stop_lines));

        recList = (SuperRecyclerView) v.findViewById(R.id.rec_list);
        LinearLayoutManager layout = new LinearLayoutManager(getActivity());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        layout.scrollToPosition(0);
        recList.setLayoutManager(layout);
        recList.getRecyclerView().setHasFixedSize(true);

        if(savedInstanceState == null || !savedInstanceState.containsKey("monBusStopLines")) {
            monBusStopLines = new ArrayList<>();
        } else {
            monBusStopLines = savedInstanceState.getParcelableArrayList("monBusStopLines");
        }

        adapter = new MonitoringBusLineStopAdapter(monBusStopLines, getActivity());
        recList.setAdapter(adapter);
        return v;
    }

    public void updateList() {
        List<SingleGCMBusStopLine> sMonList =  SingleGCMBusStopLine.listAll(SingleGCMBusStopLine.class);
        for( SingleGCMBusStopLine sMon : sMonList ) {
            if(!adapter.itemExists(sMon)) {
                monBusStopLines.add(CompleteGCMBusStopLine.createBySingle(sMon));
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList();
        if(monBusStopLines.size() == 0) {
            noItens.setVisibility(View.VISIBLE);
            recList.setVisibility(View.GONE);
        } else {
            noItens.setVisibility(View.GONE);
            recList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("monBusStopLines", monBusStopLines);
        super.onSaveInstanceState(outState);
    }
}