package edu.cs371m.reddit.ui.subreddits

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import edu.cs371m.reddit.MainActivity
import edu.cs371m.reddit.databinding.FragmentRvBinding
import edu.cs371m.reddit.ui.MainViewModel
import edu.cs371m.reddit.ui.PostRowAdapter

class Subreddits : Fragment() {
    // XXX initialize viewModel
    val viewModel : MainViewModel by activityViewModels()
    private lateinit var adapter : SubredditListAdapter

    private var _binding: FragmentRvBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): Subreddits {
            return Subreddits()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.setTitle("Pick")
        _binding = FragmentRvBinding.inflate(inflater, container, false)
        return binding.root
    }

    // XXX Write me, onViewCreated
    //TODO
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(javaClass.simpleName, "onViewCreated")
        // XXX Write me

        adapter = initAdapter(binding)

        viewModel.observeLivePostsSubreddits().observe(viewLifecycleOwner,
            Observer {
                adapter.submitList(it)
                adapter.notifyDataSetChanged()
            })
    }


    private fun initAdapter(binding: FragmentRvBinding) : SubredditListAdapter {
        binding.recyclerView.layoutManager = LinearLayoutManager(binding.recyclerView.context)
        val temp = SubredditListAdapter(viewModel, requireActivity())
        binding.recyclerView.adapter = temp

        return temp
    }




    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}