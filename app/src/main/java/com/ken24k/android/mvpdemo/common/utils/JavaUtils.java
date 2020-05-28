package com.ken24k.android.mvpdemo.common.utils;

import java.util.HashSet;
import java.util.List;

/**
 * Java工具
 * Created by wangming on 2020-05-28
 */

public class JavaUtils {

    /**
     * 使用HashSet实现List去重(无序)
     *
     * @param list
     */
    public static List removeDuplicationByHashSet(List<String> list) {
        HashSet set = new HashSet(list);
        //把List集合所有元素清空
        list.clear();
        //把HashSet对象添加至List集合
        list.addAll(set);
        return list;
    }

}
