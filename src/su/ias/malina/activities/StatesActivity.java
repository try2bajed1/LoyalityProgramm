package su.ias.malina.activities;


import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.*;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import su.ias.malina.R;
import su.ias.malina.activities.fragments.MapFragment;
import su.ias.malina.activities.fragments.OnlinePartnersListFragment;
import su.ias.malina.activities.fragments.PointsListFragment;
import su.ias.malina.activities.fragments.UserModeSelectionFragment;
import su.ias.malina.activities.states.*;
import su.ias.malina.app.AppSingleton;
import su.ias.malina.components.PagerSlidingTabStrip;
import su.ias.malina.data.MapPointData;
import su.ias.malina.interfaces.IState;
import su.ias.malina.services.GeoDetectionService;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA. User: n.senchurin Date: 11.03.14 Time: 13:52
 */

public class StatesActivity extends BaseAppCompatActivity {

    public static final String LAST_USED_DATA_TYPE = "last_used_data_type";
    public static final String LAST_USED_TIME_STAMP = "last_used_timestamp";

    public static final long MILLISECONDS_IN_24_HOURS = 86400000; // milliseconds in 24hours
    
//    private LocationUpdateReciever locationUpdateReciever;

    private final static String TAG_MAP_FRAGMENT = "TAG_MAP_FRAGMENT";
    private final static String TAG_POINTS_AS_LIST_FRAGMENT = "TAG_POINTS_AS_LIST_FRAGMENT";
    private final static String TAG_ON_SOFA_FRAGMENT = "TAG_ON_SOFA_FRAGMENT";

    private final static String FLAG_USER_SELECTION_HELP_WAS_SHOWN = "USER_SELECTION_HELP";
    private final static String FLAG_MAP_HELP_WAS_SHOWN = "MAP_HELP";


	private Dialog filterDialog;

    private FragmentManager fragmentManager;
    private View fadeRL;

	public int selectedStateIndex;

    public static ArrayList<MapPointData> pointsDataArr = new ArrayList<MapPointData>();

    private SharedPreferences prefs;

    private ModeByUserState userModeSelectionState;
    private PlacesByCarMapImplState placesByCarMapImplState;
    private PlacesByCarListImplState placesByCarListImplState;
    private PlacesByFootMapImplState placesByFootMapImplState;
    private PlacesOnSofaListImplState placesOnSofaListImplState;
    private PlacesByFootListImplState placesByFootListImplState;
    private DiscountsOfflineState discountsOfflineState;
    private DiscountsOnlineState discountsOnlineState;
    private CardsActiveState cardsActiveState;
    private CardsAllState cardsAllState;

    private IState currState;

    public static final int USER_MODE_SELECTION_STATE = 0;
    public static final int CAR_MAP_STATE = 1;
    public static final int CAR_LIST_STATE = 2;
    public static final int FOOT_MAP_STATE = 3;
    public static final int FOOT_LIST_STATE = 4;
    public static final int SOFA_LIST_STATE = 5;
    public static final int DISCOUNTS_OFFLINE_STATE = 6;
    public static final int DISCOUNTS_ONLINE_STATE = 7;
    public static final int CARDS_ACTIVE_STATE = 8;
    public static final int CARDS_ALL_STATE = 9;

    private int counter = 0;

    private ShowcaseView showcaseView;


    private static TabData[] tabsDataArr = {
                    new TabData("На машине", R.drawable.car_active,  R.drawable.car,  CAR_MAP_STATE ),
                    new TabData("Пешком",    R.drawable.man_active,  R.drawable.man,  FOOT_MAP_STATE ),
                    new TabData("На диване", R.drawable.sofa_active, R.drawable.sofa, SOFA_LIST_STATE) };


    public ViewPager viewPager;
    private PagerSlidingTabStrip tabs;
    private RelativeLayout tabsContainer;
    private SmoothProgressBar smoothProgressBar;



    @Override
	protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // state pattern is used   http://en.wikipedia.org/wiki/State_pattern
        userModeSelectionState    = new ModeByUserState(this);
        placesByCarMapImplState   = new PlacesByCarMapImplState(this);
        placesByCarListImplState  = new PlacesByCarListImplState(this);
        placesByFootMapImplState  = new PlacesByFootMapImplState(this);
        placesByFootListImplState = new PlacesByFootListImplState(this);
        placesOnSofaListImplState = new PlacesOnSofaListImplState(this);
        discountsOfflineState     = new DiscountsOfflineState(this);
        discountsOnlineState      = new DiscountsOnlineState(this);
        cardsActiveState          = new CardsActiveState(this);
        cardsAllState             = new CardsAllState(this);


