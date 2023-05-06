package com.example.advancedmobiledevcourse

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.advancedmobiledevcourse.databinding.FragmentCloudMqttBinding
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAck
import kotlin.random.Random

class CloudMqttFragment : Fragment() {

    private var _binding: FragmentCloudMqttBinding? = null

    private val binding get() = _binding!!

    private lateinit var client: Mqtt3AsyncClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCloudMqttBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // version 3, IBM Cloud, weather station
        client = MqttClient.builder()
            .useMqttVersion3()
            .sslWithDefaultConfig()
            // käytetään samaa client id:tä tällä kertaa aina, ettei HiveMQ:n ilmainen raja tule täyteen
            .identifier("typo123214215")
            .serverHost(BuildConfig.HIVEMQ_BROKER)
            .serverPort(8883)
            .buildAsync()

        client.connectWith()
            .simpleAuth()
            .username(BuildConfig.HIVEMQ_USERNAME)
            .password(BuildConfig.HIVEMQ_PASSWORD.toByteArray())
            .applySimpleAuth()
            .send()
            .whenComplete { connAck: Mqtt3ConnAck?, throwable: Throwable? ->
                if (throwable != null) {
                    Log.d("ADVTECH", "Connection failure.")
                } else {
                    // Setup subscribes or start publishing
                    subscribeToTopic()
                }
                binding.buttonSendCloudMessage.setOnClickListener {
                    var randomNumber = Random.nextInt(0, 100)
                    var stringPayload = "Hello world! " + randomNumber.toString()

                    client.publishWith()
                        .topic(BuildConfig.HIVEMQ_TOPIC)
                        .payload(stringPayload.toByteArray())
                        .send()
                }
            }
        return root
    }
    fun subscribeToTopic()
    {
        client.subscribeWith()
            .topicFilter(BuildConfig.HIVEMQ_TOPIC)
            .callback { publish ->

                // this callback runs everytime your code receives new data payload
                var result = String(publish.getPayloadAsBytes())
                Log.d("ADVTECH", result)

                // jotta koodi varmasti ajaa binding-layeria koskevan
                // koodin oikeassa threadissa, käytetään runOnUiThreadia:
                activity?.runOnUiThread {
                    binding.textViewCloudMessage.text = result
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
