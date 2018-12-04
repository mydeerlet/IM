package com.mydeerlet.im.service;

import com.mydeerlet.im.bean.UpdateModel;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LoginService {

    @GET("weather_mini")
        //  此处回调返回的可为任意类型Call<T>，再也不用自己去解析json数据啦！！！
    Single<UpdateModel> getMessage(@Query("city") String city);
}
