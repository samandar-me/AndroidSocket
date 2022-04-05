package uz.context.socketkotlin.manager

import android.app.Activity
import android.util.Log
import com.google.gson.Gson
import okhttp3.*
import okio.ByteString
import uz.context.socketkotlin.model.BitCoin
import uz.context.socketkotlin.model.Currency
import uz.context.socketkotlin.model.DataSend

class SocketManager {
    lateinit var mWebSocket: WebSocket
    var gson = Gson()
    lateinit var socketListener: SocketListener

    fun connectToSocket() {
        val client = OkHttpClient()

        val request: Request = Request.Builder().url("wss://ws.bitstamp.net").build()
        client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                mWebSocket = webSocket

                webSocket.send(
                    gson.toJson(
                        Currency(
                            "bts:subscribe",
                            DataSend("live_trades_btcusd")
                        )
                    )
                )

                /*webSocket.send("{\n" +
                      "    \"event\": \"bts:subscribe\",\n" +
                      "    \"data\": {\n" +
                      "        \"channel\": \"live_trades_btcusd\"\n" +
                      "    }\n" +
                      "}")*/
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                log("onMessage $text")
                val bitCoin = gson.fromJson(text, BitCoin::class.java)
                socketListener.onSuccess(bitCoin)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                log("onMessage $bytes")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                log("closing $code / $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                log("onFailure ${t.message}")
                socketListener.onFailure(t.message.toString())
            }
        })
        client.dispatcher.executorService.shutdown()
    }

    private fun log(msg: String) {
        Log.d("@@@@@", msg)
    }

    fun socketListener(socketListener: SocketListener) {
        this.socketListener = socketListener
    }

    interface SocketListener {
        fun onSuccess(bitCoin: BitCoin)
        fun onFailure(message: String)
    }
}