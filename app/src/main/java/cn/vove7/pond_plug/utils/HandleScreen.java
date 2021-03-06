package cn.vove7.pond_plug.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;

import static cn.vove7.pond_plug.utils.Snode.N;


/**
 * Created by Vove on 2017/5/4.
 * 读取图像转换Snode
 */

public class HandleScreen {
   public static void scanPic(Snode startNode) {
      Bitmap img = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath() + "/pond.jpg");

      int matrixHeight = (int) (img.getHeight() * 0.5109);
      int matrixWidth = (int) (img.getWidth() * 0.9048);

      int maBeginX = (int) (img.getHeight() * 0.2242);
      int maBeginY = (int) (img.getWidth() * 0.0486);
//      int maEndHeight = (maBeginHeight + matrixHeight);
//      int maEndWidth = (maBeginWidth + matrixWidth);

      int bumpHeight = (matrixHeight / (2 * N));// 1/2宽度
      int bumpWidth = (matrixWidth / (2 * N));// 1/2高度

      //
      int[][] Matrix = new int[N][N];

      int bumpNum = 1;
      for (int i = 0; i < N; i++) {
         for (int j = 0; j < N; j++) {
            if (Matrix[i][j] == 0) {
               int pointX = maBeginY + (2 * j + 1) * bumpWidth;
               int pointY = maBeginX + (2 * i + 1) * bumpHeight;
               int pixel = img.getPixel(pointX, pointY);
               System.out.print(i * 6 + j + 1);

               switch (judgeBump(pixel)) {
                  //1->鱼块   2->(2-2)    3->(2|橙)  4->(蓝3)
                  case 1: {
                     Matrix[i][j] = 1;
                     Matrix[i][j + 1] = 1;
                     Bump bump = startNode.getBumpByIndex(0);

                     bump.state = 'h';
                     bump.coor[0] = i;
                     bump.coor[1] = j;

                  }
                  break;
                  case 2:
                     int nextPointY = maBeginX + (2 * i + 3) * bumpHeight;//下一区域
                     if (i + 1 < N && judgeSeam(img, pointX, pointY, nextPointY, 2)) {
                        //橙色纵向
                        Bump bump = startNode.getBumpByIndex(bumpNum);

                        bump.state = 's';
                        bump.coor[0] = i;
                        bump.coor[1] = j;
                        Matrix[i][j] = 1 + bumpNum;
                        Matrix[i + 1][j] = 1 + bumpNum++;

                     } else {
                        ////橙色横向
                        Bump bump = startNode.getBumpByIndex(bumpNum);

                        bump.state = 'h';
                        bump.coor[0] = i;
                        bump.coor[1] = j;

                        Matrix[i][j] = 1 + bumpNum;
                        Matrix[i][j + 1] = 1 + bumpNum++;
                     }
                     break;
                  case 4: {
                     //
                     nextPointY = maBeginX + (2 * i + 3) * bumpHeight;
                     Matrix[i][j] = 1 + bumpNum;
                     if (i + 2 < N && judgeSeam(img, pointX, pointY, nextPointY, 4)) {
                        //蓝色纵向
                        Bump bump = startNode.getBumpByIndex(bumpNum);

                        bump.state = 'S';
                        bump.coor[0] = i;
                        bump.coor[1] = j;

                        Matrix[i + 1][j] = 1 + bumpNum;
                        Matrix[i + 2][j] = 1 + bumpNum++;
                     } else {//蓝色横向
                        Bump bump = startNode.getBumpByIndex(bumpNum);

                        bump.state = 'H';
                        bump.coor[0] = i;
                        bump.coor[1] = j;
                        Matrix[i][j + 1] = 1 + bumpNum;
                        Matrix[i][j + 2] = 1 + bumpNum++;
                     }
                  }
                  break;
                  case 0: {
                     Matrix[i][j] = 0;
                  }
                  break;
               }
            }
         }
      }
      /*StringBuilder builder = new StringBuilder();
      for (int i = 0; i < N; i++) {
         for (int j = 0; j < N; j++) {
            if (Matrix[i][j] < 10)
               builder.append("0").append(Matrix[i][j]).append("\t");
            else
               builder.append(Matrix[i][j]).append("\t");
         }
         builder.append("\n");
      }
      Log.d("Matrix", builder.toString());*/

      startNode.setBnum(bumpNum);
      startNode.setS(Matrix);
   }

   //判断纵向分割
   private static boolean judgeSeam(Bitmap img, int x, int yBegin, int yEnd, int color) {
      int m;
      for (m = yBegin; m < yEnd; m += 3) {
         int pixel = img.getPixel(x, m);
         if (judgeBump(pixel) != color) {
            return false;//横
         }
      }
      return true;//竖
   }

   private final static int[][] color = new int[][]{
           {230, 70, 70, 1}, {254, 180, 51, 2}, {254, 177, 45, 2}, {10, 133, 220, 4}
   };

   private static int judgeBump(int pixel) {
      int rValue = Color.red(pixel);
      int gValue = Color.green(pixel);
      int bValue = Color.blue(pixel);

      //1->鱼块   2->(2-橙)    3->(2|橙)  4->(蓝3)
      for (int[] aColor : color) {
         //误差5
         if (Math.abs(rValue - aColor[0]) < 50&&
                 Math.abs(gValue - aColor[1]) < 50&&
                 Math.abs(bValue - aColor[2]) < 50) {
            return aColor[3];
         }
      }
      return 0;
   }
}
