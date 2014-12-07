package su.ias.malina.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import su.ias.malina.R;
import su.ias.malina.data.CategoryData;

import java.util.ArrayList;

/**
* Created with IntelliJ IDEA.
* User: n.senchurin
* Date: 28.08.2014
* Time: 10:44
*/
public class CategoriesAdapter extends BaseAdapter {


    private final LayoutInflater layoutInflater;
    private Context context;
    private ArrayList<CategoryData> dataArr;

    public CategoriesAdapter(Context context, ArrayList<CategoryData> dataArr) {
        this.context = context;
        this.dataArr = dataArr;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return dataArr.size();
    }

    @Override
    public Object getItem(int position) {
        return dataArr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_categories_filter_list_item, null);
        }


        CategoryData categoryData = dataArr.get(position);

        TextView categoryName = (TextView) convertView.findViewById(R.id.category_name);
        categoryName.setText(categoryData.name);
        int paddingLeft = categoryData.parentId == 0 ? 0 : 50;
        categoryName.setPadding(paddingLeft, 0, 0, 0);

        ImageView selectionFlagIV = (ImageView) convertView.findViewById(R.id.selection_flag_img);
        selectionFlagIV.setImageDrawable(categoryData.selected ? context.getResources().getDrawable(R.drawable.checkbox_activ)
                                                               : context.getResources().getDrawable(R.drawable.checkbox));


        return convertView;
    }
}
