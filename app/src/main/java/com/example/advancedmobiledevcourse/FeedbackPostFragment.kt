package com.example.advancedmobiledevcourse

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.advancedmobiledevcourse.databinding.FragmentFeedbackPostBinding
import com.google.gson.GsonBuilder
import java.io.UnsupportedEncodingException

class FeedbackSendFragment : Fragment() {
    // change this to match your fragment name
    private var _binding: FragmentFeedbackPostBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedbackPostBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.buttonSendFeedbackApi.setOnClickListener {
            val name = binding.editTextFeedbackName.text.toString()
            val location = binding.editTextFeedbackLocation.text.toString()
            val value = binding.editTextFeedbackValue.text.toString()

            sendFeedback(name, location, value)
        }

        return root
    }

    fun sendFeedback(name : String, location : String, value : String) {
        Log.d("TESTI", "${name} - ${location} - ${value}")

        val JSON_URL = "http://10.0.2.2:8055/items/feedback?access_token=${BuildConfig.DIRECTUS_ACCESS_TOKEN}"

        var gson = GsonBuilder().create();

        // Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.POST, JSON_URL,
            Response.Listener { response ->
                Log.d("TESTI", "Lähetys Directusiin ok!")

                binding.editTextFeedbackName.setText("")
                binding.editTextFeedbackLocation.setText("")
                binding.editTextFeedbackValue.setText("")

                Toast.makeText(context, "Thank you for your feedback!", Toast.LENGTH_SHORT).show()
                activity?.onBackPressed()
            },
            Response.ErrorListener {
                // typically this is a connection error
                Log.d("TESTI", it.toString())
            })
        {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                // we have to specify a proper header, otherwise Apigility will block our queries!
                // define we are after JSON data!
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Content-Type"] = "application/json; charset=utf-8"
                return headers
            }

            // let's build the new data here
            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                // this function is only needed when sending data
                var body = ByteArray(0)
                try { // check the example "Converting a Kotlin object to JSON"
                    // on how to create this newData -variable
                    var newData = ""

                    var f : Feedback = Feedback()
                    f.name = name
                    f.location = location
                    f.value = value

                    // muunnetaan Feedback-olio JSONiksi GSONin avulla
                    newData = gson.toJson(f)

                    Log.d("TESTI", newData)

                    // JSON to bytes
                    body = newData.toByteArray(Charsets.UTF_8)
                } catch (e: UnsupportedEncodingException) {
                    // problems with converting our data into UTF-8 bytes
                }
                return body
            }
        }

        // Add the request to the RequestQueue. This has to be done in both getting and sending new data.
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}