        prefs = AppSingleton.get().prefs;
        fragmentManager = getSupportFragmentManager();

        fadeRL = (View) findViewById(R.id.temp_container);
        smoothProgressBar = (SmoothProgressBar) findViewById(R.id.smooth_pb);

        selectedStateIndex = -1;

		initTabs();

        long lastSelectedTimeStamp = prefs.getLong(StatesActivity.LAST_USED_TIME_STAMP, -1);
        boolean moreThan24hours = (lastSelectedTimeStamp - System.currentTimeMillis()) > MILLISECONDS_IN_24_HOURS;

        if (lastSelectedTimeStamp == -1 || moreThan24hours) {

            currState = userModeSelectionState;
            currState.setModeByUser();

            // если не показывали гид на странице выбора
            if(!prefs.getBoolean(FLAG_USER_SELECTION_HELP_WAS_SHOWN, false)) {
                currState.initGuide();
            }

        } else {
            selectedStateIndex = prefs.getInt(LAST_USED_DATA_TYPE, CAR_MAP_STATE);
            runStateByIndex(selectedStateIndex);
        }

        //setSelectedBottomBarItem(0);
    }


    @Override
    public void setNavButtons() {
        mapNavLL.setEnabled(false);
        ((ImageView) mapNavLL.getChildAt(0)).setImageDrawable(getResources().getDrawable(R.drawable.map_active));
        ((TextView)  mapNavLL.getChildAt(1)).setTextColor(Color.WHITE);

        discountsNavLL.setEnabled(true);
        ((ImageView) discountsNavLL.getChildAt(0)).setImageDrawable(getResources().getDrawable(R.drawable.action));
        ((TextView)  discountsNavLL.getChildAt(1)).setTextColor(getResources().getColor(R.color.navigation_btn_unactive_text_color));

        cardsNavLL.setEnabled(true);
        ((ImageView) cardsNavLL.getChildAt(0)).setImageDrawable(getResources().getDrawable(R.drawable.cards));
        ((TextView)  cardsNavLL.getChildAt(1)).setTextColor(getResources().getColor(R.color.navigation_btn_unactive_text_color));
    }



    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

        public SampleFragmentPagerAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public int getCount() {
            return tabsDataArr.length;
        }

        @Override
        public Fragment getItem(int position) {
            return PageFragment.create(position + 1);
        }

        @Override
        public String getPageTitle(int index) {
            return tabsDataArr[index].getTitle();
        }

        @Override
        public int getPageIconResId(int position) {
            return tabsDataArr[position].getIconResId();
        }
    }


    public static class PageFragment extends Fragment {

        public static final String ARG_PAGE = "ARG_PAGE";

        private int mPage;

        public static PageFragment create(int page) {
            Bundle args = new Bundle();
            args.putInt(ARG_PAGE, page);
            PageFragment fragment = new PageFragment();
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mPage = getArguments().getInt(ARG_PAGE);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.tab_custom_view, container, false);
            TextView textView = (TextView) view.findViewById(R.id.tab_label);
            textView.setText("Fragment #" + mPage);
            return view;
        }
    }



    //todo: сделать переход на следующий шаг через итератор
    public void initGuideForUserSelection() {

        counter = 0;

        View.OnClickListener userModeGuideClickHandler = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (counter) {
                    case 0:
                        showcaseView.setContentTitle("Найдите партнеров программы на карте");
                        showcaseView.setShowcase(new ViewTarget(mapNavLL), true);
                        counter++;
                        break;
                    case 1:
                        showcaseView.setContentTitle("Учавствуйте в акциях");
                        showcaseView.setShowcase(new ViewTarget(discountsNavLL), true);
                        counter++;
                        break;
                    case 2:
                        showcaseView.setContentTitle("Карты МАЛИНА всегда под рукой");
                        showcaseView.setShowcase(new ViewTarget(cardsNavLL), true);
                        counter++;
                        break;
                    case 3:
                        showcaseView.hide();
                        prefs.edit().putBoolean(FLAG_USER_SELECTION_HELP_WAS_SHOWN, true).apply();
                        break;
                }
            }
        };


        showcaseView = new ShowcaseView.Builder(this)
                            .setTarget(new ViewTarget(findViewById(R.id.frames_container)))
                            .setOnClickListener(userModeGuideClickHandler)
                            .setStyle(R.style.CustomShowcaseTheme)
                            .build();

        showcaseView.setContentTitle("Выбирайте способ передвижения по городу");
    }





    public void initGuideForSelectedMode() {
        counter = 0;
        View.OnClickListener selectedModeClickHandler = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (counter) {
                    case 0:
                        showcaseView.setContentTitle("Способ передвижения по городу");
                        showcaseView.setShowcase(new ViewTarget(findViewById(R.id.for_guide_zoom)), true);
                        counter++;
                        break;
                    case 1:
                        showcaseView.hide();
                        prefs.edit().putBoolean(FLAG_MAP_HELP_WAS_SHOWN, true).apply();
                        break;
                }
            }
        };


        showcaseView = new ShowcaseView.Builder(this)
                            .setOnClickListener(selectedModeClickHandler)
                            .setStyle(R.style.CustomShowcaseTheme)
                            .build();

        showcaseView.setContentTitle("Здесь Вы найдете Ваш счет и другую полезную информацию");

        showcaseView.setShowcase(new ViewTarget(findViewById(R.id.for_menu)), true);

    }




    private void initTabs() {

        tabsContainer = (RelativeLayout) findViewById(R.id.tabs_container);
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.sliding_tabs);

        viewPager.setAdapter(new SampleFragmentPagerAdapter());
        tabs.setViewPager(viewPager);

        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int i, float v, int i2) { }

            @Override
            public void onPageSelected(int i) {

                int posIndex = i;

                TabData tabDataObj = tabsDataArr[posIndex];
                selectedStateIndex = tabDataObj.getContentBoundTo();

                runStateByIndex(selectedStateIndex);

                //todo: move saving  onStop()
                updateSharedPrefsData(selectedStateIndex);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        };


        tabs.setOnPageChangeListener(onPageChangeListener);


