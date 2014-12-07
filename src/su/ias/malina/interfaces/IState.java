package su.ias.malina.interfaces;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 25.08.2014
 * Time: 15:48
 */

public interface IState extends IFilterDialogListener {

    void setModeByUser();

    void setPlacesByCarMapImpl();
    void setPlacesByCarListImpl();

    void setPlacesByFootMapImpl();
    void setPlacesByFootListImpl();

    void setOnSofaListImpl();

    void setDiscountsOffline();
    void setDiscountsOnline();

    void setCardsActive();
    void setCardsAll();

    void updateTabs();
    void updateActionBarMenu();
    void updateFragment();
    void updateNavigationPanel();

    void initGuide();

}
