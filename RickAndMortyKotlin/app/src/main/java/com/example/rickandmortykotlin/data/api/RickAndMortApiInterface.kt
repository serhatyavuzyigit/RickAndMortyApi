package com.example.rickandmortykotlin.data.api

import com.example.rickandmortykotlin.data.value_object.CharacterDetails
import com.example.rickandmortykotlin.data.value_object.CharacterResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortApiInterface {

    // https://rickandmortyapi.com/api/character/2
    // https://rickandmortyapi.com/api/character/?page=3
    // https://rickandmortyapi.com/api/

    @GET("character/")
    fun getCharacters(@Query("page") page: Int): Single<CharacterResponse>

    @GET("character/{character_id}")
    fun getCharacterDetails(@Path("character_id") id: Int): Single<CharacterDetails>
}