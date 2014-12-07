package su.ias.malina.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import su.ias.malina.R;
import su.ias.malina.data.UserCardItem;

import java.util.ArrayList;


/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 29.08.13
 * Time: 18:18
 */


public class CardsListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater layoutInflater;
    public ArrayList<UserCardItem> userCardItems;


    public CardsListAdapter(Context c, ArrayList<UserCardItem> userCardItems) {
        mContext = c;
        layoutInflater = LayoutInflater.from(mContext);
        this.userCardItems = userCardItems;
    }



    public int getCount() {
        return userCardItems.size();
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
            convertView = layoutInflater.inflate(R.layout.layout_user_card_list_listitem, null);

            viewHolder = new ViewHolder();
            viewHolder.cardCodeTV = ((TextView) convertView.findViewById((R.id.card_code)));
            viewHolder.cardImg = (ImageView) convertView.findViewById(R.id.card_pic);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        UserCardItem userCardItem = userCardItems.get(position);

        viewHolder.cardImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.item1));

        viewHolder.cardCodeTV.setText(userCardItem.getCode());

        return convertView;
    }




    static class ViewHolder {
        TextView cardCodeTV;
        ImageView cardImg;
    }

}

















