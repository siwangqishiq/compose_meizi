package xyz.panyi.meizi.model

open class HttpResp<T> {
    var code:Int = 0
    var msg:String? = null
    var data:T? = null
}

data class Album (
    val id:Int = 0,
    val title:String?,
    val href:String?,
    val cover:String?,
    val images:List<String?>?
)

