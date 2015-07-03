package br.ufg.inf.es.dsm.partiuufg.fragment.page;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.fragment.BusStopListFragment;

/**
 * Created by Bruno on 02/07/2015.
 */
public class MostVisitedPageFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.home_page_most_visited,container,false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState == null) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            Fragment fragment = new BusStopListFragment();
            Bundle b = new Bundle();
            b.putInt("mode", BusStopListFragment.DATABASE_MODE);
            fragment.setArguments(b);
            ft.add(R.id.most_visited_stop_bus, fragment);
            ft.commit();
        }
    }
}
