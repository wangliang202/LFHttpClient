package com.example;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.client.ClientProtocolException;
import java.util.Comparator;

class SpellComparator implements Comparator<Object> {
    public int compare(Object o1, Object o2) {
        try{
            String s1 = new String(o1.toString().getBytes("GB2312"), "ISO-8859-1");
            String s2 = new String(o2.toString().getBytes("GB2312"), "ISO-8859-1");
            return s1.compareTo(s2);
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }
}

public class stAuth {

    public static final String id = "cc6cf13826d1493d9ee87c33d02af9bd";
    public static final String secret = "1ae8be1e3e4f44e1b13037e19dd08331";
    private static final String HASH_ALGORITHM = "HmacSHA256";

    static String api_key = "xxxx";
    static String api_secret = "xxxx";
    static String timestamp = Long.toString(System.currentTimeMillis());
    static String nonce = RandomStringUtils.randomAlphanumeric(16);

    public static String genOriString(String api_key){

        ArrayList<String> beforesort = new ArrayList<>();
        beforesort.add(api_key);
        beforesort.add(timestamp);
        beforesort.add(nonce);

        Collections.sort(beforesort, new SpellComparator());
        StringBuffer aftersort = new StringBuffer();
        for (int i = 0; i < beforesort.size(); i++) {
            aftersort.append(beforesort.get(i));
        }

        String OriString = aftersort.toString();
        return OriString;
    }

    public static String genEncryptString(String genOriString, String api_secret)throws SignatureException {
        try{
            Key sk = new SecretKeySpec(api_secret.getBytes(), HASH_ALGORITHM);
            Mac mac = Mac.getInstance(sk.getAlgorithm());
            mac.init(sk);
            final byte[] hmac = mac.doFinal(genOriString.getBytes());
            StringBuilder sb = new StringBuilder(hmac.length * 2);

            @SuppressWarnings("resource")
            Formatter formatter = new Formatter(sb);
            for (byte b : hmac) {
                formatter.format("%02x", b);
            }
            String EncryptedString = sb.toString();
            return EncryptedString;
        }catch (NoSuchAlgorithmException e1){
            throw new SignatureException("error building signature, no such algorithm in device "+ HASH_ALGORITHM);
        }catch (InvalidKeyException e){
            throw new SignatureException("error building signature, invalid key " + HASH_ALGORITHM);
        }
    }

    public static String genHeaderParam(String api_key, String api_secret) throws SignatureException{

        String GenOriString = genOriString(api_key);
        String EncryptedString = genEncryptString(GenOriString, api_secret);

        String HeaderParam = "key=" + api_key
                +",timestamp=" + timestamp
                +",nonce=" + nonce
                +",signature=" + EncryptedString;
        System.out.println(HeaderParam);
        return HeaderParam;
    }

    public static void main(String[] args) throws ClientProtocolException, IOException, SignatureException{
        genHeaderParam(id, secret);
    }
}