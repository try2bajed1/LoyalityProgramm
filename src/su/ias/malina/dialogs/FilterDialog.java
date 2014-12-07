package su.ias.malina.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import su.ias.malina.R;
import su.ias.malina.adapters.PartnersGridAdapter;
import su.ias.malina.app.AppSingleton;
import su.ias.malina.data.PartnerData;
import su.ias.malina.interfaces.IFilterDialogListener;

import java.util.ArrayList;

/**
* Created with IntelliJ IDEA.
* User: n.senchurin
* Date: 28.08.2014
* Time: 11:30
*/
public class FilterDialog extends Dialog {

    private GridView filterGrid;
    private ArrayList<PartnerData> partnersArr;

    private Button selectAllBtn;
    private Button doneBtn;
    private boolean selectAllFlag = false;


    public FilterDialog(final Activity activity, final IFilterDialogListener listener) {

        super(activity, R.style.Theme_Dialog_Translucent);

        setCancelable(true);
        setContentView(R.layout.layout_partners_filter_grid);

        partnersArr = AppSingleton.get().getDBAdapter().getEnabledPartnersByRegion(AppSingleton.get().userRegion);

        filterGrid = (GridView) findViewById(R.id.filter_grid);
        filterGrid.setAdapter(new PartnersGridAdapter(activity, partnersArr));
        filterGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                PartnerData partnerData = partnersArr.get(position);
                partnerData.setShowOnMapFlag(!partnerData.getShowOnMapFlag());

                ImageView flagIV = (ImageView) view.findViewById(R.id.selection_flag_img);
                flagIV.setImageDrawable(partnerData.getShowOnMapFlag() ? activity.getResources().getDrawable(R.drawable.checkbox_activ)
                                                                       : activity.getResources().getDrawable(R.drawable.checkbox));

            }
        });


        selectAllBtn = (Button)findViewById(R.id.select_all_btn);
        selectAllBtn.setText(selectAllFlag ?"Выделить все" :"Очистить");
        selectAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (selectAllFlag) {
                    for (PartnerData partner : partnersArr) {
                        if (!partner.getShowOnMapFlag()) {
                            partner.setShowOnMapFlag(true);
                        }
                    }
                    selectAllBtn.setText("Очистить");
                    selectAllFlag = false;
                } else {

                    for (PartnerData partner : partnersArr) {
                        if (partner.getShowOnMapFlag()) {
                            partner.setShowOnMapFlag(false);
                        }
                    }
                    selectAllBtn.setText("Выделить все");
                    selectAllFlag = true;
                }

                ((PartnersGridAdapter) filterGrid.getAdapter()).notifyDataSetChanged();

            }
        });



        doneBtn = (Button) findViewById(R.id.done_btn);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                saveFilteredIndexesToDB();


//                setActionBarLoading(true);
//                updateFrameContent();

                listener.applyFilter();

                dismiss();
            }
        });
    }


    private void saveFilteredIndexesToDB() {
        AppSingleton.get().getDBAdapter().saveFilteredIds(partnersArr);

    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void show() {
        super.show();
    }

}
