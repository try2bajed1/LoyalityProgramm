package su.ias.malina.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import su.ias.malina.R;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 08.10.2014
 * Time: 12:33
 */
public class AboutActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        findViewById(R.id.logo_ias).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.be-interactive.ru/")));
            }
        });
    }
}