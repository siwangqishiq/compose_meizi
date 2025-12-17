package xyz.panyi.meizi.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import xyz.panyi.meizi.core.AlbumsRepository
import xyz.panyi.meizi.core.LogUtil
import xyz.panyi.meizi.model.Album

class AlbumsViewModel : ViewModel() {
    private val repo = AlbumsRepository()
    
    private val _albums = MutableStateFlow<List<Album>>(emptyList())
    val albums: StateFlow<List<Album>> = _albums
    
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading
    
    init {
        loadMore()
    }
    
    fun refresh(){
        _albums.value = emptyList()
        loadMore()
    }
    
    fun loadMore(){
        if (_loading.value) return
        _loading.value = true
        
        viewModelScope.launch(Dispatchers.IO){
            repo.loadAlbums(findAlbumLastId())
                .catch {
                    _loading.value = false
                    LogUtil.e("AlbumsViewModel","loadAlbums request error!")
                }.collect { resp ->
                    _albums.value += (resp.data?:emptyList())
                    _loading.value = false
                }
        }
    }
    
    fun findAlbumLastId() : Int {
        val list = albums.value
        if(list.isEmpty()){
            return 0
        }
        return list.last().id
    }
}
