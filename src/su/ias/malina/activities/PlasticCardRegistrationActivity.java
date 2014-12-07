package su.ias.malina.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import org.json.JSONException;
import org.json.JSONObject;
import su.ias.malina.R;
import su.ias.malina.app.AppSingleton;
import su.ias.malina.async.PostTask;
import su.ias.malina.interfaces.IListener;
import su.ias.malina.maskedEditText.MaskedEditText;
import su.ias.malina.utils.AppApiUtils;

import java.nio.charset.Charset;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 25.09.2014
 * Time: 14:49
 */

public class PlasticCardRegistrationActivity extends BaseActivity implements IListener {


    private int myYear;
    private int myMonth;
    private int myDay;
    private static final int ID_DATEPICKER = 0;
    private String formatedDateStr ="";


    private MaskedEditText cardCodeET;
    private MaskedEditText phoneET;
    private EditText receivedCodeET;

    private EditText passwordET;
    private EditText confirmpasswordET;
    private EditText nameET;
    private EditText surnameET;


    private Button doneBtn;
    private TextView birthTV;
    private Runnable successSignUpRequestAction;




    private Button datePickerBtn;
    private Button getSmsCodeBtn;


    private String phoneNumStr;
    private String cardCodeStr;
    private TextView acceptRulesTV;


//    private boolean isExistingCardMode;
//    private LinearLayout extendedModeContainerRL;
    private CheckBox acceptRulesChBox;



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_registration);

        actionBar.setTitle("Регистрация карты");

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        cardCodeET = (MaskedEditText) findViewById(R.id.card_number);
        phoneET = (MaskedEditText) findViewById(R.id.phone_number);

