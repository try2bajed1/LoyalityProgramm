package su.ias.malina.activities.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import su.ias.malina.R;
import su.ias.malina.app.AppSingleton;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 21.03.14
 * Time: 12:36
 */
public class PartnerDescriptionFragment extends Fragment {

    public static final String KEY_ID= "key_to_get_args";

    private TextView descriptionTV;
    private Button onMapBtn;
    private int partnerIndex;


    @Override
    public void onAttach(android.app.Activity activity){
        super.onAttach(activity);

        partnerIndex = getArguments().getInt(KEY_ID, 0);


    }




    @Override
    public View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_selected_partner, container, false);

        descriptionTV = (TextView) view.findViewById(R.id.partner_description);

        onMapBtn = (Button) view.findViewById(R.id.partners_on_map_btn);
        onMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;

    }



    @Override
    public void onResume() {

        super.onResume();


        String descr = AppSingleton.get().getDBAdapter().getPartnerDescriptionById("msk", partnerIndex);
        descriptionTV.setText(Html.fromHtml(descr));

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
