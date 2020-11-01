package com.example.rickandmortykotlin.data.value_object


import com.google.gson.annotations.SerializedName

data class CharacterResponse(
    @SerializedName("info")
    val info: Info,
    @SerializedName("results")
    val rickAndMortyCharacters: List<RickAndMortyCharacter>
)