/*        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.removeAllTabs();

        tabListener = new ActionBar.TabListener() {

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                View tabView = tab.getCustomView();
                ((ImageView) tabView.findViewById(R.id.tab_icon)).setImageDrawable(getResources().getDrawable(tabsDataArr[tab.getPosition()].iconDisabledResId));
                ((TextView)  tabView.findViewById(R.id.tab_label)).setTextColor(getResources().getColor(R.color.tab_inactive_text_color));
            }



            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

                int posIndex = tab.getPosition();

                TabData tabDataObj = tabsDataArr[posIndex];
                selectedStateIndex = tabDataObj.getContentBoundTo();

                View tabView = tab.getCustomView();
                ((ImageView) tabView.findViewById(R.id.tab_icon)).setImageDrawable(getResources().getDrawable(tabDataObj.iconResId));
                ((TextView)  tabView.findViewById(R.id.tab_label)).setTextColor(Color.WHITE);

                runStateByIndex(selectedStateIndex);

                //todo: move saving  onStop()
                updateSharedPrefsData(selectedStateIndex);
            }


            @Override
            public void onTabReselected(ActionBar.Tab tab,FragmentTransaction ft) {}

        };




        for (int i = 0; i < tabsDataArr.length; i++) {
            actionBar.addTab(actionBar.newTab()
                                      .setCustomView(R.layout.tab_custom_view)
                                      .setTabListener(tabListener), false); // isSelected

            View tabView = actionBar.getTabAt(i).getCustomView();
            ((TextView)  tabView.findViewById(R.id.tab_label)).setText(tabsDataArr[i].title);
            ((ImageView) tabView.findViewById(R.id.tab_icon)).setImageDrawable(getResources().getDrawable(tabsDataArr[i].iconDisabledResId));
        }*/


    }



    public void runStateByIndex(int currentStateNum) {

        if (currentStateNum == CAR_MAP_STATE) {
            //todo: избавиться от повторного захода в этот метод
            if(currState == placesByCarMapImplState){
                return;
            }

            currState = placesByCarMapImplState;
            currState.setPlacesByCarMapImpl();


            if(!prefs.getBoolean(FLAG_MAP_HELP_WAS_SHOWN, false)) {
                currState.initGuide();
            } else {
                Log.i("@","****do not init");
            }
        }


        if (currentStateNum == CAR_LIST_STATE) {
            if(currState == placesByCarListImplState){
                return;
            }
            currState = placesByCarListImplState;
            currState.setPlacesByCarListImpl();
        }


        if (currentStateNum == FOOT_MAP_STATE) {
            if(currState == placesByFootMapImplState){
                return;
            }
            currState = placesByFootMapImplState;
            currState.setPlacesByFootMapImpl();

            if(!prefs.getBoolean(FLAG_MAP_HELP_WAS_SHOWN,false)) {
                currState.initGuide();
            }
        }


        if (currentStateNum == FOOT_LIST_STATE) {
            if(currState == placesByFootListImplState){
                return;
            }
            currState = placesByFootListImplState;
            currState.setPlacesByFootListImpl();
        }

        if (currentStateNum == SOFA_LIST_STATE) {
            if(currState == placesOnSofaListImplState){
                return;
            }
            currState = placesOnSofaListImplState;
            currState.setOnSofaListImpl();
            if(!prefs.getBoolean(FLAG_MAP_HELP_WAS_SHOWN,false)) {
                currState.initGuide();
            }
        }

        if (currentStateNum == DISCOUNTS_OFFLINE_STATE) {
            if(currState == discountsOfflineState){
                return;
            }
            currState = discountsOfflineState;
            currState.setDiscountsOffline();
        }

        if (currentStateNum == DISCOUNTS_ONLINE_STATE) {
            if(currState == discountsOnlineState){
                return;
            }
            currState = discountsOnlineState;
            currState.setDiscountsOnline();
        }

        if (currentStateNum == CARDS_ACTIVE_STATE) {
            if(currState == cardsActiveState){
                return;
            }
            currState = cardsActiveState;
            currState.setCardsActive();
        }


        if (currentStateNum == CARDS_ALL_STATE) {
            if(currState == cardsAllState){
                return;
            }
            currState = cardsAllState;
            currState.setCardsAll();
        }

        if (currentStateNum == USER_MODE_SELECTION_STATE) {
            if(currState == userModeSelectionState){
                return;
            }
            currState = userModeSelectionState;
            currState.setModeByUser();
        }

    }



