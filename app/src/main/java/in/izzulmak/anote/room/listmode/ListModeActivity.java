package in.izzulmak.anote.room.listmode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import in.izzulmak.anote.R;
import in.izzulmak.anote.core.Algorithm;

public class ListModeActivity extends AppCompatActivity {

    ListObject listObject;
    EditText et_lastItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_mode);
        listObject = new ListObject();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_mode, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * set key in ListMode room
     * @param view view triggering the method
     */
    public void listModeSetKey(View view) {
        String password = ((EditText) findViewById(R.id.et_list_pasword)).
                getText().toString();
        Algorithm.setKey(password);
        ListObject listItem = ListObject.firstObject;
        while (listItem!=null) {
            EditText et_item = (EditText) listItem.theView;
            if (listItem.encryptedData!=null) {
                try {
                    et_item.setText(Algorithm.decode(listItem.encryptedData).trim());
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                }
            }
            listItem = listItem.next;
        }
    }

    /**
     * add new list
     * @param view view triggering the method,  wont be checked
     */
    public void listModeAdd(View view) {
        LinearLayout ll_list = (LinearLayout) findViewById(R.id.ll_list);

        final LinearLayout ll_line = new LinearLayout(ll_list.getContext());
        ll_line.setOrientation(LinearLayout.HORIZONTAL);

        //create
        et_lastItem = new EditText(ll_line.getContext());
        et_lastItem.setText("");
        et_lastItem.setWidth(ll_list.getMeasuredWidth() - 100);
        ll_line.addView(et_lastItem);

        ListObject item = ListObject.add(et_lastItem);

        Button bt_delete = new Button(ll_line.getContext());
        bt_delete.setTag((Object) et_lastItem);// the delete button tag will be linked to its edit text
        bt_delete.setText("x");
        bt_delete.setMaxWidth(100);
        ll_line.addView(bt_delete);
        ll_list.addView(ll_line);


        et_lastItem.addTextChangedListener(new ItemChangedListener(item));

        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListObject item = (ListObject) ((EditText) view.getTag()).getTag();
                LinearLayout container = (LinearLayout) item.theView.getParent();
                container.removeAllViews();
                container.setVisibility(View.GONE);
                item.remove();
            }
        });
    }
}
