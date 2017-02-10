package in.izzulmak.anote.room.listmode.listeners;

import android.widget.EditText;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import in.izzulmak.anote.R;
import in.izzulmak.anote.core.Algorithm;
import in.izzulmak.anote.room.listmode.ListObject;

/**
 * Created by Izzulmakin on 11/02/17.
 */
public class PasswordChangedRunnable implements Runnable {
    private String newString;
    /**
     * constructor
     * @param s the string data to be used as new password
     */
    public PasswordChangedRunnable(String s) {
        newString = s;
    }
    @Override
    public void run() {
        String password = newString;
        Algorithm.setKey(password);
        ListObject listItem = ListObject.firstObject;
        while (listItem!=null) {
            EditText et_item = (EditText) listItem.theView;
            if (listItem.encryptedData!=null) {
                try {
                    et_item.setText(Algorithm.decode(listItem.encryptedData).trim());
                    if (listItem.encryptedJudge==null | (!Algorithm.decode(listItem.encryptedJudge).trim().equals("judge")))
                        et_item.setBackgroundColor(0x0F440000);
                    else
                        et_item.setBackgroundColor(0x22005500);
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                }
            }
            listItem = listItem.next;
        }
    }
}
