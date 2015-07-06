package br.ufg.inf.es.dsm.partiuufg.fragment.page;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.adapter.recyclerView.BusStopAdapter;
import br.ufg.inf.es.dsm.partiuufg.dbModel.SingleBusStop;

/**
 * Created by Bruno on 02/07/2015.
 */
public class MostVisitedPageFragment extends Fragment {
    private static final String TAG = MostVisitedPageFragment.class.getSimpleName();
    private SuperRecyclerView recList;
    private BusStopAdapter<SingleBusStop> adapter;
    private TextView noItens;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_page_most_visited,container,false);

        noItens = (TextView) v.findViewById(R.id.no_itens);
        noItens.setText(getString(R.string.no_top_access));

        recList = (SuperRecyclerView) v.findViewById(R.id.rec_list);
        LinearLayoutManager layout = new LinearLayoutManager(getActivity());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        layout.scrollToPosition(0);
        recList.setLayoutManager(layout);
        recList.getRecyclerView().setHasFixedSize(true);

        adapter = new BusStopAdapter<>(null, null);
        recList.setAdapter(adapter);

        loadByDatabase();
        return v;
    }

    public void loadByDatabase(){
        List<SingleBusStop> mostVisitedBusStopList;
        try {
            mostVisitedBusStopList = SingleBusStop.find(SingleBusStop.class, null, null, null,
                    "access_count DESC", "10" );
        } catch(SQLiteException e) {
            mostVisitedBusStopList = new ArrayList<>();
        }

        adapter.setBusStops(mostVisitedBusStopList);
        adapter.notifyDataSetChanged();

        if(adapter.getBusStops().size() == 0) {
            noItens.setVisibility(View.VISIBLE);
            recList.setVisibility(View.GONE);
        } else {
            noItens.setVisibility(View.GONE);
            recList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadByDatabase();
    }
}
