package com.youpon.home1.http;

import android.util.Log;import io.xlink.wifi.sdk.util.MyLog;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.youpon.home1.comm.App;
import com.youpon.home1.comm.Comconst;
import com.youpon.home1.comm.Constant;
import com.youpon.home1.comm.tools.MyCallback;
import com.youpon.home1.manage.DeviceManage;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.http.body.RequestBody;
import org.xutils.x;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Created by MYFLY on 2016/1/12.
 */
public class HttpManage {

    private static HttpManage instance;

    public static String COMPANY_ID = "100fa6b0ae76c200";
//    private final String host = "http://api-test.xlink.cn:8887";//测试服务器地址
    private final String host = "http://api2.xlink.cn";
//    private final String host = "https://api2.xlink.cn:443";
//    private final String host="http://139.224.7.17";
    // url
    public final String registerUrl = host + "/v2/user_register";
    public final String loginUrl = host + "/v2/user_auth";
    public final String forgetUrl = host + "/v2/user/password/forgot";
    public final String RefreshUrl = host + "/v2/user/token/refresh";

    public final String findbackUrl=host+"/v2/user/password/foundback";
    //.管理员（用户）获取所有设备分享请求列表
    public final String shareListUrl = host + "/v2/share/device/list";
    public final String getUserInfoUrl = host + "/v2/user/{user_id}";
    public final String getPubUserInfoUrl = host + "/v2/user/{user_id}/open_info";
    //获取某个用户绑定的设备列表。
    public final String subscribeListUrl = host + "/v2/user/%d/subscribe/devices";
    // public final String subscribeListUrl = host + "/v2/user/%d/subscribe/devices?version=%d";
    //设备管理员分享设备给指定用户
    public final String shareDeviceUrl = host + "/v2/share/device";
    //用户拒绝设备分享
    public final String  denyShareUrl= host + "/v2/share/device/deny";
    //用户取消设备分享
    public final String cancelShareUrl = host + "/v2/share/device/cancel";
    //用户确认设备分享
    public final String acceptShareUrl = host + "/v2/share/device/accept";
    //获取设备信息
    public final String getDeviceUrl = host + "/v2/product/{product_id}/device/{device_id}";
    //订阅设备（待定）
    public final String subscribeUrl = host + "/v2/user/{user_id}/subscribe";
    //修改用户信息
    public final String modifyUserUrl = host + "/v2/user/{user_id}";
    //重置密码
    public final String resetPasswordUrl = host + "/v2/user/password/reset";
    //获取数据端点列表
    public final String getDatapointsUrl = host + "/v2/product/{product_id}/datapoints";
    //取消订阅设备
    public final String unsubscribeUrl = host + "/v2/user/{user_id}/unsubscribe";

    //.管理员或用户删除这条分享记录
    public final String deleteShareUrl = host + "/v2/share/device/delete/{invite_code}";

    public final String deleteUserUrl = host + "/v2/user/{user_id}";

    //检查固件版本
//    public final String checkUpdateUrl = host + "/v1/user/device/version";
    public final String checkUpdateUrl = "http://app.xlink.cn/v1/user/device/version";
    //固件升级
//    public final String upgradeUrl = host + "/v1/user/device/version";
    public final String upgradeUrl = "http://app.xlink.cn/v1/user/device/upgrade";

    public final String appUrl = host+"/v2/plugin/app_android_apk/{app_id}/latest?corp_id={corp_id}";

    public final String uploadImageUrl=host+"/v2/user/avatar/upload?avatarType=";

    private final String getCodeUrl=host+"/v2/user_register/verifycode";

    private final String userSetUrl=host+"/v2/user/{user_id}/message/setting";

    private final String firmwareURL=host+"/v2/upgrade/device";

    private final String verifyCodeURL=host+"/v2/user/verifycode/verify";

    private final String updateDeviceURL=host+"/v2/product/{product_id}/device/{device_id}";

    private String addDeviceURL=host+"/v2/product/{product_id}/device";

    private String newUpdateURL=host+"/v2/upgrade/device/newest_version";

    public static final int TYPE_SINGLE=0;

    public static final int TYPE_MORE=1;

