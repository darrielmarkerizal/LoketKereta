package com.example.loketkereta.keretaAdmin

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete

@Dao
interface KeretaDao {
    @Query("SELECT * FROM Kereta")
    fun getAll(): List<Kereta>

    @Insert
    fun insert(kereta: Kereta)

    @Update
    fun update(kereta: Kereta)

    @Delete
    fun delete(kereta: Kereta)
}