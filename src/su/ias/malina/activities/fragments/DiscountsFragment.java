package su.ias.malina.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import su.ias.malina.R;
import su.ias.malina.activities.SelectedDiscountActivity;
import su.ias.malina.adapters.DiscountsListAdapter;
import su.ias.malina.data.DiscountData;

import java.util.ArrayList;


/**
 * Created with IntelliJ IDEA. User: n.senchurin Date: 21.03.14 Time: 12:36
 */
public class DiscountsFragment extends ListFragment {

    private ArrayList<DiscountData> discountsArr;

	@Override
	public void onAttach(android.app.Activity activity) {

		super.onAttach(activity);
	}



	@Override
	public View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_list,  container, false);
	}



	@Override
	public void onResume() {
		super.onResume();
	}



    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        DiscountData selectedDiscount = discountsArr.get(position);
//        String rules = selectedDiscount.getRules();


        startActivity(new Intent(getActivity(), SelectedDiscountActivity.class)
                            .putExtra(SelectedDiscountActivity.SELECTED_DISCOUNT, selectedDiscount));


/*        if(!rules.equals("null") && !rules.equals("") ) {
            startActivity(new Intent(getActivity(), SelectedDiscountActivity.class)
                                .putExtra(SelectedDiscountActivity.SELECTED_DISCOUNT, selectedDiscount));
        }  else {
            startActivity(new Intent(getActivity(), SelectedDiscountActivity.class)
                                .putExtra(SelectedDiscountActivity.SELECTED_DISCOUNT, selectedDiscount));
        }*/


    }






    public void setDiscountsAdapter(ArrayList<DiscountData> arr) {

        discountsArr = arr;
        setListAdapter(new DiscountsListAdapter(getActivity(), arr));

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
