package com.ken24k.android.mvpdemo.model.bean.device;

import java.util.ArrayList;
import java.util.List;

/**
 * 联系人
 * Created by wangming on 2020-05-28
 */

public class ContactsBean {

    /**
     * 姓名
     */
    private String name;
    /**
     * 电话列表
     */
    private List<String> phoneNumList = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPhoneNumList() {
        return phoneNumList;
    }

    public void setPhoneNumList(List<String> phoneNumList) {
        this.phoneNumList = phoneNumList;
    }

    public void addContactPhoneNums(String contactPhoneNum) {
        this.phoneNumList.add(contactPhoneNum);
    }

}