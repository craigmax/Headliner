package com.example.max.headliner

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_row.view.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ofPattern

class MainAdapter(val newsFeed: NewsFeed): RecyclerView.Adapter<CustomViewHolder>() {

    //number of Items
    override fun getItemCount(): Int {
        return newsFeed.results.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        //Create view
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.movie_row, parent, false)
        return CustomViewHolder(cellForRow)
    }

    //Bind items
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        val movie = newsFeed.results.get(position)

        holder?.view?.txtMovieTitle?.text = movie.display_title

        holder?.view?.txtSummary?.text = movie.summary_short

        //Avoid unsightly blank fields. Set to friendly text if field is empty.
        if (movie.opening_date.isNullOrEmpty()) {
            holder?.view?.txtOpening?.text = "Coming Soon"

        } else {
            // Format to long Date
            val shortDate = LocalDate.parse(movie.opening_date, DateTimeFormatter.ISO_DATE)
            val longDate = shortDate.format(ofPattern("MMM dd, yyyy"))

            //Display date
            holder?.view?.txtOpening?.text = longDate

        }

        //Avoid unsightly blank fields. Set to friendly text if field is empty.
        if (movie.mpaa_rating.isNullOrEmpty()) {
            holder?.view?.txtRating?.text = "Not yet rated"

        } else {

            holder?.view?.txtRating?.text = movie.mpaa_rating
        }

        //Set Icon source
        Picasso.get().load(movie.multimedia.src).into(holder?.view?.imgMovie)

    }

}

//Define custom view holder
class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view){


}