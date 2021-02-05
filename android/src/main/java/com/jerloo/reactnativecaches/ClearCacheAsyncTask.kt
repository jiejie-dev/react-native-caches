package com.jerloo.reactnativecaches

import android.os.AsyncTask
import com.facebook.react.bridge.Promise


/**
 * Created by qiepeipei on 2017/7/6.
 */
class ClearCacheAsyncTask(
        private val cachesModule: RNCachesModule?,
        private val promise: Promise?
) : AsyncTask<Int?, Int?, String?>() {

    override fun onPostExecute(s: String?) {
        super.onPostExecute(s)
        promise?.resolve(null)
    }

    override fun doInBackground(vararg p0: Int?): String? {
        cachesModule?.clearCache()
        return null
    }
}