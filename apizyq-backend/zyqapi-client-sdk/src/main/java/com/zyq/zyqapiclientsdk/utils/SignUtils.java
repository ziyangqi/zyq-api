package com.zyq.zyqapiclientsdk.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
public class SignUtils {

    /**
     * MD5 获取签名的内容
     * @param body
     * @param secretKey
     * @return
     */
    public static String genSign(String body, String secretKey) {
        // 使用SHA256算法的Digester
        Digester md5 = new Digester(DigestAlgorithm.SHA256);
        // 构建签名的内容
        String context = body + "." + secretKey;
        return md5.digestHex(context);
    }
}

