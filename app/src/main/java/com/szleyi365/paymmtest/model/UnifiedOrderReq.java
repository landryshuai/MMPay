package com.szleyi365.paymmtest.model;

/**
 * Created by shuaijiman on 12/25/15.
 */
public class UnifiedOrderReq {
    public String appid;
    public String mch_id;
    public String nonce_str;
    public String sign;
    public String body;
    public String detail;
    public String out_trade_no;
    public Integer total_fee;
    public String spbill_create_ip;
    public String notify_url;
    public String trade_type;
}
