package com.example.stressmeter.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.stressmeter.FullScreenImageActivity
import com.example.stressmeter.MainActivity
import com.example.stressmeter.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val gridView: GridView = binding.gridView
        val nextButton: Button = binding.nextButton

        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        val imageAdapter = ImageAdapter(requireContext())
        gridView.adapter = imageAdapter

        //Create list of groups
        val imageGroups = mutableListOf<List<Int>>()
        for (i in 0 until 3) {
            val group = (1..16).map { resourceId ->
                val imageNumber = i * 16 + resourceId
                val imageId = resources.getIdentifier("image$imageNumber", "drawable", requireContext().packageName)
                imageId
            }
            imageGroups.add(group)
        }

        imageAdapter.setImageCollection(imageGroups[0])


        //More images button is clicked
        var currentGroup = 0
        nextButton.setOnClickListener {
            //stop notification if button clicked
            (activity as MainActivity).stopNotification()
            currentGroup = (currentGroup + 1) % 3
            imageAdapter.setImageCollection(imageGroups[currentGroup])
        }

        //Logic for clicking an image in the grid
        gridView.setOnItemClickListener { _, view, position, _ ->
            val clickedImageResource = imageAdapter.getItem(position) as Int


            //  open new activity: image response
            val intent = Intent(requireContext(), FullScreenImageActivity::class.java)
            intent.putExtra("imageResource", clickedImageResource)
            intent.putExtra("imageScore", position)
            startActivity(intent)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}