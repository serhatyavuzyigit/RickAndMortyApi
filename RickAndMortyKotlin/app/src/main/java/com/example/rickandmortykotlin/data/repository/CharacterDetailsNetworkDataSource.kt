package com.example.rickandmortykotlin.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviemvvm.data.repository.NetworkState
import com.example.rickandmortykotlin.data.api.RickAndMortApiInterface
import com.example.rickandmortykotlin.data.value_object.CharacterDetails
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class CharacterDetailsNetworkDataSource(private val apiService: RickAndMortApiInterface, private val compositeDisposable: CompositeDisposable) {

    private val _networkState  = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState                   //with this get, no need to implement get function to get networkSate

    private val _downloadedCharacterDetailsResponse =  MutableLiveData<CharacterDetails>()
    val downloadedCharacterResponse: LiveData<CharacterDetails>
        get() = _downloadedCharacterDetailsResponse

    fun fetchCharacterDetails(characterId: Int) {

        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                apiService.getCharacterDetails(characterId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadedCharacterDetailsResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)
                        },
                        {
                            _networkState.postValue(NetworkState.ERROR)
                            Log.e("MovieDetailsDataSource", it.message)
                        }
                    )
            )

        }

        catch (e: Exception){
            Log.e("MovieDetailsDataSource",e.message)
        }


    }
}