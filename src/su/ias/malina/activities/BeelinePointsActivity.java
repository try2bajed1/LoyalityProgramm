package su.ias.malina.activities;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import org.json.JSONException;
import org.json.JSONObject;
import su.ias.malina.R;
import su.ias.malina.async.PostTask;
import su.ias.malina.interfaces.IListener;
import su.ias.malina.utils.AppApiUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 24.09.2014
 * Time: 10:35
 */

public class BeelinePointsActivity extends BaseActivity implements IListener {

    private LinearLayout step1LL;
    private LinearLayout step2LL;

    private Spinner cardsSpinner;
    private EditText beelineNumET;
    private EditText smsCodeET;
    private Button sendBtn;


    private Runnable successStep1Action;
    private Runnable successStep2Action;

    private int currStep;
    private String selectedCardCode;
    private String beelinePhone;
    private String receivedSmsCode;
    public static final String EXTRA_CARDS_ARRAY = "extra_cards";
    public static final String EXTRA_CARD_ID = "card_id";

    private static final int STEP_1 = 1;
    private static final int STEP_2 = 2;

    private String[] cardsArr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_beeline);

        cardsArr = getIntent().getStringArrayExtra(EXTRA_CARDS_ARRAY);
        if (cardsArr == null) {
            cardsArr = new String[]{getIntent().getStringExtra(EXTRA_CARD_ID)};
        }



        currStep = STEP_1;

        actionBar.setIcon(R.drawable.logo_filter);
        actionBar.setTitle("Добавление карты");

        step1LL = (LinearLayout) findViewById(R.id.step_1);
        step2LL = (LinearLayout) findViewById(R.id.step_2);
        step2LL.setVisibility(View.GONE);

        cardsSpinner = (Spinner) findViewById(R.id.cards_spinner);
        cardsSpinner.setAdapter(new CardsSpinnerAdapter(cardsArr));

/*        cardsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        })*/


        beelineNumET = (EditText) findViewById(R.id.beeline_num);
        beelineNumET.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {

                String expression = "\\d{10}$";
                CharSequence inputStr = beelineNumET.getText().toString().replaceAll("[-]","");
                Pattern pattern = Pattern.compile(expression);
                Matcher matcher = pattern.matcher(inputStr);
                sendBtn.setEnabled(matcher.matches());
            }

        });

        beelineNumET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(beelineNumET.getWindowToken(), 0);

                    step1Handler();
                }
                return true;
            }
        });



        smsCodeET = (EditText) findViewById(R.id.belline_sms_code_input);
        smsCodeET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() != 0) {
                    sendBtn.setEnabled(true);
                } else {
                    sendBtn.setEnabled(false);
                }
            }
        });
        smsCodeET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    step2Handler();
                }
                return true;
            }
        });



        sendBtn = (Button) findViewById(R.id.send_btn);
        sendBtn.setEnabled(false);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBtnHandler();
            }
        });


        successStep1Action = new Runnable() {
            @Override
            public void run() {
                messageDialog.dismiss();
                currStep = STEP_2;
                sendBtn.setEnabled(false);
                step1LL.setVisibility(View.GONE);
                step2LL.setVisibility(View.VISIBLE);
            }
        };

        successStep2Action = new Runnable() {
            @Override
            public void run() {
                messageDialog.dismiss();
                finish();
            }
        };
    }



    private void sendBtnHandler() {
        if (currStep == STEP_1) {
            step1Handler();
        } else {
            step2Handler();
        }
    }



    private void step1Handler(){
        selectedCardCode = cardsArr[cardsSpinner.getSelectedItemPosition()].replaceAll("[ ]", "");
        beelinePhone = beelineNumET.getText().toString().replaceAll("[-]","");
        String strToPost = AppApiUtils.getJsonForBeeline(selectedCardCode, beelinePhone);
        new PostTask(this).execute(AppApiUtils.ACTION_REGISTER, strToPost);
    }



    private void step2Handler() {
        receivedSmsCode = smsCodeET.getText().toString();
        String strToPost = AppApiUtils.getJsonForBeeline(selectedCardCode, beelinePhone, receivedSmsCode);
        new PostTask(this).execute(AppApiUtils.ACTION_CHECK_BEELINE_CODE, strToPost);
    }



    @Override
    public void responseCompleteHandler(String actionName, String jsonFromApi) {
        if (actionName.equals(AppApiUtils.ACTION_REGISTER)) {
            showMessageDialog("Уведомление", getString(R.string.check_sms_beeline),successStep1Action,false);
        } else {
            String alertMsg = "Номер телефона "+ beelinePhone +"успешно привязан к карте МАЛИНА "+ selectedCardCode;
            showMessageDialog("Уведомление", alertMsg, successStep2Action,false);
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



    private class CardsSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

        String[] data;

        public CardsSpinnerAdapter(String[] data){
            super();
            this.data = data;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.layout_spinner_item, null);
            }

            TextView text = (TextView) convertView.findViewById(R.id.list_item_text);
            text.setText(data[position]);
            return convertView;
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return data[position];
        }

        @Override
        public int getCount() {
            return data.length;
        }


        public View getDropDownView(int position, View convertView,ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.layout_spinner_item, null);
            }

            TextView tv= (TextView) convertView.findViewById(R.id.list_item_text);
            tv.setText(Html.fromHtml(data[position]));

            return convertView;
        }

    }

}
