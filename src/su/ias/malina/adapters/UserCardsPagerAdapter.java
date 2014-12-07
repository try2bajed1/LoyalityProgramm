package su.ias.malina.adapters;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 07.03.14
 * Time: 12:45
 */

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import su.ias.malina.activities.fragments.UserCardsFragment;


public class UserCardsPagerAdapter extends FragmentPagerAdapter{

    final int PAGE_COUNT = 2;


    public UserCardsPagerAdapter(FragmentManager fm) {
        super(fm);
    }



    public ListFragment getItem(int position) {

        /*UserCardsFragment userCardsFragment = new UserCardsFragment();
        Bundle args = new Bundle();

        // use same fragment class, but put different values to bundle
        switch(position){

            case 0:
                args.putInt(UserCardsFragment.CARD_TYPE, UserCardsFragment.MODE_ACTIVE_CARDS);
                userCardsFragment.setArguments(args); //setArguments can only be called before the Fragment is attached to the Activity.
            return userCardsFragment;

            case 1:
                args.putInt(UserCardsFragment.CARD_TYPE, UserCardsFragment.MODE_ALL_CARDS);
                userCardsFragment.setArguments(args); //setArguments can only be called before the Fragment is attached to the Activity.
            return userCardsFragment;
        }


        return null;*/

        return new UserCardsFragment();
    }


    @Override
    public int getCount() {
        return PAGE_COUNT;
    }



    @Override
    public String getPageTitle(int index) {
        return index == 0 ? "Активные": "Заблокированные";
    }


}