package cf.android666.applibrary.utils

//import net.pbdavey.awt.Graphics2D
//import org.apache.poi.hslf.usermodel.SlideShow
import android.graphics.*
import android.media.MediaMetadataRetriever
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import kotlin.math.min


/**
 *  Created by jixiaoyong1995@gmail.com
 *  Data: 2019/7/5.
 *  Description:获取文件缩略图,支持视频、PPT、图片、音乐等格式
 */
object ThumbnailUtils {

    @JvmStatic
    @JvmOverloads
    fun getThumbnailBitmap(path: String, height: Int = 260, width: Int = 317): Bitmap? {
        val bitmap = when {
            false -> null
            path.endsWith(".ppt", true) -> getPptThumbnail(path, width, height)
            path.endsWith(".mp3", true) -> getMusicThumbnail(path)
            FileUtil.checkImageFile(path) -> getImageThumbnail(path, width, height)
            FileUtil.checkVideoFile(path) -> getVideoThumbnail(path)
            else -> null
        } ?: return null

        val scaleBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
        bitmap.recycle()
        return scaleBitmap
    }

    fun getRoundThumbnailBitmap(fileName: String, roundPx: Float = 10F, height: Int = 260, width: Int = 317): Bitmap? {
        val bitmap = getThumbnailBitmap(fileName, height, width)
                ?: return null
        return getRoundedCornerBitmap(bitmap, roundPx)
    }

    fun getThumbnailByteArray(name: String): ByteArray? {
        val bitmap = getThumbnailBitmap(name) ?: return null
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        return baos.toByteArray()
    }

    fun getVideoThumbnail(path: String): Bitmap? {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        var bitmap: Bitmap? = null
        try {
            mediaMetadataRetriever.setDataSource(File(path).absolutePath)
            bitmap = mediaMetadataRetriever.frameAtTime
        } catch (e: Exception) {
        } finally {
            mediaMetadataRetriever.release()
        }
        return bitmap
    }

    fun getPptThumbnail(path: String, width: Int = 0, height: Int = 0): Bitmap? {
//        try {
//            val ppt = SlideShow(File(path))
//            val pgsize = ppt.pageSize
//            val pptSlide = ppt.slides[0]
//            val bmp = Bitmap.createBitmap(pgsize.getWidth().toInt(), pgsize.getHeight().toInt(),
//                    Bitmap.Config.RGB_565)
//            val canvas = Canvas(bmp)
//            val paint = Paint()
//            paint.color = Color.WHITE
//            paint.flags = Paint.ANTI_ALIAS_FLAG
//            canvas.drawPaint(paint)
//            val graphics2d = Graphics2D(canvas)
//            val isCanceled = AtomicBoolean(false)
//            pptSlide.draw(graphics2d, isCanceled, null, 0)
//            return bmp
//        } catch (e: Exception) {
//            e.printStackTrace()
        return null
//        }
    }

    fun getMusicThumbnail(path: String): Bitmap? {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        var bitmap: Bitmap? = null
        try {
            mediaMetadataRetriever.setDataSource(path)
            val picture = mediaMetadataRetriever.embeddedPicture
            if (picture != null && picture.isNotEmpty()) {
                bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.size)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mediaMetadataRetriever.release()
        }
        return bitmap
    }

    @JvmStatic
    fun getImageThumbnail(path: String, width: Int, height: Int): Bitmap? {
        var fileInputStream: FileInputStream? = null
        return try {
            val bitmapOpt = BitmapFactory.Options()
            bitmapOpt.inJustDecodeBounds = true
            fileInputStream = FileInputStream(File(path))
            BitmapFactory.decodeStream(fileInputStream, null, bitmapOpt)
            bitmapOpt.inJustDecodeBounds = false
            bitmapOpt.inPreferredConfig = Bitmap.Config.RGB_565

            if (width * height != 0) {
                val scale = min(bitmapOpt.outHeight.toFloat() / height,
                        bitmapOpt.outWidth.toFloat() / width).toInt()
                bitmapOpt.inSampleSize = if (scale < 1) 1 else scale
                bitmapOpt.outWidth = width
                bitmapOpt.outHeight = height
            }
            fileInputStream = FileInputStream(File(path))
            val bitmap = BitmapFactory.decodeStream(fileInputStream, null, bitmapOpt)
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            fileInputStream?.close()
        }
    }

    fun getRoundedCornerBitmap(bitmap: Bitmap, roundPx: Float): Bitmap {
        //要画圆角，必须是Bitmap.Config.ARGB_8888
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val paint = Paint()
        paint.isAntiAlias = true
        val rectF = RectF(0F, 0F, bitmap.width.toFloat(), bitmap.height.toFloat())
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, null, rectF, paint)

        bitmap.recycle()
        return output
    }
}