package com.mh55.httplibrary.encrypt

import android.util.Base64
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

class RSAUtils {

    /**
     * RSA公钥加密
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    fun encrypt(str: String,publicKey:String): String {

        var outStr = ""
        try {
            // base64编码的公钥
            val decoded: ByteArray = Base64.decode(publicKey, Base64.DEFAULT)
            val pubKey = KeyFactory.getInstance("RSA")
                .generatePublic(X509EncodedKeySpec(decoded)) as RSAPublicKey
            // RSA加密
            val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            cipher.init(Cipher.ENCRYPT_MODE, pubKey)
            outStr = Base64.encodeToString(
                cipher.doFinal(str.toByteArray(charset("UTF-8"))),
                Base64.DEFAULT
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return outStr
    }
}