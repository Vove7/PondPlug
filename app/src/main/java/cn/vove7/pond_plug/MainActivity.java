package cn.vove7.pond_plug;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import cn.vove7.pond_plug.utils.SimulateScreen;

import com.ant.liao.GifView;


public class MainActivity extends AppCompatActivity {

    private static final int ALERT_WINDOW_PERMISSION_CODE = 100;
    private static FloatWindow floatWindow;
    private TextView speedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((GifView) findViewById(R.id.gifView)).setGifImage(R.drawable.gif);

        Button btn = (Button) findViewById(R.id.show_float_btn);
        btn.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else if (Build.VERSION.SDK_INT > 22) {
                sdk23Permission();
            } else {
                openFloatWindow();
            }

        });

        //震动控制
        CheckBox checkBox = (CheckBox) findViewById(R.id.isOpenVibrator);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> FloatWindow.setOpenVibrator(isChecked));

        //速度控制
        speedText = (TextView) findViewById(R.id.speed_text);
        SeekBar speedSeekBar = (SeekBar) findViewById(R.id.seekBar);
        speedSeekBar.setProgress(200);
        speedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SimulateScreen.setSpeed(450 - progress);
                speedText.setText(Integer.toString(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        findViewById(R.id.remove_float).setOnClickListener(v -> {
            if (floatWindow != null)
                floatWindow.hideFloat();
        });
        findViewById(R.id.copy_num).setOnClickListener(v->{
           //获取剪贴板管理器：
           ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
           ClipData mClipData = ClipData.newPlainText("Label", "209304025");
            // 将ClipData内容放到系统剪贴板里。
           cm.setPrimaryClip(mClipData);
           Toast.makeText(this,R.string.copy_completed,Toast.LENGTH_SHORT).show();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sdk23Permission();
                } else {
                    Toast.makeText(this, "deny permission", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void openFloatWindow() {
        if (floatWindow == null) {
            synchronized (FloatWindow.class) {
                floatWindow = new FloatWindow(this, null);
            }
        }
        floatWindow.showFloatWindow();
    }

    /**
     * @description 安卓6.0下权限处理
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void sdk23Permission() {
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(MainActivity.this, "当前无权限使用悬浮窗，请授权！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, ALERT_WINDOW_PERMISSION_CODE);
        } else {
            openFloatWindow();
        }
    }

    /**
     * 用户返回
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ALERT_WINDOW_PERMISSION_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(MainActivity.this, "权限授予失败，无法开启悬浮窗", Toast.LENGTH_SHORT).show();
            } else {
                openFloatWindow();
            }

        }
    }
}