package com.mydeerlet.im.service;

import com.mydeerlet.im.bean.HttpResult;
import com.mydeerlet.im.bean.User;

import java.util.Map;

import io.reactivex.Single;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginService {

    @FormUrlEncoded
    @POST("login.do")
    Single<HttpResult<User>> login(@FieldMap Map<String,String> map);

    @FormUrlEncoded
    @POST("userList.do")
    Single<HttpResult<User>> getList(@FieldMap Map<String,String> map);
}
