package su.ias.malina.components;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 03.10.2014
 * Time: 13:18
 */
public class NoSwipebleViewPager extends ViewPager {

    public NoSwipebleViewPager(Context context) {
        super(context);
    }

    public NoSwipebleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        // Never allow swiping to switch between pages
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return false;
    }
}
