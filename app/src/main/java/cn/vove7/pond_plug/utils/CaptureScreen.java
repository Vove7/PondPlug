package cn.vove7.pond_plug.utils;

import android.content.Context;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;


/**
 * Created by Vove on 2017/5/4.
 * 截屏
 */

public class CaptureScreen {
   private Handler handler;
   private Context context;
//   private static int resultCode = 1;
   private static final String Pic_Path = Environment.getExternalStorageDirectory().getPath() + "/pond.jpg";

   public CaptureScreen(Context context) {
      this.context = context;
   }

   @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
   public boolean capWithCast() {
      MediaProjectionManager projectionManager = (MediaProjectionManager) context.getSystemService(Context.MEDIA_PROJECTION_SERVICE);


      return true;
   }

   public boolean captureScreen_su() {
      Message msg = new Message();
//      Log.d("Pic_Path", Pic_Path);
      String pic_Path = "sdcard/pond.jpg";
      if (RootCmd.rootCmd("screencap -p " + pic_Path)) {
         return true;
      } else {
         return false;
      }
   }
}

/*
//本地截屏
   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US);
   String fName = "/sdcard/" + sdf.format(new Date()) + ".png";

   View view = v.getRootView();
    view.setDrawingCacheEnabled(true);
    view.buildDrawingCache();
    Bitmap bitmap = view.getDrawingCache();
    if (bitmap != null) {
    System.out.println("bitmap got!");
    try {
    FileOutputStream out = new FileOutputStream(fName);
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

    Toast.makeText(context,"file " + fName + "output done.",Toast.LENGTH_SHORT).show();
    } catch(Exception e) {
    e.printStackTrace();
    }

    } else {
    Toast.makeText(context,"bitmap is NULL!",Toast.LENGTH_SHORT).show();
*/
