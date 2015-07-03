package br.ufg.inf.es.dsm.partiuufg.adapter.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.ufg.inf.es.dsm.partiuufg.fragment.page.CampiPageFragment;
import br.ufg.inf.es.dsm.partiuufg.fragment.page.MostVisitedPageFragment;

/**
 * Created by hp1 on 21-01-2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    CharSequence Titles[];
    int NumbOfTabs;
    HashMap<Integer, Fragment> pages = new HashMap<>();

    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
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
        }

        pages.put(position, page);
        return page;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}