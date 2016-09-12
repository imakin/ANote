package in.izzulmak.anote.room.listmode;

import javax.crypto.IllegalBlockSizeException;

import in.izzulmak.anote.algorithm.Coding;

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
            listObject.encryptedData = Coding.encode(newString);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }
}