//        getCodeBtn = (Button) findViewById(R.id.get_sms_code);
        receivedCodeET = (EditText) findViewById(R.id.code_from_sms);

        passwordET = (EditText) findViewById(R.id.input_password);
        confirmpasswordET = (EditText) findViewById(R.id.repeat_password);

        nameET  =   (EditText) findViewById(R.id.name);
        surnameET = (EditText) findViewById(R.id.second_name);

        birthTV = (TextView) findViewById(R.id.birth_value);
        datePickerBtn = (Button)findViewById(R.id.birth_date_picker_btn);
        datePickerBtn.setOnClickListener(datePickerButtonOnClickListener);


        acceptRulesChBox = (CheckBox) findViewById(R.id.accept_chbox);
        acceptRulesChBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                doneBtn.setEnabled(isChecked);
            }
        });


        acceptRulesTV = (TextView) findViewById(R.id.accept_tv);
        acceptRulesTV.setPaintFlags(acceptRulesTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        acceptRulesTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlasticCardRegistrationActivity.this, TermsAndConditionsActivity.class));
            }
        });


        getSmsCodeBtn = (Button) findViewById(R.id.get_sms_code_btn);
        getSmsCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = true;

                if (cardCodeET.getText().toString().replaceAll("[ -]", "").isEmpty()) {
                    cardCodeET.setError("Нет данных");
                    cardCodeET.requestFocus();
                    result = false;
                }

                if (phoneET.getText().toString().replaceAll("[ -]", "").isEmpty()) {
                    phoneET.setError("Нет данных");
                    phoneET.requestFocus();
                    result = false;
                }

                if(result) {
                    phoneNumStr = "7" + phoneET.getText().toString().replaceAll("[ -]", "");
                    new PostTask(PlasticCardRegistrationActivity.this).execute(AppApiUtils.ACTION_GET_ACTIVATION_CODE,
                                                                               AppApiUtils.getJsonForActivationCode(phoneNumStr));
                }else {
                    Toast.makeText(PlasticCardRegistrationActivity.this, "Не указан номер карты или телефона", Toast.LENGTH_SHORT).show();
                }
            }
        });



        doneBtn = (Button) findViewById(R.id.done_btn);
        doneBtn.setEnabled(false);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = true;

                phoneNumStr = "7" + phoneET.getText().toString().replaceAll("[ -]", "");
                cardCodeStr = "6393" + cardCodeET.getText().toString().replaceAll("[ -]", "");

                String smsCode = receivedCodeET.getText().toString().trim();
                if (smsCode.isEmpty()) {
                    result = false;
                    receivedCodeET.setError("Нет данных");
                }

                String password = passwordET.getText().toString().trim();
                if (password.isEmpty()) {
                    result = false;
                    passwordET.setError("Нет данных");
                }
                if (password.length() < 6 || password.length() > 20) {
                    passwordET.setError("от 6 до 20 символов");
                }

                String confrirmedPass = confirmpasswordET.getText().toString().trim();
                if (confrirmedPass.isEmpty()) {
                    result = false;
                    confirmpasswordET.setError("Нет данных");
                }
                if (confrirmedPass.length() < 6 || confrirmedPass.length() > 20) {
                    confirmpasswordET.setError("от 6 до 20 символов");
                }

                if (!password.equals(confrirmedPass)) {
                    result = false;
                    passwordET.setError("Пароли не совпадают");
                    confirmpasswordET.setError("Пароли не совпадают");
                }


                String name = nameET.getText().toString().trim();
                if (name.isEmpty()) {
                    result = false;
                    nameET.setError("Нет данных");
                }

                String surname = surnameET.getText().toString().trim();
                if (surname.isEmpty()) {
                    result = false;
                    surnameET.setError("Нет данных");
                }


                if (formatedDateStr.isEmpty()) {
                    Toast.makeText(PlasticCardRegistrationActivity.this, "Не указана дата рождения", Toast.LENGTH_SHORT).show();
                }


                if (result) {
                    String jsonToPost = AppApiUtils.getJsonForSignUp(phoneNumStr,
                                                                        smsCode,
                                                                        cardCodeStr,
                                                                        password,
                                                                        name,
                                                                        surname,
                                                                        formatedDateStr,
                                                                        AppSingleton.get().userRegion);

                    new PostTask(PlasticCardRegistrationActivity.this).execute(AppApiUtils.ACTION_SIGN_UP, jsonToPost);
                }

            }
        });


        successSignUpRequestAction = new Runnable() {
            @Override
            public void run() {
                finish();
            }
        };
    }






    private Button.OnClickListener datePickerButtonOnClickListener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            final Calendar c = Calendar.getInstance();
            myYear = c.get(Calendar.YEAR);
            myMonth = c.get(Calendar.MONTH);
            myDay = c.get(Calendar.DAY_OF_MONTH);
            showDialog(ID_DATEPICKER);
        }
    };



    @Override
    protected Dialog onCreateDialog(int id) {

        switch(id){
            case ID_DATEPICKER:
                return new DatePickerDialog(this,myDateSetListener, myYear, myMonth, myDay);

            default:
                return null;
        }
    }



    private DatePickerDialog.OnDateSetListener myDateSetListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            formatedDateStr = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
            birthTV.setText(formatedDateStr);
        }
    };




    @Override
    public void responseCompleteHandler(String actionName, String jsonFromApi) {
        if (actionName.equals(AppApiUtils.ACTION_GET_ACTIVATION_CODE)) {
            showResultDialog("Уведомление", getString(R.string.check_sms_mobile_card));
        } else if(actionName.equals(AppApiUtils.ACTION_SIGN_UP)) {
            showMessageDialog("Уведомление", getString(R.string.card_registered), successSignUpRequestAction, false);
        }
    }




    @Override
    public void responseErrorHandler(String actionName, String errorStr) {
        try {
            JSONObject jsonFromApi = new JSONObject(errorStr);
            String alertMsg = jsonFromApi.getString("message");
            final Charset UTF_8 = Charset.forName("UTF-8");
            String encodedStr = new String(alertMsg.getBytes(UTF_8), UTF_8);

            showResultDialog("Ошибка!", encodedStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}