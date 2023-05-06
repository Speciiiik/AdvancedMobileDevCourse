package com.example.advancedmobiledevcourse

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.advancedmobiledevcourse.databinding.FragmentDataBinding
import com.example.advancedmobiledevcourse.dataclass.Comment
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.UnsupportedEncodingException

const val JSON_URL = "https://jsonplaceholder.typicode.com/comments"

class DataFragment : Fragment() {

    private var _binding: FragmentDataBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var adapter: CommentAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDataBinding.inflate(inflater, container, false)
        val root: View = binding.root

        linearLayoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = linearLayoutManager

    // navigate to another fragment, pass some parameter too
        binding.navigateButton.setOnClickListener {
            val action = DataFragmentDirections.actionDataFragmentToDetailFragment(123)
            it.findNavController().navigate(action)
        }

        onResponse()

        binding.getdataButton.setOnClickListener {

            sendData()

        }
        return root
    }
    private fun onResponse(){ //GET

        // Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Method.GET, JSON_URL,
            Response.Listener { response ->

                val rows : List<Comment> = gson.fromJson(response, Array<Comment>::class.java).toList()

                //for(item: Comment in rows)
                //{
                    //Log.d("ADVTECH", item.email.toString())
                //}

                adapter = CommentAdapter(rows)
                binding.recyclerView.adapter = adapter

            },
            Response.ErrorListener {
                // typically this is a connection error
                Log.d("ADVTECH", it.toString())
            })
        {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {

                // basic headers for the data
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Content-Type"] = "application/json; charset=utf-8"
                return headers
            }
        }

        // Add the request to the RequestQueue. This has to be done in both getting and sending new data.
        // if using this in an activity, use "this" instead of "context"
        Singleton.getInstance(requireContext()).addToRequestQueue(stringRequest)
        //val requestQueue = Volley.newRequestQueue(context)
        //requestQueue.add(stringRequest)
    }

    private fun sendData() { //POST

        // Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, JSON_URL,
            Response.Listener { response ->
                // usually APIs return the added new data back
                // when sending new data
                // therefore the response here should contain the JSON version
                // of the data you just sent below
                Log.d("ADVTECH", response)
            },
            Response.ErrorListener {
                // typically this is a connection error
                Log.d("ADVTECH", it.toString())
            })
        {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                // we have to specify a proper header, otherwise the API might block our queries!
                // define that we are after JSON data!
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
                    val newData = Comment(3,3, "Kalle", "asdf@saf.com", "lorem ipsums")
                    val jsonData = gson.toJson(newData)

                    // create a new TodoItem object here, and convert it to string format (GSON)

                    // JSON to bytes
                    body = jsonData.toByteArray(Charsets.UTF_8)
                } catch (e: UnsupportedEncodingException) {
                    // problems with converting our data into UTF-8 bytes
                }
                return body
            }
        }

        // Add the request to the RequestQueue. This has to be done in both getting and sending new data.
        // if using this in an activity, use "this" instead of "context"
        Singleton.getInstance(requireContext()).addToRequestQueue(stringRequest)
        //val requestQueue = Volley.newRequestQueue(context)
        //requestQueue.add(stringRequest)
    }
        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }
