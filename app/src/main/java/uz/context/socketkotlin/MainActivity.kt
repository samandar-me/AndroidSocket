package uz.context.socketkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import okhttp3.*
import okio.ByteString
import uz.context.socketkotlin.databinding.ActivityMainBinding
import uz.context.socketkotlin.manager.SocketManager
import uz.context.socketkotlin.model.BitCoin

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var webSocket: SocketManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        webSocket = SocketManager()

        initViews()

    }

    private fun initViews() {
        binding.btnConnect.setOnClickListener {
            webSocket.connectToSocket()
        }
        webSocket.socketListener(object : SocketManager.SocketListener {
            override fun onSuccess(bitCoin: BitCoin) {
                runOnUiThread {
                    if (bitCoin.event == "bts:subscription_succeeded") {
                        binding.btnConnect.text = "Successfully Connected,Wait a moment"
                    } else {
                        binding.btnBitcoin.text = "1 BTC"
                        binding.btnUsd.text = "${bitCoin.data.price_str}\$"
                    }
                }
            }

            override fun onFailure(message: String) {
                runOnUiThread {
                    binding.btnConnect.text = message
                }
            }
        })
    }
}