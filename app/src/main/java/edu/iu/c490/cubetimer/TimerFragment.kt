package edu.iu.c490.cubetimer

import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_timer.*
import java.util.*
import java.util.concurrent.Executors

/**
 * A simple [Fragment] subclass.
 * Use the [TimerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimerFragment : Fragment() {

    private lateinit var viewModel: TimerViewModel
    private lateinit var appViewModel: AppViewModel
    private lateinit var repository: SolveRepository

    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TimerViewModel::class.java)

        appViewModel = activity?.run {
            ViewModelProviders.of(this).get(AppViewModel::class.java)
        }!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_timer, container, false)

        repository = SolveRepository.get()
//        executor.execute {
//            repository.deleteAll()
//        }

        view.findViewById<TextView>(R.id.timerText).setOnClickListener {
            onTimerClick()
        }
        appViewModel.selectedPuzzle.observe(viewLifecycleOwner, Observer {
            onPuzzleChange(it)
        })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.scrambleLiveData.observe(viewLifecycleOwner,
            Observer { scramble ->
                scrambleText.text = scramble
            })

    }

    private fun onTimerClick() {
        if (viewModel.timerIsRunning) {
            stopTimer()
            val puzzle = appViewModel.selectedPuzzle.value!!
            val solve = Solve(
                UUID.randomUUID(),
                viewModel.millis,
                puzzle
            )

            executor.execute {
                repository.addSolve(solve)
            }. run {
                updateStats(puzzle)
            }
        } else {
            startTimer()
        }
    }
    private fun onPuzzleChange(newPuzzle: String) {

        viewModel.getNewScramble(newPuzzle).observe(this, Observer { scramble ->
            viewModel.scrambleLiveData.value = scramble
            val size = if (newPuzzle == "6x6x6" || newPuzzle == "7x7x7") 20f else 24f
            scrambleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
        })
        stopTimer()
        timerText.text = "00:00.00"
        updateStats(newPuzzle)
    }

    private fun startTimer() {
        viewModel.millis = 0
        viewModel.timer = Timer()
        viewModel.timer.scheduleAtFixedRate(object: TimerTask() {
            override fun run() {
                activity?.runOnUiThread {
                    viewModel.millis++
                    timerText?.text = CustomUtils.formatTime(viewModel.millis)
                }
            }
        }, 0, 1)
        viewModel.timerIsRunning = true
    }
    private fun stopTimer() {
        if (viewModel.timerIsRunning) {
            viewModel.timer.cancel()
            viewModel.timer.purge()
            viewModel.timerIsRunning = false
        }
    }
    private fun updateStats(puzzle: String) {
        repository.getSolves(puzzle).observe(this,
            Observer { solves ->
                // Get number of solves
                solve_count.text = "Solves: ${solves.size}"

                // Get overall average
                if (solves.isNotEmpty()) {
                    val avg = solves.fold(0) {sum: Long, solve -> sum + solve.time} / solves.size
                    mean.text = "Avg: ${CustomUtils.formatTime(avg, TimeFormat.SHORT)}"
                } else
                    mean.text = resources.getString(R.string.mean_default)

                // Get Average of 5
                if (solves.size >= 5) {
                    val time = solves.take(5).fold(0) {sum: Long, solve -> sum + solve.time} / 5
                    avg_of_5.text = "Ao5: ${CustomUtils.formatTime(time, TimeFormat.SHORT)}"
                } else
                    avg_of_5.text = resources.getString(R.string.ao5_default)

                // Get average of 10
                if (solves.size >= 10) {
                    val time = solves.take(10).fold(0) {sum: Long, solve -> sum + solve.time} / 10
                    avg_of_10.text = "Ao10: ${CustomUtils.formatTime(time, TimeFormat.SHORT)}"
                } else
                    avg_of_10.text = resources.getString(R.string.ao10_default)

                // Get average of 50
                if (solves.size >= 50) {
                    val time = solves.take(10).fold(0) {sum: Long, solve -> sum + solve.time} / 50
                    avg_of_50.text = "Ao10: ${CustomUtils.formatTime(time, TimeFormat.SHORT)}"
                } else
                    avg_of_50.text = resources.getString(R.string.ao50_default)

                // Get best
                if (solves.isNotEmpty()) {
                    val min: Solve = solves.minBy { it.time }!!
                    best.text = "Best: ${CustomUtils.formatTime(min.time, TimeFormat.SHORT)}"
                } else
                    best.text = resources.getString(R.string.best_default)
            })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment TimerFragment.
         */
        @JvmStatic
        fun newInstance() = TimerFragment()
    }
}
