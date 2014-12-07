package su.ias.malina.async;

import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import su.ias.malina.app.AppSingleton;
import su.ias.malina.interfaces.IListener;
import su.ias.malina.utils.AppApiUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 11.07.2014
 * Time: 12:36
 */

public class PostTask extends AsyncTask<String, Void, Boolean> {


    private String errorCode;
    private String errorMessage;
    private IListener listener;

    private String actionName;
    private String dataFromServer;

    private static final String OK = "ok";
    private static final String ERROR = "error";


    public PostTask(IListener listener) {
        this.listener = listener;
    }


    @Override
    protected Boolean doInBackground(String... params) {

        boolean result = true;

        actionName = params[0];
        String jsonToPostStr = params[1];
        DefaultHttpClient client = new DefaultHttpClient();
        client.getParams().setBooleanParameter("http.protocol.expect-continue", false);

        if (AppSingleton.cookieStore != null) {
            client.setCookieStore(AppSingleton.cookieStore);

            /*[

            Log.i("@","_______ "+ AppSingleton.cookieStore);

            [version: 0]
            [name: sessionid]
            [value: .eJwVkMmNRUEMAlOy3V7D8RrFD37eHEEISng2dZsFjN9S-Ao8Z81t1pG2JDInAYcqbxcx4HczVloK8UORNMq3PMYUk87Vc21y2F2BX-Opv0VjB2hKEx_nUO18kb8EmrAZOgkOyKdxsbOQ2b2Hc49zZYUp8W59OYoa1Z5tRicKtyy6Un2bUimbdC6E9w51R3sbZNYUEeeLvvrc7o-FyDUdzr1Djj_V8tR16oyh5ug1Fb8PCfMEqU8GWO6b3n0c-WBbiv9p1yvh9g7L7_tBI3zkD80bZFM:1XWMZi:13XU3-Qwy3o37UvvuNIRdr6eiCk]
            [domain: .malina.ru]
            [path: /]
            [expiry: Tue Oct 07 13:39:58 GMT+03:00 2014],

            [version: 0]
            [name: region]
            [value: MSK]
            [domain: .malina.ru]
            [path: /]
            [expiry: Wed Sep 23 13:40:01 GMT+03:00 2015],

            [version: 0]
            [name: cardholder]
            [value: 3649237]
            [domain: .malina.ru]
            [path: /]
            [expiry: Tue Oct 07 13:40:01 GMT+03:00 2014]  ]*/

        }


        HttpPost httppost = new HttpPost(AppApiUtils.SERVER_URL);

        try {

            httppost.setEntity(new StringEntity(jsonToPostStr, "UTF-8"));

            HttpResponse response = client.execute(httppost);

            BufferedReader ins = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder sb = new StringBuilder("");
            dataFromServer = "";
            while ((dataFromServer = ins.readLine()) != null) {
                sb.append(dataFromServer);
            }
            dataFromServer = sb.toString();

            String encodedStr = new String(dataFromServer.getBytes("UTF-8"), "UTF-8");

            JSONObject jsonObject = new JSONObject(encodedStr);
            String status = jsonObject.getString("status");
            result = status.equals(OK); // else ERROR

        } catch (UnsupportedEncodingException e) {
            result = false;
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            result = false;
            e.printStackTrace();
        } catch (IOException e) {
            result = false;
            e.printStackTrace();
        } catch (JSONException e) {
            result = false;
            e.printStackTrace();
        }

        return result;
    }





    @Override
    protected void onPostExecute(Boolean result) {

//        if(listener == null) return;

        if (result) {
            listener.responseCompleteHandler(actionName, dataFromServer);
        } else {
            Log.e("@", "error "+ dataFromServer);
            listener.responseErrorHandler(actionName, dataFromServer);
        }
    }


    @Override
    protected void onCancelled() {

    }


}
