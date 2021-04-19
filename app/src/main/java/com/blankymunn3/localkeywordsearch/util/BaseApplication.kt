package com.blankymunn3.localkeywordsearch.util

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DEBUG = isDebuggable(this)
    }

    private fun isDebuggable(context: Context): Boolean {
        var debuggable = false
        val pm = context.packageManager
        try {
            val appInfo = pm.getApplicationInfo(context.packageName, 0)
            debuggable = 0 != appInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("BaseApplication :::", "${e.printStackTrace()}")
        }
        return debuggable
    }


    companion object {
        var instance = BaseApplication()
        @Volatile
        var DEBUG = false
    }



    fun onDestroy() {
        super.onCreate()
    }
}
