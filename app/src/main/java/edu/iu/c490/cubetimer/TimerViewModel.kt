package edu.iu.c490.cubetimer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import java.util.*

class TimerViewModel: ViewModel() {
    val scrambleLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    var millis: Long = 0
    var timerIsRunning = false
    var timer: Timer = Timer()

    fun getNewScramble(puzzle: String): LiveData<String> {
        return ScrambleFetcher().fetchScramble(puzzle)
    }
}