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
import su.ias.malina.data.MapPointData;

import java.util.ArrayList;


/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 29.08.13
 * Time: 18:18
 */

public class PointsListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater layoutInflater;
    public ArrayList<MapPointData> pointsDataArr;





    public PointsListAdapter(Context c, ArrayList<MapPointData> pointsDataArr) {
        mContext = c;
        layoutInflater = LayoutInflater.from(mContext);
//        BitmapDrawer.INSTANCE.setDefaultImageRes(R.drawable.logo_filter, ImageView.ScaleType.CENTER_INSIDE);
        this.pointsDataArr = pointsDataArr;
    }



    public int getCount() {
        return pointsDataArr.size();
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
            convertView = layoutInflater.inflate(R.layout.layout_places_as_list_listitem, null);

            viewHolder = new ViewHolder();
            viewHolder.parnterNameTV = ((TextView) convertView.findViewById((R.id.partner_name)));
            viewHolder.distanceTV = ((TextView) convertView.findViewById((R.id.distance)));
            viewHolder.addressTV = ((TextView) convertView.findViewById((R.id.address)));
            viewHolder.logoIV = (ImageView) convertView.findViewById(R.id.partner_logo);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MapPointData partnerData = pointsDataArr.get(position);
        viewHolder.parnterNameTV.setText(partnerData.getMapPointName());
        viewHolder.addressTV.setText(partnerData.getAddress());
        //viewHolder.distanceTV.setText( String.valueOf(partnerData.getDistance()));

        ImageLoader.getInstance().displayImage(partnerData.getLogoUrl(), viewHolder.logoIV);

        return convertView;
    }



    static class ViewHolder {
        TextView parnterNameTV;
        TextView distanceTV;
        TextView addressTV;
        ImageView logoIV;
    }

}

















