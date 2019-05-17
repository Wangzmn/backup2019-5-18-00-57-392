package zmn.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import wclass.android.ui.LinearGradientAdvancer;
import wclass.android.ui.drawable.GradientWithStrokesDrawable;
import wclass.android.util.JumpPageUT;
import wclass.android.util.PhoneUTUT_misc;
import wclass.android.util.SizeUT;
import wclass.android.util.ViewUT;
import wclass.enums.LayoutGravity;
import wclass.enums.Orien2;
import wclass.util.ColorUT;
import zmn.w.uiutility.R;
import zmn.w.uiutility.ServiceMe;
import zmn.w.uiutility.main_class.a_pending_class.color_selector_frame.ColorSelector;
import zmn.w.uiutility.main_class.Prefer;
import wclass.android.ui.view.Buttons;
import zmn.w.uiutility.main_class.window.Window;
import zmn.w.uiutility.second_class.InterceptFrameVG;

public class MainActivity extends Activity {
    GradientWithStrokesDrawable s;
    Prefer prefer;
    //----------------------------------------------------------------------
    private int cm;

    //----------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        find();
        Prefer.init(this);
        prefer = Prefer.getInstance();
        cm = SizeUT.getCMpixel(this);
        addDebugButton("","","","");
        //----------------------------------------------------------------------
//        editPluginView();
//        gradientAdvancer();
        startService(new Intent(this, ServiceMe.class));
        //todo 加一个判断service是否存在的。
        //        finish();
//        SeekBar s = new SeekBar(this);
//        ViewUT.adjustSize(s,50,200);
//        root.addView(s,0);
        //--------------------------------------------------
//     colorSelector = new ColorSelector(this);
//        root.addView(colorSelector.root,0);
        //--------------------------------------------------
//        ImageView view = new ImageView(this);
//        view.setImageDrawable(new RightTriangle(
//                0xffaa00aa));
//        ViewUT.adjustSize(view,500,500);
//        root.addView(view,0);
        //--------------------------------------------------
//        root.setBackgroundColor(0xff0000ff);
    }
    ColorSelector colorSelector;

    private void addDebugButton(String... s) {
        Buttons b0 = new Buttons(this, new Buttons.OnClickListener() {
            @Override
            public void onClick(View view, int number) {
                switch (number) {
                    case 1:
                        delSize();
                        break;
                    case 2:
                        addSize();
                        break;
                    case 3:
                        JumpPageUT.jumpToUserInfoPage(MainActivity.this, 1);
                        break;
                    case 4:
                        JumpPageUT.jumpToOverlayPage(MainActivity.this,0);
//                        Window hand = prefer.director.hand;
//                        int x = hand.getX();
//                        int y = hand.getY();
//                        int width = hand.getWidth();
//                        int height = hand.getHeight();
//                        Log.e("TAG"," x = "+x+"    "
//                                +" y = "+y +"    "
//                                +" width = "+width+"    "
//                                +" height = "+height +"    ");

                        break;
                    case 5:
                        startService(new Intent(MainActivity.this, ServiceMe.class));
//                        prefer.dataSave.clear();
                        break;
                }
            }
        }, 5,
                "大小-", "大小+", "开启用户信息", "开启最上层", "启动悬浮球");
        Buttons b1 = new Buttons(this, new Buttons.OnClickListener() {
            @Override
            public void onClick(View view, int number) {
                switch (number) {
                    case 1:
                        delGridCount();
                        break;
                    case 2:
                        addGridCount();
                        break;
                }
            }
        }, 5, "menu数量-", "menu数量+", "", "");
        Buttons b2 = new Buttons(this, new Buttons.OnClickListener() {
            @Override
            public void onClick(View view, int number) {
                switch (number) {
                    case 1:
                        prefer.setLayoutGravity(LayoutGravity.LEFT_TOP);
                        break;
                    case 2:
                        prefer.setLayoutGravity(LayoutGravity.LEFT_BOTTOM);
                        break;
                    case 3:
                        prefer.setLayoutGravity(LayoutGravity.RIGHT_TOP);
                        break;
                    case 4:
                        prefer.setLayoutGravity(LayoutGravity.RIGHT_BOTTOM);
                        break;
                }
            }
        },4,"左上角","左下角","右上角","右下角");
        Buttons b3 = new Buttons(this, new Buttons.OnClickListener() {
            @Override
            public void onClick(View view, int number) {
                switch (number) {
                    case 1:
                        prefer.setOrien(Orien2.HORIZONTAL);
                        break;
                    case 2:
                        prefer.setOrien(Orien2.VERTICAL);
                        break;
                }
            }
        },2,"横向悬浮窗口","纵向悬浮窗口");
        root.addView(b0, 0);
        root.addView(b1, 1);
        root.addView(b2, 2);
        root.addView(b3, 3);
    }

    private void addGridCount() {
        prefer.addGridCount_forMatchScreenWidth();
    }

    private void delGridCount() {
        prefer.delGridCount_forMatchScreenWidth();
    }

    private void addSize() {
        prefer.addSize(5);
    }

    private void delSize() {
        prefer.delSize(5);
    }

    private void gradientAdvancer() {
       LinearGradientAdvancer la = new LinearGradientAdvancer(
                0, 0, 0, 1,
                new int[]{0xffffffff, 0xffffffff, 0xffdddddd},
                new float[]{0, 0.5f, 1},
//                new int[]{0xffff0000,0xff155643, 0xff00ff00,0xff465496,0xff0000ff},
//                new float[]{0,0.3f,0.4f,0.6f, 1},
                Shader.TileMode.CLAMP);
        s = new GradientWithStrokesDrawable(la, 1 / 5f,
                new float[]{ 0xddaaaaaa, 5, 0xee888888, 5});
        tv.setBackground(s);
    }


    //step：阴影的3个颜色。
    public static final int COLOR1 = ColorUT.argb(0.90f, 0, 0, 0);
    public static final int COLOR2 = ColorUT.argb(0.95f, 0, 0, 0);
    public static final int COLOR3 = 0xffa1a1a1;

    private void forRecy() {
        screenW = SizeUT.getScreenWidth(this);
        screenH = SizeUT.getScreenHeight(this);
        ViewUT.adjustSize(rv, screenW, screenH / 2);
        int rows = 4;
        rv.setLayoutManager(new GridLayoutManager(this, rows,
                LinearLayoutManager.HORIZONTAL, false));
//        rv.setAdapter(new Adapter(this, screenH / 2 / 4));
        rv.getLayoutManager().setAutoMeasureEnabled(false);
    }

    int screenW, screenH;

    public void onButton5(View view) {
    }

    public void onButton4(View view) {
    }

    public void onButton3(View view) {
    }

    public void onButton2(View view) {
    }

    public void onButton1(View view) {
    }


    public void onButton10(View view) {
    }

    public void onButton9(View view) {
    }

    public void onButton8(View view) {
    }

    public void onButton7(View view) {
    }

    public void onButton6(View view) {
    }

    private TextView tv;
    private Button mButton;
    private Button mButton2;
    private Button mButton3;
    private Button mButton4;
    private Button mButton5;
    private Button mButton6;
    private Button mButton7;
    private Button mButton8;
    private Button mButton9;
    private Button mButton10;
    private RecyclerView rv;
    private LinearLayout root;

    private void find() {
        root = findViewById(R.id.root);
        rv = findViewById(R.id.rv);
        tv = findViewById(R.id.tv);
        mButton = findViewById(R.id.button);
        mButton2 = findViewById(R.id.button2);
        mButton3 = findViewById(R.id.button3);
        mButton4 = findViewById(R.id.button4);
        mButton5 = findViewById(R.id.button5);
        mButton6 = findViewById(R.id.button6);
        mButton7 = findViewById(R.id.button7);
        mButton8 = findViewById(R.id.button8);
        mButton9 = findViewById(R.id.button9);
        mButton10 = findViewById(R.id.button10);
    }
}
//    <?xml version="1.0" encoding="utf-8"?>
//<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
//        xmlns:tools="http://schemas.android.com/tools"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        android:orientation="vertical"
//        tools:context="zmn.w.uiutility.MainActivity">
//
//<TextView
//        android:id="@+id/tv"
//                android:layout_width="match_parent"
//                android:layout_height="82dp"
//                android:textSize="30dp"
//                android:text="Hello World!"
//                />
//
//<LinearLayout
//        android:layout_width="match_parent"
//                android:layout_height="wrap_content"
//                android:orientation="horizontal"
//                >
//<Button
//            android:id="@+id/button"
//                    android:layout_width="wrap_content"
//                    android:layout_height="wrap_content"
//                    android:text="Button1"
//                    android:onClick="onButton1"
//                    tools:layout_editor_absoluteX="0dp"
//                    tools:layout_editor_absoluteY="0dp" />
//
//<Button
//            android:id="@+id/button2"
//                    android:onClick="onButton2"
//                    android:layout_width="wrap_content"
//                    android:layout_height="wrap_content"
//                    android:text="Button2"
//                    tools:layout_editor_absoluteX="76dp"
//                    tools:layout_editor_absoluteY="0dp" />
//
//<Button
//            android:id="@+id/button3"
//                    android:onClick="onButton3"
//                    android:layout_width="wrap_content"
//                    android:layout_height="wrap_content"
//                    android:text="Button3"
//                    tools:layout_editor_absoluteX="153dp"
//                    tools:layout_editor_absoluteY="0dp" />
//
//<Button
//            android:id="@+id/button4"
//                    android:onClick="onButton4"
//                    android:layout_width="wrap_content"
//                    android:layout_height="wrap_content"
//                    android:text="Button4"
//                    tools:layout_editor_absoluteX="230dp"
//                    tools:layout_editor_absoluteY="0dp" />
//
//<Button
//            android:id="@+id/button5"
//                    android:onClick="onButton5"
//                    android:layout_width="wrap_content"
//                    android:layout_height="wrap_content"
//                    android:text="Button5"
//                    tools:layout_editor_absoluteX="307dp"
//                    tools:layout_editor_absoluteY="0dp" />
//</LinearLayout>
//<LinearLayout
//        android:layout_width="match_parent"
//                android:layout_height="wrap_content"
//                android:orientation="horizontal"
//                >
//<Button
//            android:id="@+id/button6"
//                    android:onClick="onButton6"
//                    android:layout_width="wrap_content"
//                    android:layout_height="wrap_content"
//                    android:text="Button6"
//                    tools:layout_editor_absoluteX="0dp"
//                    tools:layout_editor_absoluteY="0dp" />
//
//<Button
//            android:id="@+id/button7"
//                    android:onClick="onButton7"
//                    android:layout_width="wrap_content"
//                    android:layout_height="wrap_content"
//                    android:text="Button7"
//                    tools:layout_editor_absoluteX="76dp"
//                    tools:layout_editor_absoluteY="0dp" />
//
//<Button
//            android:id="@+id/button8"
//                    android:onClick="onButton8"
//                    android:layout_width="wrap_content"
//                    android:layout_height="wrap_content"
//                    android:text="Button8"
//                    tools:layout_editor_absoluteX="153dp"
//                    tools:layout_editor_absoluteY="0dp" />
//
//<Button
//            android:id="@+id/button9"
//                    android:onClick="onButton9"
//                    android:layout_width="wrap_content"
//                    android:layout_height="wrap_content"
//                    android:text="Button9"
//                    tools:layout_editor_absoluteX="230dp"
//                    tools:layout_editor_absoluteY="0dp" />
//
//<Button
//            android:id="@+id/button10"
//                    android:onClick="onButton10"
//                    android:layout_width="wrap_content"
//                    android:layout_height="wrap_content"
//                    android:text="Button10"
//                    tools:layout_editor_absoluteX="307dp"
//                    tools:layout_editor_absoluteY="0dp" />
//</LinearLayout>
//</LinearLayout>
