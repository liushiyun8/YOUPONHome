package com.youpon.home1.comm.tools;

import android.content.Intent;

import com.google.gson.Gson;
import com.youpon.home1.comm.App;
import com.youpon.home1.http.HttpManage;
import com.youpon.home1.ui.user.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;

/**
 * Created by liuyun on 2017/5/24.
 */
public abstract class MyCallback implements Callback.CommonCallback<String> {

    public abstract void onSuc(String result);

    public abstract void onFail(int code,String msg);

    ;

    @Override
    public void onSuccess(String result) {
        onSuc(result);
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        ex.printStackTrace();
        HttpException httpException= (HttpException) ex;
        String result = httpException.getResult();
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject error = jsonObject.optJSONObject("error");
            int code = error.optInt("code");
            String msg = error.optString("msg");
            if(code==4031021){
                XlinkUtils.shortTips("Token过期，请重新登录");
                App.ctx.startActivity(new Intent(App.ctx, MainActivity.class));
            }
            onFail(code,"".equals(getMsg(code))?msg:getMsg(code));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }
    
    public String getMsg(int errCode){
        String errInfo="";
        switch (errCode) {
            case 4001001:errInfo = "请求数据字段验证不通过";break;
            case 4001002:errInfo = "请求数据必须字段不可为空";break;
            case 4001003:errInfo = "手机验证码不存在";break;
            case 4001004:errInfo = "手机验证码错误";break;
            case 4001005:errInfo = "注册的手机号已存在";break;
            case 4001006:errInfo = "注册的邮箱已存在";break;
            case 4001007:errInfo = "密码错误";break;
            case 4001008:errInfo = "帐号不合法";break;
            case 4001009:errInfo = "企业成员状态不合法";break;
            case 4001010:errInfo = "刷新token不合法";break;
            case 4001011:errInfo = "未知成员角色类型";break;
            case 4001012:errInfo = "只有管理员才能邀请";break;
            case 4001013:errInfo = "不可修改其他成员信息";break;
            case 4001014:errInfo = "不能删除本人";break;
            case 4001015:errInfo = "未知的产品连接类型";break;
            case 4001016:errInfo = "已发布的产品不可删除";break;
            case 4001017:errInfo = "固件版本已存在";break;
            case 4001018:errInfo = "数据端点未知数据类型";break;
            case 4001019:errInfo = "数据端点索引已存在";break;
            case 4001020:errInfo = "已发布的数据端点不可删除";break;
            case 4001021:errInfo = "该产品下设备MAC地址已存在";break;
            case 4001022:errInfo = "不能删除已激活的设备";break;
            case 4001023:errInfo = "扩展属性Key为预留字段";break;
            case 4001024:errInfo = "设备扩展属性超过上限";break;
            case 4001025:errInfo = "新增已存在的扩展属性";break;
            case 4001026:errInfo = "更新不存在的扩展属性";break;
            case 4001027:errInfo = "属性字段名不合法";break;
            case 4001028:errInfo = "邮件验证码不存在";break;
            case 4001029:errInfo = "邮件验证码错误";break;
            case 4001030:errInfo = "用户状态不合法";break;
            case 4001031:errInfo = "用户手机尚未认证";break;
            case 4001032:errInfo = "用户邮箱尚未认证";break;
            case 4001033:errInfo = "用户已经订阅设备";break;
            case 4001034:errInfo = "用户没有订阅该设备";break;
            case 4001035:errInfo = "自动升级任务名称已存在";break;
            case 4001036:errInfo = "升级任务状态未知";break;
            case 4001037:errInfo = "已有相同的起始版本升级任务";break;
            case 4001038:errInfo = "设备激活失败";break;
            case 4001039:errInfo = "设备认证失败";break;
            case 4001041:errInfo = "订阅设备认证码错误";break;
            case 4001042:errInfo = "授权名称已存在";break;
            case 4001043:errInfo = "该告警规则名称已存在";break;
            case 4001045:errInfo = "数据变名称已存在";break;
            case 4001046:errInfo = "产品固件文件超过大小限制";break;
            case 4001047:errInfo = "APN密钥文件超过大小限制";break;
            case 4001048:errInfo = "APP的APN功能未启用";break;
            case 4001049:errInfo = "产品未允许用户注册设备";break;
            case 4001050:errInfo = "该类型的邮件模板已存在";break;
            case 4001051:errInfo = "邮件模板正文内容参数缺失";break;
            case 4001053:errInfo = "设备版本是最新的";break;
            case 4001054:errInfo = "设备离线，不能更新";break;
            case 4031001:errInfo = "禁止访问";break;
            case 4031002:errInfo = "禁止访问，需要Access-Token";break;
            case 4031003:errInfo = "无效的Access-Token";break;
            case 4031004:errInfo = "需要企业的调用权限";break;
            case 4031005:errInfo = "需要企业管理员权限";break;
            case 4031006:errInfo = "需要数据操作权限";break;
            case 4031007:errInfo = "禁止访问私有数据";break;
            case 4031008:errInfo = "分享已经被取消";break;
            case 4031009:errInfo = "分享已经接受";break;
            case 4031010:errInfo = "用户没有订阅设备，不能执行操作";break;
            case 4041001:errInfo = "URL找不到";break;
            case 4041002:errInfo = "企业成员帐号不存在";break;
            case 4041003:errInfo = "企业成员不存在";break;
            case 4041004:errInfo = "激活的成员邮箱不存在";break;
            case 4041005:errInfo = "产品信息不存在";break;
            case 4041006:errInfo = "产品固件不存在";break;
            case 4041007:errInfo = "数据端点不存在";break;
            case 4041008:errInfo = "设备不存在";break;
            case 4041009:errInfo = "设备扩展属性不存在";break;
            case 4041010:errInfo = "企业不存在";break;
            case 4041011:errInfo = "用户不存在";break;
            case 4041012:errInfo = "用户扩展属性不存在";break;
            case 4041013:errInfo = "升级任务不存在";break;
            case 4041014:errInfo = "第三方身份授权不存在";break;
            case 4041015:errInfo = "告警规则不存在";break;
            case 4041016:errInfo = "数据表不存在";break;
            case 4041017:errInfo = "数据不存在";break;
            case 4041018:errInfo = "分享资源不存在";break;
            case 4041019:errInfo = "企业邮箱不存在";break;
            case 4041020:errInfo = "APP不存在";break;
            case 4041021:errInfo = "产品转发规则不存在";break;
            case 4041022:errInfo = "邮件模板不存在";break;
            case 5031001:errInfo = "服务端发生异常";break;
        }
        return errInfo;
    }
}
