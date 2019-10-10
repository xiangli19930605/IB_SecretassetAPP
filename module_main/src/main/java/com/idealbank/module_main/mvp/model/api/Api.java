package com.idealbank.module_main.mvp.model.api;

import com.idealbank.module_main.bean.Location;
import com.idealbank.module_main.bean.UpAssetsBean;
import com.idealbank.module_main.mvp.model.entity.UpLoad;

import java.util.ArrayList;

import io.reactivex.Observable;
import me.jessyan.armscomponent.commonsdk.bean.BaseResponseBean;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.AssetsBean;
import me.jessyan.armscomponent.commonsdk.constants.Constants;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

import static me.jessyan.retrofiturlmanager.RetrofitUrlManager.DOMAIN_NAME_HEADER;

/**
 * ================================================
 * 存放一些与 API 有关的东西,如请求地址,请求码等
 * <p>
 * Created by ArmsComponentTemplate on 02/18/2019 16:33
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/ArmsComponent">Star me</a>
 * <a href="https://github.com/JessYanCoding/ArmsComponent/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/ArmsComponent-Template">模版请保持更新</a>
 * ================================================
 */
public interface Api {
    String wanandroid_DOMAIN_NAME = "gank";
    String GITHUB_DOMAIN_NAME = "github";
    String baseurl1 = "http://www.wanandroid.com";


    @GET("http://is.snssdk.com/api/news/feed/v62/?iid=5034850950&device_id=6096495334&refer=1&count=20&aid=13")
    Observable<AssetsBean> getRfid(
            @Query("category") String category,
            @Query("max_behot_time") String maxBehotTime);


    @POST("http://localhost:8092//sacs/pda/login" + "")
    Observable<AssetsBean> login(
            @Query("category") String category,
            @Query("max_behot_time") String maxBehotTime);


    @Headers({DOMAIN_NAME_HEADER+ Constants.WANGYI_DOMAIN_NAME })
    @POST("/sacs/terminal/inspection/getLocationList")
    Observable<BaseResponseBean<ArrayList<Location>>> getLocationList();


    @Headers({DOMAIN_NAME_HEADER+ Constants.WANGYI_DOMAIN_NAME })
    @POST("/sacs/terminal/inspection/getListByRfid")
    Observable<BaseResponseBean<UpLoad>> getListByRfid(@Body UpAssetsBean task);

}
