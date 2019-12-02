package cf.android666.applibrary.logger

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 *  Created by jixiaoyong1995@gmail.com
 *  Data: 2019/12/2.
 *  Description:
 */
object LoggerThreadPool {

    fun getSingleThreadPool(): ExecutorService {
        return Executors.newSingleThreadExecutor()
    }
}