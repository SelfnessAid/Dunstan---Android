package mobidev.com.dunstan.classes;

import android.content.Context;
import android.text.Editable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Heltgolf on 5/2/2017.
 */

public class APIManager {
    public interface APISuccessListener{
        void onFailure(String error);
        void onSuccess(Response response);
    }

    private static APIManager ourInstance = new APIManager();

    public static APIManager getInstance() {
        return ourInstance;
    }

    private Context context;

    private static final int KEY_REQUEST_REGISTER = 599001;

    private static final String URL_ADDNEWDOOR = "http://52.56.190.147/api/v1/account/sign_up";

    private OkHttpClient client;

    public APIManager() {
        client = new OkHttpClient.Builder()
                .connectTimeout(1000, TimeUnit.MINUTES)
                .readTimeout(1000, TimeUnit.MINUTES)
                .writeTimeout(1000, TimeUnit.MINUTES)
                .build();
    }

    public void addNewDoor(String name, String phone, String code, String password, String email, final APISuccessListener listener){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", email);
            jsonObject.put("phone_number", phone);
            jsonObject.put("keypad_code", code);
            jsonObject.put("password", password);
            jsonObject.put("door_name", name);
            this.requestParam(jsonObject, URL_ADDNEWDOOR, KEY_REQUEST_REGISTER, listener);
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    public void confirmCode(String phone, String activationCode, final APISuccessListener listener) {
//        try {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("activationCode", activationCode);
//            String url = URL_BASE + URL_ConfirmCode + phone;
//            this.putRequestAPICall(jsonObject, url, KEY_REQUEST_CONFIRM, listener);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void registerDeviceToken(String phone, String deviceToken, boolean flag, final APISuccessListener listener) {
//        try {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("deviceToken", deviceToken);
//            String url = URL_BASE;
//            if (flag) {
//                url += URL_Register_DeviceToken_WithTenant;
//            } else {
//                url += URL_Register_DeviceToekn_WithPrequalTenant;
//            }
//
//            url += phone;
//
//            if (flag) {
//                this.putRequestAPICall(jsonObject, url, KEY_REGISTER_DEVICETOKEN_WITHTENANT, listener);
//            } else {
//                this.putRequestAPICall(jsonObject, url, KEY_REGISTER_DEVICETOKEN_WITHPREQUALTENANT, listener);
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void getUserInfo(String phone, boolean flag, final APISuccessListener listener){
//        String url = URL_BASE;
//        url += (flag)?URL_Get_User_Information_WithTenant:URL_Get_User_Information_WithPrequalTenant;
//        url += phone;
//        this.getRequestAPICall(url, KEY_GET_USERINFO, listener);
//    }
//
//    public void setUserInfo(Boolean isUpdated, String phone, String deviceToken, String first, String last, String street, String city, String state, String zip, String apt, final APISuccessListener listener) {
//        String url = URL_BASE;
//        if (!isUpdated)
//            url += "prequal-tenants";
//        else
//            url += URL_Set_UserInfo_WithPrequalTenant + phone;
//        try {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("first", first);
//            jsonObject.put("last", last);
//            jsonObject.put("deviceToken", deviceToken);
//            jsonObject.put("street", street);
//            jsonObject.put("city", city);
//            jsonObject.put("state", state);
//            jsonObject.put("zip", zip);
//            jsonObject.put("apt", apt);
//            if (!isUpdated) {
//                jsonObject.put("id", phone);
//                requestParam(jsonObject, url, KEY_SET_USERINFO, listener);
//            } else {
//                putRequestAPICall(jsonObject, url, KEY_UPDATE_USERINFO, listener);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void getUserName(String phone, boolean flag, final APISuccessListener listener){
//        String url = URL_BASE;
//        url += (flag)?URL_Get_UserName_WithTenant:URL_Get_UserName_WithPrequalTenant;
//        url += phone;
//        this.getRequestAPICall(url, KEY_GET_USERNAME, listener);
//    }
//
//    public void setUserName(String phone, String first, String last, final APISuccessListener listener) {
//        try {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("id", phone);
//            jsonObject.put("first", first);
//            jsonObject.put("last", last);
//            String url = URL_BASE + URL_Set_UserName + phone;
//            this.putRequestAPICall(jsonObject, url, KEY_REQUEST_CONFIRM, listener);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    //PUT
//    public void putRequestAPICall(JSONObject params, String url, final int key, final APISuccessListener listener) {
//        MediaType JSON = MediaType.parse("application/json");
//        RequestBody body = RequestBody.create(JSON, params.toString());
//        Request request = new Request.Builder()
//                .url(url)
//                .put(body)
//                .addHeader("content-type", "application/json")
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                listener.onFailure(e.getLocalizedMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                switch (key) {
//                    case KEY_REQUEST_CONFIRM:
//                    case KEY_UPDATE_USERINFO:
//                    case KEY_REGISTER_DEVICETOKEN_WITHTENANT:
//                    case KEY_REGISTER_DEVICETOKEN_WITHPREQUALTENANT:
//                    {
//                        try {
//                            listener.onSuccess(response);
//                        } catch (Exception e){
//                            e.printStackTrace();
//                            listener.onFailure(e.getLocalizedMessage());
//                        }
//                    }
//                    break;
//                    default:
//                        break;
//                }
//            }
//        });
//    }

    //POST
    public void requestParam(JSONObject params, String url, final int key, final APISuccessListener listener){
        try {
            MediaType JSON = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(JSON, params.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("content-type", "application/json")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    listener.onFailure(e.getLocalizedMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    switch (key){
                        case KEY_REQUEST_REGISTER:
                        {
                            try {
                                listener.onSuccess(response);
                            }catch(Exception e){
                                e.printStackTrace();
                                listener.onFailure("API is not working properly.");
                            }
                        }
                        default:
                            break;
                    }
                }
            });

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    //GET
    public void getRequestAPICall(String url, final int key, final APISuccessListener listener) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFailure(e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                listener.onSuccess(response);
            }
        });
    }
}
