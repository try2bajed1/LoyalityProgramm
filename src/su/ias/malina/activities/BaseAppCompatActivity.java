package su.ias.malina.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.*;
import android.widget.*;
import su.ias.malina.R;
import su.ias.malina.app.AppSingleton;

import java.util.ArrayList;



public abstract class  BaseAppCompatActivity extends ActionBarActivity {

	public ActionBar actionBar;
	protected DrawerLayout mDrawer;
	protected ActionBarDrawerToggle mDrawerToggle;
	public Menu menu;

	private ArrayList<SliceMenuItem> sliceMenuDataArr;
	private ListView sliceMenuLV;
    protected RelativeLayout framesContainer;

    protected LinearLayout mapNavLL;
    protected LinearLayout discountsNavLL;
    protected LinearLayout cardsNavLL;

    protected LinearLayout navigationBarLL;



	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_base_drawer);
        framesContainer = (RelativeLayout) findViewById(R.id.frames_container);

		actionBar = getSupportActionBar();

		initDrawer();
        initNavButtons();
        setNavButtons();
	}


   public abstract void setNavButtons();



    protected void initNavButtons() {
        navigationBarLL = (LinearLayout) findViewById(R.id.navigation_bar);


        mapNavLL = (LinearLayout) findViewById(R.id.button_places);
        mapNavLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSelectedActivity(StatesActivity.class);
            }
        });


        discountsNavLL = (LinearLayout) findViewById(R.id.button_promo);
        discountsNavLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSelectedActivity(DiscountsActivity.class);
            }
        });


        cardsNavLL = (LinearLayout) findViewById(R.id.button_cards);
        cardsNavLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSelectedActivity(UserCardsActivity.class);
            }
        });
    }


    private void startSelectedActivity(Class<? extends BaseAppCompatActivity> cl ) {
        startActivity(new Intent(this, cl).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        finish();
        overridePendingTransition(0, 0);
    }


    @Override
    protected void onResume() {
        super.onResume();
        sliceMenuLV.setAdapter(new SliceMenuListAdapter(BaseAppCompatActivity.this, sliceMenuDataArr,  AppSingleton.get().getAccountData().getPointsAvaliable()));
    }




    protected void hideMenuButtons() {

        if (menu == null) {
            return;
        }

        menu.findItem(R.id.action_filter).setVisible(false);
        menu.findItem(R.id.action_show_as_list).setVisible(false);
        menu.findItem(R.id.action_map).setVisible(false);
        menu.findItem(R.id.action_add_card).setVisible(false);
    }




    @SuppressLint("InlinedApi")
	protected void initDrawer() {

		mDrawer = (android.support.v4.widget.DrawerLayout) findViewById(R.id.drawer);

		// Если drawer не используется, то не паникуем
		if (mDrawer == null) return;

		mDrawerToggle = new android.support.v4.app.ActionBarDrawerToggle(this, mDrawer, R.drawable.ic_drawer, R.string.opened, R.string.closed);
		mDrawer.setDrawerListener(mDrawerToggle);
		mDrawer.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);

		// Включим кнопки на action bar
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setIcon(R.drawable.logo_filter);
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.data_selection_btn_bcg)));

		initSliceMenu();
	}




	@Override
	protected void onPostCreate(Bundle savedInstanceState) {

		super.onPostCreate(savedInstanceState);

		if (mDrawerToggle != null) {
			mDrawerToggle.syncState();
		}
	}



	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		// Просто вызов
		if (mDrawerToggle != null) mDrawerToggle.onConfigurationChanged(newConfig);
	}



	protected void initSliceMenu() {

		// todo: init sliceMenuDataArr once
		sliceMenuDataArr = new ArrayList<SliceMenuItem>();
        sliceMenuDataArr.add(new SliceMenuItem(R.drawable.logo_filter, "Счет", BillInformationActivity.class));
        sliceMenuDataArr.add(new SliceMenuItem(R.drawable.user, "Моя контактная информация", MyContactsActivity.class));
        sliceMenuDataArr.add(new SliceMenuItem(R.drawable.portfolio, "Партнеры программы", PartnersActivity.class));
        sliceMenuDataArr.add(new SliceMenuItem(R.drawable.contacts, "Контакты", ProgramContactsActivity.class));
        sliceMenuDataArr.add(new SliceMenuItem(R.drawable.config,  "Параметры", SettingsActivity.class));

        sliceMenuLV = (ListView) findViewById(R.id.slice_menu_list);
		sliceMenuLV.setAdapter(new SliceMenuListAdapter(BaseAppCompatActivity.this, sliceMenuDataArr,  AppSingleton.get().getAccountData().getPointsAvaliable()));
		sliceMenuLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(), sliceMenuDataArr.get(position).activityToStart));
            }
        });

	}





	protected class SliceMenuListAdapter extends BaseAdapter {

		private Context mContext;
		private LayoutInflater layoutInflater;
		public ArrayList<SliceMenuItem> itemsArr;



		public SliceMenuListAdapter(Context c, ArrayList<SliceMenuItem> itemsArr, String pointsValue) {

			mContext = c;
			layoutInflater = LayoutInflater.from(mContext);
			this.itemsArr = itemsArr;
		}

		public int getCount() {
			return itemsArr.size();
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		// create a new ImageView for each item referenced by the Adapter
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.slice_menu_list_item, null);
			}
            
			SliceMenuItem sliceItem = itemsArr.get(position);

            TextView billTV = (TextView) convertView.findViewById(R.id.bill_points_value);
            if(position==0) {
                billTV.setVisibility(View.VISIBLE);
                billTV.setText(AppSingleton.get().getAccountData().getPointsAvaliable()+"\n"+"баллов");
            } else {
                billTV.setVisibility( View.GONE);
            }

            TextView itemTV = ((TextView) convertView.findViewById((R.id.slice_menu_item_text)));
            itemTV.setText(sliceItem.itemText);

			 ImageView logoIV = (ImageView) convertView.findViewById((R.id.slice_menu_item_logo));
			 logoIV.setImageDrawable(mContext.getResources().getDrawable(sliceItem.iconResource));

			 return convertView;
		}


	}




	private class SliceMenuItem {

		private int iconResource;
		private String itemText;
		public Class activityToStart;

		private SliceMenuItem(int iconResource, String itemText, Class activityToStart) {
			this.iconResource = iconResource;
			this.itemText = itemText;
			this.activityToStart = activityToStart;
		}

	}

}
