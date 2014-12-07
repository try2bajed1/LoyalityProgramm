package su.ias.malina.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import org.json.JSONException;
import org.json.JSONObject;
import su.ias.malina.R;
import su.ias.malina.async.PostTask;
import su.ias.malina.interfaces.IListener;
import su.ias.malina.utils.AppApiUtils;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 31.03.2014
 * Time: 19:03
 */


public class CardAdditionActivity  extends ActionBarActivity implements IListener {

    private Button addNewCardBtn;
    private Button addExistingCardBtn;
    private Dialog confirmDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);

        ActionBar mActionbar = getSupportActionBar();
        mActionbar.setTitle("Добавление карты");
        mActionbar.setIcon(R.drawable.action_bar_icon);

        setContentView(R.layout.activity_card_addition);

        addNewCardBtn = (Button) findViewById(R.id.add_new_card_btn);
        addNewCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.show();
            }
        });

        addExistingCardBtn = (Button) findViewById(R.id.add_existiong_card_btn);
        addExistingCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CardAdditionActivity.this, AddExistingCardActivity.class));
            }
        });

        confirmDialog = getCongratulationsDialog();

    }



    public AlertDialog getCongratulationsDialog() {

        final Activity currentActivity = this;

        AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
        builder.setCancelable(true);
        builder.setTitle("Создание новой карты");
        builder.setMessage("Вы уверены, что хотите создать новую карту?");
        builder.setInverseBackgroundForced(true);

        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new PostTask(CardAdditionActivity.this).execute(AppApiUtils.CREATE_CARD, AppApiUtils.getJsonForCreateCard());
                dialog.dismiss();
                addNewCardBtn.setEnabled(false);
                addExistingCardBtn.setEnabled(false);
                setProgressBarIndeterminateVisibility(true);
            }
        });

        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }



    @Override
    public void responseCompleteHandler(String actionName, String jsonFromApi) {

        try {
            JSONObject jsonObject = new JSONObject(jsonFromApi);
            String cardId = jsonObject.getString("card");
            finish();
            startActivity(new Intent(this, SuccessfulAdditionActivity.class).putExtra(SuccessfulAdditionActivity.EXTRA_CARD_ID, cardId));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        addNewCardBtn.setEnabled(true);
        addExistingCardBtn.setEnabled(true);

        setProgressBarIndeterminateVisibility(false);
    }



    @Override
    public void responseErrorHandler(String actionName, String errorStr) {
        addNewCardBtn.setEnabled(true);
        addExistingCardBtn.setEnabled(true);
        setProgressBarIndeterminateVisibility(false);
    }




}
