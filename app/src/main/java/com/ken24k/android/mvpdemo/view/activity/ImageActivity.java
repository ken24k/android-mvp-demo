package com.ken24k.android.mvpdemo.view.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ken24k.android.mvpdemo.R;
import com.ken24k.android.mvpdemo.common.glide.GlideManager;
import com.ken24k.android.mvpdemo.view.base.BaseActivity;

/**
 * Created by wangming on 2020-05-28
 */

public class ImageActivity extends BaseActivity<IImageActivity, ImagePresenter> implements IImageActivity {

    private static ImageActivity instance;

    private ImageView img;
    private TextView tv;
    private ScrollView sv;

    public static ImageActivity getInstance() {
        return instance;
    }

    @Override
    protected void setInstance() {
        instance = this;
    }

    @Override
    protected ImagePresenter createPresenter() {
        return new ImagePresenter();
    }

    @Override
    protected void initBeforeSetContent() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_image;
    }

    @Override
    protected void bindView() {
        img = findViewById(R.id.img);
        tv = findViewById(R.id.tv);
        sv = findViewById(R.id.sv);
    }

    @Override
    protected void initView() {
        String path = getIntent().getStringExtra("path");
        String text = getIntent().getStringExtra("text");
        if (path != null && path.length() > 0) {
            img.setVisibility(View.VISIBLE);
            GlideManager.getInstance().load(getInstance(), img, path);
        } else if (text != null && text.length() > 0) {
            sv.setVisibility(View.VISIBLE);
            tv.setText(text);
        } else {
            finish();
        }
    }

}
