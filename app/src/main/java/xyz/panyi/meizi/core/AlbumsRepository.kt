package xyz.panyi.meizi.core

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import okhttp3.Request
import xyz.panyi.meizi.HOST
import xyz.panyi.meizi.model.Album
import xyz.panyi.meizi.model.HttpResp

class AlbumsRepository {
    private val client = OkHttpClient()
    private val gson = Gson()
    
    fun loadAlbums(id:Int) : Flow<HttpResp<List<Album>?>> = flow{
        val url = "${HOST}ablums" + if(id>0)"?id=${id}" else ""
        
        LogUtil.i("AlbumsRepository", "request $url")
        val request = Request.Builder()
            .url(url)
            .get().build()
        
        val response =  client.newCall(request).execute()
        if (!response.isSuccessful) {
            val errResp : HttpResp<List<Album>?> = HttpResp()
            errResp.code = response.code
            emit(errResp)
            return@flow
        }
        
        try {
            val body = response.body.string()
            LogUtil.i("AlbumsRepository", "resp $body")
            val type = object : TypeToken<HttpResp<List<Album>?>>() {}.type
            val httpResp = gson.fromJson(body, type) as HttpResp<List<Album>?>
            emit(httpResp)
        }catch (e: Exception){
            LogUtil.e("AlbumsRepository",e.message)
            val errResp : HttpResp<List<Album>?> = HttpResp()
            errResp.code = 500
            errResp.msg = e.message
            emit(errResp)
            return@flow
        }
        
    }
}