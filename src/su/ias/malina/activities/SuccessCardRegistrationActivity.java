package su.ias.malina.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import su.ias.malina.R;
import su.ias.malina.app.AppSingleton;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 23.10.2014
 * Time: 15:25
 */
public class SuccessCardRegistrationActivity extends ActionBarActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_reg);


        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setIcon(R.drawable.action_bar_icon);
            actionBar.setTitle("Получение мобильной карты");
        }


        TextView textView = (TextView) findViewById(R.id.thnx_text);
        textView.setText(getResources().getString(R.string.thnx_for_reg)+" "+ AppSingleton.get().getAccountData().getBonusPoints() + "баллов");

        Button fillBtn = (Button) findViewById(R.id.fill_btn);
        fillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(SuccessCardRegistrationActivity.this, MyContactsActivity.class)
                                    .putExtra(MyContactsActivity.EDIT_MODE_EXTRA, true));
            }
        });


    }

}