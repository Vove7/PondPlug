package cn.vove7.pond_plug;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.view.*;
import android.widget.Button;
import android.widget.ImageView;

import cn.vove7.pond_plug.handler.MessageHandler;
import cn.vove7.pond_plug.threads.SearchThread;


/**
 * Created by Vove on 2017/4/9.
 * 悬浮窗
 */

public class FloatWindow {
    private static int posX;
    private static int posY;

    static {
        posX = 100;
        posY = 200;
    }

    //    private static long lastClickTime = new Date().getTime();
    public boolean isRunning = false;
    private Context context;
    private View view;
    private WindowManager windowManager = null;
    private WindowManager.LayoutParams mParams = null;
    public static boolean isOpenVibrator = false;
    private static MessageHandler messageHandler;
    private SearchThread searchThread;


    static void setOpenVibrator(boolean openVibrator) {
        isOpenVibrator = openVibrator;
    }


    FloatWindow(final Context context, final View view) {
        this.context = context;
        this.view = view;
        messageHandler = new MessageHandler(context);
        showFloatWindow();
    }

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR2)
    private void initView() {
        if (windowManager == null) {
            windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        }
        final Point size = new Point();
        windowManager.getDefaultDisplay().getSize(size);
        mParams = new WindowManager.LayoutParams();
        mParams.packageName = context.getPackageName();
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;

        mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        mParams.format = PixelFormat.RGBA_8888;
        mParams.gravity = Gravity.TOP | Gravity.START;

        mParams.x = posX;//dp2px(posX);
        mParams.y = posY;//dp2px(posY);

        view = LayoutInflater.from(context).inflate(R.layout.float_layout, null);

        final ImageView imageView = (ImageView) view.findViewById(R.id.float_icon);

        imageView.setOnClickListener(v -> {
            //captureScreen_su();//截屏
        });
        imageView.setOnLongClickListener(v -> {
            //simulateOperate();
            return false;
        });
        imageView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE: {
                    //event.getRawX()  获取手指位置
                    posX = mParams.x = (int) event.getRawX() - imageView.getWidth() / 2;
                    posY = mParams.y = (int) event.getRawY() - imageView.getHeight() / 2;
                    windowManager.updateViewLayout(view, mParams);
                }
                break;
                case MotionEvent.ACTION_DOWN: {
                    //双击
                    /*long currentClickTime = new Date().getTime();
                    if ((currentClickTime - lastClickTime) < 290) {
                        //
                    } else lastClickTime = currentClickTime;*/
                }
                break;
            }
            return false;
        });

        view.findViewById(R.id.close_float).setOnClickListener(v -> hideFloat());
        //开始停止按钮
        Button beginSearch = (Button) view.findViewById(R.id.begin_search);
        messageHandler.setButton(beginSearch);
        beginSearch.setOnClickListener(v -> {
            if (!isRunning) {
                isRunning = true;
                changeBtnStatus(R.drawable.stop);
                begin();
            } else {
                searchThread.interrupt();
                changeBtnStatus(R.drawable.begin);
                isRunning = false;
            }
        });
    }

    public void changeBtnStatus(int resId) {
        Message msg = new Message();
        msg.arg1 = 1;
        msg.arg2 = resId;
        messageHandler.sendMessage(msg);

    }

    private void begin() {
        searchThread = new SearchThread(context,this);
        view.findViewById(R.id.scrollView).setVisibility(View.GONE);//保留
        searchThread.start();
    }

    void showFloatWindow() {
        if (view == null) {
            initView();
            windowManager.addView(view, mParams);
        }
    }

    void hideFloat() {
        if (view != null) {
            windowManager.removeView(view);
            view = null;
        }
    }
}