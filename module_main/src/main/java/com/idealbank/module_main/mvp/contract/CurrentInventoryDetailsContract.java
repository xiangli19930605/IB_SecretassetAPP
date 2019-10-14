package com.idealbank.module_main.mvp.contract;

import android.app.Activity;

import com.idealbank.module_main.bean.UpAssetsBean;
import com.idealbank.module_main.bean.UpLoadAssetsBean;
import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;

import java.util.ArrayList;

import io.reactivex.Observable;
import me.jessyan.armscomponent.commonsdk.bean.BaseResponseBean;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.AssetsBean;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 03/06/2019 13:48
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public interface CurrentInventoryDetailsContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        Activity getActivity();
        void receiveResult(ArrayList<AssetsBean> list);
        void upLoadResult();
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        public Observable<BaseResponseBean<ArrayList<AssetsBean>>> getListByRfid(UpAssetsBean task);

        public Observable<BaseResponseBean> saveCheckTask(UpLoadAssetsBean upLoadAssetsBean);
    }
}
