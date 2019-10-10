package me.jessyan.armscomponent.commonres.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import me.jessyan.armscomponent.commonres.R;
import me.jessyan.armscomponent.commonres.dialog.LVCircularRing;


/**
 * 等待对话框
 * Created by wtq on 2016/3/14.
 */
public class LoadingView extends LinearLayout {

    private LVCircularRing mLoadingView;
    private Context context;
    private String msg = "加载中···";
    TextView loadingText;
    private boolean cancelable = true;
    private boolean isShow;
    public LoadingView(Context context) {
        super(context);

    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.view_loading, this);
        // 获取整个布局
        LinearLayout layout = view.findViewById(R.id.dialog_view);
        // 页面中的LoadingView
        mLoadingView = view.findViewById(R.id.lvcr_loading);
        // 页面中显示文本
         loadingText = view.findViewById(R.id.loading_text);
        loadingText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });
    }

    public void show() {
        if(!isShowing()){
            mLoadingView.setVisibility(VISIBLE);
            mLoadingView.startAnim();
            loadingText.setClickable(false);
            // 显示文本
            loadingText.setText("加载中");
            Observable.timer(5000, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Long>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable disposable) {

                        }
                        @Override
                        public void onNext(@NonNull Long number) {
                            mLoadingView.stopAnim();
                            mLoadingView.setVisibility(View.GONE);
                            loadingText.setText("重新查询");
                            loadingText.setClickable(true);
                            isShow = true;
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            //取消订阅
                        }

                        @Override
                        public void onComplete() {
                            //取消订阅
                        }
                    });
        }
    }


    /**
     * 设置提示信息
     */
    public LoadingView setTitleText(String msg) {
        this.msg = msg;
        return this;
    }


    public void dismiss() {
        if(isShow){
            mLoadingView.stopAnim();
            mLoadingView.setVisibility(View.GONE);
            isShow = false;
        }
    }



    public boolean isShowing() {
        return isShow;
    }
}
