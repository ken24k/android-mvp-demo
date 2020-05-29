package com.ken24k.android.mvpdemo.common.intentaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.MediaStore;

import com.ken24k.android.mvpdemo.R;
import com.ken24k.android.mvpdemo.common.utils.AndroidUtils;
import com.ken24k.android.mvpdemo.common.utils.DateUtils;
import com.ken24k.android.mvpdemo.common.utils.FileUtils;

import java.io.File;

/**
 * intent事件方法
 * Created by wangming on 2020-05-28
 */

public class IntentActionService {

    private IntentActionService() {
    }

    private static class SingleTonHolder {
        private static final IntentActionService INSTANCE = new IntentActionService();
    }

    public static IntentActionService getInstance() {
        return SingleTonHolder.INSTANCE;
    }

    /**
     * 拍照存储地址
     */
    private String mTakePhotoPath;

    /**
     * 打电话
     */
    @SuppressLint("MissingPermission")
    public void phoneCall(Context context, String mPhoneCallNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri tel = Uri.parse("tel:" + mPhoneCallNumber);
        intent.setData(tel);
        context.startActivity(intent);
    }

    /**
     * 拍照
     */
    public void takePhoto(Context context, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String imageId = DateUtils.getCurrentDate(DateUtils.DateFormat.yyyyMMddHHmmssSSS);
        File file = new File(FileUtils.ExternalFilePath.JPG_PATH, imageId + FileUtils.FileType.JPG);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            uri = Uri.fromFile(file);
        }
        // 兼容android7.0以上，使用共享文件的形式
        else {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        ((Activity) context).startActivityForResult(intent, requestCode);
        mTakePhotoPath = file.getPath();
    }

    /**
     * 打开通讯簿
     */
    public void contactsBook(Context context, int requestCode) {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        Intent intent = new Intent(Intent.ACTION_PICK, uri);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    /**
     * 获取拍照照片
     */
    public String getTakePhotoPath() {
        return mTakePhotoPath;
    }

    /**
     * 获取通讯录数据
     */
    public String[] getContactsInfo(Uri uri, Context context) {
        String[] contact = new String[2];
        // 得到ContentResolver对象
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            // 取得联系人姓名
            contact[0] = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            // 取得电话数量
            int phoneCount = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            contact[1] = "";
            if (phoneCount > 0) {
                String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor phoneCursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
                if (phoneCursor != null && phoneCursor.moveToFirst()) {
                    contact[1] = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    // 遍历联系人下面所有的电话号码
                    while (phoneCursor.moveToNext()) {
                        contact[1] = contact[1] + "," + phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    phoneCursor.close();
                }
            }
            cursor.close();
        }
        if (contact[1] == null || contact[1].length() == 0) {
            AndroidUtils.showToast(AndroidUtils.getResString(R.string.notify_no_phone_number));
        }
        return contact;
    }

}