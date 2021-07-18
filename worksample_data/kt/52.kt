package com.popularmovies.vpaliy.popularmoviesapp.ui.search

import android.content.Context
import android.graphics.Color
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.popularmovies.vpaliy.popularmoviesapp.R
import com.popularmovies.vpaliy.popularmoviesapp.ui.search.movies.MovieResult
import com.popularmovies.vpaliy.popularmoviesapp.ui.search.people.PeopleResult
import com.popularmovies.vpaliy.popularmoviesapp.ui.search.shows.TVResult
import com.popularmovies.vpaliy.popularmoviesapp.ui.view.ChipPagerAdapter
import com.popularmovies.vpaliy.popularmoviesapp.ui.view.ChipTab
import com.vpaliy.kotlin_extensions.getCompatColor
import com.vpaliy.kotlin_extensions.getDimension

class SearchAdapter(
    val context: Context,
    manager: FragmentManager
) : ChipPagerAdapter(manager), QueryListener {

  private val listeners = mutableListOf<QueryListener>()

  override fun getItem(position: Int): Fragment {
    val fragment = when (position) {
      0 -> MovieResult()
      1 -> TVResult()
      else -> PeopleResult()
    }
    fragment.callback = context as SearchResult.Callback
    listeners.add(fragment)
    return fragment
  }

  override fun getCount() = 3

  override fun getPageTitle(position: Int): String = when (position) {
    0 -> context.getString(R.string.movies)
    1 -> context.getString(R.string.tv_shows)
    else -> context.getString(R.string.people_title)
  }

  override fun queryTyped(query: String) {
    listeners.forEach { it.queryTyped(query) }
  }

  override fun inputCleared() {
    listeners.forEach { it.inputCleared() }
  }

  override fun styleFor(position: Int): ChipTab.StyleBuilder {
    return ChipTab.StyleBuilder().apply {
      this.textColor = context.getCompatColor(R.color.colorReveal)
      this.background = Color.TRANSPARENT
      this.selectedBackgroundColor = textColor
      this.selectedTextColor = Color.WHITE
      this.textAppearance = R.style.Widget_SearchChip
      this.selectedElevation = context.getDimension(R.dimen.z_app_bar)
    }
  }
}