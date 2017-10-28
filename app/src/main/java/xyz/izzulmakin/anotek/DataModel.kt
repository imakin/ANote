package xyz.izzulmakin.anotek

import android.content.Context
import android.database.Cursor
import android.support.v7.widget.AppCompatEditText
import android.widget.LinearLayout
import xyz.izzulmakin.anotek.util.ModelMain

/**
 * Created by Izzulmakin on 28/10/17.
 * @param context: activity context
 * @param key_edittext: refer to edittext read as key currently used
 */
class DataModel(context: Context, key_edittext: AppCompatEditText) : ModelMain() {
    val context: Context
    val key_edittext:AppCompatEditText

    companion object {
        val tablename: String = "data"
    }

    init {
        this.context = context
        this.key_edittext = key_edittext
        initDatabase(context)
        provisioningAdd(tablename, "id data_encrypted terbaca_encrypted", "id", false, true)
        provisioningApply()
    }


    /**
     * @param ll_data: Data object instantiated will be put on ll_data
     * data.putToLinearLayout(ll_data)
     */
    fun load_all(ll_datas: LinearLayout) {
        var cursor = getdb().query(
                tablename,
                arrayOf("id", "data_encrypted", "terbaca_encrypted"),
                null,
                null,null,null,null
        ) as Cursor

        while (cursor.moveToNext()) {
            var db_id = cursor.getString(cursor.getColumnIndex("id"))
            var data_encrypted = cursor.getBlob(cursor.getColumnIndex("data_encrypted")) as ByteArray
            var terbaca_encrypted = cursor.getBlob(cursor.getColumnIndex("terbaca_encrypted")) as ByteArray
            var data:Data = Data(context, key_edittext, getdb(),db_id)
            data.data_encrypted = data_encrypted
            data.terbaca_encrypted= terbaca_encrypted
            data.putToLinearLayout(ll_datas)

        }
    }

}