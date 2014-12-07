package su.ias.malina.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import su.ias.malina.R;

public class Guide_Pager_Adapter extends PagerAdapter {

    private Context context;
    private LayoutInflater inflater;



    public Guide_Pager_Adapter(Context context) {
        this.context = context;
    }



    @Override
    public int getCount() {
        return 3;
    }



    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = null;

        if (position == 0) {
            itemView = inflater.inflate(R.layout.guide_page_1, container, false);
            itemView.setTag(position);
        }

        if (position == 1) {
            itemView = inflater.inflate(R.layout.guide_page_2, container, false);
            itemView.setTag(position);
        }

        if (position == 2) {
            itemView = inflater.inflate(R.layout.guide_page_3, container, false);
            itemView.setTag(position);
        }

        ((ViewPager) container).addView(itemView);

        return itemView;
    }




    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        (container).removeView((View) object);
    }


}









