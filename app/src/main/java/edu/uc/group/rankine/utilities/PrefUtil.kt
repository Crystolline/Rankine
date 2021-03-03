package edu.uc.group.rankine.utilities

import android.content.Context
import android.content.SharedPreferences

class PrefUtil {

    companion object {
        const val PREF_NAME_COUNTER = "pref_name_counter"
        const val PREF_NAME_DATA = "pref_name_data"
        const val PREF_NAME_NAME = "pref_name_name"
        val TOTAL_RANK_DATA_PREF_KEY = "total_rank_data_key"
        val RANK_DATA_PREF_KEY = "rank_data_pref_key"
        val RANK_DATA_PREF_NAME_KEY = "rank_data_pref_name_key"

        fun saveTotalPref(context: Context, total: Int) {
            val totalPref = context.getSharedPreferences(PREF_NAME_COUNTER, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = totalPref.edit()
            editor.putInt(TOTAL_RANK_DATA_PREF_KEY, total)
            editor.apply()
        }

        fun loadTotalPref(context: Context): Int {
            val totalPref = context.getSharedPreferences(PREF_NAME_COUNTER, Context.MODE_PRIVATE)
            return totalPref.getInt(TOTAL_RANK_DATA_PREF_KEY, 0)
        }

        fun removeTotalPref(context: Context) {
            val totalPref = context.getSharedPreferences(PREF_NAME_COUNTER, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = totalPref.edit()
            editor.remove(PREF_NAME_COUNTER)
            editor.apply()
        }

        fun saveRankPref(context: Context, data: String) {
            val dataPref = context.getSharedPreferences(PREF_NAME_DATA, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = dataPref.edit()
            editor.putString(RANK_DATA_PREF_KEY + loadTotalPref(context), data)
            editor.apply()
        }

        fun loadRankPref(context: Context): String? {
            val totalPref = context.getSharedPreferences(PREF_NAME_DATA, Context.MODE_PRIVATE)
            return totalPref.getString(RANK_DATA_PREF_KEY + loadTotalPref(context), "")
        }

        fun removeRankPref(context: Context){
            val dataPref = context.getSharedPreferences(PREF_NAME_COUNTER, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = dataPref.edit()
            editor.clear()
            editor.apply()
        }

        fun saveRankNamePref(context: Context, name: String) {
            val namePref = context.getSharedPreferences(PREF_NAME_NAME, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = namePref.edit()
            var test = RANK_DATA_PREF_NAME_KEY + loadTotalPref(context)
            editor.putString(test, name)
            editor.apply()
        }

        fun loadNamePref(context: Context): String? {
            val namePref = context.getSharedPreferences(PREF_NAME_NAME, Context.MODE_PRIVATE)
            return namePref.getString(RANK_DATA_PREF_NAME_KEY + loadTotalPref(context), "")
        }

        fun removeNamePref(context: Context){
            val namePref = context.getSharedPreferences(PREF_NAME_NAME, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = namePref.edit()
            editor.clear()
            editor.apply()
        }

        fun registerPref(context: Context, listener: SharedPreferences.OnSharedPreferenceChangeListener) {
            val pref = context.getSharedPreferences(PREF_NAME_COUNTER, Context.MODE_PRIVATE)
            pref.registerOnSharedPreferenceChangeListener(listener)
        }

        fun unregister(context: Context, listener: SharedPreferences.OnSharedPreferenceChangeListener) {
            val pref = context.getSharedPreferences(PREF_NAME_COUNTER, Context.MODE_PRIVATE)
            pref.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }


}