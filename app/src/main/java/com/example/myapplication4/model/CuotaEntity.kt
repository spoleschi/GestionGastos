package com.example.myapplication4.model
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "cuotas",
    foreignKeys = [
        ForeignKey(
            entity = TransactionEntity::class,
            parentColumns = ["id"],
            childColumns = ["transaccionId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CuotaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val transaccionId: Int,
    val nroCuota: Int,
    val fechaPago: LocalDate
)

