package com.ken24k.android.mvpdemo.common.retrofit;

import com.alibaba.fastjson.JSONObject;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * retrofit接口
 * Created by wangming on 2020-05-28
 */

public interface RetrofitService {

    /**
     * 更新校验
     */
    @POST("appVersion/getVersion")
    Observable<BaseResponse<JSONObject>> checkUpdate(@Body RequestBody param);

    @Streaming
    @GET
    /**
     * 文件下载
     */
    Observable<ResponseBody> downloadFile(@Url String fileUrl);

}