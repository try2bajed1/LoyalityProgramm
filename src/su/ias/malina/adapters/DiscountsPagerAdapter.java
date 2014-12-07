package su.ias.malina.adapters;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 07.03.14
 * Time: 12:45
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import su.ias.malina.activities.fragments.DiscountsFragment;


public class DiscountsPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;


    public DiscountsPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    public Fragment getItem(int position) {
        return new DiscountsFragment();
    }


    @Override
    public int getCount() {
        return PAGE_COUNT;
    }


    @Override
    public String getPageTitle(int index) {
        return index == 0 ? "Online": "Offline";
    }

}