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

public class LoginTask extends AsyncTask<String, Void, Boolean> {


    private String errorCode;
    private String errorMessage;

    private IListener listener;

    private String actionName;
    private String dataFromServer;

    private static final String OK = "ok";
    private static final String ERROR = "error";


    public LoginTask(IListener listener) {
        this.listener = listener;
    }



    @Override
    protected Boolean doInBackground(String... params) {

        boolean result = true;

        actionName = params[0];
        String jsonToPostStr = params[1];
        DefaultHttpClient client = new DefaultHttpClient();
        client.getParams().setBooleanParameter("http.protocol.expect-continue", false);


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

            //save cookies
            AppSingleton.cookieStore = client.getCookieStore();

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



    /*private void processLoginResponce() {
        AppSingleton.cookieStore = client.getCookieStore();
        AppSingleton.get().prefs.edit().putString(AppSingleton.SAVED_USER_LOGIN, login)
                .putString(AppSingleton.SAVED_USER_PASSWORD, password).commit();
    }*/




    @Override
    protected void onPostExecute(Boolean result) {

//        if(listener == null) return;


        if (result) {
            listener.responseCompleteHandler(actionName,dataFromServer);
        } else {
            Log.e("@", "error "+ dataFromServer);
            listener.responseErrorHandler(actionName, dataFromServer);
        }
    }



    @Override
    protected void onCancelled() {

    }


}
