package in.izzulmak.anote.room.listmode.listeners;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import in.izzulmak.anote.core.Algorithm;
import in.izzulmak.anote.room.listmode.ListObject;

/**
 * Created by Izzulmakin on 12/09/16.
 */
public class ItemChangedRunnable implements Runnable {
    private String newString;
    private ListObject listObject;
    /**
     * constructor
     * @param s the string data to be encrypted
     */
    public ItemChangedRunnable(String s, ListObject listObject) {
        newString = s;
        this.listObject = listObject;
    }
    @Override
    public void run() {
        try {
            if (listObject.encryptedJudge==null ||
                    Algorithm.decode(listObject.encryptedJudge).trim().equals("judge")) {
                listObject.encryptedData = Algorithm.encode(newString);
                listObject.encryptedJudge = Algorithm.encode("judge");
            }
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
    }
}