    public static final String SUBTABLE="subdevice";
    public static final String SENSORTABLE="Dbsensor";
    public static final String SCENETABLE="Scenebean";
    public static final String PANELTABLE="panel";
    public static final String MAINTABLE="MainBean";
    public static final String ROOMTABLE="Roombean";



    /**
     * code : 5031001
     * msg : service unavailable
     */


    public static HttpManage getInstance() {
        if (instance == null) {
            instance = new HttpManage();
        }
        return instance;
    }

    /**
     * 全局的http代理
     */
    private static AsyncHttpClient client = new AsyncHttpClient();

    static {
        // 设置网络超时时间
        client.setTimeout(5000);
        client.setConnectTimeout(3000);
    }


    /**
     * http 邮箱注册接口
     *
     * @param mail 用户 邮箱
     * @param name 昵称（别名，仅供后台管理平台观看，对用户来说记住uid和pwd就行）
     * @param pwd  密码
     */
    public void registerUserByMail(String mail, String name, String pwd, final ResultCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", mail);
        params.put("nickname", name);
        params.put("corp_id", COMPANY_ID);
        params.put("password", pwd);
        params.put("source", "2");
        post(registerUrl, params, callback);
    }

    public void registerUserByPhone(String phone, String name,String code, String pwd, final ResultCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("corp_id",COMPANY_ID);
        params.put("phone",phone);
        params.put("nickname", name);
        params.put("password", pwd);
        params.put("verifycode",code);
        params.put("source", "2");
        post(registerUrl, params, callback);
    }

    public void getCode(String phone,String zone,String captcha,final ResultCallback callback){
        Map<String, String> params = new HashMap<String, String>();
        params.put("corp_id",COMPANY_ID);
        params.put("phone",phone);
        if(zone!=null||zone!=""){
            params.put("phone_zone",zone);
        }
        if(captcha!=null||captcha!=""){
            params.put("captcha",captcha);
        }
        post(getCodeUrl, params, callback);
    }

    /**
     * http 邮箱登录接口
     *
     * @param mail 用户 邮箱
     * @param pwd  密码
     */

    public void login(String mail, String pwd, final ResultCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", mail);
        params.put("corp_id", COMPANY_ID);
        params.put("password", pwd);
        post(loginUrl, params, callback);
    }

