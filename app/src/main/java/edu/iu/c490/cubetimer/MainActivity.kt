package edu.iu.c490.cubetimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProviders


private const val TAG = "MAIN"
class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var appViewModel: AppViewModel

    private val fragmentContainer by lazy { findViewById<ConstraintLayout>(R.id.fragment_container) }
    private val puzzleSelector by lazy { findViewById<Spinner>(R.id.puzzle_selector) }
    private val timerBtn by lazy { findViewById<Button>(R.id.timer_btn) }
    private val timeListBtn by lazy { findViewById<Button>(R.id.time_list_btn) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        appViewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
        // Setup the puzzle selector Spinner
        ArrayAdapter.createFromResource(
            this,
            R.array.puzzle_names,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            puzzleSelector.adapter = adapter
            puzzleSelector.onItemSelectedListener = this
            puzzleSelector.setSelection(1)
        }

        val timerFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (timerFragment == null) {
            val fragment = TimerFragment()
            fragment.retainInstance = true
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }


    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        Log.d(TAG,"Item Selected: ${parent?.getItemAtPosition(pos)}")
        appViewModel.selectedPuzzle.value = parent?.getItemAtPosition(pos).toString()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        Log.e(TAG, "ERROR: No item selected")
    }
}
