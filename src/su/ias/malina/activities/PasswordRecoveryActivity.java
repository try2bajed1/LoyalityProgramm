package su.ias.malina.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import org.json.JSONException;
import org.json.JSONObject;
import su.ias.malina.R;
import su.ias.malina.async.PostTask;
import su.ias.malina.interfaces.IListener;
import su.ias.malina.maskedEditText.MaskedEditText;
import su.ias.malina.utils.AppApiUtils;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 26.09.2014
 * Time: 13:31
 */


public class PasswordRecoveryActivity extends BaseActivity implements IListener {


    private LinearLayout step1LL;
    private LinearLayout step2LL;

    private static final String ACTION_REQUEST_GET_SMS_CODE = "request_restore_password";
    private static final String ACTION_REQUEST_CONFIRM_SMS_CODE = "check_restore_password_code";
    private static final String ACTION_CHANGE_PASSWORD = "change_password";

    private static final int STEP_GET_SMS_CODE = 1;
    private static final int STEP_CHECK_SMS_CODE = 2;
    private static final int STEP_SEND_NEW_PASS = 3;
    private int currStep;



    public static final int PASSWORD_IS_CORRECT = 0;
    public static final int EMPTY_FIELDS = 1;
    public static final int TOO_SHORT_OR_LONG = 2;
    public static final int NOT_EQUAL = 3;


    private EditText recievedSmsCodeET;
    private String recievedSmsCodeStr;
    private EditText passwordET;
    private EditText confirmPasswordET;


    private Button getSmsCodeBtn;
    private Button doneBtn;
    private Runnable successGetSmsAction;
    private Runnable successCheckSmsCodeAction;
    private Runnable successSendNewPasswordAction;


