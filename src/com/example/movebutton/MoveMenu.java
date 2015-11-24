package com.example.movebutton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;

public class MoveMenu extends FrameLayout implements OnTouchListener,
        OnClickListener {

    public interface OnMenuClickListener {
        public void onMenuClick();
    }

    private final String TAG = this.getClass().getName();
    private final Context mContext;
    private int mScreenWidth;
    private int mScreenHeight;
    private int lastX;
    private int lastY;
    private int newX;
    private int newY;
    private int mMoveStep;

    private final OnMenuClickListener mOnMenuClickListener;
    private View mMenuLayout;
    private View mMenuView;
    private int mMoveType;
    private int mSensibility = 5;
    private int mEdgeInLeft = 0;
    private int mEdgeInTop = 0;
    private int mEdgeInRight;
    private int mEdgeInBottom;
    private int mStopShowScale;
    FrameLayout.LayoutParams mMenuViewParams;
    private FrameLayout mBackground;
    private int mMenuLayoutPosition;
    private int mMenuLayoutPositionOffsetX = 0;
    private int mMenuLayoutPositionOffsetY = 0;
    private LayoutParams mMenuLayoutParams;

    public static final int MENULAYOUT_POSITION_TOP = 1;
    public static final int MENULAYOUT_POSITION_CENTER = 2;
    public static final int MENULAYOUT_POSITION_BOTTOM = 3;
    public static final int MENULAYOUT_POSITION_AROUND = 4;

    public static final int MOVETYPE_STOP_IN_SITUATION = 1;
    public static final int MOVETYPE_STOP_IN_SCREEN_EDGE = 2;
    public static final int MOVETYPE_STOP_IN_LEFT_AND_RIGHT = 3;
    public static final int MOVETYPE_STOP_IN_TOP_AND_BOTTOM = 4;

    public static final int SHORTEST_DIRECTION_IS_LEFT = 0;
    public static final int SHORTEST_DIRECTION_IS_RIGHT = 1;
    public static final int SHORTEST_DIRECTION_IS_TOP = 2;
    public static final int SHORTEST_DIRECTION_IS_BOTTOM = 3;

    private static final int MENUVIEW_ID = 20000;
    private static final int MENULAYOUT_ID = 20001;

    public static final int SHOW_SCALE_FULL = 0;
    public static final int SHOW_SCALE_HALF = 2;
    private static final int MAXLENGTH = 1000000;

    public MoveMenu(Context context, OnMenuClickListener listener) {
        this(context, listener, null);
    }

    @SuppressLint("NewApi")
    public MoveMenu(Context context, OnMenuClickListener listener, View menuView) {
        super(context);
        this.mContext = context;
        this.mOnMenuClickListener = listener;
        this.mMenuView = menuView;
        if (mMenuView == null) {
            mMenuView = new Button(mContext);
        }
        initView();
    }

    /**
     * 初始化界面
     */
    public void initView() {
        mMenuViewParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mMenuView.setLayoutParams(mMenuViewParams);
        mMenuView.setOnTouchListener(this);
        mMenuView.setOnClickListener(this);
        mMenuView.setId(MENUVIEW_ID);
        mBackground = new FrameLayout(mContext);
        mBackground.setId(MENULAYOUT_ID);
        mBackground.setVisibility(View.INVISIBLE);
        mBackground.setOnClickListener(this);
        mMoveType = MOVETYPE_STOP_IN_SITUATION;
        mStopShowScale = SHOW_SCALE_FULL;
        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        this.mScreenWidth = dm.widthPixels;
        this.mScreenHeight = dm.heightPixels;
        this.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * 设置松手后按钮停靠边缘比例
     * @param stopShowScale
     */
    public void setStopShowScale(int stopShowScale) {
        mStopShowScale = stopShowScale;
    }

    public void setSensibility(int sensibility) {
        mSensibility = sensibility;
    }

    public void setMenuLayoutPosition(int position) {
        this.setMenuLayoutPosition(position, 0, 0);
    }

    public void setMenuLayoutPosition(int position, int offsetX, int offsetY) {
        mMenuLayoutPosition = position;
        mMenuLayoutPositionOffsetX = offsetX;
        mMenuLayoutPositionOffsetY = offsetY;
    }

    /**
     * 设置按钮事件弹出的菜单
     * @param resource
     * @param root
     * @param attachToRoot
     */
    public void inflateMenuLayout(int resource, ViewGroup root,
            boolean attachToRoot) {
        this.inflateMenuLayout(resource, root, attachToRoot, 0);
    }

    public void inflateMenuLayout(int resource, ViewGroup root,
            boolean attachToRoot, int position) {
        mMenuLayout = LayoutInflater.from(mContext).inflate(resource, root,
                attachToRoot);
        mMenuLayoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mMenuLayout.setLayoutParams(mMenuLayoutParams);
        mMenuLayout.setVisibility(View.INVISIBLE);
    }

    /**
     * 设置点击后弹出层背景
     * @param resource
     */
    public void setBackground(int resource) {
        mBackground.setBackgroundResource(resource);
    }

    public void setBackgroundColor(int color) {
        mBackground.setBackgroundColor(color);
    }

    /**
     * 设置悬浮窗初始位置
     * @param gravity
     */
    public void setInitPosition(int gravity) {
        mMenuViewParams.gravity = gravity;
    }

    /**
     * 显示悬浮窗
     * @param viewGroup
     */
    public void showInView(ViewGroup viewGroup) {
        if (viewGroup != null) {
            viewGroup.addView(this);
        }
        this.addView(mBackground);
        if (mMenuLayout == null) {
            return;
        }
        this.addView(mMenuLayout);
        if (mMenuView == null) {
            return;
        }
        if (mMenuViewParams != null) {
            this.addView(mMenuView, mMenuViewParams);
        } else {
            this.addView(mMenuView);
        }
    }

    /**
     * 设置停靠模式
     * @param moveType
     */
    public void setMoveType(int moveType) {
        mMoveType = moveType;
    }

    /**
     * 设置悬浮窗移动范围大小
     * @param leftInEdge
     * @param topInEdge
     * @param rightInEdge
     * @param bottomInEdge
     */
    public void setMoveEdge(int leftInEdge, int topInEdge, int rightInEdge,
            int bottomInEdge) {
        mEdgeInLeft = leftInEdge;
        mEdgeInTop = topInEdge;
        mEdgeInRight = rightInEdge;
        mEdgeInBottom = bottomInEdge;
        mMenuViewParams.topMargin = mEdgeInTop;
        mMenuViewParams.leftMargin = mEdgeInLeft;
        mMenuViewParams.rightMargin = mEdgeInRight;
        mMenuViewParams.bottomMargin = mEdgeInBottom;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(final View v, MotionEvent event) {
        int eventAction = event.getAction();
        int dl = v.getLeft();
        int db = v.getBottom();
        int dr = v.getRight();
        int dt = v.getTop();
        int llength = dl;
        int rlength = this.mScreenWidth - dr;
        int tlength = dt;
        int blength = this.mScreenHeight - db;
        int moveDirect = calculateDistanceToEdge(llength, rlength, tlength,
                blength);
        switch (eventAction) {
        case MotionEvent.ACTION_DOWN:
            mMoveStep = 0;
            v.layout(dl, dt, dr, db);
            v.postInvalidate();
            this.lastX = (int) event.getRawX();// 获取触摸事件触摸位置的原始X坐标
            this.lastY = (int) event.getRawY();
            break;
        case MotionEvent.ACTION_MOVE:
            this.mMoveStep++;
            moveViewWithFinger(v, event);
            break;
        case MotionEvent.ACTION_UP:
            moveToScreenEdge(moveDirect, llength, rlength, tlength, blength, v);
            this.MoveToEdge();
            break;
        }
        return false;
    }

    /**
     * 计算手指离开屏幕时悬浮窗动作数据
     * @param moveDirect
     * @param llength
     * @param rlength
     * @param tlength
     * @param blength
     * @param v
     */
    private void moveToScreenEdge(int moveDirect, int llength, int rlength,
            int tlength, int blength, View v) {
        switch (moveDirect) {
        case SHORTEST_DIRECTION_IS_LEFT:
            this.newX = -(llength + v.getWidth() * mStopShowScale / 4);
            this.newY = 0;
            break;
        case SHORTEST_DIRECTION_IS_RIGHT:
            this.newX = rlength + v.getWidth() * mStopShowScale / 4;
            this.newY = 0;
            break;
        case SHORTEST_DIRECTION_IS_TOP:
            this.newX = 0;
            this.newY = -(tlength + v.getHeight() * mStopShowScale / 4);
            break;
        case SHORTEST_DIRECTION_IS_BOTTOM:
            this.newX = 0;
            this.newY = blength + v.getHeight() * mStopShowScale / 4;
            break;
        default:
            break;
        }
    }

    /**
     * 控件随手指移动
     * @param v
     * @param event
     */
    private void moveViewWithFinger(View v, MotionEvent event) {
        int dx = (int) event.getRawX() - this.lastX;
        int dy = (int) event.getRawY() - this.lastY;

        int l = v.getLeft() + dx;
        int b = v.getBottom() + dy;
        int r = v.getRight() + dx;
        int t = v.getTop() + dy;
        // 下面判断移动是否超出屏幕 如果只是想与边框贴合就使用底下这个代码
        if (l < mEdgeInLeft) {
            l = mEdgeInLeft;
            r = l + v.getWidth();
        }
        if (r > this.mScreenWidth - mEdgeInRight) {
            r = this.mScreenWidth - mEdgeInRight;
            l = r - v.getWidth();
        }
        if (t < mEdgeInTop) {
            t = mEdgeInLeft;
            b = t + v.getHeight();
        }
        if (b > this.mScreenHeight - mEdgeInBottom) {
            b = this.mScreenHeight - mEdgeInBottom;
            t = b - v.getHeight();
        }
        this.lastX = (int) event.getRawX();
        this.lastY = (int) event.getRawY();
        // Log.i("MoveMenu", "当前位置：" + l + "," + t + "," + r + "," + b);
        v.layout(l, t, r, b);
        v.postInvalidate();
    }

    /**
     * 计算距离最近的边
     * @param llength
     * @param rlength
     * @param tlength
     * @param blength
     * @return
     */
    private int calculateDistanceToEdge(int llength, int rlength, int tlength,
            int blength) {
        int[] lengths = { llength, rlength, tlength, blength };
        switch (mMoveType) {
        case MOVETYPE_STOP_IN_SCREEN_EDGE:
            break;
        case MOVETYPE_STOP_IN_LEFT_AND_RIGHT:
            lengths[2] = MAXLENGTH;
            lengths[3] = MAXLENGTH;
            break;
        case MOVETYPE_STOP_IN_TOP_AND_BOTTOM:
            lengths[0] = MAXLENGTH;
            lengths[1] = MAXLENGTH;
            break;
        case MOVETYPE_STOP_IN_SITUATION:
            return -1;
        default:
            break;
        }
        int minLength = lengths[0];
        int index = 0;
        for (int i = 0; i < lengths.length; i++) {
            if (minLength > lengths[i]) {
                minLength = lengths[i];
                index = i;
            }
        }
        return index;
    }

    /**
     * 悬浮窗动作动画
     */
    @SuppressLint("NewApi")
    private void MoveToEdge() {
        Animation myAnimation_Translate = new TranslateAnimation(0, this.newX,
                0, this.newY);
        myAnimation_Translate.setDuration(200);
        mMenuView.startAnimation(myAnimation_Translate);
        myAnimation_Translate.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mMenuView.layout(mMenuView.getLeft() + MoveMenu.this.newX,
                        mMenuView.getTop() + MoveMenu.this.newY,
                        mMenuView.getRight() + MoveMenu.this.newX,
                        mMenuView.getBottom() + MoveMenu.this.newY);
                TranslateAnimation anim = new TranslateAnimation(0, 0, 0, 0);
                mMenuView.setAnimation(anim);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mMenuView.getId()) {
            Log.i(TAG, "mMoveStep: " + mMoveStep);
            if (mMoveStep < mSensibility) { // 移动距离小于5认为是点击，而不是移动
                if (mMenuLayout != null) {
                    mMenuLayout.setVisibility(View.VISIBLE);
                    menuLayoutOnPosition(mMenuLayout);
                    mBackground.setVisibility(View.VISIBLE);
                }
                mOnMenuClickListener.onMenuClick();
            }
        } else if (v.getId() == mBackground.getId()) {
            mMenuLayout.setVisibility(View.INVISIBLE);
            mBackground.setVisibility(View.INVISIBLE);
            Log.i(TAG, "mBackground: " + mBackground.getVisibility());
        }

    }

    /**
     * 设置菜单的出现位置 注：MENULAYOUT_POSITION_AROUND类型未完成
     * @param menuLayout
     */
    private void menuLayoutOnPosition(View menuLayout) {
        switch (mMenuLayoutPosition) {
        case MENULAYOUT_POSITION_TOP:
            menuLayout.layout((mScreenWidth - menuLayout.getWidth()) / 2
                    + mMenuLayoutPositionOffsetX, mEdgeInTop
                    + mMenuLayoutPositionOffsetY,
                    (mScreenWidth + menuLayout.getWidth()) / 2
                            + mMenuLayoutPositionOffsetX, mEdgeInTop
                            + menuLayout.getHeight()
                            + mMenuLayoutPositionOffsetY);
            break;
        case MENULAYOUT_POSITION_CENTER:
            menuLayout.layout((mScreenWidth - menuLayout.getWidth()) / 2
                    + mMenuLayoutPositionOffsetX,
                    (mScreenHeight - menuLayout.getHeight()) / 2
                            + mMenuLayoutPositionOffsetY,
                    (mScreenWidth + menuLayout.getWidth()) / 2
                            + mMenuLayoutPositionOffsetX,
                    (mScreenHeight + menuLayout.getHeight()) / 2
                            + mMenuLayoutPositionOffsetY);
            break;
        case MENULAYOUT_POSITION_BOTTOM:
            menuLayout.layout((mScreenWidth - menuLayout.getWidth()) / 2
                    + mMenuLayoutPositionOffsetX, mScreenHeight - mEdgeInBottom
                    - menuLayout.getHeight() + mMenuLayoutPositionOffsetY,
                    (mScreenWidth + menuLayout.getWidth()) / 2
                            + mMenuLayoutPositionOffsetX, mScreenHeight
                            - mEdgeInBottom + mMenuLayoutPositionOffsetY);
            break;
        case MENULAYOUT_POSITION_AROUND:

            break;
        default:
            break;
        }
    }
}
