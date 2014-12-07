package su.ias.malina.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import su.ias.malina.interfaces.IFilterDialogListener;
import su.ias.malina.R;
import su.ias.malina.adapters.CategoriesAdapter;
import su.ias.malina.app.AppSingleton;
import su.ias.malina.data.CategoryData;

import java.util.ArrayList;

/**
* Created with IntelliJ IDEA.
* User: n.senchurin
* Date: 28.08.2014
* Time: 11:32
*/

public class CategoriesDialog extends Dialog {


    private ListView categoriesLV;
    private ArrayList<CategoryData> categoriesArr;

    private Button selectBtn;
    private boolean selectAllFlag = false;


    public CategoriesDialog(Activity activity, final IFilterDialogListener listener) {

        super(activity, R.style.Theme_Dialog_Translucent);

        setCancelable(true);
        setContentView(R.layout.layout_categories_filter_list);

        categoriesArr = AppSingleton.get().getDBAdapter().getSortedCategories();

        selectBtn = (Button) findViewById(R.id.select_all_btn);
        selectBtn.setText(selectAllFlag ? "Выделить все" : "Очистить");

        categoriesLV = (ListView) findViewById(R.id.categories_list);
        categoriesLV.setAdapter(new CategoriesAdapter(activity, categoriesArr));
        categoriesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CategoryData selectedCategory = categoriesArr.get(position);

                if (selectedCategory.parentId == 0) {
                    //если клик по родительскому, то его переключаем (вместе с дочерними)
                    boolean parentIsSelected = selectedCategory.selected;
                    for (CategoryData subCategory : categoriesArr) {
                        if (subCategory.parentId == selectedCategory.id) {
                            subCategory.selected = !parentIsSelected;
                        }
                    }
                    selectedCategory.selected = !parentIsSelected;
                } else {
                    selectedCategory.selected = !selectedCategory.selected;
                    int parentId = selectedCategory.parentId;
                    for (CategoryData cat : categoriesArr) {
                        // если кликаем по дочернему и родительский залочен,
                        // то ищем родительский и  включаем его
                        if(cat.id == parentId){
                            if(!cat.selected){
                                cat.selected = true;
                            }
                        }
                    }

                    //когда снимается выделение с последнего дочернего, снимаем выделение и с родительского
                    boolean allChildsAreDeselected = true;
                    for (CategoryData cat : categoriesArr) {
                        if (cat.parentId == parentId) {
                            if(cat.selected) allChildsAreDeselected = false;
                        }
                    }

                    for (CategoryData cat : categoriesArr) {
                        if (cat.id == parentId && allChildsAreDeselected) {
                            cat.selected = false;
                            break;
                        }
                    }
                }

                ((BaseAdapter)categoriesLV.getAdapter()).notifyDataSetChanged();
            }
        });




        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectAllFlag) {
                    for (CategoryData cat : categoriesArr) {
                        if (!cat.selected) {
                            cat.selected = true;
                        }
                    }
                    selectBtn.setText("Очистить");
                    selectAllFlag = false;
                } else {

                    for (CategoryData cat : categoriesArr) {
                        if (cat.selected) {
                            cat.selected = false;
                        }
                    }
                    selectBtn.setText("Выделить все");
                    selectAllFlag = true;
                }
                ((BaseAdapter)categoriesLV.getAdapter()).notifyDataSetChanged();
            }
        });



        findViewById(R.id.done_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveCategoriesSelectionToDB();
                listener.applyFilter();

                dismiss();
            }

        });
    }



    private void saveCategoriesSelectionToDB() {
        AppSingleton.get().getDBAdapter().saveFilteredCategories(categoriesArr);

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
