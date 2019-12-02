package edu.iu.c490.cubetimer

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AppViewModel : ViewModel() {
    val selectedPuzzle: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    var showingTimer = true
    var fragmentContainer: Fragment? = null

}