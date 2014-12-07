package su.ias.malina.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import su.ias.malina.R;
import su.ias.malina.async.PostTask;
import su.ias.malina.interfaces.IListener;
import su.ias.malina.utils.AppApiUtils;

/**
 * Created with IntelliJ IDEA. User: n.senchurin Date: 04.03.14 Time: 13:14
 */
public class MyContactsActivity extends BaseActivity implements IListener {

    public static final String ACTION_GET_USER_DATA = "get_contacts";
    public static final String ACTION_SAVE_USER_DATA = "modify_contacts";


    private EditText cityET;
    private TextView cityTV ;

    private EditText streetET;
    private TextView streetTV ;

    private EditText houseET;
    private TextView houseTV ;

    private EditText flatET;
    private TextView flatTV;

    private EditText homePhoneET;
    private TextView homePhoneTV;

    private EditText cellPhoneET;
    private TextView cellPhoneTV;

    private EditText emailET;
    private TextView emailTV;

    private Button doneBtn;

    private boolean isEditMode;
    private Runnable successStep1Action;

    public static final String EDIT_MODE_EXTRA = "edit_mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        actionBar.setTitle("Моя контактная информация");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        setContentView(R.layout.activity_my_contacts);

        initTextFields();

        doneBtn = (Button) findViewById(R.id.modify_button);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isEditMode) {
                    isEditMode = true;
                } else {
                    isEditMode = false;
                    doneBtn.setEnabled(false);
                    new PostTask(MyContactsActivity.this).execute(ACTION_SAVE_USER_DATA, getJsonForSavingUserData());
                }

                setEditMode(isEditMode);
            }
        });

        isEditMode = getIntent().getBooleanExtra(EDIT_MODE_EXTRA, false);
        setEditMode(isEditMode);

        new PostTask(this).execute(ACTION_GET_USER_DATA, getJsonForGettingUserData());

        successStep1Action = new Runnable() {
            @Override
            public void run() {

                doneBtn.setEnabled(true);
                setEditMode(false);
            }
        };

    }




    private void initTextFields() {

        cityTV = (TextView) findViewById(R.id.city_tv);
        cityET = (EditText) findViewById(R.id.city_et);

        streetET = (EditText)findViewById(R.id.street_et);
        streetTV = (TextView)findViewById(R.id.street_tv);

        houseET = (EditText) findViewById(R.id.house_et);
        houseTV = (TextView) findViewById(R.id.house_tv);

        flatET = (EditText) findViewById(R.id.flat_et);
        flatTV = (TextView) findViewById(R.id.flat_tv);

        homePhoneET = (EditText) findViewById(R.id.home_phone_et);
        homePhoneTV = (TextView) findViewById(R.id.home_phone_tv);

        cellPhoneET = (EditText) findViewById(R.id.cell_phone_et);
        cellPhoneTV = (TextView) findViewById(R.id.cell_phone_tv);

        emailET = (EditText) findViewById(R.id.email_et);
        emailTV = (TextView) findViewById(R.id.email_tv);
    }



    // если режим редактирования - прячем лейблы и показываем инпуты, и наоборот
    private void setEditMode(boolean edit) {

        cityET.setVisibility(edit ? View.VISIBLE : View.GONE);
        streetET.setVisibility(edit ? View.VISIBLE : View.GONE);
        houseET.setVisibility(edit ? View.VISIBLE : View.GONE);
        flatET.setVisibility(edit ? View.VISIBLE : View.GONE);
        homePhoneET.setVisibility(edit ? View.VISIBLE : View.GONE);
        cellPhoneET.setVisibility(edit ? View.VISIBLE : View.GONE);
        emailET.setVisibility(edit ? View.VISIBLE : View.GONE);


        cityTV.setVisibility(edit ? View.GONE : View.VISIBLE);
        streetTV.setVisibility(edit ? View.GONE : View.VISIBLE);
        houseTV.setVisibility(edit ? View.GONE : View.VISIBLE);
        flatTV.setVisibility(edit ? View.GONE : View.VISIBLE);
        homePhoneTV.setVisibility(edit ? View.GONE : View.VISIBLE);
        cellPhoneTV.setVisibility(edit ? View.GONE : View.VISIBLE);
        emailTV.setVisibility(edit ? View.GONE : View.VISIBLE);


        doneBtn.setText( edit ? "Сохранить" :"Изменить");

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





    public  String getJsonForSavingUserData() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(AppApiUtils.ACTION, ACTION_SAVE_USER_DATA);
            JSONObject updateContacts = new JSONObject();
            updateContacts.put("home_phone", homePhoneET.getText().toString());
            updateContacts.put("street", streetET.getText().toString());
            updateContacts.put("email",emailET.getText().toString());
            updateContacts.put("house_no",houseET.getText().toString());
            updateContacts.put("city",cityET.getText().toString());
//            updateContacts.put("postcode",postIndexET.getText().toString());
            updateContacts.put("flat_no", flatET.getText().toString());
            updateContacts.put("mobile_phone", cellPhoneET.getText().toString());

            jsonObject.put("new_contacts", updateContacts);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }




    public static String getJsonForGettingUserData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(AppApiUtils.ACTION, ACTION_GET_USER_DATA);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }




    @Override
    protected void onResume() {

        super.onResume();

        updateContent();
    }





    private void updateContent() {

    }




    @Override
    public void responseCompleteHandler(String actionName, String jsonFromApi) {

        if (actionName.equals(ACTION_GET_USER_DATA)) {

            try {

                JSONObject apiJson = new JSONObject(jsonFromApi);
                JSONObject userContactsJson = apiJson.getJSONObject("contacts");

                /*{"birth_date":null,"middle_name":"Фёдорович","street":null,"postcode":null,"codeword":"TEST",
                "city":"Москва","mobile_phone":"79240600008","city_type":null,"email":"dyadya.ancle-fedor@yandex.ru",
                "building_no":null,"house_no":null,"home_phone":null,"gender":"M","house_frac":null,"district":null,"flat_no":null}*/

              /*  String postCodeStr = userContactsJson.getString("postcode");
                if(postCodeStr.equals("null"))
                    postCodeStr = "";
                postIndexET.setText(postCodeStr);
                postIndexTV.setText(postCodeStr);
*/


                String cityStr = userContactsJson.getString("city");
                if(cityStr.equals("null"))
                    cityStr = "";
                cityET.setText(cityStr);
                cityTV.setText(cityStr);


                String streetStr = userContactsJson.getString("street");
                if(streetStr.equals("null"))
                    streetStr = "";
                streetET.setText(streetStr);
                streetTV.setText(streetStr);


                String houseNumStr = userContactsJson.getString("house_no");
                if(houseNumStr.equals("null"))
                    houseNumStr = "";
                houseET.setText(houseNumStr);
                houseTV.setText(houseNumStr);


                String flatStr = userContactsJson.getString("flat_no");
                if(flatStr.equals("null"))
                    flatStr = "";
                flatET.setText(flatStr);
                flatTV.setText(flatStr);


                String homePhoneStr = userContactsJson.getString("home_phone");
                if(homePhoneStr.equals("null"))
                    homePhoneStr = "";
                homePhoneET.setText(homePhoneStr);
                homePhoneTV.setText(homePhoneStr);


                String mobPhoneStr = userContactsJson.getString("mobile_phone");
                if(mobPhoneStr.equals("null"))
                    mobPhoneStr = "";
                cellPhoneET.setText(mobPhoneStr);
                cellPhoneTV.setText(mobPhoneStr);


                String emailStr = userContactsJson.getString("email");
                if(emailStr.equals("null"))
                    emailStr = "";
                emailET.setText(emailStr);
                emailTV.setText(emailStr);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if(actionName.equals(ACTION_SAVE_USER_DATA)) {
            showMessageDialog("Уведомление",  "Контактные данные успешно изменены.", successStep1Action,false);
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
