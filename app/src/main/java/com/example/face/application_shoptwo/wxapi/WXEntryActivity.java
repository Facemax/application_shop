package com.example.face.application_shoptwo.wxapi;

/**
 * Created by Face on 2016/11/23.
 */


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.example.face.application_shoptwo.R;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity  extends AppCompatActivity implements IWXAPIEventHandler {
    private final String APP_ID= "wxc676771dbb9f0bfd";
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, APP_ID, false);
        api.registerApp(APP_ID);
        api.handleIntent(getIntent(), this);
    }

    //微信发送消息给app，app接受并处理的回调函数
    @Override
    public void onReq(BaseReq req) {

    }

    //app发送消息给微信，微信返回的消息回调函数,根据不同的返回码来判断操作是否成功
    @Override
    public void onResp(BaseResp resp) {
        String result ;

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result ="分享成功";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result ="分享取消";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result ="分享拒绝";
                break;
            default:
                result ="无分享操作";
                break;
        }

        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        finish();
    }
}