    /**
     * http //.管理员（用户）获取所有设备分享请求列表
     */
    public void getShareList(final ResultCallback callback) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Access-Token", App.getApp().getAccessToken());
        get(shareListUrl, headers, callback);
    }

    /**
     * 11.获取用户详细信息
     */
    public void getUserInfo(int userId,Callback.CommonCallback<String> callback) {
        String url = getUserInfoUrl.replace("{user_id}", userId + "");
        RequestParams requestParams = new RequestParams(url);
        requestParams.setHeader("Access-Token", App.getApp().getAccessToken());
        x.http().get(requestParams,callback);
    }

    /**
     * 11.获取用户详细信息
     */
    public void getPubUserInfo(int userId,MyCallback callback) {
        String url = getPubUserInfoUrl.replace("{user_id}", userId + "");
        RequestParams requestParams = new RequestParams(url);
        requestParams.setHeader("Access-Token", App.getApp().getAccessToken());
        x.http().get(requestParams,callback);
    }


    public void getDatapoints(String pid, final ResultCallback callback) {
        String url = getDatapointsUrl.replace("{product_id}", pid);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Access-Token", App.getApp().getAccessToken());
        get(url, headers, callback);
    }

    /**
     * http 忘记密码
     *
     * @param num 用户 邮箱
     */
    public void forgetPasswd(String num,int type, final ResultCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("corp_id", COMPANY_ID);
        if(type==0){
            params.put("email",num);
        }else if(type==1){
            params.put("phone",num);
        }
        post(forgetUrl, params, callback);
    }

    public void findPasswd(String num,String verifycode,String new_password, final ResultCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("corp_id", COMPANY_ID);
        params.put("phone",num);
        params.put("new_password",new_password);
        params.put("verifycode",verifycode);
        post(findbackUrl, params, callback);
    }

    public void refreshToken(final MyCallback callback) {
        RequestParams enty=new RequestParams(RefreshUrl);
        enty.addHeader("Access-Token",App.getApp().getAccessToken());
        enty.setMethod(HttpMethod.POST);
        enty.setAsJsonContent(true);
        enty.addBodyParameter("refresh_token",App.getApp().getRefreshToken());
        x.http().post(enty,callback);
    }



    /**
     * 设备管理员分享设备给指定用户
     *
     */
    public void shareDevice(int deviceId,MyCallback callback) {
        RequestParams requestParams = new RequestParams(shareDeviceUrl);
        requestParams.addHeader("Access-Token", App.getApp().getAccessToken());
        requestParams.setMethod(HttpMethod.POST);
        requestParams.setAsJsonContent(true);
        requestParams.addBodyParameter("device_id", deviceId + "");
        requestParams.addBodyParameter("mode", "qrcode");
        requestParams.addBodyParameter("expire", "3600");
        x.http().post(requestParams,callback);
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("user", mail);
//        params.put("expire", "3600");
//        params.put("mode", "qrcode");
//        params.put("device_id", deviceId + "");
//        Map<String, String> headers = new HashMap<String, String>();
//        headers.put("Access-Token", App.getApp().getAccessToken());
//        post(shareDeviceUrl, headers, params, callback);
    }

    /**
     * 用户拒绝设备分享
     *
     * @param inviteCode 分享ID
     */
    public void denyShare(String inviteCode, final ResultCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("invite_code", inviteCode);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Access-Token", App.getApp().getAccessToken());
        post(denyShareUrl, headers, params, callback);
    }

    public void cancelShare(String inviteCode,MyCallback callback) {
        RequestParams params = new RequestParams(cancelShareUrl);
        params.setHeader("Access-Token", App.getApp().getAccessToken());
        params.addBodyParameter("invite_code", inviteCode);
        params.setAsJsonContent(true);
        params.setMethod(HttpMethod.POST);
        x.http().post(params,callback);
    }

    /**
     * 订阅设备
     *
     * @param userId userId
     */
    public void subscribe(String userId, String productId, int deviceId, final ResultCallback callback) {
        String url = subscribeUrl.replace("{user_id}", userId);
        Map<String, String> params = new HashMap<String, String>();
        params.put("product_id", productId);
        params.put("device_id", deviceId + "");
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Access-Token", App.getApp().getAccessToken());
        post(url, headers, params, callback);
    }
    /**
     * 取消订阅设备
     *
     */
    public void unsubscribe( int deviceId, final ResultCallback callback) {
        String url = unsubscribeUrl.replace("{user_id}",App.getApp().appid+"");
        Map<String, String> params = new HashMap<String, String>();
        params.put("device_id", deviceId + "");
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Access-Token", App.getApp().getAccessToken());
        post(url, headers, params, callback);
    }

    /**
     * .修改用户信息
     *
     * @param userId userId
     */
    public void modifyUser(int userId, String nickname, final ResultCallback callback) {
        String url = modifyUserUrl.replace("{user_id}", userId + "");
        Map<String, String> params = new HashMap<String, String>();
        params.put("nickname", nickname);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Access-Token", App.getApp().getAccessToken());
        put(url, headers, params, callback);
    }

    /**
     * .重置密码
     */
    public void resetPassword(String newPasswd, String oldPasswd, final ResultCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("old_password", oldPasswd);
        params.put("new_password", newPasswd);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Access-Token", App.getApp().getAccessToken());
        put(resetPasswordUrl, headers, params, callback);
    }

    /**
     * 用户确认设备分享
     *
     * @param inviteCode 分享ID
     */
    public void acceptShare(String inviteCode, final ResultCallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("invite_code", inviteCode);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Access-Token", App.getApp().getAccessToken());
        post(acceptShareUrl, headers, params, callback);
    }

    /**
     * 获取设备信息
     *
     * @param deviceId 设备ID
     */
    public void getDevice(String productIdd, int deviceId, final ResultCallback callback) {
        String url = getDeviceUrl.replace("{device_id}", deviceId + "");
        url = url.replace("{product_id}", productIdd);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Access-Token", App.getApp().getAccessToken());
        get(url, headers, callback);
    }

    /**
     * http //.获取某个用户绑定的设备列表。
     */
    public void getSubscribeList(int uid, int versionid,Callback.CommonCallback<String> callback) {
        String url = String.format(subscribeListUrl, uid);
        RequestParams enty =new RequestParams(url);
        enty.setHeader("Access-Token", App.getApp().getAccessToken());
        x.http().get(enty,callback);
    }

    /**
     * http //.获取某个用户绑定的设备列表。
     */
    public void deleteShare(String inviteCode, final ResultCallback callback) {
        String url = deleteShareUrl.replace("{invite_code}", inviteCode);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Access-Token", App.getApp().getAccessToken());
        delete(url, headers, callback);
    }

    public void deleteUser(String inviteCode, final MyCallback callback) {
        String url = deleteShareUrl.replace("{invite_code}", inviteCode);
        RequestParams enty =new RequestParams(url);
        enty.setHeader("Access-Token", App.getApp().getAccessToken());
        x.http().request(HttpMethod.DELETE, enty,callback);
    }

    public void getAppVersion(final ResultCallback callback) {
            String url=appUrl.replace("{app_id}", Comconst.APPID).replace("{corp_id}",COMPANY_ID);
            Map<String, String> headers = new HashMap<String, String>();
//            headers.put("Access-Token", App.getApp().getAccessToken());
            get(url,headers,callback);
    }

