package in.izzulmak.anote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import in.izzulmak.anote.core.Algorithm;
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

    byte [] tempbuffer;
    public void encrypt(View view) {
        String data = ((EditText) findViewById(R.id.et_main_data)).
                getText().toString();


        try {
            tempbuffer = Algorithm.encode(data);
            String display = "";
            for (byte b:tempbuffer) {
                display = display+String.format("%2x ",(int)(b&0xff));
            }
            ((TextView) findViewById(R.id.tv_main_print)).setText(display);

        } catch (IllegalBlockSizeException e) {
            ((TextView) findViewById(R.id.tv_main_print)).setText("failure");
            e.printStackTrace();
        }
    }

    public void decrypt(View view) {
        String password = ((EditText) findViewById(R.id.et_main_password)).
                getText().toString();
        ((TextView) findViewById(R.id.tv_main_print2)).setText("faipelure");
        String display = null;
        try {
            display = Algorithm.decode(tempbuffer);
            ((TextView) findViewById(R.id.tv_main_print2)).setText(display);
        }
        catch (IllegalBlockSizeException e) {e.printStackTrace();}
        catch (BadPaddingException e) {e.printStackTrace();}


    }

    public void setKey(View view) {
        String password = ((EditText) findViewById(R.id.et_main_password)).
                getText().toString();
        Algorithm.setKey(password);
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
