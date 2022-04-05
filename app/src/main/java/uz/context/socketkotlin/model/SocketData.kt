package uz.context.socketkotlin.model

data class SocketData(
    val channel: String,
    val data: Data,
    val event: String
)

data class Currency(
    var event: String,
    var data: DataSend
)

data class BitCoin(val channel: String, val data: Data, val event: String)