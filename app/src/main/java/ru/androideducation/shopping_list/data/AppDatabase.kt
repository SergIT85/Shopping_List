package ru.androideducation.shopping_list.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ShopItemDbModel::class], version = 3, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun shopListDao(): ShopListDao

    companion object {
        private var INSTANSE: AppDatabase? = null
        private val LOCK = Any()
        private const val NAME_DB = "shop_item.db"

        fun getInstanceDb(application: Application): AppDatabase {
            INSTANSE?.let {
                return it
            }
            synchronized(LOCK) {
                INSTANSE?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    application,
                    AppDatabase::class.java,
                    NAME_DB
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANSE = db
                return db
            }
        }
    }
}