package cf.android666.applibrary.utils

import android.graphics.Bitmap
import android.util.LruCache
import cf.android666.applibrary.logger.Logger
import java.io.File
import java.io.FileOutputStream

/**
 *  Created by jixiaoyong1995@gmail.com
 *  Data: 2019/8/5.
 *  Description: 图片获取、缓存工具
 *  三级缓存：内存 > 本地 > 重新获取（网络、本地获取预览图……）
 */
object ImageLoader {

    private var CACHE_PATH = ""
    private var bitmapCaches: LruCache<String, Bitmap>

    init {
        val maxMemory = Runtime.getRuntime().maxMemory() / 8
        bitmapCaches = object : LruCache<String, Bitmap>(maxMemory.toInt()) {
            override fun sizeOf(key: String, value: Bitmap): Int {
                return value.byteCount
            }
        }
        Logger.d("max memory:${maxMemory / 1024}kb")
    }

    @JvmStatic
    fun init(dataPath: String) {
        CACHE_PATH = dataPath + File.separator + "image" + File.separator
        try {
            val cacheFile = File(CACHE_PATH)
            if (!cacheFile.isDirectory || !cacheFile.exists()) {
                cacheFile.mkdirs()
            }
        } catch (e: Exception) {
        }
    }

    /**
     * 清除所有缓存（内存，文件）
     */
    @JvmStatic
    fun cleanAllCache() {
        try {
            bitmapCaches.evictAll()
            cleanDirectory(File(CACHE_PATH))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun cleanDirectory(file: File) {
        if (!file.exists()) {
            return
        }
        if (file.isFile) {
            file.delete()
        } else if (file.isDirectory) {
            file.listFiles().forEach {
                cleanDirectory(it)
            }
        }
    }

    fun getBitmapCache(filePath: String): Bitmap? {
        val imageNameMd5 = Md5Utils.getFileMd5HexString(File(filePath))
        //1. load screenshot from cache
        var bitmap = bitmapCaches.get(imageNameMd5)

        //2. load screenshot from file
        if (bitmap == null) {
            bitmap = getFromFileCached(imageNameMd5)
        }
        //3. create screenshot from file
        if (bitmap == null) {
            bitmap = createFromFileCached(filePath, imageNameMd5)
        }
        return bitmap
    }

    /**
     * 从缓存的文件中获取图片
     * 并缓存到内存
     */
    private fun getFromFileCached(imageNameMd5: String): Bitmap? {
        Logger.d("load form file cache:$imageNameMd5")
        val imagePath = CACHE_PATH + imageNameMd5
        val bitmap = ThumbnailUtils.getThumbnailBitmap(imagePath)
        bitmap?.let {
            bitmapCaches.put(imageNameMd5, bitmap)
        }
        return bitmap
    }

    /**
     * 重新获取图片（创建缩略图）
     * 并缓存到内存和文件中
     */
    private fun createFromFileCached(filePath: String, imageNameMd5: String): Bitmap? {
        Logger.d("create form file:$imageNameMd5 file:$filePath")
        val bitmap = ThumbnailUtils.getThumbnailBitmap(filePath)
        bitmap?.let {
            saveBitmapToFileCache(bitmap, imageNameMd5)
            bitmapCaches.put(imageNameMd5, bitmap)
        }
        return bitmap
    }

    /**
     * 将Bitmap缓存到本地文件
     */
    private fun saveBitmapToFileCache(bitmap: Bitmap, imageNameMd5: String) {
        val imagePath = CACHE_PATH + imageNameMd5
        var fileOutputStream: FileOutputStream? = null
        try {
            val cacheFile = File(imagePath)
            if (!cacheFile.exists()) {
                cacheFile.createNewFile()
            }
            fileOutputStream = FileOutputStream(cacheFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            fileOutputStream?.close()
        }
    }

}
