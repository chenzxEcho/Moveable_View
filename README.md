# Moveable_View
A view can move by finger in activity

Activity中的悬浮窗控件

使用的时候将MenuButton拷贝至工程，然后通过简单的设置便可加入至布局中。
示例：

        setContentView(R.layout.activity_main);
        // rl为该页面布局的根layout
        RelativeLayout rl = (RelativeLayout) this.findViewById(R.id.layout);
        
        // 实例化悬浮窗对象，最后一个参数是按钮的View对象 必要
        MoveMenu mMoveMenu = new MoveMenu(mContext, this, new Button(this));
        
        // 设置悬浮窗按钮菜单 可选
        mMoveMenu.inflateMenuLayout(R.layout.menulayout, null, false);
        
        // 设置悬浮窗移动类型 可选
        mMoveMenu.setMoveType(MoveMenu.MOVETYPE_STOP_IN_SCREEN_EDGE);
        
        // 设置悬浮窗菜单激活时背景 可选
        mMoveMenu.setBackgroundColor(0x60000000);
        
        // 设置悬浮窗靠边停留时的比例 可选
        mMoveMenu.setStopShowScale(MoveMenu.SHOW_SCALE_HALF);
        
        // 设置悬浮窗菜单的位置 可选
        mMoveMenu.setMenuLayoutPosition(MoveMenu.MENULAYOUT_POSITION_CENTER);
        
        // 显示悬浮窗 必要
        mMoveMenu.showInView(rl);
        
        // 悬浮窗菜单中的控件事件监听
        Button btn = (Button) mMoveMenu.findViewById(R.id.button1);
        btn.setOnClickListener(this);
        
    ---------------------------------------------------------------------------------------------------
        1、设置悬浮窗按钮菜单
        该菜单为点击悬浮窗出现的菜单，可以为任何常见布局。
        
        2、设置悬浮窗移动类型
        该类型为手指抬起后悬浮窗的动作
        悬浮窗留在原位                  MOVETYPE_STOP_IN_SITUATION
        悬浮窗自动回到最近的屏幕边缘    MOVETYPE_STOP_IN_SCREEN_EDGE
        悬浮窗自动回到屏幕左右两侧      MOVETYPE_STOP_IN_LEFT_AND_RIGHT
        悬浮窗自动回到屏幕上下两侧      MOVETYPE_STOP_IN_TOP_AND_BOTTOM
        
        3、设置悬浮窗菜单激活时背景
        该背景为点击悬浮窗时的背景，可以是颜色或资源文件
        
        4、设置悬浮窗靠边停留时的比例
        该比例为悬浮窗设置了自动停靠屏幕边缘时，悬浮窗的显示比例
        全部在屏幕中                    SHOW_SCALE_FULL
        一半在屏幕中                    SHOW_SCALE_HALF
        
        5、设置悬浮窗菜单的位置
        该位置为点击悬浮窗后出现菜单的位置
        在顶部出现                      MENULAYOUT_POSITION_TOP
        在中间出现                      MENULAYOUT_POSITION_CENTER
        在底部出现                      MENULAYOUT_POSITION_BOTTOM
        在悬浮窗周围出现                MENULAYOUT_POSITION_AROUND      （待完成）
        
![image](https://github.com/ctmwd/Moveable_View/blob/master/screenshot/screenshot.png =100x20)

![image](https://github.com/ctmwd/Moveable_View/blob/master/screenshot/screenshot3.png =100x20)

![image](https://github.com/ctmwd/Moveable_View/blob/master/screenshot/screenshot4.png =100x20)

![image](https://github.com/ctmwd/Moveable_View/blob/master/screenshot/screenshot5.png =100x20)
        
