package in.izzulmak.anote.room.console;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import in.izzulmak.anote.R;
import in.izzulmak.anote.core.ModelMain;

public class ConsoleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_console);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_console, menu);
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

    public void dumpConsoleQuery(View view) {
        String sqlitequery = ((EditText) findViewById(R.id.et_Console)).getText().toString();
        SQLiteDatabase db = ModelMain.getdb();

        Cursor dbv;
        try {
            dbv = db.rawQuery(sqlitequery, null);
            String hasil = "";
            while (dbv.moveToNext())
            {
                for (int i=0; i<dbv.getColumnCount(); i++)
                {
                    hasil += dbv.getString(i)+", ";
                }
                hasil += "\n";
            }

            ((TextView) findViewById(R.id.tv_Console)).setText(hasil);
            dbv.close();

        }
        catch (Exception e) {
            ((TextView) findViewById(R.id.tv_Console)).setText(e.toString());
        }

        return;
    }
}
