package com.example.rickandmortykotlin.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.rickandmortykotlin.data.api.RickAndMortApiInterface
import com.example.rickandmortykotlin.data.value_object.RickAndMortyCharacter
import io.reactivex.disposables.CompositeDisposable


class CharacterDataSourceFactory(private val apiService: RickAndMortApiInterface, private val compositeDisposable: CompositeDisposable)
    : DataSource.Factory<Int, RickAndMortyCharacter>() {

    val charactersLiveDataSource = MutableLiveData<CharacterDataSource>()

    override fun create(): DataSource<Int, RickAndMortyCharacter> {
        val characterDataSource = CharacterDataSource(apiService, compositeDisposable)
        charactersLiveDataSource.postValue(characterDataSource)
        return characterDataSource
    }

}