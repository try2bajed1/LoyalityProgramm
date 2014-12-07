package su.ias.malina.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import org.json.JSONException;
import org.json.JSONObject;
import su.ias.malina.R;
import su.ias.malina.app.AppSingleton;
import su.ias.malina.async.PostTask;
import su.ias.malina.interfaces.IListener;
import su.ias.malina.maskedEditText.MaskedEditText;
import su.ias.malina.utils.AppApiUtils;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 25.09.2014
 * Time: 14:49
 */

public class AddExistingCardActivity extends BaseActivity implements IListener {

    private MaskedEditText cardCodeET;
    private EditText receivedCodeET;

    private Button doneBtn;
    private Runnable successSignUpRequestAction;

    private Button recieveAgainBtn;
    private String cardCodeStr;



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_existing);

        actionBar.setTitle("Добавление карты");

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        cardCodeET = (MaskedEditText) findViewById(R.id.card_number);


        receivedCodeET = (EditText) findViewById(R.id.code_from_sms);
        receivedCodeET.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                doneBtn.setEnabled(s.length() != 0);
            }
        });


        recieveAgainBtn = (Button) findViewById(R.id.recieve_again_btn);
        recieveAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardCodeStr = "6393" + cardCodeET.getText().toString().replaceAll("[ -]", "");
                String jsonToPost = AppApiUtils.getJsonForActivationCode(cardCodeStr, AppSingleton.get().getAccountData().getUserMobileNum());
                new PostTask(AddExistingCardActivity.this).execute(AppApiUtils.ACTION_GET_ACTIVATION_CODE, jsonToPost);
            }
        });


        doneBtn = (Button) findViewById(R.id.done_btn);
        doneBtn.setEnabled(false);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cardCodeStr = "6393" + cardCodeET.getText().toString().replaceAll("[ -]", "");
                String jsonToPost = AppApiUtils.getJsonForAddCard(cardCodeStr, receivedCodeET.getText().toString());
                new PostTask(AddExistingCardActivity.this).execute(AppApiUtils.ACTION_ADD_CARD, jsonToPost);
            }
        });


        successSignUpRequestAction = new Runnable() {
            @Override
            public void run() {
                finish();
            }
        };
    }



    @Override
    public void responseCompleteHandler(String actionName, String jsonFromApi) {
        if(actionName.equals(AppApiUtils.ACTION_ADD_CARD)) {
            showMessageDialog("Уведомление", getString(R.string.card_registered), successSignUpRequestAction, false);
        } else if (actionName.equals(AppApiUtils.ACTION_GET_ACTIVATION_CODE)) {
            showResultDialog("Уведомление", getString(R.string.check_sms_mobile_card));
        }
    }



    @Override
    public void responseErrorHandler(String actionName, String errorStr) {
        try {
            JSONObject jsonFromApi = new JSONObject(errorStr);
            String alertMsg = jsonFromApi.getString("message");
            showResultDialog("Ошибка!", alertMsg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}