package com.example.movebutton;

import com.example.movebutton.MoveMenu.OnMenuClickListener;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements OnMenuClickListener,
        OnClickListener {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        RelativeLayout rl = (RelativeLayout) this.findViewById(R.id.layout);
        // 实例化悬浮窗对象，最后一个参数是按钮的View对象
        MoveMenu mMoveMenu = new MoveMenu(mContext, this, new Button(this));
        // 设置悬浮窗按钮菜单
        mMoveMenu.inflateMenuLayout(R.layout.menulayout, null, false);
        // 设置悬浮窗移动类型
        mMoveMenu.setMoveType(MoveMenu.MOVETYPE_STOP_IN_SCREEN_EDGE);
        // 设置悬浮窗菜单激活时背景
        mMoveMenu.setBackgroundColor(0x60000000);
        // 设置悬浮窗靠边停留时的比例
        mMoveMenu.setStopShowScale(MoveMenu.SHOW_SCALE_HALF);
        // 设置悬浮窗菜单的位置
        mMoveMenu.setMenuLayoutPosition(MoveMenu.MENULAYOUT_POSITION_CENTER);
        // 显示悬浮窗
        mMoveMenu.showInView(rl);
        // 悬浮窗菜单中的控件事件监听
        Button btn = (Button) mMoveMenu.findViewById(R.id.button1);
        btn.setOnClickListener(this);

    }

    // 悬浮窗按钮点击事件的监听
    @Override
    public void onMenuClick() {
        Toast.makeText(this, "menu click", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.button1:
            Toast.makeText(mContext, "--------------------------------button",
                    Toast.LENGTH_SHORT).show();
            break;
        default:
            break;
        }
    }

}
