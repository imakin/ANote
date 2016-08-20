package in.izzulmak.anote.list;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import in.izzulmak.anote.MainActivity;
import in.izzulmak.anote.R;

/**
 * Created by Izzulmakin on 10/08/16.
 * TODO: RoomList uses new Activity but share the same drawer fragment instead!!
 */
public class RoomList {
    MainActivity mref;
    View container;
    static RoomList self=null;

    public RoomList(View containerref) {
        this.container = containerref;
    }

    /**
     * get singleton
     * @param containerref reference to this room container view
     * */
    public static RoomList getRoom(View containerref) {
        if (self==null) {
            self = new RoomList(containerref);
        }
        return self;
    }

    /**
     * redraw the room List
     */
    public void draw() {



        LinearLayout ll_list = (LinearLayout) container.findViewById(R.id.ll_list);
        ll_list.removeAllViews();
        for (int i=0; i<2; i++) {
            EditText et_temp = new EditText(ll_list.getContext());
            et_temp.setText("test");
            ll_list.addView(et_temp);
        }
    }

    /**
     * setup all event
     */
    public void setupListener(){
        // for password
        EditText password = (EditText) container.findViewById(R.id.et_list_pasword);
    }

}
