package com.szleyi365.paymmtest;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.OkHttpClient;
import com.szleyi365.paymmtest.model.UnifiedOrderReq;
import com.szleyi365.paymmtest.model.UnifiedOrderResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.SimpleXmlConverterFactory;

public class MainActivity extends AppCompatActivity {

    private IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        api = WXAPIFactory.createWXAPI(this, MMPayConstants.APP_ID);
        api.registerApp(MMPayConstants.APP_ID);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OkHttpClient client = new OkHttpClient();
                client.networkInterceptors().add(new StethoInterceptor());
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(MMPayConstants.UNIFIED_PAY_URL)
                        .addConverterFactory(SimpleXmlConverterFactory.create())
                        .client(client)
                        .build();

                MMPrepayService service = retrofit.create(MMPrepayService.class);
                Call<UnifiedOrderResp> call = service.unifiedorder(createTestReq());
                call.enqueue(new Callback<UnifiedOrderResp>() {
                    @Override
                    public void onResponse(Response<UnifiedOrderResp> response, Retrofit retrofit) {
                        if (response.isSuccess()) {
                            UnifiedOrderResp resp = response.body();
                            if ("SUCCESS".equals(resp.return_code)) {
                               if ("SUCCESS".equals(resp.result_code)) {
                                   String repayId = resp.prepay_id;
                                   requestPay(repayId, resp.nonce_str);
                               } else {
                                   Toast.makeText(MainActivity.this, "err_code_des:" + resp.err_code_des, Toast.LENGTH_LONG).show();
                               }
                            } else {
                                Toast.makeText(MainActivity.this, "return_msg:" + resp.return_msg, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "http code" + response.code(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            }
        });
    }
    private UnifiedOrderReq createTestReq() {
        UnifiedOrderReq req = new UnifiedOrderReq();
        req.appid = MMPayConstants.APP_ID;
        req.body = "test";
        req.detail = "测试1分钱";
        req.mch_id = MMPayConstants.MCH_ID;
        req.nonce_str = RandomStringGenerator.getRandomStringByLength(32);
        req.notify_url = "http://www.szleyi365.com/notifypay.html";
        req.out_trade_no = "20151225" + SystemClock.elapsedRealtime();
        req.spbill_create_ip = "";
        req.total_fee = 1;
        req.trade_type="APP";
        try {
            req.sign = Signature.getSign(req);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return  req;
    }
    private void requestPay(String repayId, String nonce) {
        PayReq req = new PayReq();
        req.appId = MMPayConstants.APP_ID;
        req.partnerId = MMPayConstants.MCH_ID;
        req.prepayId = repayId;
        req.nonceStr = nonce;
        /**
         * 标准北京时间，时区为东八区；如果商户的系统时间为非标准北京时间。
         * 参数值必须根据商户系统所在时区先换算成标准北京时间， 例如商户所在地为0时区的伦敦，
         * 当地时间为2014年11月11日0时0分0秒，换算成北京时间为2014年11月11日8时0分0秒。
         */
        req.timeStamp = Long.toString(SystemClock.elapsedRealtime()/1000);
        req.packageValue = "Sign=WXPay";

        try {
            req.sign = Signature.getSign(req);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //req.extData = "test"; // optional
        Toast.makeText(MainActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        api.sendReq(req);
        //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //        .setAction("Action", null).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
