package su.ias.malina.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 22.10.2014
 * Time: 14:40
 */
public class AppApiUtils {


    public static final String SERVER_URL = "http://***";



    public static final String ACTION = "action";
    public static final String ACTION_LOGIN = "login";
    public static final String ACTION_GET_ACCOUNT = "get_account";
    public static final String ACTION_GET_CATEGORIES = "get_partners_categories";
    public static final String ACTION_GET_PARTNERS = "get_partners";
    public static final String ACTION_GET_CONTACTS = "get_contacts";
    public static final String ACTION_ACQUIRE_CARD = "acquire_card";
    public static final String ACTION_SIGN_UP_MOBILE_CARD = "signup_mobile_card";
    public static final String ACTION_GET_ACTIVATION_CODE = "get_activation_code";
    public static final String ACTION_ADD_CARD = "add_card";
    public static final String ACTION_PARTICIPATE = "participate";
    public static final String ACTION_SIGN_UP = "signup";
    public static final String ACTION_SIGN_UP_BONUS = "signup_bonus";
    public static final String ACTION_GET_PARTNERS_POINTS = "get_partners_points";
    public static final String ACTION_GET_OFFERS = "get_offers";


    public static final String PARTNERS = "partners";
    public static final String ID = "id";
    public static final String LOGO_URL = "logo_url";
    public static final String NAME = "name";
    public static final String POINTS_RULE = "points_rule";
    public static final String TYPE = "type";
    public static final String FILTER_PARAMS = "filter_params";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String RADIUS = "radius";
    public static final String LOCATION = "location";

    public static final int MODE_BY_CAR = 0; // режим "на машине"
    public static final int MODE_BY_FOOT = 1; // режим "пешком"
    public static final String ACTION_REGISTER = "register_beeline_phone";
    public static final String ACTION_CHECK_BEELINE_CODE = "check_beeline_activation_code";
    public static final String CREATE_CARD = "create_card";


    public static String getJsonForSignUp(String phoneNumStr,String smsCode, String cardCodeStr, String pass, String name, String surname,String birth,String city) {

        JSONObject jsonReq = new JSONObject();

        try {
            jsonReq.put("action", ACTION_SIGN_UP);
            jsonReq.put("phone", phoneNumStr);
            jsonReq.put("activation_code", smsCode);
            jsonReq.put("card", cardCodeStr);
            jsonReq.put("password", pass);
            jsonReq.put("first_name", name);
            jsonReq.put("last_name", surname);
            jsonReq.put("birth_date", birth);
            jsonReq.put("city", city);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonReq.toString();
    }



    public static String getJsonForParticipate(String accNum, String discountId) {

        JSONObject jsonReq = new JSONObject();
        try {
            jsonReq.put(ACTION, ACTION_PARTICIPATE);
            jsonReq.put("account_number", accNum);
            jsonReq.put("action_id", discountId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonReq.toString();
    }




    public static String getJsonForActivationCode(String code, String phone) {
        JSONObject jsonReq = new JSONObject();
        try {
            jsonReq.put(ACTION, ACTION_GET_ACTIVATION_CODE);
            jsonReq.put("phone", phone);
            jsonReq.put("card", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonReq.toString();
    }




    public static String getJsonForActivationCode(String phoneStr) {

        JSONObject jsonReq = new JSONObject();
        try {
            jsonReq.put(ACTION, ACTION_GET_ACTIVATION_CODE);
            jsonReq.put("phone", phoneStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonReq.toString();
    }




    public static String getJsonForAddCard(String cardCode, String smsCode) {

        JSONObject jsonReq = new JSONObject();
        try {
            jsonReq.put(ACTION, ACTION_ADD_CARD);
            jsonReq.put("card", cardCode);
            jsonReq.put("activation_code", smsCode);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonReq.toString();
    }






    public static String getJsonForBillInfo() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ACTION, ACTION_GET_ACCOUNT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }







    public static String getJsonForSignUp(String phone, String code, String pass, String name, String surname, String birth, String city) {

        JSONObject jsonReq = new JSONObject();
        try {

            jsonReq.put(ACTION, ACTION_SIGN_UP_MOBILE_CARD);
            jsonReq.put("phone", phone);
            jsonReq.put("activation_code", code);
            jsonReq.put("password", pass);
            jsonReq.put("first_name", name);
            jsonReq.put("last_name", surname );
            jsonReq.put("birth_date", birth);
            jsonReq.put("city", city);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonReq.toString();
    }




    public static String getJsonForAcquireCard(String phoneNum) {

        JSONObject jsonReq = new JSONObject();
        try {
            jsonReq.put(ACTION, ACTION_ACQUIRE_CARD);
            jsonReq.put("phone", phoneNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonReq.toString();
    }



    public static String getCategoriesJson() {
        return String.format("{\"%s\" : \"%s\" }", ACTION, ACTION_GET_CATEGORIES);
    }



    public static String getLoginJson(String login, String pass) {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(ACTION, ACTION_LOGIN);
            jsonObject.put("login", login);
            jsonObject.put("password", pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }



    public static String getJsonForGettingUserData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ACTION, ACTION_GET_CONTACTS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }


    public static String getJsonForBonus() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ACTION, ACTION_SIGN_UP_BONUS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }



    public static String getPartnersPostHeaderJson() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ACTION, ACTION_GET_PARTNERS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }


    public static JSONObject _getMapPointsPostJson(int movingType, int radius, double longitude, double latitude) {

        JSONObject jsonToPost = new JSONObject();

        try {

            jsonToPost.put(AppApiUtils.ACTION, ACTION_GET_PARTNERS_POINTS);
            JSONObject filterParams = new JSONObject();
//          filterParams.put(PARTNER_TYPE, PARTNER_TYPE_OFFLINE);

            JSONObject locationJson = new JSONObject();
            locationJson.put(RADIUS, radius);
            locationJson.put(LONGITUDE, longitude);
            locationJson.put(LATITUDE, latitude);

            filterParams.put("points_type", movingType == MODE_BY_CAR ? "car" : "foot");
            filterParams.put(LOCATION, locationJson);

            jsonToPost.put(FILTER_PARAMS, filterParams);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonToPost;
    }



    public static String getJsonForBeeline(String... params) {
        JSONObject jsonObject = new JSONObject();
        try {

            if(params.length == 2){
                jsonObject.put(ACTION, ACTION_REGISTER);
                jsonObject.put("card", params[0]);
                jsonObject.put("phone", "7"+params[1]);
            } else if (params.length == 3) {
                        jsonObject.put(ACTION, ACTION_CHECK_BEELINE_CODE);
                        jsonObject.put("card", params[0]);
                        jsonObject.put("phone", "7"+params[1]);
                        jsonObject.put("activation_code", params[2]);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }


    public static String getJsonForCreateCard() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ACTION, CREATE_CARD);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
