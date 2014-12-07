package su.ias.malina.activities.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import su.ias.malina.R;
import su.ias.malina.activities.StatesActivity;

public class UserModeSelectionFragment extends Fragment {

    private Button carBtn;
    private Button footBtn;
    private Button sofaBtn;

    private StatesActivity activity;

    @Override
	public void onAttach(android.app.Activity activity) {
		super.onAttach(activity);
	}



	@Override
	public View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_mode_selection, container, false);
        carBtn =  (Button) view.findViewById(R.id.car_btn);
        footBtn = (Button) view.findViewById(R.id.on_foot_btn);
        sofaBtn = (Button) view.findViewById(R.id.on_sofa_btn);

		return view;
	}





    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        activity = (StatesActivity) getActivity();

        carBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setCurrentState(activity.getPlacesByCarMapImplState());
                activity.getCurrentState().setPlacesByCarMapImpl();
            }
        });


        footBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*activity.actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
              activity.actionBar.setSelectedNavigationItem(1);*/

                activity.setCurrentState(activity.getPlacesByFootMapImplState());
                activity.getCurrentState().setPlacesByFootMapImpl();
            }
        });


        sofaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              activity.actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//              activity.actionBar.setSelectedNavigationItem(2);
                activity.setCurrentState(activity.getPlacesOnSofaListImplState());
                activity.getCurrentState().setOnSofaListImpl();

            }
        });

    }



    @Override
	public void onResume() {
		super.onResume();
	}



}
