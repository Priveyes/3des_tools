package cn.bankdev;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Desfunc
{
  private static final String Algorithm = "DESede";

  public static byte[] encryptMode(byte[] keybyte, byte[] src)
  {
    try
    {
      SecretKey deskey = new SecretKeySpec(keybyte, "DESede");

      Cipher c1 = Cipher.getInstance("DESede");
      c1.init(1, deskey);
      return c1.doFinal(src);
    }
    catch (NoSuchAlgorithmException e1) {
      e1.printStackTrace();
    } catch (NoSuchPaddingException e2) {
      e2.printStackTrace();
    } catch (Exception e3) {
      e3.printStackTrace();
    }
    return null;
  }

  public static byte[] decryptMode(byte[] keybyte, byte[] src)
  {
    try
    {
      SecretKey deskey = new SecretKeySpec(keybyte, "DESede");

      Cipher c1 = Cipher.getInstance("DESede");
      c1.init(2, deskey);
      return c1.doFinal(src);
    }
    catch (NoSuchAlgorithmException e1) {
      e1.printStackTrace();
    } catch (NoSuchPaddingException e2) {
      e2.printStackTrace();
    } catch (Exception e3) {
      e3.printStackTrace();
    }
    return null;
  }

  public static byte[] build3DesKey(String keyStr) throws UnsupportedEncodingException
  {
    byte[] key = new byte[24];

    byte[] temp = keyStr.getBytes("UTF-8");

    if (key.length > temp.length)
    {
      System.arraycopy(temp, 0, key, 0, temp.length);
    }
    else {
      System.arraycopy(temp, 0, key, 0, key.length);
    }
    return key;
  }

  public static String byte2Hex(byte[] b)
  {
    String hs = "";
    String stmp = "";
    for (int n = 0; n < b.length; n++) {
      stmp = Integer.toHexString(b[n] & 0xFF);
      if (stmp.length() == 1)
        hs = hs + "0" + stmp;
      else {
        hs = hs + stmp;
      }

    }

    return hs.toUpperCase();
  }

  public static void main(String[] args)
  {
    byte[] keyBytes = null;
    try {
      keyBytes = build3DesKey("abcd1234efgh6789hijkmyth");
    }
    catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
    String szSrc = "1";
    System.out.println("加密前的字符串:" + szSrc);
    byte[] encoded = encryptMode(keyBytes, szSrc.getBytes());

    System.out.println("加密后的字符串:" + byte2Hex(encoded));
    byte[] srcBytes = decryptMode(keyBytes, encoded);
    System.out.println("解密后的字符串:" + new String(srcBytes));
  }
}