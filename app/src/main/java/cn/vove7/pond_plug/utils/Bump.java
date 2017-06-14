package cn.vove7.pond_plug.utils;


/**
 * Created by Vove on 2017/4/16.
 *
 */

public class Bump {
   int coor[] = new int[2];
   char state;


   public Bump copyBump() {
      Bump newBump = new Bump();
         System.arraycopy(coor,0,newBump.coor,0,coor.length);

      newBump.state=state;
      return newBump;
   }
}