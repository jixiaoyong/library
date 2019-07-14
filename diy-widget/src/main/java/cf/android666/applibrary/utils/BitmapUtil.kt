package cf.android666.applibrary.utils

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur

/**
 *  Created by jixiaoyong1995@gmail.com
 *  Data: 2018/11/30.
 *  Description:
 */
object BitmapUtil {

    /**
     *  模糊化bitmap
     */
    fun blurBitmap(context: Context, bitmap: Bitmap, radius: Float): Bitmap {
        val renderScript = RenderScript.create(context)
        val blurScript = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
        blurScript.setRadius(radius)

        val inAllocation = Allocation.createFromBitmap(renderScript, bitmap)
        val outAllocation = Allocation.createTyped(renderScript, inAllocation.type)

        blurScript.setInput(inAllocation)
        blurScript.forEach(outAllocation)

        outAllocation.copyTo(bitmap)
        blurScript.destroy()
        renderScript.destroy()

        return bitmap
    }
}