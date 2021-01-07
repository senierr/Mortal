package com.senierr.mortal.repository.store.db

import android.content.Context
import androidx.room.Room
import com.senierr.mortal.repository.store.db.migration.Migration1To2
import com.senierr.mortal.repository.store.db.migration.Migration2To3

/**
 * 数据库管理
 *
 * @author zhouchunjie
 * @date 2019/11/28
 */
object DatabaseManager {

    private const val DB_NAME = "repository_db.db" // 数据库名
    const val DB_VERSION = 3    // 数据库版本

    private lateinit var database: AppDatabase

    /**
     * 初始化
     */
    fun initialize(context: Context) {
        database = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
            .addMigrations(Migration1To2(), Migration2To3())
            .build()
    }

    /**
     * 获取数据库
     */
    fun getDatabase(): AppDatabase {
        return database
    }
}