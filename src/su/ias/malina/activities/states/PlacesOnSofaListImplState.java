package su.ias.malina.activities.states;

import android.app.Dialog;
import android.util.Log;
import android.widget.Toast;
import su.ias.malina.R;
import su.ias.malina.activities.StatesActivity;
import su.ias.malina.dialogs.CategoriesDialog;
import su.ias.malina.interfaces.IState;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 25.08.2014
 * Time: 17:00
 */
public class PlacesOnSofaListImplState implements IState {

    private final StatesActivity activity;


    public PlacesOnSofaListImplState(StatesActivity activity) {
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

    }



    @Override
    public void setPlacesByFootListImpl() {

    }


    @Override
    public void setOnSofaListImpl() {

        /*if(activity.actionBar.getNavigationMode() != ActionBar.NAVIGATION_MODE_TABS) {
            activity.actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        }*/
        activity.showTabs();
        activity.viewPager.setCurrentItem(2);
//        activity.actionBar.setSelectedNavigationItem(2); // hardcode
        activity.setActionBarLoading(true);
//        updateActionBarMenu();
        activity.loadListFrameForOnlinePartners(StatesActivity.SOFA_LIST_STATE);
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

        Log.i("@", "sofa update menu");

        if (activity.menu == null) {
            Toast.makeText(activity, "menu is null", Toast.LENGTH_SHORT).show();
            return;
        }

        activity.menu.findItem(R.id.action_filter).setVisible(true);
        activity.menu.findItem(R.id.action_show_as_list).setVisible(false);
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
        return new CategoriesDialog(activity, this);
    }


    @Override
    public void applyFilter() {
        activity.filterListByCategories();
    }


}
