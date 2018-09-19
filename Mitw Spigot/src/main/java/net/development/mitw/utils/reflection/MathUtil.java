/*    */ package net.development.mitw.utils.reflection;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class MathUtil
/*    */ {
/*    */   public static int floor(double d1)
/*    */   {
/* 34 */     int i = (int)d1;
/* 35 */     return d1 >= i ? i : i - 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\msi\Downloads\HologramAPI_v1.6.2.jar!\de\inventivegames\hologram\reflection\MathUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */