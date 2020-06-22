package com.senierr.repository.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * 版本 1 -> 2
 *
 * @author zhouchunjie
 * @date 2020/6/22
 */
class Migration1To2 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE collect (" +
                    "objectId TEXT NOT NULL," +
                    "username TEXT, " +
                    "password TEXT, " +
                    "sessionToken TEXT, " +
                    "createdAt TEXT, " +
                    "updatedAt TEXT, " +
                    "PRIMARY KEY(objectId)" +
                    ")"
        )
    }
}