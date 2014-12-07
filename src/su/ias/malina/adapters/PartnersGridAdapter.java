package su.ias.malina.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import su.ias.malina.R;
import su.ias.malina.data.PartnerData;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA. User: n.senchurin Date: 29.08.13 Time: 18:18
 */

public class PartnersGridAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater layoutInflater;
    public ArrayList<PartnerData> partnersArr;

    public PartnersGridAdapter(Context c, ArrayList<PartnerData> partnersArr) {
        mContext = c;
        layoutInflater = LayoutInflater.from(mContext);
        this.partnersArr = partnersArr;
    }



    public int getCount() {
        return partnersArr.size();
    }


    public Object getItem(int position) {
        return null;
    }


    public long getItemId(int position) {
        return 0;
    }



    // todo: add ViewHolder

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_filter_grid_item, null);
        }

        PartnerData partnerData = partnersArr.get(position);

        TextView parnterNameTV = ((TextView) convertView.findViewById((R.id.partner_name)));
        parnterNameTV.setText(partnersArr.get(position).getName());

        ImageView flagIV = (ImageView) convertView.findViewById(R.id.selection_flag_img);
        final ImageView logoIV = (ImageView) convertView.findViewById(R.id.partner_logo);

        flagIV.setImageDrawable(partnerData.getShowOnMapFlag() ? mContext.getResources().getDrawable(R.drawable.checkbox_activ) : mContext.getResources().getDrawable(R.drawable.checkbox));

        ViewTreeObserver observer = logoIV.getViewTreeObserver();

        final String logoImagePath = mContext.getExternalCacheDir().getPath() + "/logos" + position + ".jpg";
        final String logoImageUrl = partnersArr.get(position).getLogoUrl();


        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {

                Log.w("pic", "creating image for position " + position);
                Log.w("pic", "path for logo: " + logoImagePath);
                Log.w("pic", "url for logo: " + logoImageUrl);

                logoIV.getViewTreeObserver().removeOnPreDrawListener(this);

                ImageLoader.getInstance().displayImage(logoImageUrl, logoIV);

//                BitmapDrawer.INSTANCE.drawBitmap(logoImagePath, logoImageUrl,logoIV, BitmapDrawer.DRAW_ALL, true, true);
                return true;
            }
        });


        return convertView;
    }

}
