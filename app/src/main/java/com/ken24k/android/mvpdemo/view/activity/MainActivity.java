package com.ken24k.android.mvpdemo.view.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ken24k.android.mvpdemo.R;
import com.ken24k.android.mvpdemo.common.customview.recyclerview.BaseRecyclerAdapter;
import com.ken24k.android.mvpdemo.common.customview.recyclerview.BaseRecyclerHolder;
import com.ken24k.android.mvpdemo.common.utils.AndroidUtils;
import com.ken24k.android.mvpdemo.common.utils.JavaUtils;
import com.ken24k.android.mvpdemo.common.utils.SPUtils;
import com.ken24k.android.mvpdemo.view.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangming on 2020-05-27
 */

public class MainActivity extends BaseActivity<IMainActivity, MainPresenter> implements IMainActivity {

    private static MainActivity instance;

    public static MainActivity getInstance() {
        return instance;
    }

    private EditText et;
    private RecyclerView rv;
    private Button btn;
    private List<String> rvList = new ArrayList<>();
    private BaseRecyclerAdapter<String> rvAdapter;

    @Override
    protected void setInstance() {
        instance = this;
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    protected void initBeforeSetContent() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void bindView() {
        et = findViewById(R.id.et);
        rv = findViewById(R.id.rv);
        btn = findViewById(R.id.btn);
    }

    @Override
    protected void initView() {
        initRv();
        initBtn();
        mPresenter.upgrade();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initRv() {
        rvList = SPUtils.getInstance().getArrayData(SPUtils.KeyName.WEB_URL, String.class) != null ? SPUtils.getInstance().getArrayData(SPUtils.KeyName.WEB_URL, String.class) : new ArrayList<String>();
        rvAdapter = new BaseRecyclerAdapter<String>(getInstance(), rvList, R.layout.item_main_rv) {
            @Override
            public void convert(BaseRecyclerHolder holder, String item, final int position, boolean isScrolling) {
                Button delete = holder.getView(R.id.delete);
                TextView tv = holder.getView(R.id.tv);
                tv.setText(rvList.get(position));
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rvList.remove(position);
                        rvAdapter.notifyDataSetChanged();
                        SPUtils.getInstance().saveArrayData(SPUtils.KeyName.WEB_URL, rvList);
                    }
                });
            }
        };
        rv.setLayoutManager(new LinearLayoutManager(getInstance(), RecyclerView.VERTICAL, false));
        rv.setAdapter(rvAdapter);
        rvAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerHolder holder, RecyclerView parent, View view, int position) {
                et.setText(rvList.get(position));
            }
        });
    }

    private void initBtn() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = et.getText().toString();
                if (url.trim().length() == 0)
                    return;
                rvList.add(url);
                JavaUtils.removeDuplicationByHashSet(rvList);
                rvAdapter.notifyDataSetChanged();
                SPUtils.getInstance().saveArrayData(SPUtils.KeyName.WEB_URL, rvList);
                gotoWebview(url);
            }
        });
    }

    private void gotoWebview(String url) {
        Intent intent = new Intent();
        intent.putExtra("url", url);
        AndroidUtils.gotoAct(intent, getInstance(), WebviewActivity.class);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AndroidUtils.REQUEST_INSTALL_PACKAGES) {
            AndroidUtils.openFileBO(getInstance());
        }
    }

}
