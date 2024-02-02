package com.bennysamuel.whatsmynextcountry.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [UserDataForTable::class], version = 1, exportSchema = false)
abstract class UserDatabase: RoomDatabase() {
    abstract val userDatabaseDAO: UserDatabaseDAO

    companion object{
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getInstance(context: Context): UserDatabase{
            var instance = INSTANCE
            if(instance == null){
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    name = "user_ids").allowMainThreadQueries().fallbackToDestructiveMigration().build()

                INSTANCE = instance
            }
            return instance
        }
    }
}


