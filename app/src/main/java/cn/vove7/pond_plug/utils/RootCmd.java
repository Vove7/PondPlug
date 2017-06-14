package cn.vove7.pond_plug.utils;

import java.io.DataOutputStream;

/**
 * Created by Vove on 2017/5/13.
 */

public class RootCmd {
   public static boolean rootCmd(String cmd) {
      Process process = null;
      DataOutputStream os = null;
      try {
         process=Runtime.getRuntime().exec("su");
         os = new DataOutputStream(process.getOutputStream());
         os.writeBytes(cmd + "\n");
         os.writeBytes("exit\n");
         os.flush();
         process.waitFor();
         int result = process.exitValue();

         //失败1 成功0
         return result == 0;
      } catch (Exception e) {
         return false;
      } finally {
         try {
            if (os != null) {
               os.close();
            }
            if (process != null)
               process.destroy();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }
}
