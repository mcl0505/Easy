package com.mh55.httplibrary.encrypt;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    public MD5Utils() {
    }

    public static String MD5(String sourceStr) {
        String result = "";

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte[] b = md.digest();
            StringBuffer buf = new StringBuffer("");

            for(int offset = 0; offset < b.length; ++offset) {
                int i = b[offset];
                if (i < 0) {
                    i += 256;
                }

                if (i < 16) {
                    buf.append("0");
                }

                buf.append(Integer.toHexString(i));
            }

            result = buf.toString();
        } catch (NoSuchAlgorithmException var7) {
            System.out.println(var7);
        }

        return result;
    }

    public static String md5(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        } else {
            MessageDigest md5 = null;

            try {
                md5 = MessageDigest.getInstance("MD5");
                byte[] bytes = md5.digest(str.getBytes());
                String result = "";
                byte[] var4 = bytes;
                int var5 = bytes.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    byte b = var4[var6];
                    String temp = Integer.toHexString(b & 255);
                    if (temp.length() == 1) {
                        temp = "0" + temp;
                    }

                    result = result + temp;
                }

                return result;
            } catch (NoSuchAlgorithmException var9) {
                var9.printStackTrace();
                return "";
            }
        }
    }

    public static String md5(File file) {
        if (file != null && file.isFile() && file.exists()) {
            FileInputStream in = null;
            String result = "";
            byte[] buffer = new byte[8192];

            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                in = new FileInputStream(file);

                int len;
                while((len = in.read(buffer)) != -1) {
                    md5.update(buffer, 0, len);
                }

                byte[] bytes = md5.digest();
                byte[] var7 = bytes;
                int var8 = bytes.length;

                for(int var9 = 0; var9 < var8; ++var9) {
                    byte b = var7[var9];
                    String temp = Integer.toHexString(b & 255);
                    if (temp.length() == 1) {
                        temp = "0" + temp;
                    }

                    result = result + temp;
                }
            } catch (Exception var20) {
                var20.printStackTrace();
            } finally {
                if (null != in) {
                    try {
                        in.close();
                    } catch (IOException var19) {
                        var19.printStackTrace();
                    }
                }

            }

            return result;
        } else {
            return "";
        }
    }

    public static String md5(String str, int times) {
        if (TextUtils.isEmpty(str)) {
            return "";
        } else {
            String md5 = md5(str);

            for(int i = 0; i < times - 1; ++i) {
                md5 = md5(md5);
            }

            return md5(md5);
        }
    }

    public static String md5(String str, String key) {
        if (TextUtils.isEmpty(str)) {
            return "";
        } else {
            MessageDigest md5 = null;

            try {
                md5 = MessageDigest.getInstance("MD5");
                byte[] bytes = md5.digest((str + key).getBytes());
                String result = "";
                byte[] var5 = bytes;
                int var6 = bytes.length;

                for(int var7 = 0; var7 < var6; ++var7) {
                    byte b = var5[var7];
                    String temp = Integer.toHexString(b & 255);
                    if (temp.length() == 1) {
                        temp = "0" + temp;
                    }

                    result = result + temp;
                }

                return result;
            } catch (NoSuchAlgorithmException var10) {
                var10.printStackTrace();
                return "";
            }
        }
    }
}

