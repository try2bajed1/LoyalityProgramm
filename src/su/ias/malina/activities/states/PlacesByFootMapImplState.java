package su.ias.malina.activities.states;

import android.app.Dialog;
import android.util.Log;
import su.ias.malina.R;
import su.ias.malina.activities.StatesActivity;
import su.ias.malina.dialogs.FilterDialog;
import su.ias.malina.interfaces.IState;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 25.08.2014
 * Time: 17:00
 */

public class PlacesByFootMapImplState implements IState {

    private final StatesActivity activity;

    public PlacesByFootMapImplState(StatesActivity activity) {
        this.activity = activity;
    }


    @Override
    public void setModeByUser() {
        //nop
    }


    @Override
    public void setPlacesByCarMapImpl() {

    }



    @Override
    public void setPlacesByCarListImpl() {

    }



    @Override
    public void setPlacesByFootMapImpl() {

/*
        if(activity.actionBar.getNavigationMode() != ActionBar.NAVIGATION_MODE_TABS) {
            activity.actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        }
*/

        activity.showTabs();
        activity.viewPager.setCurrentItem(1);

//        activity.actionBar.setSelectedNavigationItem(1); // hardcode

        activity.setActionBarLoading(true);

        activity.loadMapFrame();
    }




    @Override
    public void updateActionBarMenu() {

        //todo: может меню передавать ссылкой
        if(activity.menu == null) {
            Log.i("@","foot map update menu #### return");
            return;
        }

        Log.e("@","foot map update menu");

        activity.menu.findItem(R.id.action_filter).setVisible(true);
        activity.menu.findItem(R.id.action_show_as_list).setVisible(true);
        activity.menu.findItem(R.id.action_map).setVisible(false);
        activity.menu.findItem(R.id.action_add_card).setVisible(false);

    }



    @Override
    public void setPlacesByFootListImpl() {

    }

    @Override
    public void setOnSofaListImpl() {

    }

    @Override
    public void setDiscountsOffline() {

    }


    @Override
    public void setDiscountsOnline() {

    }


    @Override
    public void setCardsActive() {

    }



    @Override
    public void setCardsAll() {

    }



    @Override
    public void updateTabs() {

    }



    @Override
    public void updateFragment() {

    }



    @Override
    public void updateNavigationPanel() {

    }

    @Override
    public void initGuide() {
        activity.initGuideForSelectedMode();
    }


    @Override
    public Dialog getFilterDialog() {
        return new FilterDialog(activity, this);
    }




    @Override
    public void applyFilter() {
        Log.i("@", "filter places by foot on map");
        // todo: save to db
        activity.loadMapFrame();
    }
}
