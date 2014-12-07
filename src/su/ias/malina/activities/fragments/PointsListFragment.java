package su.ias.malina.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import su.ias.malina.R;
import su.ias.malina.activities.SelectedPartnerActivity;
import su.ias.malina.activities.SinglePartnerPointsActivity;
import su.ias.malina.activities.StatesActivity;
import su.ias.malina.adapters.PointsListAdapter;
import su.ias.malina.data.MapPointData;

import java.util.ArrayList;

public class PointsListFragment extends ListFragment {

    private int contentType;
    private boolean allowBackBtn;

    public static final String MODE_EXTRA = "mode";
    public static final String POINTS_ARRAY_EXTRA = "points_array";
    public static final String ALLOW_BACK_BTN_EXTRA = "allow_back";

    private ArrayList<MapPointData> dataArr;

    public static PointsListFragment getInstance(int mode){
        PointsListFragment pointsListFragment = new PointsListFragment();
        Bundle args = new Bundle();
        args.putInt(MODE_EXTRA, mode);
        pointsListFragment.setArguments(args);
        return pointsListFragment;
    }


    @Override
    public void onAttach(android.app.Activity activity) {

        super.onAttach(activity);

        //возможны добавления для отображения точек в виде списка для определенной акции
        //загружать будем этот же фрагмент, а данные для адаптера будем выбирать в зависимости от поля contentType
        contentType = getArguments().getInt(MODE_EXTRA, -1);

        dataArr = contentType == SinglePartnerPointsActivity.SINGLE_PARTNER_MODE ? SinglePartnerPointsActivity.pointsDataArr
                                                                                 : StatesActivity.pointsDataArr;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, null);
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setListAdapter(new PointsListAdapter(getActivity(),dataArr));

        if (getActivity() != null) {

            if(getActivity() instanceof  StatesActivity) {
                ((StatesActivity) getActivity()).setActionBarLoading(false);
            }
        }
    }



    @Override
    public void onResume() {
        super.onResume();

    }




    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {


        MapPointData pointData =dataArr.get(position);
        int selectedOnlinePartnerId = pointData.getPartner_id();

        startActivity(new Intent(getActivity(), SelectedPartnerActivity.class)
                .putExtra(SelectedPartnerActivity.SELECTED_PARTNER_ID, selectedOnlinePartnerId)
                .putExtra(SelectedPartnerActivity.MODE_EXTRA, SelectedPartnerActivity.ALL_MODE)
                .putExtra(SelectedPartnerActivity.IS_ONLINE, false));
    }







    // TODO: добавить отображение точек акций (ждем правки на сервере)
 /*   private void setNearestPoints() {
        ((StatesActivity) getActivity()).setActionBarLoading(false);
    }
*/


    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



/*
    private void setList() {

        ArrayList<SofaItemData> sofaItemDataArr = AppSingleton.get().getDBAdapter().getPartnersByType("msk", "online");
        contentLV.setAdapter(new PartnersListAdapter(getActivity(), sofaItemDataArr));

        if (getActivity() != null) ((StatesActivity) getActivity()).setActionBarLoading(false);
    }*/


    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }


    public boolean getAllowBackBtn() {
        return allowBackBtn;
    }




}
