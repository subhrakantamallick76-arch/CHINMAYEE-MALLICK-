package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        TeamEntity::class,
        JoinedContestEntity::class,
        WalletEntity::class,
        TransactionEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SKCotrageDatabase : RoomDatabase() {
    abstract fun fantasyDao(): FantasyDao

    companion object {
        @Volatile
        private var INSTANCE: SKCotrageDatabase? = null

        fun getDatabase(context: Context): SKCotrageDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SKCotrageDatabase::class.java,
                    "sk_cotrage_fantasy.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
