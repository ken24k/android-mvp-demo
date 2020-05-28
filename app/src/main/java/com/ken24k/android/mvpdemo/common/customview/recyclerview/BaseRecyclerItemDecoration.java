package com.ken24k.android.mvpdemo.common.customview.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ken24k.android.mvpdemo.R;


/**
 * Created by wangming on 2020-05-28
 */

public class BaseRecyclerItemDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    private Drawable mDivider;
    private int mDividerHeight;// 分割线高度，默认为1
    private int mPadding;// 左右padding，默认10
    private int mOrientation;// 列表的方向：LinearLayoutManager.VERTICAL 或 LinearLayoutManager.HORIZONTAL
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private boolean mBottomLine = true;
    private int mItemCount = 0;

    /**
     * 默认分割线：高度为2px，颜色为灰色
     *
     * @param context
     * @param orientation 列表方向
     */
    public BaseRecyclerItemDecoration(Context context, int orientation) {
        if (orientation != LinearLayoutManager.VERTICAL && orientation != LinearLayoutManager.HORIZONTAL) {
            throw new IllegalArgumentException("请输入正确的参数！");
        }
        mOrientation = orientation;

        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();

        mDividerHeight = context.getResources().getDimensionPixelOffset(R.dimen.common_size_1);
        mPadding = context.getResources().getDimensionPixelOffset(R.dimen.common_size_10);
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param orientation 列表方向
     * @param bottomLine  是否显示底部分割线
     */
    public BaseRecyclerItemDecoration(Context context, int orientation, boolean bottomLine, int itemCount) {
        this(context, orientation);
        mBottomLine = bottomLine;
        mItemCount = itemCount;
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param orientation 列表方向
     * @param drawableId  分割线图片
     */
    public BaseRecyclerItemDecoration(Context context, int orientation, int drawableId) {
        this(context, orientation, drawableId, true, 0);
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param orientation 列表方向
     * @param drawableId  分割线图片
     * @param bottomLine  是否显示底部分割线
     */
    public BaseRecyclerItemDecoration(Context context, int orientation, int drawableId, boolean bottomLine, int itemCount) {
        this(context, orientation);
        mDivider = ContextCompat.getDrawable(context, drawableId);
        mDividerHeight = mDivider.getIntrinsicHeight();
        mBottomLine = bottomLine;
        mItemCount = itemCount;
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param orientation   列表方向
     * @param dividerHeight 分割线高度
     * @param dividerColor  分割线颜色
     */
    public BaseRecyclerItemDecoration(Context context, int orientation, int dividerHeight, int dividerColor) {
        this(context, orientation, dividerHeight, dividerColor, true, 0);
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param orientation   列表方向
     * @param dividerHeight 分割线高度
     * @param dividerColor  分割线颜色
     * @param bottomLine    是否显示底部分割线
     */
    public BaseRecyclerItemDecoration(Context context, int orientation, int dividerHeight, int dividerColor, boolean bottomLine, int itemCount) {
        this(context, orientation);
        mDividerHeight = dividerHeight;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(dividerColor);
        mPaint.setStyle(Paint.Style.FILL);
        mBottomLine = bottomLine;
        mItemCount = itemCount;
    }

    //获取分割线尺寸
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            if (parent.getChildCount() == mItemCount && !mBottomLine)
                outRect.set(0, 0, 0, 0);
            else
                outRect.set(0, 0, 0, mDividerHeight);
        } else {
            if (parent.getChildCount() == mItemCount && !mBottomLine)
                outRect.set(0, 0, 0, 0);
            else
                outRect.set(0, 0, mDividerHeight, 0);
        }
    }

    //绘制分割线
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    // 刷新分割线
    public void refreshChange(int itemCount) {
        mItemCount = itemCount;
    }

    /**
     * 绘制纵向列表时的分隔线  这时分隔线是横着的
     * 每次 left相同，top根据child变化，right相同，bottom也变化
     *
     * @param canvas
     * @param parent
     */
    private void drawVertical(Canvas canvas, RecyclerView parent) {
        final int left = parent.getPaddingLeft() + mPadding;
        final int right = parent.getMeasuredWidth() - parent.getPaddingRight() - mPadding;
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + layoutParams.bottomMargin;
            final int bottom = top + mDividerHeight;
            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

    /**
     * 绘制横向列表时的分隔线  这时分隔线是竖着的
     * l、r 变化； t、b 不变
     *
     * @param canvas
     * @param parent
     */
    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        final int top = parent.getPaddingTop() + mPadding;
        final int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom() - mPadding;
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + layoutParams.rightMargin;
            final int right = left + mDividerHeight;
            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

}
