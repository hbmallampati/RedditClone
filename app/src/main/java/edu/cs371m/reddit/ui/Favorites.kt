package edu.cs371m.reddit.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.app.NavUtils
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import edu.cs371m.reddit.MainActivity
import edu.cs371m.reddit.api.RedditPost
import edu.cs371m.reddit.databinding.FragmentRvBinding
import java.util.stream.IntStream

class Favorites: Fragment() {

    // XXX initialize viewModel
    private val viewModel :  MainViewModel by activityViewModels()
    private lateinit var adapter : PostRowAdapter

    private var _binding: FragmentRvBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): Favorites {
            return Favorites()
        }
    }
    private fun setDisplayHomeAsUpEnabled(value : Boolean) {
        val mainActivity = requireActivity() as MainActivity
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(value)
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.setTitle("Favourites")
        _binding = FragmentRvBinding.inflate(inflater)
        return binding.root
    }

    private fun initAdapter(binding: FragmentRvBinding) : PostRowAdapter {
        binding.recyclerView.layoutManager = LinearLayoutManager(binding.recyclerView.context)
        val temp = PostRowAdapter(viewModel)
        binding.recyclerView.adapter = temp

        return temp
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setDisplayHomeAsUpEnabled(true)

        // XXX Write me
        // Setting itemAnimator = null on your recycler view might get rid of an annoying
        // flicker

        adapter = initAdapter(binding)
        adapter.submitList(viewModel.getFavsList())

        viewModel.observeLivePostsFavs().observe(viewLifecycleOwner,
            Observer {
                adapter.submitList(it)
                adapter.notifyDataSetChanged()

            })

        //TODO : Uncomment
        // Add to menu
        val menuHost: MenuHost = requireActivity()

        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Menu is already inflated by main activity
            }
            // XXX Write me, onMenuItemSelected
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return if(menuItem.itemId == android.R.id.home) {
                    //NavUtils.navigateUpFromSameTask(requireActivity())
                    //this.finish()
                    parentFragmentManager.popBackStack()
                    true
                }
                else
                {
                    return false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        // XXX Write me
        // Don't let back to home button stay when we exit favorites
        setDisplayHomeAsUpEnabled(false)
        _binding = null
        super.onDestroyView()

    }
}