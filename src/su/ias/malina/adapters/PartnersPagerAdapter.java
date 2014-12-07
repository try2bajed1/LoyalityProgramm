package su.ias.malina.adapters;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 07.03.14
 * Time: 12:45
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import su.ias.malina.activities.fragments.PartnerDescriptionFragment;

import java.util.ArrayList;


public class PartnersPagerAdapter extends FragmentPagerAdapter {

    private int pageCount = 0;

    private ArrayList<Integer> parntersIdsArr;

    /** Constructor of the class */
    public PartnersPagerAdapter(FragmentManager fm, ArrayList<Integer>  partnersIdsArr) {

        super(fm);

        this.parntersIdsArr = partnersIdsArr;
        pageCount = partnersIdsArr.size();
    }


    /** This method will be invoked when a page is requested to create */
    public Fragment getItem(int position) {

        PartnerDescriptionFragment descrFragment = new PartnerDescriptionFragment();

        Bundle args = new Bundle();
        args.putInt(PartnerDescriptionFragment.KEY_ID, parntersIdsArr.get(position));
        descrFragment.setArguments(args); //setArguments can only be called before the Fragment is attached to the Activity.
        return descrFragment;
    }


    /** Returns the number of pages */
    @Override
    public int getCount() {
        return pageCount;
    }


}