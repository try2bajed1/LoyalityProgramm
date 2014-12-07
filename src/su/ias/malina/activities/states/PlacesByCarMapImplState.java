package su.ias.malina.activities.states;

import android.app.Dialog;
import android.util.Log;
import android.widget.Toast;
import su.ias.malina.R;
import su.ias.malina.activities.StatesActivity;
import su.ias.malina.interfaces.IState;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 25.08.2014
 * Time: 17:00
 */

public class PlacesByCarMapImplState implements IState {

    private final StatesActivity activity;


    public PlacesByCarMapImplState(StatesActivity activity) {
        this.activity = activity;
    }


    @Override
    public void setModeByUser() {
        //nop
    }


    @Override
    public void setPlacesByCarMapImpl() {

        /*if(activity.actionBar.getNavigationMode() != ActionBar.NAVIGATION_MODE_TABS) {
           activity.actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        }*/


        activity.showTabs();
        //activity.actionBar.setSelectedNavigationItem(0);
          activity.viewPager.setCurrentItem(0);

        activity.setActionBarLoading(true);

        activity.loadMapFrame();
    }



    @Override
    public void setPlacesByCarListImpl() {

    }

    @Override
    public void setPlacesByFootMapImpl() {

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
    public void updateActionBarMenu() {

        if(activity.menu == null){
            Toast.makeText(activity, "MEnu", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.e("@", "car map update menu");

        activity.menu.findItem(R.id.action_show_as_list).setVisible(true);
        activity.menu.findItem(R.id.action_filter).setVisible(false); // для машины не показывам фильтр, тк. там один партнер - BP
        activity.menu.findItem(R.id.action_map).setVisible(false);
        activity.menu.findItem(R.id.action_add_card).setVisible(false);

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
        return null;
    }

    @Override
    public void applyFilter() {

    }


}
