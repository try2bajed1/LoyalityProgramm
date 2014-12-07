package su.ias.malina.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import su.ias.malina.R;
import su.ias.malina.data.PartnerDataSimplified;

import java.util.ArrayList;


/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 29.08.13
 * Time: 18:18
 */

public class PartnersListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater layoutInflater;
    public ArrayList<PartnerDataSimplified> sofaItemsArr;



    public PartnersListAdapter(Context c, ArrayList<PartnerDataSimplified> sofaItemsArr) {
        mContext = c;
        layoutInflater = LayoutInflater.from(mContext);
        this.sofaItemsArr = sofaItemsArr;
    }



    public int getCount() {
        return sofaItemsArr.size();
    }


    public Object getItem(int position) {
        return null;
    }


    public long getItemId(int position) {
        return 0;
    }



    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.layout_on_sofa_list_item, null);

            viewHolder = new ViewHolder();
            viewHolder.parnterNameTV = ((TextView) convertView.findViewById((R.id.partner_name)));
            viewHolder.priceDescrTV = ((TextView) convertView.findViewById((R.id.price)));
            viewHolder.logoIV = (ImageView) convertView.findViewById(R.id.partner_logo);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        PartnerDataSimplified partnerData = sofaItemsArr.get(position);

        viewHolder.parnterNameTV.setText(partnerData.getPartnerName());
        viewHolder.priceDescrTV.setText(partnerData.getPointsRule());
        ImageLoader.getInstance().displayImage(partnerData.getLogoUrl(), viewHolder.logoIV);

        return convertView;
    }



    static class ViewHolder {
        TextView parnterNameTV;
        TextView priceDescrTV;
        ImageView logoIV;
    }

}

















