package com.ken24k.android.mvpdemo.common.idcard;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ken24k.android.mvpdemo.R;
import com.ken24k.android.mvpdemo.common.glide.GlideManager;
import com.ken24k.android.mvpdemo.common.permission.PermissionConstants;
import com.ken24k.android.mvpdemo.common.permission.PermissionManager;
import com.ken24k.android.mvpdemo.common.utils.AndroidUtils;
import com.ken24k.android.mvpdemo.view.base.BaseActivity;

import java.io.ByteArrayOutputStream;

/**
 * Created by wangming on 2020-05-28
 */

public class CameraActivity extends BaseActivity<ICameraActivity, CameraPresenter> implements ICameraActivity, View.OnClickListener {

    private static CameraActivity instance;

    private Bitmap mCropBitmap;
    private CameraPreview mCameraPreview;
    private ImageView mIvCameraCrop;
    private ImageView mIvCameraView;
    private View mLlCameraOption1;
    private View mLlCameraResult1;
    private View mLlCameraOption2;
    private View mLlCameraResult2;
    private TextView mViewCameraCropBottom;
    private LinearLayout mLLContainer;

    private int mType;// 拍摄类型

    public static CameraActivity getInstance() {
        return instance;
    }

    @Override
    protected void setInstance() {
        instance = this;
    }

    @Override
    protected CameraPresenter createPresenter() {
        return new CameraPresenter();
    }

