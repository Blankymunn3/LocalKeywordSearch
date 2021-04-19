package com.blankymunn3.localkeywordsearch.util

import android.app.Activity
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import java.util.ArrayList

open class BaseActivity : AppCompatActivity() {

    companion object {
        var actList = ArrayList<Activity>()
        fun clearActivity() {
            for (act in actList) {
                act.finish()
            }
            actList.clear()
        }
    }

    protected inline fun <reified T : ViewDataBinding> binding(@LayoutRes resId: Int): Lazy<T> =
        lazy { DataBindingUtil.setContentView<T>(this, resId) }
}