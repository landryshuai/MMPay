package com.szleyi365.paymmtest;

import com.szleyi365.paymmtest.model.UnifiedOrderReq;
import com.szleyi365.paymmtest.model.UnifiedOrderResp;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by shuaijiman on 12/25/15.
 */
public interface MMPrepayService {
    @POST("/pay/unifiedorder")
    Call<UnifiedOrderResp> unifiedorder(@Body UnifiedOrderReq req);
}
