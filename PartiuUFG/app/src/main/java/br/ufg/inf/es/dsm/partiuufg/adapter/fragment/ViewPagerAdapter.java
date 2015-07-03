package br.ufg.inf.es.dsm.partiuufg.adapter.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import br.ufg.inf.es.dsm.partiuufg.fragment.page.CampiPageFragment;
import br.ufg.inf.es.dsm.partiuufg.fragment.page.MonBusStopLinesPageFragment;
import br.ufg.inf.es.dsm.partiuufg.fragment.page.MostVisitedPageFragment;

/**
 * Created by hp1 on 21-01-2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<String> titles;
    private HashMap<Integer, Fragment> pages = new HashMap<>();

    public ViewPagerAdapter(FragmentManager fm, ArrayList<String> titles) {
        super(fm);
        this.titles = titles;
    }

    @Override
    public synchronized Fragment getItem(int position) {
        if(pages.containsKey(position)) {
            return pages.get(position);
        }

        Fragment page;
        switch(position) {
            case 0:
                page = new MostVisitedPageFragment();
                break;
            case 1:
            default:
                page = new CampiPageFragment();
                break;
            case 2:
                page = new MonBusStopLinesPageFragment();
                break;
        }

        pages.put(position, page);
        return page;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public int getCount() {
        return titles.size();
    }
}