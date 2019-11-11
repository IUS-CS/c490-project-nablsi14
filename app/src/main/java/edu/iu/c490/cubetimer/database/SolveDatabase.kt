package edu.iu.c490.cubetimer.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import edu.iu.c490.cubetimer.Solve

@Database(entities = [Solve::class], version = 1)
@TypeConverters(SolveTypeConverters::class)
abstract class SolveDatabase : RoomDatabase() {
    abstract fun solveDao(): SolveDao
}