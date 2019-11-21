package edu.iu.c490.cubetimer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AppViewModel : ViewModel() {
    val selectedPuzzle: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}