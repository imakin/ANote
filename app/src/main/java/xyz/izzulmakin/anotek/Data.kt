package xyz.izzulmakin.anotek

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.AppCompatEditText
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import xyz.izzulmakin.anotek.util.EncDec
import xyz.izzulmakin.anotek.util.Util

/**
 * Created by Izzulmakin on 27/10/17.
 * @param context: activity context
 * @param key_edittext: refer to edittext read as key currently used
 */
class Data(context: Context, key_edittext: AppCompatEditText, db_tosave: SQLiteDatabase, db_id: String?) {
    val tag:String = "Data"
    var ll_data: LinearLayout
    var ll_data_bt: LinearLayout
    var ll_data_et: LinearLayout

    var bt_data_encrypt: AppCompatButton
    var bt_data_decrypt: AppCompatButton
//    var bt_data_save: AppCompatButton
//    var bt_data_delete: AppCompatButton

    var et_data: AppCompatEditText
    var et_key: AppCompatEditText

    var data_encrypted: ByteArray
    //encrypt can only be done if currect key can read terbaca_encrypted as "terbaca"
    var terbaca_encrypted: ByteArray
    val db_tosave:SQLiteDatabase
    var db_id:String?
    init {
        this.db_tosave = db_tosave
        this.db_id = db_id

        ll_data = LinearLayout(context)
        ll_data.orientation = LinearLayout.VERTICAL
        ll_data_bt = LinearLayout(context)
        ll_data_bt.orientation = LinearLayout.HORIZONTAL
        ll_data_et = LinearLayout(context)
        ll_data_et.orientation = LinearLayout.HORIZONTAL
        bt_data_encrypt = AppCompatButton(context)
        bt_data_encrypt.text = "encrypt"
        bt_data_decrypt = AppCompatButton(context)
        bt_data_decrypt.text = "decrypt"
//        bt_data_save = AppCompatButton(context)
//        bt_data_save.text = "save"
//        bt_data_delete = AppCompatButton(context)
//        bt_data_delete.text = "delete"
        et_data = AppCompatEditText(context)
        et_data.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        )
        ll_data_bt.addView(bt_data_encrypt)
        ll_data_bt.addView(bt_data_decrypt)
//        ll_data_bt.addView(bt_data_save)
//        ll_data_bt.addView(bt_data_delete)
        ll_data_et.addView(et_data)
        ll_data.addView(ll_data_bt)
        ll_data.addView(ll_data_et)

        et_key = key_edittext

        bt_data_decrypt.setOnClickListener(
                View.OnClickListener {
                    decrypt()
                }
        )
        bt_data_encrypt.setOnClickListener(
                View.OnClickListener {
                    encrypt()
                }
        )

        //by default, using currently set key
        EncDec.setKey(et_key.text.toString())
        terbaca_encrypted = EncDec.encode("terbaca");
        Log.i(tag, "decrypted terbaca is ")
        Log.i(tag, EncDec.decode(terbaca_encrypted))
        data_encrypted = byteArrayOf();
    }
    fun putToLinearLayout(ll: LinearLayout) {
        ll.addView(ll_data)
    }
    fun encrypt() {
        //encrypt can only be done if currect key can read terbaca_encrypted as "terbaca"
        //check if current key is correct
        EncDec.setKey(et_key.text.toString())
        if (EncDec.decode(terbaca_encrypted)=="terbaca") {
            //ok
            data_encrypted = EncDec.encode(et_data.text.toString())
            //update to db
            if (db_id==null) {
                //insert new
                //assign db_id to what we get
                db_id = Util.gen_id_from_time()
                val cv = ContentValues()
                cv.put("id", db_id)
                cv.put("data_encrypted",data_encrypted)
                cv.put("terbaca_encrypted",terbaca_encrypted)
                db_tosave.insert(DataModel.tablename,null,cv)
            }
            else {
                //update
                val cv = ContentValues()
                cv.put("id", db_id)
                cv.put("data_encrypted",data_encrypted)
                cv.put("terbaca_encrypted",terbaca_encrypted)
                db_tosave.update(DataModel.tablename, cv, "id=?", arrayOf(db_id))
            }
        }
        else {
            et_data.setText("wrong password")
        }
    }
    fun decrypt() {
        //tries to decrypt
        EncDec.setKey(et_key.text.toString())
        if (data_encrypted.size==0) {
            et_data.setText("")
        }
        else if (EncDec.decode(terbaca_encrypted)=="terbaca") {
            //ok
            et_data.setText(EncDec.decode(data_encrypted))
        }
        else {
            Log.e(tag, "terbaca decrypted is {"+EncDec.decode(terbaca_encrypted)+"}")
            et_data.setText("")
        }
    }
}
