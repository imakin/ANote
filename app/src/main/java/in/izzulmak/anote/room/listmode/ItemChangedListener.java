package in.izzulmak.anote.room.listmode;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;


/**
 * Created by Izzulmakin on 12/09/16.
 */
public class ItemChangedListener implements TextWatcher {
    Handler handler = new Handler(Looper.getMainLooper() /*UI thread*/);
    Runnable workRunnable;
    ListObject listObject;
    /** constructor
     * @param listObject listObject related to the EditText which has this listener
     */
    public ItemChangedListener(ListObject listObject) {
        this.listObject = listObject;
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }
    @Override
    public void afterTextChanged(Editable editable) {
        handler.removeCallbacks(workRunnable);
        workRunnable = new ItemChangedRunnable(editable.toString(), listObject);
        handler.postDelayed(workRunnable, 500 /*delay*/);
    }
}
