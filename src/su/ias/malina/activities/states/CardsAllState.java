package su.ias.malina.activities.states;

import android.app.Dialog;
import su.ias.malina.interfaces.IState;
import su.ias.malina.activities.StatesActivity;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 25.08.2014
 * Time: 17:00
 */
public class CardsAllState implements IState {


    private final StatesActivity activity;

    public CardsAllState(StatesActivity activity) {
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

    }

    @Override
    public void updateFragment() {

    }

    @Override
    public void updateNavigationPanel() {

    }

    @Override
    public void initGuide() {

    }

    @Override
    public Dialog getFilterDialog() {
        return null;
    }

    @Override
    public void applyFilter() {

    }
}