    @Override
    protected void initBeforeSetContent() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_camera;
    }

    @Override
    protected void bindView() {
        mCameraPreview = findViewById(R.id.camera_preview);
        mIvCameraCrop = findViewById(R.id.iv_camera_crop);
        mIvCameraView = findViewById(R.id.iv_camera_view);
        mLlCameraOption1 = findViewById(R.id.ll_camera_option_1);
        mLlCameraResult1 = findViewById(R.id.ll_camera_result_1);
        mLlCameraOption2 = findViewById(R.id.ll_camera_option_2);
        mLlCameraResult2 = findViewById(R.id.ll_camera_result_2);
        mViewCameraCropBottom = findViewById(R.id.view_camera_crop_bottom);
        mLLContainer = findViewById(R.id.ll_container);
    }

    @Override
    protected void initView() {
        PermissionManager.getInstance().requestPermissions(getInstance(), new PermissionConstants.PermissionManagerListener() {
            @Override
            public void onGranted() {
                init();
            }

            @Override
            public void onUngranted(String msg) {
                finish();
            }

            @Override
            public void onError(String msg) {
                finish();
            }
        }, PermissionConstants.Storage.WRITE_EXTERNAL_STORAGE, PermissionConstants.Storage.READ_EXTERNAL_STORAGE, PermissionConstants.Camera.CAMERA);
    }

    private void init() {
        mType = getIntent().getIntExtra(IDCardCamera.TAKE_TYPE, 0);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        initViews();
        initListener();
    }

    private void initViews() {
        int[] screenSize = AndroidUtils.getScreenSize(getInstance());
        float screenMinSize = Math.min(screenSize[1], screenSize[0]);
        float height = (int) (screenMinSize * 0.75);
        float width = (int) (height * 75.00f / 46.88f);
        LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams((int) width, ViewGroup.LayoutParams.MATCH_PARENT);
        mLLContainer.setLayoutParams(containerParams);
        LinearLayout.LayoutParams cropParams = new LinearLayout.LayoutParams((int) width, (int) height);
        mIvCameraCrop.setLayoutParams(cropParams);

        switch (mType) {
            case IDCardCamera.TYPE_IDCARD_FRONT:
                mIvCameraCrop.setImageResource(R.mipmap.camera_idcard_front);
                mViewCameraCropBottom.setText(getString(R.string.hint_front));
                break;
            case IDCardCamera.TYPE_IDCARD_BACK:
                mIvCameraCrop.setImageResource(R.mipmap.camera_idcard_back);
                mViewCameraCropBottom.setText(getString(R.string.hint_back));
                break;
        }

        // 增加0.5秒过渡界面，解决个别手机首次申请权限导致预览界面启动慢的问题
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCameraPreview.setVisibility(View.VISIBLE);
                    }
                });
            }
        }, 300);
    }

    private void initListener() {
        mCameraPreview.setOnClickListener(this);
        findViewById(R.id.iv_camera_close).setOnClickListener(this);
        findViewById(R.id.iv_camera_take).setOnClickListener(this);
        findViewById(R.id.iv_camera_result_ok).setOnClickListener(this);
        findViewById(R.id.iv_camera_result_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.camera_preview) {
            mCameraPreview.focus();
        } else if (id == R.id.iv_camera_close) {
            finish();
        } else if (id == R.id.iv_camera_take) {
            takePhoto();
        } else if (id == R.id.iv_camera_result_ok) {
            confirm();
        } else if (id == R.id.iv_camera_result_cancel) {
            mCameraPreview.setEnabled(true);
            mCameraPreview.addCallback();
            mCameraPreview.startPreview();
            setTakePhotoLayout();
        }
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        mCameraPreview.setEnabled(false);
        mPresenter.takePhoto();
    }

    /**
     * 裁剪图片
     */
    @Override
    public void cropImage(Bitmap bitmap) {
        // 计算扫描框的坐标点
        int[] location = new int[2];
        mIvCameraCrop.getLocationInWindow(location); // 获取在当前窗口内的绝对坐标
        float left = location[0];
        float top = location[1];
        float width = mIvCameraCrop.getWidth();
        float height = mIvCameraCrop.getHeight();

        // 计算扫描框坐标点占原图坐标点的比例
        float leftProportion = left / mCameraPreview.getWidth();
        float topProportion = top / mCameraPreview.getHeight();
        float widthProportion = width / mCameraPreview.getWidth();
        float heightProportion = height / mCameraPreview.getHeight();

        // 自动裁剪
        mCropBitmap = Bitmap.createBitmap(bitmap,
                (int) (leftProportion * (float) bitmap.getWidth()),
                (int) (topProportion * (float) bitmap.getHeight()),
                (int) (widthProportion * (float) bitmap.getWidth()),
                (int) (heightProportion * (float) bitmap.getHeight()));

        // 照片浏览
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setCameraView();
                setCropLayout();
            }
        });
    }

    /**
     * 设置浏览布局
     */
    private void setCropLayout() {
        mIvCameraCrop.setVisibility(View.GONE);
        mCameraPreview.setVisibility(View.GONE);
        mLlCameraOption1.setVisibility(View.GONE);
        mLlCameraOption2.setVisibility(View.GONE);
        mIvCameraView.setVisibility(View.VISIBLE);
        mLlCameraResult1.setVisibility(View.VISIBLE);
        mLlCameraResult2.setVisibility(View.VISIBLE);
        mViewCameraCropBottom.setText("");
    }

    private void setCameraView() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        mCropBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        GlideManager.getInstance().load(getInstance(), mIvCameraView, bytes);
    }

    /**
     * 设置拍照布局
     */
    private void setTakePhotoLayout() {
        mIvCameraCrop.setVisibility(View.VISIBLE);
        mCameraPreview.setVisibility(View.VISIBLE);
        mLlCameraOption1.setVisibility(View.VISIBLE);
        mLlCameraOption2.setVisibility(View.VISIBLE);
        mIvCameraView.setVisibility(View.GONE);
        mLlCameraResult1.setVisibility(View.GONE);
        mLlCameraResult2.setVisibility(View.GONE);
        switch (mType) {
            case IDCardCamera.TYPE_IDCARD_FRONT:
                mViewCameraCropBottom.setText(getString(R.string.hint_front));
                break;
            case IDCardCamera.TYPE_IDCARD_BACK:
                mViewCameraCropBottom.setText(getString(R.string.hint_back));
                break;
        }
        mCameraPreview.focus();
    }

    /**
     * 点击确认，返回图片路径
     */
    private void confirm() {
        mPresenter.confirm(mCropBitmap);
    }

    @Override
    public void goSetResult(int code, Intent intent) {
        setResult(code, intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mCameraPreview != null) {
            mCameraPreview.onStart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCameraPreview != null) {
            mCameraPreview.onStop();
        }
    }

}