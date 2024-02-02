package com.bennysamuel.whatsmynextcountry.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_data_table")
data class UserDataForTable(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "S_No")
    var sno: Int = 0,
    @ColumnInfo(name = "ApiData")
    var apiInfo: String,


) {
}