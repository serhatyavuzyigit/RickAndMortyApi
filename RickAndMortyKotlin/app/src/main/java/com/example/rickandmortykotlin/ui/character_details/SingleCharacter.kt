package com.example.rickandmortykotlin.ui.character_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.moviemvvm.data.repository.NetworkState
import com.example.rickandmortykotlin.R
import com.example.rickandmortykotlin.data.api.RickAndMortApiClient
import com.example.rickandmortykotlin.data.api.RickAndMortApiInterface
import com.example.rickandmortykotlin.data.value_object.CharacterDetails
import com.example.rickandmortykotlin.ui.viewmodel.SingleCharacterViewModel
import kotlinx.android.synthetic.main.activity_single_character.*

class SingleCharacter : AppCompatActivity() {

    private lateinit var singleCharacterViewModel: SingleCharacterViewModel
    private lateinit var characterDetailsRepository: CharacterDetailsRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_character)

        val characterId: Int = intent.getIntExtra("id", 1)
        val apiService: RickAndMortApiInterface = RickAndMortApiClient.getClient()
        characterDetailsRepository = CharacterDetailsRepository(apiService)

        singleCharacterViewModel = getViewModel(characterId)
        singleCharacterViewModel.characterDetails.observe(this, Observer {
            updateToUI(it)
        })

        singleCharacterViewModel.newtworkState.observe(this, Observer {
            progress_bar.visibility = if(it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility = if(it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })
    }

    private fun updateToUI(it: CharacterDetails?) {
        if (it != null) {
            character_name.text =  it.name
        }
        if (it != null) {
            character_status.text = it.status
        }
        if (it != null) {
            character_location.text = it.location.name
        }
        val imageUrl = it?.image
        Glide.with(this).load(imageUrl).into(character_poster)
    }

    private fun getViewModel(characterId: Int): SingleCharacterViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SingleCharacterViewModel(
                    characterDetailsRepository,
                    characterId
                ) as T
            }
        })[SingleCharacterViewModel::class.java]
    }
}
