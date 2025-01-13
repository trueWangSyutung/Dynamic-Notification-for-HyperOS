package kg.edu.yjut.enhancenoticehyperos.base

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import kg.edu.yjut.enhancenoticehyperos.dao.NoticeDao
import kg.edu.yjut.enhancenoticehyperos.entity.NoticeHistory


@Database(entities = [
    NoticeHistory::class,




], version = 1, exportSchema = false)
abstract class CodeDatabase : RoomDatabase() {

    abstract fun noticeDao(): NoticeDao


    companion object {
        @Volatile
        private var INSTANCE: CodeDatabase? = null
        fun getDatabase(context: Context): CodeDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CodeDatabase::class.java,
                    "new_database"
                ).allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}