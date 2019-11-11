package edu.iu.c490.cubetimer.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import edu.iu.c490.cubetimer.Solve

@Dao
interface SolveDao {
    @Query("select * from solve where type=(:type)")
    fun getSolves(type: String): LiveData<List<Solve>>

    @Insert
    fun addSolve(solve: Solve)
}