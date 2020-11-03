package com.example.rickandmortykotlin.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.moviemvvm.data.repository.NetworkState
import com.example.rickandmortykotlin.data.value_object.CharacterDetails
import com.example.rickandmortykotlin.ui.character_details.CharacterDetailsRepository
import io.reactivex.disposables.CompositeDisposable

class SingleCharacterViewModel (private val characterDetailsRepository: CharacterDetailsRepository, characterId: Int): ViewModel() {

    private val compositeDisposable = CompositeDisposable()


    val characterDetails : LiveData<CharacterDetails> by lazy {
        characterDetailsRepository.getCharacterDetails(compositeDisposable, characterId)
    }

    val newtworkState : LiveData<NetworkState> by lazy {
        characterDetailsRepository.getCharacterDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}