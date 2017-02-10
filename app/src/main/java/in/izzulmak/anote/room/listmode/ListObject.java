package in.izzulmak.anote.room.listmode;

import android.view.View;

/**
 * Created by Izzulmakin on 12/09/16.
 * Linked list for string data.
 * data member is public because to always use getter when there is setter is weird idea, and Interface not suppose to be obligatory
 * TODO: reposition method
 */
public class ListObject {
    /** auto filled identifier */
    public static int id;
    /** store the first object in the linkedList */
    public static ListObject firstObject = null;
    /** encrypted data */
    public byte[] encryptedData;
    /** raw data */
    public String data;
    /** encrypted judge */
    public byte[] encryptedJudge;
    /** raw judge */
    public final static String judge = "judge";
    /** view linked to this object */
    public View theView;

    /** next linkedlist ListObject */
    public ListObject next = null;
    /** previous linkedlist ListObject */
    public ListObject prev = null;

    /**
     * create new ListObject and register it to the tail of linked list
     * @param correspondingEditText the corresponding theView view linked to this data. this view setTag will be set to this object
     * @return the object reference to the newly created ListObject
     */
    public static ListObject add(View correspondingEditText) {
        if (firstObject==null) {
            firstObject = new ListObject();
            firstObject.theView = correspondingEditText;
            firstObject.theView.setTag(firstObject);//store ListObject to view's tag
            firstObject.id = 0;
            return firstObject;
        }
        else
            return firstObject.addNext(correspondingEditText);
    }

    /**
     * create new ListObject and register it to the end of linked list. <br/>
     * To add new object use ListObject.add(View) instead.
     * @param correspondingEditText the corresponding theView view linked to this data. this view setTag will be set to this object
     * @return the object reference to the newly created ListObject
     */
    public ListObject addNext(View correspondingEditText) {
        if (next!=null)
            return next.addNext(correspondingEditText);
        else {
            next = new ListObject();
            next.theView = correspondingEditText;
            next.theView.setTag(next);//store ListObject to view's tag
            next.prev = this;
            next.id = id+1;
            return next;
        }
    }

    /**
     * remove this current ListObject from linked list
     */
    public void remove() {
        if (prev!=null)
            prev.next = next;
        else
            firstObject = next;

        if (next!=null)
            next.prev = prev;

        //let GC manage the rest
    }

    /** clear all linked list objects */
    public static void clearAll() {
        if (firstObject==null)
            return;
        ListObject t = firstObject;
        while (t.next!=null) {
            t = t.next;
        }
        while (t.prev!=null) {
            t = t.prev;
            t.next = null;
        }
        firstObject = null;
        t = null;
        //let GC manage the rest
    }
}
