package com.example.rickandmortykotlin.ui.character_details

import androidx.lifecycle.LiveData
import com.example.moviemvvm.data.repository.NetworkState
import com.example.rickandmortykotlin.data.api.RickAndMortApiInterface
import com.example.rickandmortykotlin.data.repository.CharacterDetailsNetworkDataSource
import com.example.rickandmortykotlin.data.value_object.CharacterDetails
import io.reactivex.disposables.CompositeDisposable

class CharacterDetailsRepository(private val apiService: RickAndMortApiInterface) {

    lateinit var characterDetailsNetworkDataSource: CharacterDetailsNetworkDataSource

    fun getCharacterDetails(compositeDisposable: CompositeDisposable, characterId: Int): LiveData<CharacterDetails>{
        characterDetailsNetworkDataSource = CharacterDetailsNetworkDataSource(apiService, compositeDisposable)
        characterDetailsNetworkDataSource.fetchCharacterDetails(characterId)

        return characterDetailsNetworkDataSource.downloadedCharacterResponse
    }

    fun getCharacterDetailsNetworkState(): LiveData<NetworkState> {
        return characterDetailsNetworkDataSource.networkState
    }
}