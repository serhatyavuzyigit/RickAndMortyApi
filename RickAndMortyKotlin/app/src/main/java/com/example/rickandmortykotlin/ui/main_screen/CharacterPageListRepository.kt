package com.example.rickandmortykotlin.ui.main_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.moviemvvm.data.repository.NetworkState
import com.example.rickandmortykotlin.data.api.POST_PER_PAGE
import com.example.rickandmortykotlin.data.api.RickAndMortApiInterface
import com.example.rickandmortykotlin.data.repository.CharacterDataSource
import com.example.rickandmortykotlin.data.repository.CharacterDataSourceFactory
import com.example.rickandmortykotlin.data.value_object.RickAndMortyCharacter
import io.reactivex.disposables.CompositeDisposable

class CharacterPageListRepository(private val apiService: RickAndMortApiInterface) {

    lateinit var characterPagedList: LiveData<PagedList<RickAndMortyCharacter>>
    lateinit var characterDataSourceFactory: CharacterDataSourceFactory

    fun fetchLiveCharacterPagedList (compositeDisposable: CompositeDisposable): LiveData<PagedList<RickAndMortyCharacter>> {
        characterDataSourceFactory = CharacterDataSourceFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        characterPagedList = LivePagedListBuilder(characterDataSourceFactory, config).build()

        return characterPagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<CharacterDataSource, NetworkState>(
            characterDataSourceFactory.charactersLiveDataSource, CharacterDataSource::networkState)
    }
}