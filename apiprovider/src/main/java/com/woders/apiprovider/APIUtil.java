package com.woders.apiprovider;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.woders.apiprovider.utils.JsonUtil;
import com.woders.apiprovider.utils.LogUtil;
import com.woders.apiprovider.utils.OkHttpStack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * title:
 * desc: 网络处理函数
 * Created by jiangguangming on 2015/10/30.
 */
public class APIUtil {

    public static RequestQueue queues;

    private static Context mContext;

    private static APIUtil mInstance;

    public static APIUtil getInstance() {
        if (mInstance == null) {
            synchronized (APIUtil.class) {
                if (mInstance == null) {
                    mInstance = new APIUtil();
                }
            }
        }
        return mInstance;
    }

    public void initContext(Context context) {
        mContext = context;
        if (queues == null) {
            queues = Volley.newRequestQueue(mContext, new OkHttpStack());
        }
    }

    /**
     *  get方法
     * @param url
     * @param params
     * @param type
     * @param callback
     * @param clazz
     * @param <T>
     */
    public static <T> void loadJsonGet(String url, Map<String, Object> params, final int type, final APICallBack callback, final Class<T> clazz) {
        //构建url
        url = getConsParams(params, url);
        LogUtil.d("url:" + url);

        //请求成功的回调
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtil.d("response:" + response.toString());
                LogUtil.d("---end----");
                try {
                    int code = response.getInt("code");
                    String msg = response.getString("msg");
                    String result = response.getString("result");
                    resultHandler(code, msg, result, callback, type, clazz);
                } catch (Exception e) {
                    LogUtil.e(e.getLocalizedMessage());
                }
            }
        };

        //请求失败的回调
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.e("error response:" + error.getLocalizedMessage());

                try {
                    int status = error.networkResponse.statusCode;
                    LogUtil.e("statuscode: " + status);

                    switch (status) {
                        case 404:
                            //TODO:处理404错误
                            break;
                        case 500:
                            //TODO:处理500错误
                            break;
                    }
                }catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,"服务器无法连接", Toast.LENGTH_SHORT).show();
                }

            }
        };

        LogUtil.d("---start----");
        LogUtil.d("method:GET");

        //设置请求
        Request<JSONObject> request = new JsonObjectRequest(url, null, responseListener, errorListener){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                for(String s : response.headers.keySet()){
                    if(s.contains("set-cookie")){
                        //拿到session
                        String sessionID = null;
                        String cookies = response.headers.get(s);
                        if (cookies != null) {
                            sessionID = cookies.substring(0, cookies.indexOf(";"));// 获取sessionID
                        }
                        SharedPreferences sp = mContext.getSharedPreferences("session", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("sessionid", sessionID);
                        editor.commit();
                        //Log.i("info",cookies);
                        break;
                    }
                }
                return super.parseNetworkResponse(response);
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                SharedPreferences sp = mContext.getSharedPreferences("session", Activity.MODE_PRIVATE);
                String sessionid = sp.getString("sessionid", null);
                map.put("cookie", sessionid);
                return map;
            }
        };
        //为请求添加tag，便于取消
        request.setTag(url);
        //将请求添加到请求队列
        queues.add(request);
    }

    /**
     *  post方法
     * @param url
     * @param params
     * @param type
     * @param callback
     * @param clazz
     * @param <T>
     */
    public static <T> void loadJsonPost(String url, Map<String, Object> params, final int type, final APICallBack callback, final Class<T> clazz) {

        LogUtil.d("url:" + url);

        //构建参数
        JSONObject map = new JSONObject(params);
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                LogUtil.d("params:" + entry.getKey() + ":" + entry.getValue());
            }
        }

        //请求成功的回调
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtil.d("response:" + response.toString());
                LogUtil.d("---end----");
                try {
                    int code = response.getInt("code");
                    String msg = response.getString("msg");
                    String result = response.getString("result");
                    resultHandler(code, msg, result, callback, type, clazz);
                } catch (JSONException e) {
                    LogUtil.e(e.getLocalizedMessage());
                }
            }
        };

        //请求失败的回调
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.e("error response:" + error.getMessage());

                try {
                    int status = error.networkResponse.statusCode;
                    LogUtil.e("statuscode: " + status);

                    switch (status) {
                        case 404:
                            //TODO:处理404错误
                            break;
                        case 500:
                            //TODO:处理500错误
                            break;
                    }
                }catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,"服务器无法连接", Toast.LENGTH_SHORT).show();
                }
            }
        };

        LogUtil.d("---start----");
        LogUtil.d("method:POST");

        //设置请求
        Request<JSONObject> request = new JsonObjectRequest(Request.Method.POST, url, map, responseListener, errorListener){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                for(String s : response.headers.keySet()){
                    if(s.contains("set-cookie")){
                        //拿到session
                        String sessionID = null;
                        String cookies = response.headers.get(s);
                        if (cookies != null) {
                            sessionID = cookies.substring(0, cookies.indexOf(";"));// 获取sessionID
                        }
                        SharedPreferences sp = mContext.getSharedPreferences("session", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("sessionid", sessionID);
                        editor.commit();
                        //Log.i("info",cookies);
                        break;
                    }
                }
                return super.parseNetworkResponse(response);
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                SharedPreferences sp = mContext.getSharedPreferences("session", Activity.MODE_PRIVATE);
                String sessionid = sp.getString("sessionid", null);
                map.put("cookie", sessionid);
                return map;
            }
        };

        //为请求添加tag，便于取消
        request.setTag(url);
        //将请求添加到请求队列
        queues.add(request);

    }


    /**
     *  put方法
     * @param url
     * @param params
     * @param type
     * @param callback
     * @param clazz
     * @param <T>
     */
    public static <T> void loadJsonPut(String url, Map<String, Object> params, final int type, final APICallBack callback, final Class<T> clazz) {

        LogUtil.d("url:" + url);

        //构建参数
        JSONObject map = new JSONObject(params);
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                LogUtil.d("params:" + entry.getKey() + ":" + entry.getValue());
            }
        }

        //请求成功的回调
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtil.d("response:" + response.toString());
                LogUtil.d("---end----");
                try {
                    int code = response.getInt("code");
                    String msg = response.getString("msg");
                    String result = response.getString("result");
                    resultHandler(code, msg, result, callback, type, clazz);
                } catch (JSONException e) {
                    LogUtil.e(e.getLocalizedMessage());
                }
            }
        };

        //请求失败的回调
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.e("error response:" + error.getMessage());
                try {
                    int status = error.networkResponse.statusCode;
                    LogUtil.e("statuscode: " + status);

                    switch (status) {
                        case 404:
                            //TODO:处理404错误
                            break;
                        case 500:
                            //TODO:处理500错误
                            break;
                    }
                }catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,"服务器无法连接", Toast.LENGTH_SHORT).show();
                }
            }
        };

        LogUtil.d("---start----");
        LogUtil.d("method:PUT");

        //设置请求
        Request<JSONObject> request = new JsonObjectRequest(Request.Method.PUT, url, map, responseListener, errorListener){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                for(String s : response.headers.keySet()){
                    if(s.contains("set-cookie")){
                        //拿到session
                        String sessionID = null;
                        String cookies = response.headers.get(s);
                        if (cookies != null) {
                            sessionID = cookies.substring(0, cookies.indexOf(";"));// 获取sessionID
                        }
                        SharedPreferences sp = mContext.getSharedPreferences("session", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("sessionid", sessionID);
                        editor.commit();
                        //Log.i("info",cookies);
                        break;
                    }
                }
                return super.parseNetworkResponse(response);
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                SharedPreferences sp = mContext.getSharedPreferences("session", Activity.MODE_PRIVATE);
                String sessionid = sp.getString("sessionid", null);
                map.put("cookie", sessionid);
                return map;
            }
        };
        //为请求添加tag，便于取消
        request.setTag(url);
        //将请求添加到请求队列
        queues.add(request);
    }


    /**
     *  delete方法
     * @param url
     * @param params
     * @param type
     * @param callback
     * @param clazz
     * @param <T>
     */
    public static <T> void loadJsonDelete(String url, Map<String, Object> params, final int type, final APICallBack callback, final Class<T> clazz) {

        LogUtil.d("url:" + url);

        //构建参数
        JSONObject map = new JSONObject();
        if (params != null) {
            map = new JSONObject(params);
        }
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                LogUtil.d("params:" + entry.getKey() + ":" + entry.getValue());
            }
        }

        //请求成功的回调
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtil.d("response:" + response.toString());
                LogUtil.d("---end----");
                try {
                    int code = response.getInt("code");
                    String msg = response.getString("msg");
                    String result = response.getString("result");
                    resultHandler(code, msg, result, callback, type, clazz);
                } catch (JSONException e) {
                    LogUtil.e(e.getLocalizedMessage());
                }
            }
        };

        //请求失败的回调
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.e("error response:" + error.getMessage());
                try {
                    int status = error.networkResponse.statusCode;
                    LogUtil.e("statuscode: " + status);

                    switch (status) {
                        case 404:
                            //TODO:处理404错误
                            break;
                        case 500:
                            //TODO:处理500错误
                            break;
                    }
                }catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,"服务器无法连接", Toast.LENGTH_SHORT).show();
                }
            }
        };

        LogUtil.d("---start----");
        LogUtil.d("method:Delete");

        //设置请求
        Request<JSONObject> request = new JsonObjectRequest(Request.Method.DELETE, url, map, responseListener, errorListener){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                for(String s : response.headers.keySet()){
                    if(s.contains("set-cookie")){
                        //拿到session
                        String sessionID = null;
                        String cookies = response.headers.get(s);
                        if (cookies != null) {
                            sessionID = cookies.substring(0, cookies.indexOf(";"));// 获取sessionID
                        }
                        SharedPreferences sp = mContext.getSharedPreferences("session", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("sessionid", sessionID);
                        editor.commit();
                        //Log.i("info",cookies);
                        break;
                    }
                }
                return super.parseNetworkResponse(response);
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                SharedPreferences sp = mContext.getSharedPreferences("session", Activity.MODE_PRIVATE);
                String sessionid = sp.getString("sessionid", null);
                map.put("cookie", sessionid);
                return map;
            }
        };
        //为请求添加tag，便于取消
        request.setTag(url);
        //将请求添加到请求队列
        queues.add(request);
    }

    /**
     *  请求结果转换处理
     * @param code 状态码
     * @param msg 信息
     * @param result 结果集
     * @param callback 结果回调
     * @param type 结果数据类型
     * @param clazz 对应实体类
     * @param <T>
     */
    private static <T> void resultHandler(int code, String msg, String result, APICallBack callback, int type, Class<T> clazz) {
        RestMsg rs = new RestMsg();
        rs.setCode(code);
        rs.setMessage(msg);
        if (code == 1) {
            switch (type) {
                case APIType.LIST:
                    rs.setResult(JsonUtil.jsonToList(result, clazz));
                    callback.onSuccess(rs);
                    break;
                case APIType.OBJECT:
                    rs.setResult(JsonUtil.jsonToObject(result, clazz));
                    callback.onSuccess(rs);
                    break;
                case APIType.PAGE:
                    rs.setResult(JsonUtil.jsonToPage(result, clazz));
                    callback.onSuccess(rs);
                    break;
                case APIType.MAP:
                    rs.setResult(JsonUtil.jsonToMap(result));
                    callback.onSuccess(rs);
                    break;
                case APIType.NONE:
                    rs.setResult(null);
                    callback.onSuccess(rs);
                    break;
            }
        } else {
            rs.setResult(null);
            callback.onFailure(rs);
        }
    }

    /**
     *  GET方法构造URL参数
     * @param params
     * @param url
     * @return
     */
    private static String getConsParams(Map<String, Object> params, String url) {
        String urlParams = "?";
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                urlParams += entry.getKey() + "=" + entry.getValue() + "&";
                LogUtil.d("params:" + entry.getKey() + ":" + entry.getValue());
            }
            url += urlParams.substring(0, urlParams.length() - 1);
        }
        return url;
    }

}