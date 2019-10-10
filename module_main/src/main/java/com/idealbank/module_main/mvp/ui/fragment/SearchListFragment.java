package com.idealbank.module_main.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.idealbank.module_main.Netty.ChannelMap;
import com.idealbank.module_main.Netty.InstructUtils;
import com.idealbank.module_main.Netty.bean.Message;
import com.idealbank.module_main.Netty.bean.MsgType;
import com.idealbank.module_main.R2;

import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.AssetsBean;
import me.jessyan.armscomponent.commonsdk.constants.Constants;
import me.jessyan.armscomponent.commonsdk.utils.DateUtils;
import me.jessyan.armscomponent.commonsdk.utils.GsonUtil;

import com.idealbank.module_main.bean.UpAssetsBean;
import com.idealbank.module_main.mvp.model.entity.UpLoad;
import com.idealbank.module_main.mvp.ui.adapter.SearchListAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.idealbank.module_main.di.component.DaggerSearchListComponent;
import com.idealbank.module_main.mvp.contract.SearchListContract;
import com.idealbank.module_main.mvp.presenter.SearchListPresenter;

import com.idealbank.module_main.R;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import me.jessyan.armscomponent.commonsdk.base.fragment.BaseToolBarFragment;
import me.jessyan.armscomponent.commonsdk.bean.Event;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.HistoryData;
import me.jessyan.armscomponent.commonsdk.core.EventBusTags;
import me.jessyan.armscomponent.commonsdk.utils.ToastUtil;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * ================================================
 */
public class SearchListFragment extends BaseToolBarFragment<SearchListPresenter> implements SearchListContract.View {

    @BindView(R2.id.search_history_rv)
    RecyclerView mRecyclerView;
    @BindView(R2.id.search_edit)
    EditText search_edit;
    @Inject
    SearchListAdapter mAdapter;
    @Inject
    List<HistoryData> mList;


    public static SearchListFragment newInstance() {
        SearchListFragment fragment = new SearchListFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerSearchListComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_list, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initRecyclerView();
        mRecyclerView.setAdapter(mAdapter);
        mPresenter.loadAllHistoryData();
//        showError();
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    protected void reload() {
        Log.e(TAG, "reload");
        showNormal();
    }

    @OnClick({R2.id.search_tv, R2.id.search_history_clear_all_tv, R2.id.search_back_ib})
    void onClick(View view) {
        int i = view.getId();
        if (i == R.id.search_tv) {
            List<AssetsBean> addData = new ArrayList<>();
            AssetsBean assetsBean = new AssetsBean(null, null, search_edit.getText().toString(), "", 4, "", "", "", "", "", "", DateUtils.getCurrentDateStr(Constants.DATE_FORMAT_TOTAL), "", "", "", "", 4, 0);
            addData.add(assetsBean);

//            Message message = new Message();
//            message.setId(1);
//            message.setType(MsgType.RFID);
//            UpLoad upLoad = new UpLoad();
//            upLoad.setId("1");
//            upLoad.setDeviceId("");
//            upLoad.setCreateTime("");
//            upLoad.setAssetList(addData);
//            message.setResponseMessage(upLoad);
//            InstructUtils.send(message);

            UpAssetsBean upAssetsBean=new UpAssetsBean();

            UpAssetsBean.RfidIdBean.ResponseMessageBean responseMessageBean=new UpAssetsBean.RfidIdBean.ResponseMessageBean();
            responseMessageBean.setAssetList(addData);

            UpAssetsBean.RfidIdBean rfidIdBean=new UpAssetsBean.RfidIdBean();
            rfidIdBean.setId(1);
            rfidIdBean.setResponseMessage(responseMessageBean);


            upAssetsBean.setRfidId(rfidIdBean);

mPresenter.getListByRfid(upAssetsBean);



        } else if (i == R.id.search_history_clear_all_tv) {
            mPresenter.clearHistoryData();
        } else if (i == R.id.search_back_ib) {
            (_mActivity).onBackPressed();
        }
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        ArmsUtils.configRecyclerView(mRecyclerView, new LinearLayoutManager(getActivity()));
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (ChannelMap.getChannel("1") != null) {
                    List<AssetsBean> addData = new ArrayList<>();
                    AssetsBean assetsBean = new AssetsBean(null, null, mList.get(position).getData(), "", 0, "", "", "", "", "", "", DateUtils.getCurrentDateStr(Constants.DATE_FORMAT_TOTAL), "", "", "", "", 4, 0);
                    addData.add(assetsBean);

                    Message message = new Message();
                    message.setId(1);
                    message.setType(MsgType.RFID);

                    UpLoad upLoad = new UpLoad();
                    upLoad.setId("1");
                    upLoad.setDeviceId("");
                    upLoad.setCreateTime("");
                    upLoad.setAssetList(addData);

                    message.setResponseMessage(upLoad);
                    ChannelMap.getChannel("1").writeAndFlush(new TextWebSocketFrame(GsonUtil.GsonString(message)));
                    Log.e("INFO_SEARCH_RFID", "" + GsonUtil.GsonString(message));
                } else {
                    ToastUtil.showToast("未连接");
                }

            }
        });
    }


    @Override
    protected void initEventAndData() {


//        initRecyclerView();
//        mRecyclerView.setAdapter(mAdapter);
//        mPresenter.loadAllHistoryData();

    }


    @Override
    public void loadAllHistoryData(List<HistoryData> list) {
        mAdapter.replaceData(mList);
    }

    @Override
    public void addHistoryData(String data) {

    }

    @Override
    public void clearHistoryData() {

    }

    @Subscriber(tag = EventBusTags.SEARCH_RFID)
    private void updateUser(Event event) {
        //位于栈顶才接收
        if (getTopFragment() instanceof SearchListFragment) {
            List<AssetsBean> list = (List<AssetsBean>) event.getData();
            if (list.size() > 0) {
                mPresenter.addHistoryData(search_edit.getText().toString());
                AssetsBean assetsBean = list.get(0);
                start(AssetsDetailsFragment.newInstance(assetsBean));
            }
        }
    }


}
