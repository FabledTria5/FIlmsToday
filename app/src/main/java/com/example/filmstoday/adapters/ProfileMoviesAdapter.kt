package com.example.filmstoday.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmstoday.adapters.viewholders.DoubleGridViewHolder
import com.example.filmstoday.adapters.viewholders.FavoriteActorViewHolder
import com.example.filmstoday.adapters.viewholders.SimpleItemViewHolder
import com.example.filmstoday.adapters.viewholders.TripleGridViewHolder
import com.example.filmstoday.data.FavoriteActor
import com.example.filmstoday.models.movie.SimpleMovie

class ProfileMoviesAdapter(private val layoutManager: GridLayoutManager? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isReverted = false

    enum class ViewType {
        SMALL,
        MEDIUM,
        BIG,
        ACTOR
    }

    private val itemsList = arrayListOf<Any>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.SMALL.ordinal -> SimpleItemViewHolder(parent = parent)
            ViewType.MEDIUM.ordinal -> DoubleGridViewHolder(parent = parent)
            ViewType.BIG.ordinal -> TripleGridViewHolder(parent = parent)
            else -> FavoriteActorViewHolder(parent = parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SimpleItemViewHolder -> holder.bind(movieFullModel = itemsList[position] as SimpleMovie)
            is DoubleGridViewHolder -> holder.bind(movieFullModel = itemsList[position] as SimpleMovie)
            is TripleGridViewHolder -> holder.bind(movieFullModel = itemsList[position] as SimpleMovie)
            is FavoriteActorViewHolder -> holder.bind(favoriteActor = itemsList[position] as FavoriteActor)
        }
    }

    override fun getItemCount() = itemsList.count()

    override fun getItemViewType(position: Int): Int {
        return when (itemsList[position]) {
            is SimpleMovie -> {
                when (layoutManager?.spanCount) {
                    1 -> ViewType.SMALL.ordinal
                    2 -> ViewType.MEDIUM.ordinal
                    else -> ViewType.BIG.ordinal
                }
            }
            is FavoriteActor -> ViewType.ACTOR.ordinal
            else -> throw RuntimeException("Unexpected data type")
        }
    }

    fun addItems(items: List<Any>) = itemsList.addAll(items)

    fun clearMovies() = itemsList.clear()

    fun deleteItem(position: Int) {
        itemsList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun revertList() = itemsList.apply {
        reverse()
        notifyItemRangeChanged(0, count())
        isReverted = !isReverted
    }

    fun isReverted() = isReverted

    fun alphabetFiler() {
        itemsList.sortWith { item1, item2 ->
            (item1 as SimpleMovie).movieTitle.compareTo((item2 as SimpleMovie).movieTitle)
        }
        notifyItemRangeChanged(0, itemsList.count())
    }

    fun releaseFilter() {
        itemsList.sortWith { item1, item2 ->
            ((item1 as SimpleMovie).movieReleaseDate.compareTo((item2 as SimpleMovie).movieReleaseDate))
        }
        notifyItemRangeChanged(0, itemsList.count())

    }

    fun ratingFilter() {
        itemsList.sortWith { item1, item2 ->
            ((item1 as SimpleMovie).movieRating.compareTo((item2 as SimpleMovie).movieRating))
        }
        notifyItemRangeChanged(0, itemsList.count())
    }

    fun dateFilter() {
        itemsList.sortWith { item1, item2 ->
            ((item1 as SimpleMovie).order.compareTo((item2 as SimpleMovie).order))
        }
        notifyItemRangeChanged(0, itemsList.count())
    }
}