    private MaskedEditText cardNumMasked;
    private EditText overlayLoginET;
    private TextWatcher maskedTextWatcher;
    private TextWatcher overlayTextWatcher;
    private MaskedEditText phoneMasked;
    private TextView prefixTV;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);

        currStep = STEP_GET_SMS_CODE;

        actionBar.setTitle("Восстановление пароля");

        step1LL = (LinearLayout) findViewById(R.id.step_1);

        recievedSmsCodeET = (EditText) findViewById(R.id.received_code);
        passwordET = (EditText) findViewById(R.id.input_password);
        confirmPasswordET = (EditText) findViewById(R.id.repeat_password);

        cardNumMasked = (MaskedEditText) findViewById(R.id.masked_cardcode);
        phoneMasked   = (MaskedEditText) findViewById(R.id.masked_phone);

        prefixTV = (TextView) findViewById(R.id.prefix);
        overlayLoginET = (EditText) findViewById(R.id.overlay_et);
        overlayTextWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {

                overlayLoginET.setVisibility(View.INVISIBLE);

                if (s.toString().startsWith("6") && s.length()==1) {
                    cardNumMasked.setVisibility(View.VISIBLE);
                    cardNumMasked.setText("6");
                    cardNumMasked.requestFocus();
                    cardNumMasked.addTextChangedListener(maskedTextWatcher);
                } else {
                    if(!phoneMasked.getText().toString().equals(overlayLoginET.getText().toString())) {
                        phoneMasked.setText(overlayLoginET.getText());
                        phoneMasked.setVisibility(View.VISIBLE);
                        phoneMasked.requestFocus();
                        prefixTV.setVisibility(View.VISIBLE);
                        phoneMasked.addTextChangedListener(maskedTextWatcher);
                    }
                }

                overlayLoginET.removeTextChangedListener(overlayTextWatcher);
            }
        };

        overlayLoginET.addTextChangedListener(overlayTextWatcher);



        maskedTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().replaceAll("[ -]","").length() == 0 ) {

                    if(overlayLoginET.getVisibility() != View.VISIBLE) {

                        prefixTV.setVisibility(View.GONE);

                        cardNumMasked.setVisibility(View.INVISIBLE);
                        phoneMasked.setVisibility(View.INVISIBLE);

                        overlayLoginET.setVisibility(View.VISIBLE);
                        overlayLoginET.setText("");
                        overlayLoginET.requestFocus();

                        overlayLoginET.addTextChangedListener(overlayTextWatcher);
                    }
                }
            }
        };



        getSmsCodeBtn = (Button) findViewById(R.id.get_recovery_code_btn);
        getSmsCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo: создать флаг, а не обращаться к свойству вью
                if (overlayLoginET.getVisibility() == View.VISIBLE) {
                    overlayLoginET.setError("Нет данных");
                    Toast.makeText(PasswordRecoveryActivity.this, "Не введен номер карты или телефона", Toast.LENGTH_SHORT).show();
                    return;
                }

                String loginStr = (cardNumMasked.getVisibility() == View.VISIBLE ? cardNumMasked.getText().toString().replaceAll("[ -]", "")
                                                                                 : "7" + phoneMasked.getText().toString().replaceAll("[ -]", ""));

                new PostTask(PasswordRecoveryActivity.this).execute(ACTION_REQUEST_GET_SMS_CODE, getJsonForGetSmsCode(loginStr));
            }
        });



        doneBtn = (Button) findViewById(R.id.done_btn);
        doneBtn.setEnabled(false);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currStep == STEP_GET_SMS_CODE) {
                    //nop
                } else if (currStep == STEP_CHECK_SMS_CODE) {
                    confirmSmsCodeHandler();
                } else if (currStep == STEP_SEND_NEW_PASS) {
                    sendNewPasswordHandler();
                }
            }
        });


        step2LL = (LinearLayout) findViewById(R.id.step_send_new_password);
        step2LL.setVisibility(View.GONE);

        successGetSmsAction = new Runnable() {
            @Override
            public void run() {
                doneBtn.setEnabled(true);
                currStep = STEP_CHECK_SMS_CODE;
            }
        };


        successCheckSmsCodeAction = new Runnable() {
            @Override
            public void run() {
                step1LL.setVisibility(View.GONE);
                step2LL.setVisibility(View.VISIBLE);

                currStep = STEP_SEND_NEW_PASS;
            }
        };

        successSendNewPasswordAction = new Runnable() {
            @Override
            public void run() {
                finish();
            }
        };
    }




    private void confirmSmsCodeHandler() {

        recievedSmsCodeStr = recievedSmsCodeET.getText().toString().trim();
        if (recievedSmsCodeStr.equals("")) {
            recievedSmsCodeET.setError("Поле не должно быть пустым");
        }

        new PostTask(PasswordRecoveryActivity.this).execute(ACTION_REQUEST_CONFIRM_SMS_CODE, getJsonForConfirmSmsCode(recievedSmsCodeStr));
    }




    private void sendNewPasswordHandler() {

        switch (checkCorrectionResult()) {

            case PASSWORD_IS_CORRECT:
                new PostTask(PasswordRecoveryActivity.this).execute(ACTION_CHANGE_PASSWORD, getJsonForNewPassword(recievedSmsCodeStr, passwordET.getText().toString()));
                break;

            case EMPTY_FIELDS:
                Toast.makeText(this,"Поля ввода не должны быть пустыми",Toast.LENGTH_SHORT).show();
                passwordET.setError("Поле не должно быть пустым");
                confirmPasswordET.setError("Поле не должно быть пустым");
                break;

            case TOO_SHORT_OR_LONG:
                Toast.makeText(this,"Пароль должен содержать от 6 до 20 символов",Toast.LENGTH_SHORT).show();
                passwordET.setError("от 6 до 20 символов");
                confirmPasswordET.setError("от 6 до 20 символов");
                break;

            case NOT_EQUAL:
                Toast.makeText(this,"Пароли не совпадают",Toast.LENGTH_SHORT).show();
                break;
        }
    }




    private int checkCorrectionResult() {

        String pass = passwordET.getText().toString().trim();
        String confPass = confirmPasswordET.getText().toString().trim();

        if (pass.equals("") && confPass.equals(""))
            return EMPTY_FIELDS;

        if(pass.length() < 6 || pass.length() > 20
        || confPass.length() < 6 || confPass.length() >20)
            return TOO_SHORT_OR_LONG;

        if(pass.length() != confPass.length())
            return NOT_EQUAL;

        return PASSWORD_IS_CORRECT;
    }




    private String getJsonForGetSmsCode(String enteredText) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(AppApiUtils.ACTION, ACTION_REQUEST_GET_SMS_CODE);
            if (enteredText.startsWith("6393")) {
                jsonObject.put("card", enteredText.trim());
            } else {
                jsonObject.put("phone", enteredText.replaceAll("[+]",""));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }



    private String getJsonForConfirmSmsCode(String smsCode) {

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(AppApiUtils.ACTION, ACTION_REQUEST_CONFIRM_SMS_CODE);
            jsonObject.put("code", smsCode.trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }



    private String getJsonForNewPassword(String smsCode, String newPasswordStr) {

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(AppApiUtils.ACTION, ACTION_CHANGE_PASSWORD);
            jsonObject.put("code", smsCode.trim());
            jsonObject.put("password", newPasswordStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }



    @Override
    public void responseCompleteHandler(String actionName, String jsonFromApi) {

        if (actionName.equals(ACTION_REQUEST_GET_SMS_CODE)) {
            showMessageDialog("Уведомление", getString(R.string.check_sms_pass_recovery), successGetSmsAction, false);
        } else if(actionName.equals(ACTION_REQUEST_CONFIRM_SMS_CODE)) {
            showMessageDialog("Уведомление", getString(R.string.sms_code_valid_recovery), successCheckSmsCodeAction, false);
        } else if (actionName.equals(ACTION_CHANGE_PASSWORD)) {
            showMessageDialog("Готово!", getString(R.string.successfull_change_pass), successSendNewPasswordAction,false);
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
