package edu.iu.c490.cubetimer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders


private const val TAG = "TIMES"

class TimesListFragment : Fragment(), ViewTreeObserver.OnGlobalLayoutListener {


    private lateinit var appViewModel: AppViewModel
    private lateinit var repository: SolveRepository
    private lateinit var solveItemRecyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appViewModel = activity?.run {
            ViewModelProviders.of(this).get(AppViewModel::class.java)
        }!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        repository = SolveRepository.get()

        val view = inflater.inflate(R.layout.fragment_solveitem_list, container, false)

        retainInstance = true


        solveItemRecyclerView = view.findViewById(R.id.solveitem_recycler_view)
        solveItemRecyclerView.viewTreeObserver.addOnGlobalLayoutListener(this)

        appViewModel.selectedPuzzle.observe(viewLifecycleOwner, Observer {
            onPuzzleChange(it)
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val puzzle = appViewModel.selectedPuzzle.value ?: "3x3x3"
        repository.getSolves(puzzle).observe(this,
            Observer { solveItems ->
                Log.d(TAG, "Solves: ${solveItems.size}")
                solveItemRecyclerView.adapter = SolveItemAdapter(solveItems)
            })
    }

    override fun onGlobalLayout() {
        solveItemRecyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)

        val colWidth = 330
        val screenWidth = solveItemRecyclerView.measuredWidth

        val spanCount: Int = screenWidth / colWidth

        solveItemRecyclerView.layoutManager = GridLayoutManager(context, spanCount)
    }

    private fun onPuzzleChange(newPuzzle: String) {
        Log.d(TAG, "Puzzle changed to: $newPuzzle")

        repository.getSolves(newPuzzle).observe(this,
            Observer { solveItems ->
                Log.d(TAG, "Solves: ${solveItems.size}")
                solveItemRecyclerView.adapter = SolveItemAdapter(solveItems)
            })
    }

    companion object {
        @JvmStatic
        fun newInstance() = TimesListFragment()
    }

    private class SolveItemHolder(view: View, dateView: TextView, timeView: TextView) :
        RecyclerView.ViewHolder(view) {
        val bindDate: (CharSequence) -> Unit = dateView::setText
        val bindText: (CharSequence) -> Unit = timeView::setText
    }
    private inner class SolveItemAdapter(private val solveItems: List<Solve>) :
            RecyclerView.Adapter<SolveItemHolder>() {
        override fun onBindViewHolder(holder: SolveItemHolder, position: Int) {
            val date = CustomUtils.dateFormatter.format(solveItems[position].date)
            holder.bindDate(date)
            holder.bindText(CustomUtils.formatTime(solveItems[position].time, TimeFormat.SHORT))

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SolveItemHolder {
            val view = layoutInflater.inflate(R.layout.fragment_solveitem, parent, false)
            val dataView = view.findViewById<TextView>(R.id.solve_item_date)
            val timeView = view.findViewById<TextView>(R.id.solve_item_time)

            return SolveItemHolder(view, dataView, timeView)
        }

        override fun getItemCount(): Int {
            return solveItems.size
        }
    }
}
