package com.example.rickandmortykotlin.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.moviemvvm.data.repository.NetworkState
import com.example.rickandmortykotlin.data.api.FIRST_PAGE
import com.example.rickandmortykotlin.data.api.RickAndMortApiInterface

import com.example.rickandmortykotlin.data.value_object.RickAndMortyCharacter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CharacterDataSource(private val apiService: RickAndMortApiInterface, private val compositeDisposable: CompositeDisposable)
    : PageKeyedDataSource<Int, RickAndMortyCharacter>() {

    private var page = FIRST_PAGE
    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, RickAndMortyCharacter>) {
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, RickAndMortyCharacter>) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.getCharacters(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if(it.info.pages >= params.key ) {
                            callback.onResult(it.rickAndMortyCharacters, params.key+1)
                            networkState.postValue(NetworkState.LOADED)
                        } else {
                            networkState.postValue(NetworkState.ENDLIST )
                        }
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("CharacterDataSource", it.message)
                    }
                )
        )
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, RickAndMortyCharacter>) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.getCharacters(page)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.rickAndMortyCharacters, null, page+1)
                        networkState.postValue(NetworkState.LOADED)
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("CharacterDataSource", it.message)
                    }
                )
        )
    }

}