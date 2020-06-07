package com.ken24k.android.mvpdemo.common.tree;

public class BinaryTree {

    private TreeNode root;

    public TreeNode getTreeNode(){
        return root;
    }

    /**
     * 插入二叉树排序 有序插入 （大于根节点放右边 小于根节点放左边）
     *
     * @param value
     */
    public void insert(Integer value) {
        if (root == null) {
            root = new TreeNode();
            root.setValue(value);
            root.setLeft(null);
            root.setRight(null);
        } else {
            TreeNode currentNode = root;
            TreeNode parentNode = null;
            while (currentNode != null) {
                if (value < currentNode.getValue()) {
                    parentNode = currentNode;
                    currentNode = currentNode.getLeft();
                } else {
                    parentNode = currentNode;
                    currentNode = currentNode.getRight();
                }
            }
            TreeNode newNode = new TreeNode();
            newNode.setValue(value);
            newNode.setLeft(null);
            newNode.setRight(null);
            if (value < parentNode.getValue()) {
                parentNode.setLeft(newNode);
            } else {
                parentNode.setRight(newNode);
            }
        }
    }

    /**
     * 中序遍历
     *
     * @param treeNode
     */
    public void inOrder(TreeNode treeNode) {
        if (treeNode != null) {
            inOrder(treeNode.getLeft());
            System.out.print(" " + treeNode.getValue() + " ");
            inOrder(treeNode.getRight());
        }
    }

    /**
     * 后序遍历
     *
     * @param treeNode
     */
    public void afterOrder(TreeNode treeNode) {
        if (treeNode != null) {
            afterOrder(treeNode.getLeft());
            afterOrder(treeNode.getRight());
            System.out.print(" " + treeNode.getValue() + " ");
        }
    }

    /**
     * 先序遍历
     *
     * @param treeNode
     */
    public void beforeOrder(TreeNode treeNode) {
        if (treeNode != null) {
            System.out.print(" " + treeNode.getValue() + " ");
            beforeOrder(treeNode.getLeft());
            beforeOrder(treeNode.getRight());
        }
    }

}
