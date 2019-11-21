package edu.iu.c490.cubetimer

import android.app.Application

class CubeTimerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SolveRepository.initialize(this)
    }
}