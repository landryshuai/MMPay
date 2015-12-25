package com.szleyi365.paymmtest.model;

/**
 * Created by shuaijiman on 12/25/15.
 */
public class UnifiedOrderResp {

    public String return_code;
    public String return_msg;

    //以下字段在return_code为SUCCESS的时候有返回
    public String appid;
    public String mch_id;
    public String nonce_str;
    public String sign;
    public String result_code;
    public String err_code;
    public String err_code_des;
    //以下字段在return_code 和result_code都为SUCCESS的时候有返回
    public String trade_type;
    public String prepay_id;
}
