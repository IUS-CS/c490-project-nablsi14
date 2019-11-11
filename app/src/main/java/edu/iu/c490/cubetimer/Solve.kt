package edu.iu.c490.cubetimer

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Solve(@PrimaryKey var id: UUID = UUID.randomUUID(),
                 var time: Long = 0,
                 var type: String = "",
                 var date: Date = Date()
)