package su.ias.malina.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import su.ias.malina.R;
import su.ias.malina.activities.fragments.UserCardsFragment;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 05.09.2014
 * Time: 17:16
 */
public class BarcodeActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_selected_barcode);


        ImageView barcodeImg = (ImageView) findViewById(R.id.barcode_img);

        String url = getIntent().getStringExtra(UserCardsFragment.BARCODE);
        ImageLoader.getInstance().displayImage(url, barcodeImg);
    }

}
