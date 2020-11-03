package com.example.rickandmortykotlin.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.moviemvvm.data.repository.NetworkState
import com.example.rickandmortykotlin.data.value_object.RickAndMortyCharacter
import com.example.rickandmortykotlin.ui.main_screen.CharacterPageListRepository
import io.reactivex.disposables.CompositeDisposable

class MainActivityViewModel(private val characterPageListRepository: CharacterPageListRepository): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val characterPageList: LiveData<PagedList<RickAndMortyCharacter>> by lazy {
        characterPageListRepository.fetchLiveCharacterPagedList(compositeDisposable)
    }

    val networkState: LiveData<NetworkState> by lazy {
        characterPageListRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return characterPageList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}