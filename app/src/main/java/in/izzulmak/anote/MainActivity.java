package in.izzulmak.anote;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.AvoidXfermode;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.MalformedURLException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import in.izzulmak.anote.core.Algorithm;
import in.izzulmak.anote.room.listmode.ListModeModel;
import in.izzulmak.anote.room.main.CustomButton;
import in.izzulmak.anote.room.main.MainPlaceHolderFragment;
import in.izzulmak.anote.core.ModelMain;
import in.izzulmak.anote.room.console.ConsoleActivity;
import in.izzulmak.anote.room.listmode.ListModeActivity;
import in.izzulmak.anote.room.main.NavigationDrawerFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    /** Section position in navigationDrawerList (home) */
    public final static int SECTION_HOME = 0;
    /** Section position in navigationDrawerList (list) */
    public final static int SECTION_LIST = SECTION_HOME+1;
    /** Section position in navigationDrawerList (console) */
    public final static int SECTION_CONSOLE = SECTION_LIST+1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        ModelMain.initDatabase(this);

        //Initialize DB
        ModelMain.provisioningAddIfNotExist("custombutton", "urlaction buttontext", "urlaction", false, true);
        ModelMain.provisioningApply();

        //ugly, but it is Java. loadCustomButtons(); is in MainPlaceHolderFragment.onCreateView


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        if (position==SECTION_CONSOLE) {
            gotoConsole(null);
            return;
        }
        else if (position==SECTION_LIST)
        {
            gotoListMode(null);
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, MainPlaceHolderFragment.newInstance(position + 1, this))
                .commit();

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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


    public void addCutomButton(View view) {
        loadCustomButtons();
        AlertDialog.Builder inputPopupBuilder = new AlertDialog.Builder(this);
        final EditText et_customButtonText = new EditText(this);
        et_customButtonText.setHint("name your button");
        inputPopupBuilder.setView(et_customButtonText);
        inputPopupBuilder.setCancelable(true);
        inputPopupBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        EditText et_main_url = (EditText) findViewById(R.id.et_main_url);
                        addCustomButton(
                                et_main_url.getText().toString(),
                                et_customButtonText.getText().toString()
                        );
                        saveCustomButton(
                                et_main_url.getText().toString(),
                                et_customButtonText.getText().toString()
                        );
                    }
                });
        AlertDialog inputPopup = inputPopupBuilder.create();
        inputPopup.show();

    }

    public void addCustomButton(String urlaction, String customButtonText) {
        LinearLayout ll_main = null;
        ll_main = (LinearLayout) findViewById(R.id.ll_main);

        try {
            CustomButton newbt = new CustomButton(ll_main.getContext(), urlaction);
            newbt.setText(customButtonText);
            ll_main.addView(newbt);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //ll_main.addView();
    }

    public void loadCustomButtons(){
        Cursor c_buttons = ModelMain.getdb().rawQuery("select urlaction,buttontext from custombutton", null);
        while (c_buttons.moveToNext()) {
            addCustomButton(c_buttons.getString(0), c_buttons.getString(1));
        }
    }

    public void saveCustomButton(String urlaction, String customButtonText){
        ModelMain.getdb().beginTransaction();
        try {
            ContentValues v = new ContentValues();
            v.put("urlaction",urlaction);
            v.put("buttontext", customButtonText);
            ModelMain.getdb().insertOrThrow("custombutton", null, v);

            ModelMain.getdb().setTransactionSuccessful();
        } finally {
            ModelMain.getdb().endTransaction();
        }
    }



    /**
     * goto Console activity
     * @param view RFU compatible with onClick
     */
    public void gotoConsole(View view) {
        Intent mi = new Intent(MainActivity.this, ConsoleActivity.class);
        MainActivity.this.startActivity(mi);
    }

    /**
     * Goto ListMode activity
     * @param view RFU, compatible with onClick
     */
    public void gotoListMode(View view) {
        MainActivity.this.startActivity(new Intent(MainActivity.this, ListModeActivity.class));
    }
}
