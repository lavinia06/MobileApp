package com.example.exam.persistence

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.exam.model.Car

@Dao
interface AppDao {

    @Query("select * from cars where cars.id = :id")
    suspend fun getEntityById(id: Int) : Car?

    @Query("select * from cars")
    fun getEntities() : List<Car>

    @Delete
    fun deleteEntity(entity: Car)

    @Update
    fun updateEntity(entity: Car)

    @Insert
    fun insertEntity(entity: Car)

    @Query("DELETE FROM cars")
    fun deleteAllEntities()

}