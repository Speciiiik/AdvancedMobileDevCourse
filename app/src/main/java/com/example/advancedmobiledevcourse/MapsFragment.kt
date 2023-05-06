package com.example.advancedmobiledevcourse

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.advancedmobiledevcourse.databinding.FragmentMapsBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment(), GoogleMap.OnMarkerClickListener {

    // change this to match your fragment name
    private var _binding: FragmentMapsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //gMap.uiSettings.isZoomControlsEnabled = true

        binding.checkBoxZoom.setOnCheckedChangeListener { compoundButton, b ->
            gMap.uiSettings.isZoomControlsEnabled = compoundButton.isChecked
        }

        binding.radioButtonNormal.setOnCheckedChangeListener { compoundButton, b ->
            if(compoundButton.isChecked) {
                gMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            }
        }

        binding.radioButtonHybrid.setOnCheckedChangeListener { compoundButton, b ->
            if(compoundButton.isChecked) {
                gMap.mapType = GoogleMap.MAP_TYPE_HYBRID
            }
        }

        binding.radioButtonTerrain.setOnCheckedChangeListener { compoundButton, b ->
            if(compoundButton.isChecked) {
                gMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
            }
        }

        return root
    }


    private lateinit var gMap : GoogleMap

    private val callback = OnMapReadyCallback { googleMap ->

        gMap = googleMap


        val london = LatLng(51.5030973105362, -0.18264931465284565)
        var marker1 : Marker? = googleMap.addMarker(MarkerOptions().position(london).title("Marker in Sydney"))
        marker1?.tag = "London"

        val pudasjarvi = LatLng(65.3607559757255, 26.99517516340132)
        var marker2 : Marker? = googleMap.addMarker(MarkerOptions().position(pudasjarvi).title("Pudasjarvi"))
        marker2?.tag = "Pudasjarvi"

        val rovaniemi = LatLng(66.50319436087507, 25.726910828015637)
        var marker3 : Marker? = googleMap.addMarker(MarkerOptions().position(rovaniemi).title("Rovaniemi"))
        marker3?.tag = "Rovaniemi"

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(rovaniemi, 16f))

        googleMap.setOnMarkerClickListener(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        Log.d("ADVTECH", "MARKKERI")

        var coordinates : LatLng = p0.position
        Log.d("ADVTECH", coordinates.latitude.toString() + " - " + coordinates.longitude.toString())

        var city = p0.tag.toString()
        Log.d("ADVTECH", city)

        return false
    }
}