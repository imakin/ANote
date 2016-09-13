package in.izzulmak.anote.core;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import in.izzulmak.anote.MainActivity;

/**
 * Created by izzulmakin on 20/08/16.
 */
public class ModelMain {
    /** public static SQLiteDatabase object */
    private static SQLiteDatabase dbmain;
    private static MainActivity mref;
    /**
     * getter for the application used database
     * @return SQLiteDatabase reference to the application database
     */
    public static SQLiteDatabase getdb() {
        return dbmain;
    }
    /**
     * initialize database, this will open or create database, and get the tables ready
     * @param mref MainActivity reference
     */
    public static void initDatabase(MainActivity mref) {
        dbmain = mref.openOrCreateDatabase("anoteappdb.sqlite", mref.MODE_PRIVATE, null);
        ModelMain.mref = mref;

        //initialize table if not ready
        Cursor dbc_provisioning = dbmain.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name='provisioning';",null);
        if (dbc_provisioning.getCount()==0) {
            /** provisioning is table containing how the database should be, what tables should be exist or deleted
             * tablename is the tablename string
             * fields is field for the tablename, values separated by whitespace
             * unique_index is the field name to be used as unique index, empty string for none
             * is_created value is 0 or 1, referring the status of the tablename
             * should_be_created value is 0 or 1, referring the status of the tablename
             * */
            dbmain.execSQL(
                    "CREATE TABLE IF NOT EXISTS " +
                            "provisioning(tablename, fields, unique_index, is_created, should_be_created)"
            );
            dbmain.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS tablename ON provisioning (tablename)");
            provisioningAddIfNotExist("list", "id text status", "id", false, true);
            provisioningApply();
        }
        dbc_provisioning.close();
    }

    /**
     * if tablename never registered in provisioning before,
     * add table to the database through provisioning, applied in the next provisioingApply call.
     * if tablename aleredy registered before, ignore this command.
     * To update provisioning use provisioningAdd instead
     * @see ModelMain.provisioningApply(MainActivity mref)
     * @throws NullPointerException if dbmain is not initialized yet
     * @param tablename the tablename string
     * @param fields the fields in tablename, fields separated by one whitespace
     * @param unique_index the field name used as unique index, empty string for none
     * @param is_created the status of the tablename whether it has been created or not
     * @param should_be_created the status of the tablename whether it should be created or not
     * @return whether provisioning added or not, return true if added (tablename never registered before)
     */
    public static boolean provisioningAddIfNotExist(
            String tablename,
            String fields,
            String unique_index,
            boolean is_created,
            boolean should_be_created
    ) {
        Cursor dbc_tablename = dbmain.rawQuery(
                "SELECT tablename FROM provisioning WHERE tablename='"+tablename+"';",null);
        if (dbc_tablename.getCount()==0) {
            provisioningAdd(tablename, fields, unique_index, is_created, should_be_created);
            return true;
        }
        return  false;
    }

    /**
     * Add table to the database through provisioning, applied in the next provisioingApply call.
     * if the same tablename found, replace it
     * @see ModelMain.provisioningApply(MainActivity mref)
     * @throws NullPointerException if dbmain is not initialized yet
     * @param tablename the tablename string
     * @param fields the fields in tablename, fields separated by one whitespace
     * @param unique_index the field name used as unique index, empty string for none
     * @param is_created the status of the tablename whether it has been created or not
     * @param should_be_created the status of the tablename whether it should be created or not
     */
    public static void provisioningAdd(
            String tablename,
            String fields,
            String unique_index,
            boolean is_created,
            boolean should_be_created
    ) {
        dbmain.execSQL(
                "INSERT OR REPLACE INTO provisioning " +
                        "VALUES(" +
                        " '" + tablename + "', " + //tablename
                        " '" + fields + "', " + //fields
                        " '" + unique_index + "', " + //index field
                        " " + (is_created ? "1" : "0") + ", " +//is created?
                        " " + (should_be_created ? "1" : "0") + " " +//should be created?
                        ")"
        );
        if (Config.DEBUG) {
            String oo = "INSERT OR REPLACE INTO provisioning " +
                    "VALUES(" +
                    " '" + tablename + "', " + //tablename
                    " '" + fields + "', " + //fields
                    " '" + unique_index + "', " + //index field
                    " " + (is_created ? "1" : "0") + ", " +//is created?
                    " " + (should_be_created ? "1" : "0") + " " +//should be created?
                    ")";
            Log.d("MAKIN", oo);
        }

    }

    /**
     * Update database based on provisioning table
     * @throws NullPointerException if dbmain is not initialized yet
     */
    public static void provisioningApply() {
        Cursor dbc_provisioning = dbmain.rawQuery(
                "SELECT tablename, fields, unique_index, is_created, should_be_created FROM provisioning",null);
        while (dbc_provisioning.moveToNext()) {
            boolean created = (dbc_provisioning.getInt(3)==1);
            boolean should_be = (dbc_provisioning.getInt(4)==1);
            String tablename = dbc_provisioning.getString(0);
            String fields = dbc_provisioning.getString(1).trim().replaceAll("\\s+", ", ");
            String unique_index = dbc_provisioning.getString(2);

            if (!created && should_be) {
                //not created but should be created
                dbmain.execSQL("CREATE TABLE IF NOT EXISTS " + tablename + "("+fields+")");
                if (!unique_index.equals("")) {
                    dbmain.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS "+unique_index+" ON "+tablename+" ("+unique_index+")");
                }
            }
            else if (created && !should_be) {
                //created but should not be created
                dbmain.execSQL("DROP TABLE IF EXISTS "+tablename);
            }
        }
    }
}
