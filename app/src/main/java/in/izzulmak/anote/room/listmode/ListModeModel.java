package in.izzulmak.anote.room.listmode;


import android.content.ContentValues;
import android.database.Cursor;

import in.izzulmak.anote.core.ModelMain;

/**
 * Created by Izzulmakin on 12/09/16.
 * Model and action (common to be called Controller) specific to the room
 */
public class ListModeModel extends ModelMain {
    private final static int INDEX_ID = 0;
    private final static int INDEX_ENCRYPTEDDATA = 1;
    public static ListModeActivity listModeActivity;

    public static final String TABLE_NAME = "listobject";
    public static final String[] FIELDS = {"id", "encrypteddata"};

    public static void setListModeActivity(ListModeActivity l){
        listModeActivity = l;
    }

    /** initialize model
     * @param act must register ListModeActivity object
     */
    public static void init(ListModeActivity act) {
        listModeActivity = act;
        provisioningAddIfNotExist(TABLE_NAME, FIELDS[0]+" "+FIELDS[1], FIELDS[0], false, true);
        provisioningApply();
    }

    public static void load() {
        ListObject.clearAll();
        Cursor dbc_listObject = getdb().rawQuery("SELECT * from "+TABLE_NAME, null);
        while (dbc_listObject.moveToNext()) {
            listModeActivity.listModeAdd(null);
            ListObject item = (ListObject) listModeActivity.et_lastItem.getTag();
            item.encryptedData = dbc_listObject.getBlob(INDEX_ENCRYPTEDDATA);
        }
        listModeActivity.listModeSetKey(null);
    }

    public static void save() {
        ListObject t = ListObject.firstObject;
        getdb().beginTransaction();
        try {
            getdb().delete(TABLE_NAME, null, null);
            while (t != null) {
                ContentValues v = new ContentValues();
                v.put(FIELDS[0], t.id);
                v.put(FIELDS[1], t.encryptedData);
                getdb().insert(TABLE_NAME, null, v);
                t = t.next;
            }
            getdb().setTransactionSuccessful();
        } finally {
            getdb().endTransaction();
        }

    }
}
