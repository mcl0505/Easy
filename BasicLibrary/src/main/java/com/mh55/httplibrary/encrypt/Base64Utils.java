package com.mh55.httplibrary.encrypt;

public final class Base64Utils {
   private static final byte[] a = new byte[128];
   private static final char[] b = new char[64];

   public Base64Utils() {
   }

   private static boolean a(char var0) {
      return var0 == ' ' || var0 == '\r' || var0 == '\n' || var0 == '\t';
   }

   private static boolean b(char var0) {
      return var0 == '=';
   }

   private static boolean c(char var0) {
      return var0 < 128 && a[var0] != -1;
   }

   public static String encode(byte[] var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = var0.length * 8;
         if (var1 == 0) {
            return "";
         } else {
            int var2 = var1 % 24;
            int var3 = var1 / 24;
            int var4 = var2 != 0 ? var3 + 1 : var3;
            Object var5 = null;
            char[] var17 = new char[var4 * 4];
            boolean var6 = false;
            boolean var7 = false;
            boolean var8 = false;
            boolean var9 = false;
            boolean var10 = false;
            int var11 = 0;
            int var12 = 0;

            byte var14;
            byte var18;
            byte var19;
            byte var20;
            byte var21;
            for(int var13 = 0; var13 < var3; ++var13) {
               var20 = var0[var12++];
               var21 = var0[var12++];
               byte var22 = var0[var12++];
               var19 = (byte)(var21 & 15);
               var18 = (byte)(var20 & 3);
               var14 = (var20 & -128) == 0 ? (byte)(var20 >> 2) : (byte)(var20 >> 2 ^ 192);
               byte var15 = (var21 & -128) == 0 ? (byte)(var21 >> 4) : (byte)(var21 >> 4 ^ 240);
               byte var16 = (var22 & -128) == 0 ? (byte)(var22 >> 6) : (byte)(var22 >> 6 ^ 252);
               var17[var11++] = b[var14];
               var17[var11++] = b[var15 | var18 << 4];
               var17[var11++] = b[var19 << 2 | var16];
               var17[var11++] = b[var22 & 63];
            }

            byte var23;
            if (var2 == 8) {
               var20 = var0[var12];
               var18 = (byte)(var20 & 3);
               var23 = (var20 & -128) == 0 ? (byte)(var20 >> 2) : (byte)(var20 >> 2 ^ 192);
               var17[var11++] = b[var23];
               var17[var11++] = b[var18 << 4];
               var17[var11++] = '=';
               var17[var11++] = '=';
            } else if (var2 == 16) {
               var20 = var0[var12];
               var21 = var0[var12 + 1];
               var19 = (byte)(var21 & 15);
               var18 = (byte)(var20 & 3);
               var23 = (var20 & -128) == 0 ? (byte)(var20 >> 2) : (byte)(var20 >> 2 ^ 192);
               var14 = (var21 & -128) == 0 ? (byte)(var21 >> 4) : (byte)(var21 >> 4 ^ 240);
               var17[var11++] = b[var23];
               var17[var11++] = b[var14 | var18 << 4];
               var17[var11++] = b[var19 << 2];
               var17[var11++] = '=';
            }

            return new String(var17);
         }
      }
   }

   public static byte[] decode(String var0) {
      if (var0 == null) {
         return null;
      } else {
         char[] var1 = var0.toCharArray();
         int var2 = a(var1);
         if (var2 % 4 != 0) {
            return null;
         } else {
            int var3 = var2 / 4;
            if (var3 == 0) {
               return new byte[0];
            } else {
               Object var4 = null;
               boolean var5 = false;
               boolean var6 = false;
               boolean var7 = false;
               boolean var8 = false;
               boolean var9 = false;
               boolean var10 = false;
               boolean var11 = false;
               boolean var12 = false;
               int var13 = 0;
               int var14 = 0;
               int var15 = 0;

               byte[] var17;
               byte var18;
               byte var19;
               byte var20;
               byte var21;
               char var22;
               char var23;
               char var24;
               char var25;
               for(var17 = new byte[var3 * 3]; var13 < var3 - 1; ++var13) {
                  if (!c(var22 = var1[var15++]) || !c(var23 = var1[var15++]) || !c(var24 = var1[var15++]) || !c(var25 = var1[var15++])) {
                     return null;
                  }

                  var18 = a[var22];
                  var19 = a[var23];
                  var20 = a[var24];
                  var21 = a[var25];
                  var17[var14++] = (byte)(var18 << 2 | var19 >> 4);
                  var17[var14++] = (byte)((var19 & 15) << 4 | var20 >> 2 & 15);
                  var17[var14++] = (byte)(var20 << 6 | var21);
               }

               if (c(var22 = var1[var15++]) && c(var23 = var1[var15++])) {
                  var18 = a[var22];
                  var19 = a[var23];
                  var24 = var1[var15++];
                  var25 = var1[var15++];
                  if (c(var24) && c(var25)) {
                     var20 = a[var24];
                     var21 = a[var25];
                     var17[var14++] = (byte)(var18 << 2 | var19 >> 4);
                     var17[var14++] = (byte)((var19 & 15) << 4 | var20 >> 2 & 15);
                     var17[var14++] = (byte)(var20 << 6 | var21);
                     return var17;
                  } else {
                     byte[] var16;
                     if (b(var24) && b(var25)) {
                        if ((var19 & 15) != 0) {
                           return null;
                        } else {
                           var16 = new byte[var13 * 3 + 1];
                           System.arraycopy(var17, 0, var16, 0, var13 * 3);
                           var16[var14] = (byte)(var18 << 2 | var19 >> 4);
                           return var16;
                        }
                     } else if (!b(var24) && b(var25)) {
                        var20 = a[var24];
                        if ((var20 & 3) != 0) {
                           return null;
                        } else {
                           var16 = new byte[var13 * 3 + 2];
                           System.arraycopy(var17, 0, var16, 0, var13 * 3);
                           var16[var14++] = (byte)(var18 << 2 | var19 >> 4);
                           var16[var14] = (byte)((var19 & 15) << 4 | var20 >> 2 & 15);
                           return var16;
                        }
                     } else {
                        return null;
                     }
                  }
               } else {
                  return null;
               }
            }
         }
      }
   }

   private static int a(char[] var0) {
      if (var0 == null) {
         return 0;
      } else {
         int var1 = 0;
         int var2 = var0.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            if (!a(var0[var3])) {
               var0[var1++] = var0[var3];
            }
         }

         return var1;
      }
   }

   static {
      int var0;
      for(var0 = 0; var0 < 128; ++var0) {
         a[var0] = -1;
      }

      for(var0 = 90; var0 >= 65; --var0) {
         a[var0] = (byte)(var0 - 65);
      }

      for(var0 = 122; var0 >= 97; --var0) {
         a[var0] = (byte)(var0 - 97 + 26);
      }

      for(var0 = 57; var0 >= 48; --var0) {
         a[var0] = (byte)(var0 - 48 + 52);
      }

      a[43] = 62;
      a[47] = 63;

      for(var0 = 0; var0 <= 25; ++var0) {
         b[var0] = (char)(65 + var0);
      }

      var0 = 26;

      int var1;
      for(var1 = 0; var0 <= 51; ++var1) {
         b[var0] = (char)(97 + var1);
         ++var0;
      }

      var0 = 52;

      for(var1 = 0; var0 <= 61; ++var1) {
         b[var0] = (char)(48 + var1);
         ++var0;
      }

      b[62] = '+';
      b[63] = '/';
   }
}
