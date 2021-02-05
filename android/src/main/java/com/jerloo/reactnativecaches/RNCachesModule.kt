package com.jerloo.reactnativecaches

import android.content.Context
import android.os.Build
import com.facebook.react.bridge.*
import java.io.File
import java.text.DecimalFormat


class RNCachesModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName() = "RNCaches"

    override fun getConstants(): MutableMap<String, Any> {
        return hashMapOf("count" to 1)
    }

    //获取缓存大小
    @ReactMethod
    fun getCacheSize(promise: Promise) {
        // 计算缓存大小
        var fileSize: Long = 0
        val filesDir: File = reactApplicationContext.filesDir // /data/data/package_name/files
        val cacheDir: File = reactApplicationContext.cacheDir // /data/data/package_name/cache
        fileSize += getDirSize(filesDir)
        fileSize += getDirSize(cacheDir)
        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (isMethodsCompat(Build.VERSION_CODES.FROYO)) {
            val externalCacheDir: File = getExternalCacheDir(reactApplicationContext) //"<sdcard>/Android/data/<package_name>/cache/"
            fileSize += getDirSize(externalCacheDir)
        }
        val params: WritableMap = Arguments.createMap()
        if (fileSize > 0) {
            val strFileSize = formatFileSize(fileSize)
            val unit = formatFileSizeName(fileSize)
            params.putString("cacheSize", strFileSize)
            params.putString("unit", unit)
            promise.resolve(params)
        } else {
            params.putString("cacheSize", "0")
            params.putString("unit", "B")
            promise.resolve(params)
        }
    }

    //清除缓存
    @ReactMethod
    fun runClearCache(promise: Promise?) {
        clearAppCache(promise)
    }


    /**
     * 获取目录文件大小
     *
     * @param dir
     * @return
     */
    private fun getDirSize(dir: File?): Long {
        if (dir == null) {
            return 0
        }
        if (!dir.isDirectory) {
            return 0
        }
        var dirSize: Long = 0
        val files: Array<File> = dir.listFiles()
        for (file in files) {
            if (file.isFile) {
                dirSize += file.length()
            } else if (file.isDirectory) {
                dirSize += file.length()
                dirSize += getDirSize(file) // 递归调用继续统计
            }
        }
        return dirSize
    }

    /**
     * 判断当前版本是否兼容目标版本的方法
     * @param VersionCode
     * @return
     */
    private fun isMethodsCompat(VersionCode: Int): Boolean {
        val currentVersion = Build.VERSION.SDK_INT
        return currentVersion >= VersionCode
    }

    private fun getExternalCacheDir(context: Context): File {

        // return context.getExternalCacheDir(); API level 8

        // e.g. "<sdcard>/Android/data/<package_name>/cache/"
        return context.externalCacheDir!!
    }

    /**
     * 转换文件大小名称
     *
     * @param fileS
     * @return B/KB/MB/GB
     */
    private fun formatFileSizeName(fileS: Long): String {
        val df = DecimalFormat("#.00")
        var fileSizeString = ""
        fileSizeString = when {
            fileS < 1024 -> {
                "B"
            }
            fileS < 1048576 -> {
                "KB"
            }
            fileS < 1073741824 -> {
                "MB"
            }
            else -> {
                "G"
            }
        }
        return fileSizeString
    }


    /**
     * 转换文件大小
     *
     * @param fileS
     * @return B/KB/MB/GB
     */
    private fun formatFileSize(fileS: Long): String {
        val df = DecimalFormat("#.00")
        var fileSizeString = ""
        fileSizeString = when {
            fileS < 1024 -> {
                df.format(fileS.toDouble())
            }
            fileS < 1048576 -> {
                df.format(fileS.toDouble() / 1024)
            }
            fileS < 1073741824 -> {
                df.format(fileS.toDouble() / 1048576)
            }
            else -> {
                df.format(fileS.toDouble() / 1073741824)
            }
        }
        return fileSizeString
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return B/KB/MB/GB
     */
//    private String formatFileSize(long fileS) {
//        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
//        String fileSizeString = "";
//        if (fileS < 1024) {
//            fileSizeString = df.format((double) fileS) + "B";
//        } else if (fileS < 1048576) {
//            fileSizeString = df.format((double) fileS / 1024) + "KB";
//        } else if (fileS < 1073741824) {
//            fileSizeString = df.format((double) fileS / 1048576) + "MB";
//        } else {
//            fileSizeString = df.format((double) fileS / 1073741824) + "G";
//        }
//        return fileSizeString;
//    }


    /**
     * 转换文件大小
     *
     * @param fileS
     * @return B/KB/MB/GB
     */
    //    private String formatFileSize(long fileS) {
    //        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
    //        String fileSizeString = "";
    //        if (fileS < 1024) {
    //            fileSizeString = df.format((double) fileS) + "B";
    //        } else if (fileS < 1048576) {
    //            fileSizeString = df.format((double) fileS / 1024) + "KB";
    //        } else if (fileS < 1073741824) {
    //            fileSizeString = df.format((double) fileS / 1048576) + "MB";
    //        } else {
    //            fileSizeString = df.format((double) fileS / 1073741824) + "G";
    //        }
    //        return fileSizeString;
    //    }
    /**
     * 清除app缓存
     */
    fun clearAppCache(promise: Promise?) {
        val asyncTask = ClearCacheAsyncTask(this, promise)
        asyncTask.execute(10)
    }

    fun clearCache() {
        reactApplicationContext.deleteDatabase("webview.db")
        reactApplicationContext.deleteDatabase("webview.db-shm")
        reactApplicationContext.deleteDatabase("webview.db-wal")
        reactApplicationContext.deleteDatabase("webviewCache.db")
        reactApplicationContext.deleteDatabase("webviewCache.db-shm")
        reactApplicationContext.deleteDatabase("webviewCache.db-wal")
        //清除数据缓存
        clearCacheFolder(reactApplicationContext.filesDir, System.currentTimeMillis())
        clearCacheFolder(reactApplicationContext.cacheDir, System.currentTimeMillis())
        //2.2版本才有将应用缓存转移到sd卡的功能
        if (isMethodsCompat(Build.VERSION_CODES.FROYO)) {
            clearCacheFolder(getExternalCacheDir(reactApplicationContext), System.currentTimeMillis())
        }
    }

    /**
     * 清除缓存目录
     * 目录
     * 当前系统时间
     */
    private fun clearCacheFolder(dir: File?, curTime: Long): Int {
        var deletedFiles = 0
        if (dir != null && dir.isDirectory) {
            try {
                for (child in dir.listFiles()) {
                    if (child.isDirectory) {
                        deletedFiles += clearCacheFolder(child, curTime)
                    }
                    if (child.lastModified() < curTime) {
                        if (child.delete()) {
                            deletedFiles++
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return deletedFiles
    }
}
