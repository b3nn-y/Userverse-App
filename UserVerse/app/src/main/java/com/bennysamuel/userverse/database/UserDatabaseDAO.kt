package com.bennysamuel.whatsmynextcountry.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface UserDatabaseDAO {
    @Upsert
    fun insert(apiData:UserDataForTable)

    @Query("DELETE FROM user_data_table")
    fun clear()

    @Query("SELECT ApiData FROM user_data_table")
    suspend fun getData(): String?




}