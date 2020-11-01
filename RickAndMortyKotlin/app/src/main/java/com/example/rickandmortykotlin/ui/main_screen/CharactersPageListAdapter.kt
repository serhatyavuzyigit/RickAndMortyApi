package com.example.rickandmortykotlin.ui.main_screen

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviemvvm.data.repository.NetworkState
import com.example.rickandmortykotlin.R
import com.example.rickandmortykotlin.data.value_object.RickAndMortyCharacter
import com.example.rickandmortykotlin.ui.character_details.SingleCharacter
import kotlinx.android.synthetic.main.activity_single_character.view.*
import kotlinx.android.synthetic.main.character_list_item.view.*
import kotlinx.android.synthetic.main.network_state_item.view.*

class CharactersPageListAdapter(public val context: Context): PagedListAdapter<RickAndMortyCharacter, RecyclerView.ViewHolder>(CharacterDiffCallback()) {


    val CHARACTER_VIEW = 1
    val NETWORK_VIEW = 2

    private var networkState: NetworkState? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        if(viewType == CHARACTER_VIEW) {
            view = layoutInflater.inflate(R.layout.character_list_item, parent, false)
            return CharacterItemViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            return NetworkStateItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == CHARACTER_VIEW) {
            (holder as CharacterItemViewHolder).bind(getItem(position), context)
        } else {
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    private fun hasExtra(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        var extra = 0
        if(hasExtra()) extra = 1
        return super.getItemCount() + extra

    }

    override fun getItemViewType(position: Int): Int {
        if(hasExtra() && position==itemCount-1)
            return NETWORK_VIEW
        else
            return CHARACTER_VIEW
    }

    fun setNetworkState(newState: NetworkState) {
        val previousState = this.networkState
        val hadExtra = hasExtra()
        this.networkState = newState
        val hasExtra = hasExtra()

        if(hadExtra != hasExtra) {
            if(hadExtra) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if(hasExtra && previousState != newState) {
            notifyItemChanged(itemCount - 1)
        } else {

        }

    }


    class CharacterDiffCallback: DiffUtil.ItemCallback<RickAndMortyCharacter>() {
        override fun areItemsTheSame(
            oldItem: RickAndMortyCharacter,
            newItem: RickAndMortyCharacter
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: RickAndMortyCharacter,
            newItem: RickAndMortyCharacter
        ): Boolean {
            return oldItem == newItem
        }

    }

    class CharacterItemViewHolder (view: View): RecyclerView.ViewHolder(view) {

        fun bind(character: RickAndMortyCharacter?, context: Context) {
            itemView.main_page_character_name.text = character?.name

            val imageUrl = character?.image
            Glide.with(itemView.context)
                .load(imageUrl)
                .into(itemView.main_page_character_image)

            itemView.setOnClickListener{
                val intent = Intent(context, SingleCharacter::class.java)
                intent.putExtra("id", character?.id)
                context.startActivity(intent)
            }
        }
    }

    class NetworkStateItemViewHolder (view: View): RecyclerView.ViewHolder(view) {

        fun bind(networkState: NetworkState?) {
            if(networkState != null && networkState == NetworkState.LOADING)
                itemView.progress_bar_item.visibility = View.VISIBLE
            else
                itemView.progress_bar_item.visibility = View.GONE

            if(networkState != null && networkState == NetworkState.ERROR) {
                itemView.error_msg_item.visibility = View.VISIBLE
                itemView.error_msg_item.text = networkState.msg
            } else if(networkState != null && networkState == NetworkState.ENDLIST) {
                itemView.error_msg_item.visibility = View.VISIBLE
                itemView.error_msg_item.text = networkState.msg
            } else {
                itemView.error_msg_item.visibility = View.GONE
            }

        }
    }
}