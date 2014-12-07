package su.ias.malina.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import su.ias.malina.R;
import su.ias.malina.adapters.PartnersListAdapter;
import su.ias.malina.app.AppSingleton;
import su.ias.malina.data.PartnerDataSimplified;
import su.ias.malina.db.DBAdapter;
import su.ias.malina.dialogs.CategoriesDialog;
import su.ias.malina.interfaces.IFilterDialogListener;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 04.03.14
 * Time: 13:32
 */
public class PartnersActivity extends ActionBarActivity implements IFilterDialogListener{

    private ActionBar mActionbar;
    private ListView partnersLV;

    private CategoriesDialog categoriesDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mActionbar = getSupportActionBar();
        mActionbar.setTitle("Партнеры");
        mActionbar.setDisplayHomeAsUpEnabled(true);
        mActionbar.setHomeButtonEnabled(true);
        mActionbar.setIcon(R.drawable.action_bar_icon);


        AppSingleton.get().getDBAdapter().getSortedCategories();

        setContentView(R.layout.activity_partners);
        partnersLV = (ListView) findViewById(R.id.partners_list);

        final ArrayList<PartnerDataSimplified> partnersArr = AppSingleton.get().getDBAdapter().getPartnersByType(AppSingleton.get().userRegion, DBAdapter.TYPE_ALL);
        partnersLV.setAdapter(new PartnersListAdapter(this, partnersArr));
        partnersLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                PartnerDataSimplified partnerDataSimplified = partnersArr.get(position);

                startActivity(new Intent(PartnersActivity.this, SelectedPartnerActivity.class)
                        .putExtra(SelectedPartnerActivity.SELECTED_PARTNER_ID, partnerDataSimplified.getId())
                        .putExtra(SelectedPartnerActivity.MODE_EXTRA, SelectedPartnerActivity.ALL_MODE)
                        .putExtra(SelectedPartnerActivity.IS_ONLINE, partnerDataSimplified.getType().equals("online")));
            }
        });
    }




    @Override
    protected void onPause() {
        super.onPause();

        if(categoriesDialog != null)
            categoriesDialog.dismiss();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_partners, menu);

//        hideMenuButtons();

        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_filter:
                categoriesDialog = (CategoriesDialog) getFilterDialog();
                categoriesDialog.show();
                return true;



            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }




    @Override
    public Dialog getFilterDialog() {
        return new CategoriesDialog(this, this);
    }

    @Override
    public void applyFilter() {
//        nop

    }






}