/*
    private IState getPlacesStateFromShared(int savedState) {
        if(savedState == ContentData.TYPE_CAR)  return placesByCarMapImplState;
        if(savedState == ContentData.TYPE_FOOT) return placesByFootMapImplState;
        if(savedState == ContentData.TYPE_SOFA) return placesOnSofaListImplState;
        return userModeSelectionState;
    }
*/



    public void setCurrentState(IState state) {
        currState = state;
    }



    public IState getCurrentState() {
        return currState;
    }




	@Override
	protected void onResume() {
		super.onResume();

		startService(new Intent(getApplicationContext(), GeoDetectionService.class));
	}




	@Override
	protected void onPause() {

		super.onPause();
		
		stopService(new Intent(getApplicationContext(), GeoDetectionService.class));

		if (filterDialog != null) filterDialog.dismiss();

	}



    private void updateSharedPrefsData(int type) {
        prefs.edit().putLong(LAST_USED_TIME_STAMP, System.currentTimeMillis())
                    .putInt(LAST_USED_DATA_TYPE, type)
                    .apply();
    }



    @Override
	public boolean onCreateOptionsMenu(Menu menu) {

        Log.e("@","onCreateOptionsMenu");

		this.menu = menu;
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_map, menu);

        //если стэйт установился раньше меню
        currState.updateActionBarMenu();

		return true;
	}





    @Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Если событие обработано переключателем, то выходим
		if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item))
			return true;

		switch (item.getItemId()) {

            case R.id.action_filter:
                filterDialog = currState.getFilterDialog();
                filterDialog.show();
            return true;


            case R.id.action_show_as_list:
                if (currState instanceof  PlacesByCarMapImplState) {
                    currState = placesByCarListImplState;
                    currState.setPlacesByCarListImpl();
                } else if (currState == placesByFootMapImplState) {
                           currState = placesByFootListImplState;
                           currState.setPlacesByFootListImpl();
                       }
            return true;


            case R.id.action_map:
                if (currState == placesByCarListImplState) {
                    currState = placesByCarMapImplState;
                    currState.setPlacesByCarMapImpl();
                } else if (currState == placesByFootListImplState) {
                    currState = placesByFootMapImplState;
                    currState.setPlacesByFootMapImpl();
                }
            return true;


            case android.R.id.home:

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
	}



    @Override
    public void onBackPressed() {
        if (currState == placesByCarListImplState) {
            currState = placesByCarMapImplState;
            currState.setPlacesByCarMapImpl();
        } else if (currState == placesByFootListImplState) {
            currState = placesByFootMapImplState;
            currState.setPlacesByFootMapImpl();
        } else {
            super.onBackPressed();
        }
    }



    public void setActionBarLoading(boolean isLoading) {
//        Log.i("@", "loading " + isLoading);
        smoothProgressBar.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);

		if (isLoading) {
			hideMenuButtons();
		} else {
            // если контент загрузился, прелоадер прячем и показываем нужные кнопки,
            currState.updateActionBarMenu();
		}
	}


	public void showTabs() {
        tabsContainer.setVisibility(View.VISIBLE);
	}



	public void hideTabs() {
        tabsContainer.setVisibility(View.GONE);
	}



    private void showFadingAnim() {
        fadeRL.setVisibility(View.VISIBLE);
        fadeRL.setAlpha(1f);
        fadeRL.animate().setDuration(500).alpha(0);      // temp_container был добавлен изза глюка карты
    }



	public void loadMapFrame() {

        Fragment fragment = MapFragment.newInstance(selectedStateIndex);

        MapFragment mFr = (MapFragment) fragmentManager.findFragmentByTag(TAG_MAP_FRAGMENT);
        if( mFr != null ){
            if(!mFr.isAdded()){
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(framesContainer.getId(), fragment, TAG_MAP_FRAGMENT);
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentTransaction.commit();

                showFadingAnim();

            } else {
                mFr.setContentType(selectedStateIndex);
                mFr.loadPoints(selectedStateIndex);
            }
        } else {

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(framesContainer.getId(), fragment, TAG_MAP_FRAGMENT);
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.commit();

            showFadingAnim();
        }
    }



    public void loadListFrameForOnlinePartners(int contentType) {
        OnlinePartnersListFragment onlineParntersFragment;
        onlineParntersFragment = OnlinePartnersListFragment.getInstance(contentType);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.replace(framesContainer.getId(), onlineParntersFragment, TAG_ON_SOFA_FRAGMENT);
        fragmentTransaction.commit();
        showFadingAnim();
    }



	public void loadListFrameForPoints(int contentType) {
        PointsListFragment pointsListFragment  = PointsListFragment.getInstance(contentType);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.replace(framesContainer.getId(), pointsListFragment, TAG_POINTS_AS_LIST_FRAGMENT);
        fragmentTransaction.commit();
        showFadingAnim();
    }



    public void loadUserModeSelectionFrame() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.replace(framesContainer.getId(), new UserModeSelectionFragment());
        fragmentTransaction.commit();
    }



    public void filterListByCategories() {
        OnlinePartnersListFragment onSofaFr = (OnlinePartnersListFragment) fragmentManager.findFragmentByTag(TAG_ON_SOFA_FRAGMENT);
        if (onSofaFr != null) {
            onSofaFr.loadOnlinePartners();
        }
    }


    public ModeByUserState getUserModeSelectionState() {
        return userModeSelectionState;
    }

    public PlacesByCarMapImplState getPlacesByCarMapImplState() {
        return placesByCarMapImplState;
    }

    public PlacesByCarListImplState getPlacesByCarListImplState() {
        return placesByCarListImplState;
    }

    public PlacesByFootMapImplState getPlacesByFootMapImplState() {
        return placesByFootMapImplState;
    }

    public PlacesOnSofaListImplState getPlacesOnSofaListImplState() {
        return placesOnSofaListImplState;
    }

    public PlacesByFootListImplState getPlacesByFootListImplState() {
        return placesByFootListImplState;
    }

    public DiscountsOfflineState getDiscountsOfflineState() {
        return discountsOfflineState;
    }

    public DiscountsOnlineState getDiscountsOnlineState() {
        return discountsOnlineState;
    }

    public CardsActiveState getCardsActiveState() {
        return cardsActiveState;
    }

    public CardsAllState getCardsAllState() {
        return cardsAllState;
    }




    public ArrayList<MapPointData> getPointDatasArr() {
        return pointsDataArr;
    }


    public static class TabData {

        private String title;
        private int iconResId;
        private int iconDisabledResId;
        private int contentBoundTo;


        private TabData(String title, int iconResId, int iconDisabledResId, int contentBoundTo) {
            this.title = title;
            this.iconResId = iconResId;
            this.iconDisabledResId = iconDisabledResId;
            this.contentBoundTo = contentBoundTo;
        }


        public int getContentBoundTo() {
            return contentBoundTo;
        }


        public int getIconResId() {
            return iconResId;
        }


        public String getTitle() {
            return title;
        }

        public int getIconDisabledResId() { return iconDisabledResId; }

    }


}
