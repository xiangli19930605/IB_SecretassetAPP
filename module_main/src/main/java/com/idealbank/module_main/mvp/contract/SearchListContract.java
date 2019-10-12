package com.idealbank.module_main.mvp.contract;

import android.app.Activity;

import com.idealbank.module_main.bean.Location;
import com.idealbank.module_main.bean.UpAssetsBean;
import com.idealbank.module_main.mvp.model.entity.UpLoad;
import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import me.jessyan.armscomponent.commonsdk.bean.BaseResponseBean;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.AssetsBean;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.HistoryData;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 02/19/2019 09:59
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public interface SearchListContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        Activity getActivity();

        /**
         * Load all history data
         *
         * @return all history data
         */
       void loadAllHistoryData( List<HistoryData> list);

        /**
         * Add history data
         *
         * @param data history data
         */
        void addHistoryData(String data);


        /**
         * Clear history data
         */
        void clearHistoryData();


        void receiveResult(ArrayList<AssetsBean> list);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        List<HistoryData> loadAllHistoryData();

        Observable<BaseResponseBean<ArrayList<AssetsBean>>> getListByRfid(UpAssetsBean task);
    }
}
