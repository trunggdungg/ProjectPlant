package com.example.projectplant.retrofit;

import com.example.projectplant.model.ProductModel;
import com.example.projectplant.model.UserModel;


import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface ApiAppPlant {

    //GET DATA



    //POST DATA
    @POST("dangky.php")
    @FormUrlEncoded
    Observable<UserModel> dangky(
            @Field("fullname") String fullname,
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("dangnhap.php")
    @FormUrlEncoded
    Observable<UserModel> dangnhap(
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("gettree.php")
    Observable<ProductModel> getsp();


}