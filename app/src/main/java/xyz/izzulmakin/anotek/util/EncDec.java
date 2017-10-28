package xyz.izzulmakin.anotek.util;
/**
 * Created by Izzulmakin on 27/10/17.
 */

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
/**
 * Created by Izzulmakin on 06/08/16.
 * simple. TODO: update it to more secure algorithm with pbkdf-ed key
 */
public class EncDec{
    /** the key object */
    private static SecretKeySpec key;
    private static byte [] initialVector = {0,8,2,0,4,15,6,7,8,9,12,11,1,1,14,15};

    /**
     * set the default key for process
     * @param password password to generate the key
     */
    public static void setKey(String password) {
        byte[] bytekey;
        MessageDigest sha = null;
        try {
            bytekey = (password).getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            bytekey = sha.digest(bytekey);
            bytekey = Arrays.copyOf(bytekey, 16); // use only first 128 bit
            EncDec.key = new SecretKeySpec(bytekey, "AES");
        }
        catch (UnsupportedEncodingException e) {e.printStackTrace();}
        catch (NoSuchAlgorithmException e) {e.printStackTrace();}
    }

    /**
     * encode with the default key
     * @param input input string to encode
     * @return encoded data or null if failed
     */
    public static byte[] encode(String input) throws IllegalBlockSizeException {
        try {
            if (key==null)
                setKey("default");
            Cipher c = Cipher.getInstance("AES/CBC/NoPadding");
            c.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(initialVector));
            if (input.length()%16!=0) {
                int n = input.length() + 16 - (input.length()%16);
                input = String.format("%1$-" + n + "s", input);
            }
            byte[] data = input.getBytes("UTF-8");
            byte[] result = c.doFinal(data);
            return result;
        }
        catch (NoSuchAlgorithmException e) {e.printStackTrace();} //by getInstance
        catch (NoSuchPaddingException e) {e.printStackTrace();} //getInstance
        catch (InvalidKeyException e) {e.printStackTrace();}  //init
        catch (UnsupportedEncodingException e) {e.printStackTrace();} //getBytes
        catch (BadPaddingException e) {e.printStackTrace();}//doFinal (only thrown in decrypting)
        catch (InvalidAlgorithmParameterException e) {e.printStackTrace();}//init (IV)
        //catch (IllegalBlockSizeException e) {e.printStackTrace();}//doFinal, if failed to process
        return null;
    }

    /**
     *
     * @param data
     * @return
     */
    public static String decode(byte[] data) throws IllegalBlockSizeException, BadPaddingException{
        try {
            Cipher c = Cipher.getInstance("AES/CBC/NoPadding");
            c.init(Cipher.DECRYPT_MODE,key, new IvParameterSpec(initialVector));
            byte decrypted[] = c.doFinal(data);
            return new String(decrypted, "UTF-8").trim();
        }
        catch (NoSuchAlgorithmException e) {e.printStackTrace();}//getInstance
        catch (NoSuchPaddingException e) {e.printStackTrace();}//getInstance
        catch (InvalidKeyException e) {e.printStackTrace();}//init
//        catch (BadPaddingException e) {e.printStackTrace();}//doFinal
//        catch (IllegalBlockSizeException e) {e.printStackTrace();}//doFinal
        catch (UnsupportedEncodingException e) {e.printStackTrace();}//new String
        catch (InvalidAlgorithmParameterException e) {e.printStackTrace();}
        return null;
    }
}
