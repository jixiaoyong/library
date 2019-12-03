package cf.android666.applibrary.utils

import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

/**
 *  Created by jixiaoyong1995@gmail.com
 *  Data: 2019/12/3.
 *  Description:
 *  Md5校验工具类
 * copy from https://blog.csdn.net/qq_25646191/article/details/78863110
 */
object Md5Utils {

    private val hexDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f')

    /**
     * 获取文件Md5值，支持大文件
     */
    fun getFileMd5HexString(file: File): String {
        var stream: FileInputStream? = null
        var result = ""
        try {
            stream = FileInputStream(file)
            val length = 1024
            val buffer = ByteArray(length)
            var read = stream.read(buffer, 0, length)
            val digest = MessageDigest.getInstance("MD5")
            while (read > -1) {
                digest.update(buffer, 0, read)
                read = stream.read(buffer, 0, length)
            }
            result = getHexString(digest.digest())
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } finally {
            if (stream != null) {
                try {
                    stream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return result
    }

    fun getHexString(md5: ByteArray): String {
        // 用字节表示就是 16 个字节
        val str = CharArray(16 * 2) // 每个字节用 16 进制表示的话，使用两个字符，
        // 所以表示成 16 进制需要 32 个字符
        var k = 0 // 表示转换结果中对应的字符位置
        for (i in 0..15) { // 从第一个字节开始，对 MD5 的每一个字节
            // 转换成 16 进制字符的转换
            val byte0 = md5[i].toInt() and 0xFF // 取第 i 个字节
            str[k++] = hexDigits[byte0 ushr 4 and 0xf] // 取字节中高 4 位的数字转换, >>>,
            // 逻辑右移，将符号位一起右移
            str[k++] = hexDigits[(byte0 and 0xf)] // 取字节中低 4 位的数字转换
        }
        return String(str) // 换后的结果转换为字符串
    }


    /**
     * MD5校验字符串
     *
     * @param s String to be MD5
     * @return 'null' if cannot get MessageDigest
     */
    private fun getStringMD5(s: String): String? {
        val mdInst: MessageDigest
        mdInst = try { // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest.getInstance("MD5")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return ""
        }
        val btInput = s.toByteArray()
        // 使用指定的字节更新摘要
        mdInst.update(btInput)
        // 获得密文
        val md = mdInst.digest()
        // 把密文转换成十六进制的字符串形式
        return getHexString(md)
    }


    private fun getSubStr(str: String, subNu: Int, replace: Char): String? {
        var str = str
        val length = str.length
        if (length > subNu) {
            str = str.substring(length - subNu, length)
        } else if (length < subNu) { // NOTE: padding字符填充在字符串的右侧，和服务器的算法是一致的
            str += createPaddingString(subNu - length, replace)
        }
        return str
    }


    private fun createPaddingString(n: Int, pad: Char): String {
        if (n <= 0) {
            return ""
        }
        val paddingArray = CharArray(n)
        Arrays.fill(paddingArray, pad)
        return String(paddingArray)
    }

}

fun main() {
    //2a9c65de3757e1147f861fa4f623e2e7
    val filePath = "E:\\file.img"
    val md5Hashcode2: String = Md5Utils.getFileMd5HexString(File(filePath))
    println("MD5Util2计算文件md5值为：$md5Hashcode2")
    println("MD5Util2计算文件md5值的长度为：" + md5Hashcode2.length)
}