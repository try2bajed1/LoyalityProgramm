package su.ias.malina.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import su.ias.malina.R;
import su.ias.malina.app.AppSingleton;



/**
 * Created with IntelliJ IDEA. User: n.senchurin Date: 04.03.14 Time: 13:29
 */


public class SettingsActivity extends ActionBarActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

		ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setIcon(R.drawable.action_bar_icon);
        actionBar.setTitle("Настройки");
		setContentView(R.layout.activity_settings);

	}





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }




	public void clickHandler(View view) {

        switch (view.getId()) {

            case  R.id.clear_cahe_btn:
                //todo: clear cache
                break;

            case R.id.conditions_btn:
                startActivity(new Intent(SettingsActivity.this, TermsAndConditionsActivity.class));
                break;

            case R.id.guide_btn:
                startActivity(new Intent(SettingsActivity.this, GuideActivity.class));
                break;

            case R.id.developer_btn:
                startActivity(new Intent(SettingsActivity.this, AboutActivity.class));
                break;

            case R.id.logout_btn:
                AppSingleton.get().resetUserData();
                startActivity(new Intent(SettingsActivity.this, LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
        }


    }

}
