/*    */ package net.development.mitw.utils.reflection;
/*    */ 
/*    */ import java.lang.reflect.Field;
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
/*    */ 
/*    */ public abstract class NMUClass
/*    */ {
/* 35 */   private static boolean initialized = false;
/*    */   public static Class<?> gnu_trove_map_TIntObjectMap;
/*    */   public static Class<?> gnu_trove_map_hash_TIntObjectHashMap;
/*    */   public static Class<?> gnu_trove_impl_hash_THash;
/*    */   public static Class<?> io_netty_channel_Channel;
/*    */   
/*    */   static
/*    */   {
/* 43 */     if (!initialized) {
/* 44 */       for (Field f : NMUClass.class.getDeclaredFields()) {
/* 45 */         if (f.getType().equals(Class.class)) {
/*    */           try {
/* 47 */             String name = f.getName().replace("_", ".");
/* 48 */             if (Reflection.getVersion().contains("1_8")) {
/* 49 */               f.set(null, Class.forName(name));
/*    */             } else {
/* 51 */               f.set(null, Class.forName("net.minecraft.util." + name));
/*    */             }
/*    */           } catch (Exception e) {
/* 54 */             e.printStackTrace();
/*    */           }
/*    */         }
/*    */       }
/* 58 */       initialized = true;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\msi\Downloads\HologramAPI_v1.6.2.jar!\de\inventivegames\hologram\reflection\NMUClass.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */