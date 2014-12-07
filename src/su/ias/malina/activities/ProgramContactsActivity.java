package su.ias.malina.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import su.ias.malina.R;
import su.ias.malina.async.PostTask;
import su.ias.malina.interfaces.IListener;
import su.ias.malina.utils.AppApiUtils;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 04.03.14
 * Time: 13:09
 */
public class ProgramContactsActivity extends BaseActivity implements IListener {


    private static final String ACTION_GET_PROGRAM_CONTACTS = "program_contacts";

    private ListView listView;
    private ArrayList<Contact> dataArr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);



        actionBar.setTitle("Контакты");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        setContentView(R.layout.activity_contacts);

        listView = (ListView) findViewById(R.id.contacts_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("tel:" + dataArr.get(position).getPhone())));
            }
        });


        new PostTask(this).execute(ACTION_GET_PROGRAM_CONTACTS, getJsonForPost());

    }




    private String getJsonForPost() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(AppApiUtils.ACTION, ACTION_GET_PROGRAM_CONTACTS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }



    @Override
    public void responseCompleteHandler(String actionName, String jsonFromApi) {

        dataArr = new ArrayList<Contact>();

        try {

            JSONObject json = new JSONObject(jsonFromApi);
            Log.i("@", "json " + jsonFromApi);
            JSONArray contacts = json.getJSONArray("contacts");
            for (int i = 0; i < contacts.length(); i++) {
                JSONObject jsonObject = (JSONObject) contacts.get(i);
                dataArr.add(new Contact(jsonObject.getString("region"), jsonObject.getString("phone")));
            }

            listView.setAdapter(new ProgramContactsAdapter(this, dataArr));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void responseErrorHandler(String actionName, String errorStr) {
        Toast.makeText(this, "Ошибка при передаче даннных", Toast.LENGTH_SHORT).show();
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


    class Contact{

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        private String region;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        private String phone;

        Contact(String region, String phone) {
            this.region = region;
            this.phone = phone;
        }
    }


    class ProgramContactsAdapter extends BaseAdapter {

        private Context mContext;
        private LayoutInflater layoutInflater;
        public ArrayList<Contact> contactsArr;


        public ProgramContactsAdapter(Context c, ArrayList<Contact> contactsArr) {

            mContext = c;
            layoutInflater = LayoutInflater.from(mContext);
            this.contactsArr = contactsArr;
        }


        public int getCount() {
            return contactsArr.size();
        }


        public Object getItem(int position) {
            return null;
        }


        public long getItemId(int position) {
            return 0;
        }


        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                convertView = layoutInflater.inflate(R.layout.layout_program_contacts_list_item, null);
            }

            Contact contact = contactsArr.get(position);

            TextView contentTV = (TextView) convertView.findViewById((R.id.content));
            contentTV.setText(contact.getRegion() + "\n" + contact.getPhone());



            return convertView;
        }




    }


}
