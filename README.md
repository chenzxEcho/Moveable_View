# Moveable_View
A view can move by finger in activity

Activity中的悬浮窗控件

使用的时候将MenuButton拷贝至工程，然后通过简单的设置便可加入至布局中。

setContentView(R.layout.activity_main);
        // rl为该页面布局的根layout
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
