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
import su.ias.malina.data.DiscountData;

import java.util.ArrayList;


/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 29.08.13
 * Time: 18:18
 */

public class DiscountsListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater layoutInflater;
    public ArrayList<DiscountData> discountDatas;





    public DiscountsListAdapter(Context c, ArrayList<DiscountData> discountDatas) {
        mContext = c;
        layoutInflater = LayoutInflater.from(mContext);
//        BitmapDrawer.INSTANCE.setDefaultImageRes(R.drawable.logo_filter, ImageView.ScaleType.CENTER_INSIDE);
        this.discountDatas = discountDatas;
    }



    public int getCount() {
        return discountDatas.size();
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
            convertView = layoutInflater.inflate(R.layout.layout_discounts_list_item, null);

            viewHolder = new ViewHolder();
//            viewHolder.parnterNameTV = ((TextView) convertView.findViewById((R.id.partner_name)));
            viewHolder.discountTitle = ((TextView) convertView.findViewById((R.id.points_rule)));
            viewHolder.logoIV = (ImageView) convertView.findViewById(R.id.partner_logo);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        DiscountData discountData = discountDatas.get(position);
        ImageLoader.getInstance().displayImage(discountData.getLogoUrl(), viewHolder.logoIV);
        viewHolder.discountTitle.setText(discountData.getTitle());
        return convertView;
    }



    static class ViewHolder {
        TextView parnterNameTV;
        TextView discountTitle;
        ImageView logoIV;
    }

}

















