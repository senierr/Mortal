package com.senierr.base.support.utils

import java.io.Closeable
import java.io.IOException

/**
 * 流关闭工具类
 *
 * @author zhouchunjie
 * @date 2017/10/30
 */
object CloseUtil {

    /**
     * 关闭IO
     *
     * @param closeables closeables
     */
    fun closeIO(vararg closeables: Closeable?) {
        for (closeable in closeables) {
            if (closeable != null) {
                try {
                    closeable.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * 安静关闭IO
     *
     * @param closeables closeables
     */
    fun closeIOQuietly(vararg closeables: Closeable?) {
        for (closeable in closeables) {
            if (closeable != null) {
                try {
                    closeable.close()
                } catch (ignored: IOException) {
                }
            }
        }
    }
}
