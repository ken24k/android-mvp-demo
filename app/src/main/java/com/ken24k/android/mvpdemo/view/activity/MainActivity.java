package com.ken24k.android.mvpdemo.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ken24k.android.mvpdemo.R;
import com.ken24k.android.mvpdemo.common.deviceinfo.DeviceInfoManager;
import com.ken24k.android.mvpdemo.common.idcard.IDCardCamera;
import com.ken24k.android.mvpdemo.common.intentaction.IntentActionManager;
import com.ken24k.android.mvpdemo.common.intentaction.IntentActionService;
import com.ken24k.android.mvpdemo.common.utils.AndroidUtils;
import com.ken24k.android.mvpdemo.model.bean.device.DeviceInfoBean;
import com.ken24k.android.mvpdemo.view.base.BaseActivity;

/**
 * Created by wangming on 2020-05-27
 */

public class MainActivity extends BaseActivity<IMainActivity, MainPresenter> implements IMainActivity {

    private static MainActivity instance;

    public static MainActivity getInstance() {
        return instance;
    }

    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;

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
        btn1 = findViewById(R.id.btn_1);
        btn2 = findViewById(R.id.btn_2);
        btn3 = findViewById(R.id.btn_3);
        btn4 = findViewById(R.id.btn_4);
        btn5 = findViewById(R.id.btn_5);
        btn6 = findViewById(R.id.btn_6);
    }

    @Override
    protected void initView() {
        initBtn();
        mPresenter.upgrade();
    }

    private void initBtn() {
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidUtils.gotoAct(new Intent(), getInstance(), WebviewIndexActivity.class);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IDCardCamera.create(getInstance()).openCamera(IDCardCamera.TYPE_IDCARD_FRONT);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentActionManager.getInstance().startAction(getInstance(), IntentActionManager.Action.TAKE_PHOTO);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentActionManager.getInstance().startAction(getInstance(), IntentActionManager.Action.CONTACTS_BOOK);
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentActionManager.getInstance().startAction(getInstance(), IntentActionManager.Action.PHONE_CALL, "10086", true);
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeviceInfoManager.getInstance().getDeviceInfo(getInstance(), new DeviceInfoManager.DeviceInfoListener() {
                            @Override
                            public void onResults(DeviceInfoBean deviceInfoBean) {
                                Intent intent = new Intent();
                                intent.putExtra("text", JSON.toJSONString(deviceInfoBean));
                                AndroidUtils.gotoAct(intent, getInstance(), ImageActivity.class);
                            }
                        }, DeviceInfoManager.Action.DEVICE_ID
                        , DeviceInfoManager.Action.CONTACTS_INFO
                        , DeviceInfoManager.Action.CALL_LOG
                        , DeviceInfoManager.Action.PHONE_SYSTEM
                        , DeviceInfoManager.Action.PHONE_TYPE
                        , DeviceInfoManager.Action.APP_LIST
                        , DeviceInfoManager.Action.SIM_PHONE_NUMBER
                        , DeviceInfoManager.Action.DEVICE_IP
                        , DeviceInfoManager.Action.WIFI_MAC
                        , DeviceInfoManager.Action.WIFI_BSSID
                        , DeviceInfoManager.Action.WIFI_SSID
                        , DeviceInfoManager.Action.DEVICE_IP_OUTSIDE
                );
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AndroidUtils.REQUEST_INSTALL_PACKAGES) {
            AndroidUtils.openFileBO(getInstance());
        } else if (requestCode == IDCardCamera.TYPE_IDCARD_BACK || requestCode == IDCardCamera.TYPE_IDCARD_FRONT) {
            if (resultCode == IDCardCamera.RESULT_CODE) {
                String path = IDCardCamera.getImagePath(data);
                Intent intent = new Intent();
                intent.putExtra("path", path);
                AndroidUtils.gotoAct(intent, getInstance(), ImageActivity.class);
            }
        } else if (requestCode == IntentActionManager.Action.TAKE_PHOTO) {
            if (resultCode != 0) {
                String path = IntentActionService.getInstance().getTakePhotoPath();
                Intent intent = new Intent();
                intent.putExtra("path", path);
                AndroidUtils.gotoAct(intent, getInstance(), ImageActivity.class);
            }
        } else if (requestCode == IntentActionManager.Action.CONTACTS_BOOK) {
            if (resultCode != 0) {
                Uri uri = data.getData();
                String[] contacts = IntentActionService.getInstance().getContactsInfo(uri, getInstance());
                JSONObject object = new JSONObject();
                object.put("name", contacts[0]);
                object.put("phoneNumber", contacts[1]);
                AndroidUtils.showLongToast(object.toJSONString());
            }
        }
    }

}