//    public void checkUpdate(String deviceId,final ResultCallback callback){
//        String url = checkUpdateUrl;
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("deviceid",deviceId);
//        Header[] headers = new Header[3];
//        String data = new Gson().toJson(map);
//        Map<String,String> header = new HashMap<String, String>();
//        // AccessID
//        header.put("X-AccessId", HttpAgent.UPDATE_ACCESS_ID);
//        header.put("X-ContentMD5", HttpAgent.MD5(data));
//        header.put("X-Sign", HttpAgent.MD5(HttpAgent.UPDATE_SECRET_KEY + HttpAgent.MD5(data)));
//        post(url, header, map, callback);
//    }
//
//    public void upgrade(String deviceId,final ResultCallback callback){
//        String url = upgradeUrl;
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("deviceid",deviceId);
//        Header[] headers = new Header[3];
//        String data = new Gson().toJson(map);
//        Map<String,String> header = new HashMap<String, String>();
//        // AccessID
//        header.put("X-AccessId", HttpAgent.UPDATE_ACCESS_ID);
//        header.put("X-ContentMD5", HttpAgent.MD5(data));
//        header.put("X-Sign", HttpAgent.MD5(HttpAgent.UPDATE_SECRET_KEY + HttpAgent.MD5(data)));
//        post(url, header, map, callback);
//    }
//=========================================================================================

    public void upLoadImg(String type, final byte[] data,MyCallback callback){
        String url=uploadImageUrl+type;
        Map<String, String> params = new HashMap<String, String>();
        RequestParams entity = new RequestParams(url);
        entity.setHeader("Access-Token", App.getApp().getAccessToken());
        entity.setRequestBody(new RequestBody() {
            @Override
            public long getContentLength() {
                return data.length;
            }

            @Override
            public void setContentType(String contentType) {

            }

            @Override
            public String getContentType() {
                return "application/json";
            }

            @Override
            public void writeTo(OutputStream out) throws IOException {
                        out.write(data);
                        out.flush();
                        out.close();
            }
        });
        x.http().post(entity,callback);
//        params.put("invite_code",data);
//        Map<String, String> headers = new HashMap<String, String>();
//        headers.put("Access-Token", App.getApp().getAccessToken());
//        post(url, headers,data, callback);
    }

    public void userSet(String inform,String setting,Callback.CommonCallback<String> callback){
        String url=userSetUrl.replace("{user_id}",String.valueOf(Comconst.CURRENTUSER));
        RequestParams entity = new RequestParams(url);
        entity.setHeader("Access-Token", App.getApp().getAccessToken());
        entity.setMethod(HttpMethod.POST);
        entity.setAsJsonContent(true);
        entity.addBodyParameter("inform",inform);
        entity.addBodyParameter("setting",setting);
//        entity.setBodyContent(jsonObject.toString());
        x.http().post(entity,callback);
    }

    public void upDateSub(String table,String objId,String content,Callback.CommonCallback<String> callback) {
        String url=host+"/v2/data/"+table+"/"+objId;
        RequestParams entity=new RequestParams(url);
        entity.setHeader("Access-Token", App.getApp().getAccessToken());
        entity.setBodyContent(content);
        x.http().request(HttpMethod.PUT,entity,callback);
    }

    public void addSub(int type,String table, String content, Callback.CommonCallback<String> callback) {
            String url;
            if(type==TYPE_SINGLE){
                url=host+"/v2/data/"+table;
            }else {
                url=host+"/v2/data_batch/"+table;
            }
        RequestParams entity=new RequestParams(url);
        entity.setHeader("Access-Token", App.getApp().getAccessToken());
        entity.setBodyContent(content);
        x.http().post(entity,callback);

    }

    public void deleSub(String objID,String table,Callback.CommonCallback<String> callback) {
        String url=host+"/v2/data/"+table+"/"+objID;
        RequestParams entity=new RequestParams(url);
        entity.setHeader("Access-Token", App.getApp().getAccessToken());
        x.http().request(HttpMethod.DELETE,entity,callback);
    }

    public void updateDevice(int deviceid,Map<String,String> params,MyCallback callback) {
        String url=updateDeviceURL.replace("{product_id}",Constant.PRODUCTID).replace("{device_id}",deviceid+"");
        RequestParams entity=new RequestParams(url);
        entity.setHeader("Access-Token", App.getApp().getAccessToken());
        entity.setAsJsonContent(true);
        entity.setMethod(HttpMethod.PUT);
        Set<Map.Entry<String, String>> entries = params.entrySet();
        for (Map.Entry<String, String> enty:entries){
            entity.addBodyParameter(enty.getKey(),enty.getValue());
        }
        x.http().request(HttpMethod.PUT,entity,callback);
    }

    public void querySub(String table,String query,Callback.CommonCallback<String> callback) {
        String url=host+"/v2/datas/"+table;
        RequestParams entity=new RequestParams(url);
        entity.setHeader("Access-Token", App.getApp().getAccessToken());
        entity.setBodyContent(query);
        x.http().post(entity,callback);
    }

    public void getDeviceUpdate(int device_id, MyCallback myCallback) {
        RequestParams entity=new RequestParams(newUpdateURL);
        entity.setHeader("Access-Token", App.getApp().getAccessToken());
        entity.setMethod(HttpMethod.POST);
        entity.setAsJsonContent(true);
        entity.addBodyParameter("product_id",Constant.PRODUCTID);
        entity.addBodyParameter("device_id",device_id+"");
        x.http().post(entity,myCallback);
    }

    public void getDeviceUpdateTask(int device_id, MyCallback myCallback) {
        RequestParams entity=new RequestParams(host+"/v2/upgrade/firmware/check/"+device_id);
        entity.setHeader("Access-Token", App.getApp().getAccessToken());
        entity.setMethod(HttpMethod.POST);
        entity.setAsJsonContent(true);
        entity.addBodyParameter("product_id",Constant.PRODUCTID);
//        "product_id":"产品ID",
//                "type":"升级任务类型",
//                "current_version":"设备当前版本",
//                "identify":"用来定位多MCU或多子设备的情况"
        entity.addBodyParameter("type","1");
        entity.addBodyParameter("current_version","15");
        entity.addBodyParameter("identify","0");
        x.http().post(entity,myCallback);
    }

    public void addDevice(String token,String mac,String name,Callback.CommonCallback<String> callback) {
        RequestParams entity=new RequestParams(addDeviceURL.replace("{product_id}",Constant.PRODUCTID));
        entity.setHeader("Access-Token",token);
        entity.setMethod(HttpMethod.POST);
        entity.setAsJsonContent(true);
        entity.addBodyParameter("mac",mac);
        entity.addBodyParameter("name",name);
        x.http().post(entity,callback);
    }

    public void getAuthkey(Callback.CommonCallback<String> callback) {
        RequestParams entity=new RequestParams(host+"/v2/accesskey_auth");
        entity.setMethod(HttpMethod.POST);
        entity.setAsJsonContent(true);
        entity.addBodyParameter("id","320fa6b33f30e600");
        entity.addBodyParameter("secret","6811eb286493895e94e7615fd2927446");
        x.http().post(entity,callback);
    }

    public void getFirmware(int device_id,Callback.CommonCallback<String> callback) {
        RequestParams entity=new RequestParams(firmwareURL);
        entity.setHeader("Access-Token", App.getApp().getAccessToken());
        entity.setMethod(HttpMethod.POST);
        entity.setAsJsonContent(true);
        entity.addBodyParameter("product_id","160fa6b1b8a903e9160fa6b1b8a93201");
        entity.addBodyParameter("device_id",device_id+"");
        x.http().post(entity,callback);
    }

    public void verifyCode(String phone,String code,Callback.CommonCallback<String> callback) {
        RequestParams entity=new RequestParams(verifyCodeURL);
        entity.setMethod(HttpMethod.POST);
        entity.setAsJsonContent(true);
        entity.addBodyParameter("corp_id",COMPANY_ID);
        entity.addBodyParameter("phone",phone);
        entity.addBodyParameter("verifycode",code);
        x.http().post(entity,callback);
    }



    private void post(String url, Map<String, String> params, ResultCallback callback) {
        // 请求entity
        StringEntity entity = params2StringEntity(params);
        client.post(App.getApp(), url, entity, "application/json", callback);
    }

    private void get(String url, Map<String, String> headers, ResultCallback callback) {
        Header[] headersdata = map2Header(headers);
        client.get(App.getApp(), url, headersdata, null, callback);
    }

    private void delete(String url, Map<String, String> headers, ResultCallback callback) {
        Header[] headersdata = map2Header(headers);
        client.delete(App.getApp(), url, headersdata, null, callback);
    }

    private void post(String url, Map<String, String> headers, Map<String, String> params, ResultCallback callback) {
        // 请求entity
        StringEntity entity = params2StringEntity(params);
        Header[] headersdata = map2Header(headers);
        client.post(App.getApp(), url, headersdata, entity, "application/json", callback);
    }

    private void put(String url, Map<String, String> headers, Map<String, String> params, ResultCallback callback) {
        // 请求entity
        StringEntity entity = params2StringEntity(params);
        Header[] headersdata = map2Header(headers);
        client.put(App.getApp(), url, headersdata, entity, "application/json", callback);
    }

    private StringEntity params2StringEntity(Map<String, String> params) {
        StringEntity entity = null;
        try {
            entity = new StringEntity(new Gson().toJson(params), "UTF-8");
        } catch (Exception e) {
        }
        return entity;
    }

    private Header[] map2Header(Map<String, String> headers) {
        if (headers == null) {
            return null;
        }
        Header[] headersdata = new Header[headers.size()];
        int i = 0;
        for (String key : headers.keySet()) {
            headersdata[i] = new XHeader(key, headers.get(key));
            i++;
        }
        return headersdata;
    }



    public static abstract class ResultCallback<T> extends TextHttpResponseHandler {
        Type mType;
        private Gson mGson;

        public ResultCallback() {
            mType = getSuperclassTypeParameter(getClass());
            mGson = new Gson();
        }

        @Override
        public void onFailure(int code, Header[] headers, String msg, Throwable throwable) {
            if (code > 0) {
                try {
                    ErrorEntity errorEntity = mGson.fromJson(msg, ErrorEntity.class);
                    onError(headers, errorEntity.error);
                } catch (Exception e) {
                    ErrorEntity errorEntity = new ErrorEntity();
                    errorEntity.error.setMsg(throwable.getMessage());
                    errorEntity.error.setCode(HttpConstant.PARAM_NETIO_ERROR);
                    onError(headers, errorEntity.error);
                }
            } else {
                ErrorEntity errorEntity = new ErrorEntity();
                errorEntity.error.setMsg(throwable.getMessage());
                errorEntity.error.setCode(HttpConstant.PARAM_NETIO_ERROR);
                onError(headers, errorEntity.error);
            }
        }

        @Override
        public void onSuccess(int code, Header[] headers, String msg) {
            if (mType == String.class) {
                onSuccess(code, (T) msg);
            } else {
                T o = mGson.fromJson(msg, mType);
                onSuccess(code, o);
            }
        }


        static Type getSuperclassTypeParameter(Class<?> subclass) {
            Type superclass = subclass.getGenericSuperclass();
            System.out.println(superclass);
            if (superclass instanceof Class) {
                System.out.println(superclass);
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        public abstract void onError(Header[] headers, Error error);

        public abstract void onSuccess(int code, T response);
    }


    public static class Error {
        private int code;
        private String msg;

        public void setCode(int code) {
            this.code = code;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }

    private static class ErrorEntity {
        public Error error;

        public ErrorEntity() {
            error = new Error();
        }
    }
}
