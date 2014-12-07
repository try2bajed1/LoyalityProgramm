package su.ias.malina.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import su.ias.malina.R;
import su.ias.malina.activities.BarcodeActivity;
import su.ias.malina.adapters.CardsListAdapter;
import su.ias.malina.data.UserCardItem;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 24.03.2014
 * Time: 12:54
 */
public class UserCardsFragment extends ListFragment {


    private ArrayList<UserCardItem> cardsArr;


    public static final String BARCODE = "barcode";
//    public static final int MODE_ALL_CARDS = 1;
//    public static final int MODE_ACTIVE_CARDS = 2;




    @Override
    public void onAttach(android.app.Activity activity) {

        super.onAttach(activity);
    }



    @Override
    public View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);
        return view;
    }



    public void setCardsAdapter(ArrayList<UserCardItem> arr) {
        cardsArr = arr;
        setListAdapter(new CardsListAdapter(getActivity(), arr));
    }




    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        UserCardItem selectedDiscount = cardsArr.get(position);
        String barCodeUrl = selectedDiscount.getBarCodeUrl();

        if(!barCodeUrl.equals("null") && !barCodeUrl.equals("") ) {
            startActivity(new Intent(getActivity(), BarcodeActivity.class)
                    .putExtra(BARCODE, barCodeUrl));
        }

    }


    @Override
    public void onResume() {

        super.onResume();

        getListView().setDivider(null);
    }



    @Override
    public void onPause() {
        super.onPause();
    }



    @Override
    public void onDestroy() {


        super.onDestroy();
    }

}
