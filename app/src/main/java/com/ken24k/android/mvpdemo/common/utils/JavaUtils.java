package com.ken24k.android.mvpdemo.common.utils;

import com.ken24k.android.mvpdemo.common.tree.BinaryTree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Java工具
 * Created by wangming on 2020-05-28
 */

public class JavaUtils {

    public static void main(String[] args){
        BinaryTree binaryTree = new BinaryTree();
        binaryTree.insert(6);
        binaryTree.insert(1);
        binaryTree.insert(14);
        binaryTree.insert(7);
        binaryTree.insert(8);
        binaryTree.insert(3);
        binaryTree.insert(2);
        binaryTree.insert(10);
        binaryTree.insert(11);
        binaryTree.insert(41);
        System.out.print("中序遍历:");
        binaryTree.inOrder(binaryTree.getTreeNode());
        System.out.print("后序遍历:");
        binaryTree.afterOrder(binaryTree.getTreeNode());
        System.out.print("先序遍历:");
        binaryTree.beforeOrder(binaryTree.getTreeNode());

        List list = new ArrayList();
        list.add(1);
        list.add(3);
        list.add(123);
        list.add(4);
        list.add(7);
        list.add(0);
        list.add(98);
        list.add(4);
        list.add(2);
        list.add(11);
        System.out.println("sort before:" + list.toString());
        bubbleSort(list);
        System.out.println("sort after:" + list.toString());
    }

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

    /**
     * 冒泡排序法
     */
    public static List bubbleSort(List<Integer> list){
        for(int i = 0; i < list.size(); i++){
            for (int j = 0; j < list.size()-i-1; j++){
                if (list.get(j) > list.get(j+1)){
                    int temp = list.get(j);
                    list.set(j, list.get(j+1));
                    list.set(j+1, temp);
                }
            }
        }
        return list;
    }

    /**
     * 选择排序法
     */
    public static List selectionSort(List<Integer> list){
        for(int i = 0; i < list.size(); i++){
            int min = i;
            for (int j = i+1; j < list.size(); j++){
                if (list.get(min) > list.get(j)){
                    min = j;
                }
            }
            int temp = list.get(i);
            list.set(i, list.get(min));
            list.set(min, temp);
        }
        return list;
    }

}
