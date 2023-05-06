package com.example.advancedmobiledevcourse

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.advancedmobiledevcourse.databinding.FragmentWeatherStationBinding
import com.example.advancedmobiledevcourse.dataclass.weatherstation.WeatherStation
import com.google.gson.GsonBuilder
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAck
import java.util.Date
import java.util.Locale
import java.util.UUID


class WeatherStationFragment : Fragment() {
    // change this to match your fragment name
    private var _binding: FragmentWeatherStationBinding? = null

    var MQTT_URL = BuildConfig.MQTT_BROKER
    private lateinit var client: Mqtt3AsyncClient

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherStationBinding.inflate(inflater, container, false)
        val root: View = binding.root
        // the binding -object allows you to access views in the layout, textviews etc.

        // binding.buttonAddDataTest.setOnClickListener {
        // var text = "Test Data to see if the rows are working!"
        // binding.latestDataViewTester.addData(text)
        // }

        // version 3, IBM Cloud, weather station
        client = MqttClient.builder()
            .useMqttVersion3()
            .sslWithDefaultConfig()
            .identifier(BuildConfig.MQTT_CLIENT_ID + UUID.randomUUID().toString())
            .serverHost(BuildConfig.MQTT_BROKER)
            .serverPort(8883)
            .buildAsync()



        client.connectWith()
            .simpleAuth()
            .username(BuildConfig.MQTT_USERNAME)
            .password(BuildConfig.MQTT_PASSWORD.toByteArray())
            .applySimpleAuth()
            .send()
            .whenComplete { connAck: Mqtt3ConnAck?, throwable: Throwable? ->
                if (throwable != null) {
                    Log.d("ADVTECH", "Connection failure.")
                } else {
                    // Setup subscribes or start publishing
                    subscribeToTopic()
                }
            }
        return root
    }

    fun subscribeToTopic() {

        val gson = GsonBuilder().setPrettyPrinting().create()

        client.subscribeWith()
            .topicFilter(BuildConfig.MQTT_TOPIC)
            .callback { publish ->

                // this callback runs everytime your code receives new data payload
                var result = String(publish.getPayloadAsBytes())
                Log.d("ADVTECH", result)

                try {
                    var item : WeatherStation = gson.fromJson(result, WeatherStation::class.java)
                    Log.d("ADVTECH", item.d.get1().v.toString() + "C")

                    var temperature = item.d.get1().v
                    var pressure = item.d.get2().v
                    var humidity = item.d.get3().v

                    var text = "Temperature: ${temperature}C \nPressure: ${pressure} bar\nHumidity: ${humidity}%\n\n"
                    text += item.ts.toString()

                    var time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                    var message = "${time} - Temperature: ${temperature}℃, humidity: ${humidity}%"

                    activity?.runOnUiThread {
                        binding.textViewWeatherstationText.text = text
                        binding.customtemperatureviewWeatherstation.changeTemperature(temperature.toInt())

                        binding.buttonAddDataTest.setOnClickListener {
                            binding.latestDataViewTester.addData(message)
                        }

                    }
                }
                catch(e : Exception) {
                    Log.d("ADVTECH", e.message.toString())
                }

            }
            .send()
            .whenComplete { subAck, throwable ->
                if (throwable != null) {
                    // Handle failure to subscribe
                    Log.d("ADVTECH", "Subscribe failed.")
                } else {
                    // Handle successful subscription, e.g. logging or incrementing a metric
                    Log.d("ADVTECH", "Subscribed!")
                }
            }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        client.disconnect()
    }
}

