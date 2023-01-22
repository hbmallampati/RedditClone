package edu.cs371m.reddit.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import edu.cs371m.reddit.databinding.FragmentRvBinding


// XXX Write most of this file
class HomeFragment: Fragment() {

    // XXX initialize viewModel
    private val viewModel :  MainViewModel by activityViewModels()
    private lateinit var adapter : PostRowAdapter

    private var _binding: FragmentRvBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }


    // TODO : Set up the adapter
    private fun initAdapter(binding: FragmentRvBinding) : PostRowAdapter {
        binding.recyclerView.layoutManager = LinearLayoutManager(binding.recyclerView.context)
        val temp = PostRowAdapter(viewModel)
        binding.recyclerView.adapter = temp


        return temp
    }

    private fun notifyWhenFragmentForegrounded(postRowAdapter: PostRowAdapter) {
        // When we return to our fragment, notifyDataSetChanged
        // to pick up modifications to the favorites list.  Maybe do more.

    }

    private fun initSwipeLayout(swipe : SwipeRefreshLayout) {
        swipe.setOnRefreshListener {
            viewModel.netPostsRefresh()
        }

        viewModel.observePostsFetchDone().observe(viewLifecycleOwner) {
            swipe.isRefreshing = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.setTitle("r/${viewModel.getSearchTerm()}")
        _binding = FragmentRvBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // XXX Write me

        adapter = initAdapter(binding)
        initSwipeLayout(binding.swipeRefreshLayout)


        viewModel.observeFavOnPostClicked().observe(viewLifecycleOwner) {
            //adapter.notifyDataSetChanged()
            val x = viewModel.getFavItemPos()
            if(x >= 0)
            {
                adapter.notifyItemChanged(viewModel.getFavItemPos())
            }
            else
                Log.d("home_frag", "Something is wrong")
        }

        viewModel.observeLivePostsPosts().observe(viewLifecycleOwner,
            Observer {
                adapter.submitList(it)
                adapter.notifyDataSetChanged()

            })

    }

}