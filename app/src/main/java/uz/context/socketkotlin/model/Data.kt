package uz.context.socketkotlin.model

data class Data(
    val amount:Double,
    val amount_str:String,
    val buy_order_id:Long,
    val id:Int,
    val microtimestamp:String,
    val price:Double,
    val price_str:String,
    val sell_order_id:Long,
    val timestamp:String,
    val type:Int
)
data class DataSend(var channel:String)