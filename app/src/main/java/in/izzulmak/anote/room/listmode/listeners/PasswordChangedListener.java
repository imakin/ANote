package in.izzulmak.anote.room.listmode.listeners;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import in.izzulmak.anote.room.listmode.ListObject;

/**
 * Created by Izzulmakin on 11/02/17.
 */
public class PasswordChangedListener implements TextWatcher {
    Handler handler = new Handler(Looper.getMainLooper() /*UI thread*/);
    Runnable workRunnable;
    /** constructor
     */
    public PasswordChangedListener() {
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }
    @Override
    public void afterTextChanged(Editable editable) {
        handler.removeCallbacks(workRunnable);//cancel unprocessed callback
        workRunnable = new PasswordChangedRunnable(editable.toString());
        handler.postDelayed(workRunnable, 500 /*delay*/);
    }
}
