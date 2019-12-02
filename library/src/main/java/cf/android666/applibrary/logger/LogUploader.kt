package cf.android666.applibrary.logger

/**
 *  Created by jixiaoyong1995@gmail.com
 *  Data: 2019/12/2.
 *  Description:
 */
interface FileUploader {
    fun uploadFileToServer(dirPath: String, logType: Int = LogCollector.LoggerType.LOG)
}