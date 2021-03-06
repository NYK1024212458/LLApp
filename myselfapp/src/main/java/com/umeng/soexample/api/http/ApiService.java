package com.umeng.soexample.api.http;

import com.example.http.response.BaseResponse;
import com.umeng.soexample.bean.CategoryVO;
import com.umeng.soexample.bean.SearchVO;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import rx.Observable;

public interface ApiService {

    //POST请求
    @FormUrlEncoded
    @POST("api/user/checkMobile")
    Observable<BaseResponse> getVerfcationCodePostMap(@FieldMap Map<String, String> map);


    //判断验证码是否正确
    @FormUrlEncoded
    @POST("api/user/verifyCode")
    Observable<BaseResponse> VerifyCode(@FieldMap Map<String, String> map);

    //发送验证码
    @FormUrlEncoded
    @POST("api/user/sendCode")
    Observable<BaseResponse> SendCode(@FieldMap Map<String, String> map);

    //注册
    @FormUrlEncoded
    @POST("api/user/register")
    Observable<BaseResponse> Register(@FieldMap Map<String, String> map);

    //修改密码
    @FormUrlEncoded
    @POST("api/user/changePassword")
    Observable<BaseResponse> ChangePwd(@FieldMap Map<String, String> map);

    //重置密码
    @FormUrlEncoded
    @POST("api/user/resetPassword")
    Observable<BaseResponse> ResetPwd(@FieldMap Map<String, String> map);

    //    /*上传文件*/
//    @Multipart
//    @POST("api/user/modifyDevice")
//    Observable<BaseResponse> uploadImage(@Part("token") RequestBody token, @Part("client_id") RequestBody  client_id,
//                                         @Part("device_name") RequestBody device_name, @Part("device_header") RequestBody  device_header,
//                                                            @Part MultipartBody.Part file);
    /*上传文件*/
    @Multipart
    @POST("api/user/modifyDevice")
    Observable<BaseResponse> uploadImage(@PartMap Map<String, RequestBody> map, @Part MultipartBody.Part file);

    @GET("data/{category}/{number}/{page}")
    Observable<CategoryVO> getCategoryDate(@Path("category") String category, @Path("number") int number, @Path("page") int page);

    @GET("random/data/福利/{number}")
    Observable<CategoryVO> getRandomBeauties(@Path("number") int number);

    @GET("search/query/{key}/category/all/count/{count}/page/{page}")
    Observable<SearchVO> getSearchResult(@Path("key") String key, @Path("count") int count, @Path("page") int page);
}
