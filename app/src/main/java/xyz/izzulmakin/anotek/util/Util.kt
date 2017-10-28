package xyz.izzulmakin.anotek.util

import java.sql.Timestamp
import java.util.*


/**
 * Created by Izzulmakin on 28/10/17.
 */
class Util {
    companion object {
        fun gen_id_from_time(): String {
            val currentTimestamp = java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
            return currentTimestamp.toString()
        }
    }
}