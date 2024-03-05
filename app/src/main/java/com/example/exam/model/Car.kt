package com.example.exam.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "cars", indices = [Index(value = ["id"], unique = true)])
data class Car(
    @PrimaryKey() val id :Int,

    @ColumnInfo(name = "name") val name: String? = null,
    @ColumnInfo(name = "supplier") val supplier: String? = null,
    @ColumnInfo(name = "details") val details: String = "",
    @ColumnInfo(name = "status") val status: String ="",
    @ColumnInfo(name = "quantity") val quantity: Int? = null,
    @ColumnInfo(name = "type") val type: String = ""

){
    override fun toString(): String {
        return "$name $supplier $quantity"
    }
}