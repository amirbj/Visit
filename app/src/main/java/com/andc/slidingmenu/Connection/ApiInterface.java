package com.andc.slidingmenu.Connection;

import com.andc.slidingmenu.Modals.AccessToken;
import com.andc.slidingmenu.Modals.AndroidParameterNewFinalList;
import com.andc.slidingmenu.Modals.BaseInfoModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by SiaJam on 2/20/2017.
 */

public interface ApiInterface {



    @POST("api/AuthService/login")
    Call<AccessToken> Authenticate(@Body LoginObject login);


    @GET("api/BaseService/GetVisitParameters")
    Call<List<AndroidParameterNewFinalList>> getBsse( @Header("UserName") String Username, @Header("Token") String Token);

}
