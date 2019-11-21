package edu.iu.c490.cubetimer

import android.content.Context
import androidx.room.Room
import edu.iu.c490.cubetimer.database.SolveDatabase
import java.lang.IllegalStateException
import java.util.concurrent.Executors

private const val DATABASE_NAME = "solve-database"

class SolveRepository private constructor(context: Context) {
    companion object {
        private var instance: SolveRepository? = null

        fun initialize(context: Context) {
            if (instance == null)
                instance = SolveRepository(context)
        }

        fun get(): SolveRepository {
            return instance ?: throw IllegalStateException("SolveRepository must be initialized!")
        }
    }

    private val database: SolveDatabase = Room.databaseBuilder(
        context.applicationContext,
        SolveDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val solveDao = database.solveDao()


    fun addSolve(solve: Solve) = solveDao.addSolve(solve)
    fun getSolves(type: String) = solveDao.getSolves(type)
    fun deleteAll() = solveDao.deleteAll()


}