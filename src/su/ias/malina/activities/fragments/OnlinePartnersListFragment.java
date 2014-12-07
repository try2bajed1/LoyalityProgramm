package su.ias.malina.activities.fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import su.ias.malina.R;
import su.ias.malina.activities.SelectedPartnerActivity;
import su.ias.malina.activities.StatesActivity;
import su.ias.malina.adapters.PartnersListAdapter;
import su.ias.malina.app.AppSingleton;
import su.ias.malina.async.PostTask;
import su.ias.malina.data.PartnerDataSimplified;
import su.ias.malina.interfaces.IListener;
import su.ias.malina.utils.AppApiUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class OnlinePartnersListFragment extends ListFragment implements IListener {


    private int contentType;
    public static final String MODE_EXTRA = "mode";

    private AsyncTask<String, Void, Boolean> _getFilteredPartnersTask;
    private ArrayList<PartnerDataSimplified> onlinePartnersArr;


    public static OnlinePartnersListFragment getInstance(int mode){

        OnlinePartnersListFragment pointsListFragment = new OnlinePartnersListFragment();
        Bundle args = new Bundle();
        args.putInt(MODE_EXTRA, mode);
        pointsListFragment.setArguments(args);

        return pointsListFragment;
    }




    @Override
    public void onAttach(android.app.Activity activity) {

        // this.activity = getActivity();
        super.onAttach(activity);

        contentType = getArguments().getInt(MODE_EXTRA, -1);
        onlinePartnersArr = new ArrayList<PartnerDataSimplified>();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadOnlinePartners();
    }



    @Override
    public void onResume() {
        super.onResume();

    }



    @Override
    public void onStop() {
        super.onStop();

        cancelTaskIfRunning();
    }




    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        PartnerDataSimplified onlinePartner = onlinePartnersArr.get(position);
        int selectedOnlinePartnerId = onlinePartner.getId();
        String rules = onlinePartner.getPointsRule();

        if(!rules.equals("null") && !rules.equals("") ) {
            startActivity(new Intent(getActivity(), SelectedPartnerActivity.class)
                                    .putExtra(SelectedPartnerActivity.SELECTED_PARTNER_ID, selectedOnlinePartnerId)
                                    .putExtra(SelectedPartnerActivity.MODE_EXTRA, SelectedPartnerActivity.ONLY_SOFA_MODE)
                                    .putExtra(SelectedPartnerActivity.IS_ONLINE, true));
        }
    }



    private void cancelTaskIfRunning() {
        if (_getFilteredPartnersTask != null && _getFilteredPartnersTask.getStatus() == AsyncTask.Status.RUNNING) {
            _getFilteredPartnersTask.cancel(true);
        }
    }


    private void restartTask(String jsonAsStr) {

        cancelTaskIfRunning();

        _getFilteredPartnersTask = new PostTask(this);
        _getFilteredPartnersTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, AppApiUtils.ACTION_GET_PARTNERS, jsonAsStr);
    }



    private String getPartnersPostHeaderJson() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(AppApiUtils.ACTION, AppApiUtils.ACTION_GET_PARTNERS);
            JSONObject filterParams = new JSONObject();
            filterParams.put("categories_ids", new JSONArray(Arrays.toString(AppSingleton.get().getDBAdapter().getSelectedCategories())));
            filterParams.put("partner_type", "online");
            jsonObject.put(AppApiUtils.FILTER_PARAMS, filterParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("@", " " + jsonObject.toString());
        return jsonObject.toString();
    }




    public void loadOnlinePartners() {

        restartTask(getPartnersPostHeaderJson());


    }



    @Override
    public void onPause() {
        super.onPause();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }



    @Override
    public void responseCompleteHandler(String actionName, String jsonStrFromApi) {

        onlinePartnersArr.clear();

        try {

            //{"id":366,"type":"online","points_rule":"до 6 баллов за каждые 10 рублей",
            // "logo_url":"http:\/\/lpvstatic.ru\/media\/partners\/brands\/logo_70_1.jpg",
            // "points_type":"","name":"003.ru"},
            // {"id":422,"type":"online","points_rule":"6 баллов за каждые 10 рублей","logo_url":"http:\/\/lpvstatic.ru\/media\/partners\/brands\/logo_127_1.jpg","points_type":"","name":"ALBA"},{"id":943,"type":"online","points_rule":"4 балла за каждые 10 рублей","logo_url":"http:\/\/lpvstatic.ru\/media\/partners\/brands\/logo_85.JPG","points_type":"","name":"ASMC - армейский магазин"},{"id":432,"type":"online","points_rule":"3 балла за каждые 10 рублей","logo_url":"http:\/\/lpvstatic.ru\/media\/partners\/brands\/asos_logo_1.jpg","points_type":"","name":"ASOS RU"},{"id":372,"type":"online","points_rule":"1 500 баллов за заказ","logo_url":"http:\/\/lpvstatic.ru\/media\/partners\/brands\/logo_200_2.jpg","points_type":"","name":"Acoola"},{"id":722,"type":"online","points_rule":"1 балл за каждые 10 рублей","logo_url":"http:\/\/lpvstatic.ru\/media\/partners\/brands\/logo_244.jpg","points_type":"","name":"Apple Store "},{"id":435,"type":"online","points_rule":"2 балла за каждые 10 рублей","logo_url":"http:\/\/lpvstatic.ru\/media\/partners\/brands\/logo_140_1.jpg","points_type":"","name":"Audiomania"},{"id":387,"type":"online","points_rule":"1 балл за каждые 20 рублей","logo_url":"http:\/\/lpvstatic.ru\/media\/partners\/brands\/logo_94_1.jpg","points_type":"","name":"Aviasales.ru"},{"id":336,"type":"online","points_rule":"2 балла за каждые 10 рублей","logo_url":"http:\/\/lpvstatic.ru\/media\/partners\/brands\/logo_187_2.jpg","points_type":"","name":"BUTIK.RU"},{"id":473,"type":"online","points_rule":"1 600 баллов за заказ","logo_url":"http:\/\/lpvstatic.ru\/media\/partners\/brands\/logo_177_1.jpg","points_type":"","name":"Babadu"},{"id":445,"type":"online","points_rule":"2 200 баллов за заказ","logo_url":"http:\/\/lpvstatic.ru\/media\/partners\/brands\/logo_150_1.jpg","points_type":"","name":"Bestwatch.ru"},{"id":401,"type":"online","points_rule":"4 балла за каждые 10 рублей","logo_url":"http:\/\/lpvstatic.ru\/media\/partners\/brands\/logo_107_1.jpg","points_type":"","name":"Bobber"},{"id":419,"type":"online","points_rule":"6 баллов за каждые 10 рублей","logo_url":"http:\/\/lpvstatic.ru\/media\/partners\/brands\/logo_124_1.jpg","points_type":"","name":"Brand in Trend"},{"id":1043,"type":"online","points_rule":"3 балла за каждые 10 рублей","logo_url":"http:\/\/lpvstatic.ru\/media\/partners\/brands\/logo_262.JPG","points_type":"","name":"DC Shoes"},{"id":470,"type":"online","points_rule":"2 балла за каждые 10 рублей","logo_url":"http:\/\/lpvstatic.ru\/media\/partners\/brands\/logo_174_1.jpg","points_type":"","name":"DeoShop.ru"},{"id":947,"type":"online","points_rule":"4 балла за каждые 10 рублей","logo_url":"http:\/\/lpvstatic.ru\/media\/partners\/brands\/logo_255.jpg","points_type":"","name":"Easy Meal"},{"id":403,"type":"online","points_rule":" 4 балла за каждые 10 рублей","logo_url":"http:\/\/lpvstatic.ru\/media\/partners\/brands\/logo_109_1.jpg","points_type":"","name":"ElitDress.ru"},{"id":459,"type":"online","points_rule":"2 500 баллов за заказ","logo_url":"http:\/\/lpvstatic.ru\/media\/partners\/brands\/logo_163_1.jpg","points_type":"","name":"Fashion Galaxy"},{"id":449,"type":"online","points_rule":"4 балла за каждые 10 рублей","logo_url":"http:\/\/lpvstatic.ru\/media\/partners\/brands\/logo_153_1.jpg","points_type":"","name":"Fixon.ru"},{"id":443,"type":"online","points_rule":"1 балл за каждые 10 рублей","logo_url":"http:\/\/lpvstatic.ru\/media\/partners\/brands\/logo_148_1.jpg","points_type":"","name":"ForOffice"},{"id":405,"type":"online","point

            JSONObject jsonResponce = new JSONObject(jsonStrFromApi);
            JSONArray array = jsonResponce.getJSONArray(AppApiUtils.PARTNERS);

            for (int i = 0; i < array.length(); i++) {

                JSONObject partnerObj = array.getJSONObject(i);

                Integer id = partnerObj.has("id") ? partnerObj.getInt("id") : -1;
                String name = partnerObj.has("name") ? partnerObj.getString("name") : "";
                String logoUrl = partnerObj.has("logo_url") ? partnerObj.getString("logo_url") : "";
                String pointsRule = partnerObj.has("points_rule") ? partnerObj.getString("points_rule") : "";

                PartnerDataSimplified partnerDataSimplified = new PartnerDataSimplified(id,name,  logoUrl, pointsRule, "online");

                onlinePartnersArr.add(partnerDataSimplified);
            }

        } catch (JSONException e) {
            Log.i("@"," parnters error "+ e.toString());
        }

        setListAdapter(new PartnersListAdapter(getActivity(), onlinePartnersArr));
        ((StatesActivity) getActivity()).setActionBarLoading(false);
    }



    @Override
    public void responseErrorHandler(String actionName, String errorStr) {
        Toast.makeText(getActivity(), "Ошибка при передаче данных", Toast.LENGTH_SHORT).show();
        ((StatesActivity) getActivity()).setActionBarLoading(false);
    }




}
