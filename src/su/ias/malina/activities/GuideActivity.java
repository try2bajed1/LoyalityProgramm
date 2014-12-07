package su.ias.malina.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import su.ias.malina.R;
import su.ias.malina.adapters.Guide_Pager_Adapter;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 02.10.2014
 * Time: 16:53
 */
public class GuideActivity extends Activity {


    private int counter=0;
    private ShowcaseView showcaseView;
    private ViewPager viewPager;

    private ImageView addIcon;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_guide);
        addIcon = (ImageView) findViewById(R.id.add_icon);
        addIcon.setVisibility(View.GONE);

        viewPager = (ViewPager) findViewById(R.id.guide_view_pager);
        viewPager.setAdapter(new Guide_Pager_Adapter(this));
        viewPager.setEnabled(false);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                Log.i("@", "select " + i);
                if (i == 0) {
                    addIcon.setVisibility(View.GONE);
                    initGuideForUserSelection();
                }

                if (i == 1) {
                    addIcon.setVisibility(View.GONE);
                    initGuideForSelectedMode();
                }

                if (i == 2) {
                    addIcon.setVisibility(View.VISIBLE);
                    initGuideForUserCards();
                }


            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

//        viewPager.setCurrentItem(0);

        initGuideForUserSelection();


    }


    public void initGuideForUserSelection() {

        counter = 0;

        View.OnClickListener userModeGuideClickHandler = new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                switch (counter) {
                    case 0:
                        showcaseView.setContentTitle("Найдите партнеров программы на карте");
                        showcaseView.setShowcase(new ViewTarget(findViewById(R.id.button_places)), true);
                        counter++;
                        break;
                    case 1:
                        showcaseView.setContentTitle("Учавствуйте в акциях");
                        showcaseView.setShowcase(new ViewTarget(findViewById(R.id.button_promo)), true);
                        counter++;
                        break;
                    case 2:
                        showcaseView.setContentTitle("Карты МАЛИНА всегда под рукой");
                        showcaseView.setShowcase(new ViewTarget(findViewById(R.id.button_cards)), true);
                        counter++;
                        break;
                    case 3:
                        showcaseView.hide();
//                        prefs.edit().putBoolean(FLAG_MAP_HELP_WAS_SHOWN, true).commit();
                        viewPager.setCurrentItem(1,true);
                        break;
                }

//                counter++;
            }
        };




        showcaseView = new ShowcaseView.Builder(this)
                .setTarget(new ViewTarget(findViewById(R.id.guide_view_pager)))
                .setOnClickListener(userModeGuideClickHandler)
                .setStyle(R.style.CustomShowcaseTheme)
                .build();

        showcaseView.setContentTitle("Выбирайте способ передвижения по городу");

    }




    public void initGuideForSelectedMode() {

        counter = 0;

        View.OnClickListener selectedModeClickHandler = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View currPageView = viewPager.findViewWithTag(viewPager.getCurrentItem());

                switch (counter) {
                    case 0:
                        Log.i("@","conter "+ counter);
                        showcaseView.setContentTitle("Способ передвижения по городу ");
                        View tabZoomTo = currPageView.findViewById(R.id.tab_1);
                        showcaseView.setShowcase(new ViewTarget(tabZoomTo), true);
                        counter++;
                        break;

                    case 1:
                        Log.i("@","conter "+ counter);
                        showcaseView.setContentTitle("Способ передвижения по городу");
                        View tab2 = currPageView.findViewById(R.id.tab_2);
                        showcaseView.setShowcase(new ViewTarget(tab2), true);
                        counter++;
                        break;

                    case 2:
                        Log.i("@","conter "+ counter);
                        showcaseView.hide();
//                        prefs.edit().putBoolean(FLAG_MAP_HELP_WAS_SHOWN, true).commit();
                        viewPager.setCurrentItem(2,true);

                        break;
                }


            }
        };


        showcaseView = new ShowcaseView.Builder(this)
                .setOnClickListener(selectedModeClickHandler)
                .setStyle(R.style.CustomShowcaseTheme)
                .build();

        showcaseView.setContentTitle("Здесь Вы найдете Ваш счет и другую полезную информацию");
//        showcaseView.setButtonText(getString(R.string.next));
        showcaseView.setBlocksTouches(true);

        showcaseView.setShowcase(new ViewTarget(findViewById(R.id.menu_btn)), true);

    }




    public void initGuideForUserCards() {

        counter = 0;

        View.OnClickListener userCardsGuideClickHandler = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (counter) {
                    case 0:
                        showcaseView.setContentTitle("Подключите номер билайн для начисления баллов");
                        View btnZoomTo = viewPager.findViewWithTag(viewPager.getCurrentItem()).findViewById(R.id.beeline_btn);
                        showcaseView.setShowcase(new ViewTarget(btnZoomTo), true);
                        counter++;
                        break;
                    case 1:
                        showcaseView.setContentTitle("Нажмите на карту для ее отображения на весь экран");
                        showcaseView.setShowcase(new ViewTarget(findViewById(R.id.card_pic)), true);
                        counter++;
                        break;
                    case 2:
                        showcaseView.hide();
                        viewPager.setCurrentItem(0);
                        break;
                }


            }
        };


        showcaseView = new ShowcaseView.Builder(this)
                .setOnClickListener(userCardsGuideClickHandler)
                .setStyle(R.style.CustomShowcaseTheme)
                .build();

        showcaseView.setContentTitle("Регистрируйте новые или добавляйте существующие карты");
        showcaseView.setBlocksTouches(true);
        showcaseView.setShowcase(new ViewTarget(findViewById(R.id.add_icon)), true);

    }
}