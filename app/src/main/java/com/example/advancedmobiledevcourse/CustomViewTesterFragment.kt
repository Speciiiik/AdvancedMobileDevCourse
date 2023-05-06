package com.example.advancedmobiledevcourse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.advancedmobiledevcourse.databinding.FragmentCustomViewTesterBinding
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import kotlin.random.Random

class CustomViewTesterFragment : Fragment() {
    // change this to match your fragment name
    private var _binding: FragmentCustomViewTesterBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomViewTesterBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.speedView.speedPercentTo(75, 4000)

        binding.buttonChangeTemperature.setOnClickListener {
            val temp = Random.nextInt(-50, 50)
            binding.customtemperatureviewTester.changeTemperature(temp)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}