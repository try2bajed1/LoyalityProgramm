package su.ias.malina.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import su.ias.malina.R;
import su.ias.malina.async.PostTask;
import su.ias.malina.interfaces.IListener;
import su.ias.malina.utils.AppApiUtils;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 24.09.2014
 * Time: 18:00
 */


public class TermsAndConditionsActivity extends ActionBarActivity implements IListener {

    private WebView webView;
    public static final String ACTION_RULES = "program_rules";



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_terms);

        getSupportActionBar().setTitle("Условия участия");
        getSupportActionBar().setIcon(R.drawable.action_bar_icon);
        webView = (WebView) findViewById(R.id.web_view);

        new PostTask(this).execute(ACTION_RULES, getJsonForPost());
    }



    private String getJsonForPost() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(AppApiUtils.ACTION, ACTION_RULES);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }




    @Override
    public void responseCompleteHandler(String actionName, String jsonFromApi) {

        Log.i("@", ""+ jsonFromApi);

        try {
            String html = new JSONObject(jsonFromApi).getString("rules");

            String mime = "text/html; charset=utf-8";
            String encoding = "UTF-8" ;
            Log.i("@", "" + html);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadData(html, mime, "UTF-8");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void responseErrorHandler(String actionName, String errorStr) {
        Toast.makeText(this, "Ошибка при передаче даннных", Toast.LENGTH_SHORT).show();
    }

}