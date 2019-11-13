package edu.iu.c490.cubetimer

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class TimerViewModel: ViewModel() {
    var scrambleLiveData: LiveData<String>

    init {
        scrambleLiveData = ScrambleFetcher().fetchScramble("3x3x3")
    }

    fun getNewScramble(puzzle: String) {
        scrambleLiveData = ScrambleFetcher().fetchScramble(puzzle)
    }

}