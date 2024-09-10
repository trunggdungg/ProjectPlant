package com.example.projectplant.retrofit;

import com.example.projectplant.model.ProductModel;
import com.example.projectplant.model.UserModel;


import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface ApiAppPlant {

    //POST DATA
    @POST("dangky.php")
    @FormUrlEncoded
    Observable<UserModel> dangky(
            @Field("fullname") String fullname,
            @Field("email") String email,
            @Field("password") String password,
            @Field("uid") String uid
    );

    @POST("dangnhap.php")
    @FormUrlEncoded
    Observable<UserModel> dangnhap(
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("gettree.php")
    Observable<ProductModel> getsp();

    @POST("updatetoken.php")
    @FormUrlEncoded
    Observable<UserModel> updatetoken(
            @Field("id") int id,
            @Field("token") String token
    );

}