package kerim.android.bade

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kerim.android.bade.api.DataSource
import kerim.android.bade.data.Tsery

class ListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        fun newInstance() = ListFragment()
    }

    private val viewModel: MainActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.list_fragment, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        var tempList: List<Tsery>

        val badestederAdapter = BadestederAdapter()
        recyclerView.adapter = badestederAdapter

        val mSwipeRefreshLayout = view.findViewById<SearchView>(R.id.swipe_container) as SwipeRefreshLayout
        mSwipeRefreshLayout.setOnRefreshListener(this)
        mSwipeRefreshLayout.setDistanceToTriggerSync(300)

        // Create the dropdown menu for our sorting types
        val sortSpinner: Spinner = view.findViewById(R.id.my_spinner)
        ArrayAdapter.createFromResource(
            view.context,
            R.array.sorting_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sortSpinner.adapter = adapter
        }


        val searchBar = view.findViewById<SearchView>(R.id.search)
        sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val staticList = viewModel.getStaticList().value ?: return
                tempList = searchList(staticList, searchBar.query.toString())

                viewModel.updateTseryData(
                    tempList.sortedWith(DataSource.getComparator(sortSpinner.selectedItem.toString()))
                )
                // could use parent.getItemAtPosition(pos).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) { }
        }

        // functionality to search for specific names
        searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextChange(query: String): Boolean {
                val staticList = viewModel.getStaticList().value ?: return false
                tempList = searchList(staticList, query)

                viewModel.updateTseryData(
                    tempList.sortedWith(DataSource.getComparator(sortSpinner.selectedItem.toString()))
                )
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
        })

        viewModel.getBadeDataObserver().observe(viewLifecycleOwner){
            badestederAdapter.setUpdatedData(it)
            println("updated")

            /*val staticList = viewModel.getStaticList().value
            if (staticList != null && mSwipeRefreshLayout.isRefreshing){
                tempList = searchList(staticList, searchBar.query.toString())

                viewModel.updateTseryData(
                    tempList.sortedWith(DataSource.getComparator(sortSpinner.selectedItem.toString()))
                )
            }*/

            mSwipeRefreshLayout.isRefreshing = false
        }

        return view
    }

    fun searchList(list: List<Tsery>, pattern: String) :List<Tsery> {
        val newList = ArrayList<Tsery>()
        for (i in list) {
            if (pattern.lowercase() in i.header.extra.name.lowercase()) {
                newList.add(i)
            }
        }
        return newList.toList()
    }

    override fun onRefresh() {
        viewModel.loadTseryData()
    }
}