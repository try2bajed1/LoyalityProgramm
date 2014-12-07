package su.ias.malina.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import su.ias.malina.R;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 04.09.2014
 * Time: 16:49
 */

public class SuccessfulAdditionActivity extends ActionBarActivity {


    public static final String EXTRA_CARD_ID = "card_id";
    private String cardId;
    private Button beelineBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        cardId =  getIntent().getStringExtra(EXTRA_CARD_ID);

        setContentView(R.layout.activity_success_addition);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Акции");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.drawable.logo_filter);


        TextView descrTV = (TextView) findViewById(R.id.congrats);
        String resultText = "Новая карта "+ cardId +" добавлена.\nВоспользоваться ей Вы можете в разделе Карты";
        descrTV.setText(resultText);


        beelineBtn = (Button) findViewById(R.id.beeline_btn);
        beelineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SuccessfulAdditionActivity.this, BeelinePointsActivity.class)
                        .putExtra(BeelinePointsActivity.EXTRA_CARD_ID, cardId));
            }
        });

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


}
