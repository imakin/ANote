package in.izzulmak.anote.algorithm;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
/**
 * Created by Izzulmakin on 06/08/16.
 * simple. TODO: update it to more secure algorithm with pbkdf-ed key
 */
public class Coding {
    /** the key object */
    private static SecretKeySpec key;

    /**
     * set the default key for process
     * @param password password to generate the key
     */
    public static void setKey(String password) {
        byte[] bytekey = new byte[0];
        MessageDigest sha = null;
        try {
            bytekey = (password).getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            bytekey = sha.digest(bytekey);
            bytekey = Arrays.copyOf(bytekey, 16); // use only first 128 bit
            Coding.key = new SecretKeySpec(bytekey, "AES");
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
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] data = input.getBytes("UTF-8");
            byte[] result = c.doFinal(data);
            return result;
        }
        catch (NoSuchAlgorithmException e) {e.printStackTrace();} //by getInstance
        catch (NoSuchPaddingException e) {e.printStackTrace();} //getInstance
        catch (InvalidKeyException e) {e.printStackTrace();}  //init
        catch (UnsupportedEncodingException e) {e.printStackTrace();} //getBytes
        catch (BadPaddingException e) {e.printStackTrace();}//doFinal (only thrown in decrypting)
        //catch (IllegalBlockSizeException e) {e.printStackTrace();}//doFinal, if failed to process
        return null;
    }

    /**
     *
     * @param data
     * @return
     */
    public static String decode(byte[] data) {
        try {
            Cipher c = Cipher.getInstance("AES/CBC/NoPadding");
            c.init(Cipher.DECRYPT_MODE,key);
            byte decrypted[] = c.doFinal(data);
            return new String(decrypted, "UTF-8");
        }
        catch (NoSuchAlgorithmException e) {e.printStackTrace();}//getInstance
        catch (NoSuchPaddingException e) {e.printStackTrace();}//getInstance
        catch (InvalidKeyException e) {e.printStackTrace();}//init
        catch (BadPaddingException e) {e.printStackTrace();}//doFinal
        catch (IllegalBlockSizeException e) {e.printStackTrace();}//doFinal
        catch (UnsupportedEncodingException e) {e.printStackTrace();}//new String
        return null;
    }
}
