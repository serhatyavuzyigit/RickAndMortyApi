package com.example.rickandmortykotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviemvvm.data.repository.NetworkState
import com.example.rickandmortykotlin.data.api.RickAndMortApiClient
import com.example.rickandmortykotlin.data.api.RickAndMortApiInterface
import com.example.rickandmortykotlin.ui.character_details.SingleCharacter
import com.example.rickandmortykotlin.ui.character_details.SingleCharacterViewModel
import com.example.rickandmortykotlin.ui.main_screen.CharacterPageListRepository
import com.example.rickandmortykotlin.ui.main_screen.CharactersPageListAdapter
import com.example.rickandmortykotlin.ui.main_screen.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainActivityViewModel: MainActivityViewModel

    lateinit var characterPageListRepository: CharacterPageListRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiService : RickAndMortApiInterface = RickAndMortApiClient.getClient()

        characterPageListRepository = CharacterPageListRepository(apiService)
        mainActivityViewModel = getViewModel()

        val characterPageListAdapter = CharactersPageListAdapter(this)

        val gridLayoutManager = GridLayoutManager(this, 3)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = characterPageListAdapter.getItemViewType(position)
                if(viewType == characterPageListAdapter.CHARACTER_VIEW) {
                    return 1
                }
                else
                    return 3
            }
        }

        all_characters_list.layoutManager = gridLayoutManager
        all_characters_list.setHasFixedSize(true)
        all_characters_list.adapter = characterPageListAdapter

        mainActivityViewModel.characterPageList.observe(this, Observer {
            characterPageListAdapter.submitList(it)
        })

        mainActivityViewModel.networkState.observe(this, Observer {
            if(mainActivityViewModel.listIsEmpty() && it== NetworkState.LOADING)
                all_characters_progress_bar.visibility = View.VISIBLE
            else
                all_characters_progress_bar.visibility = View.GONE

            if(!mainActivityViewModel.listIsEmpty()) {
                characterPageListAdapter.setNetworkState(it)
            }
        })

    }

    private fun getViewModel(): MainActivityViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainActivityViewModel(characterPageListRepository) as T
            }
        })[MainActivityViewModel::class.java]
    }
}
