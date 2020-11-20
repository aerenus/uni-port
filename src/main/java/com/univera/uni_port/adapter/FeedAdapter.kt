package com.univera.uni_port.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.univera.uni_port.R
import com.univera.uni_port.fragments.FeedFragment
import com.univera.uni_port.model.Feed
import kotlinx.android.synthetic.main.fragment_feed_row.view.*


class FeedAdapter(private val feedList: ArrayList<Feed>, private val listener: FeedFragment) : RecyclerView.Adapter<FeedAdapter.RowHolder>() {
    interface Listener {
        fun onItemClick(feedModel: Feed)
    }

    class RowHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(feedModel: Feed, position: Int, listener: Listener){

            itemView.setOnClickListener{
                listener.onItemClick(feedModel)
            }
            itemView.messageRow.text = feedModel.message
            itemView.userRow.text = feedModel.author
            itemView.versionRow.text = feedModel.version

            itemView.timeRow.text = feedModel.time


            if(feedModel.cat == 1) {
                if (itemView.imageView != null) {itemView.imageView.setImageResource(R.drawable.nativesms)}
                itemView.rowImage.setImageResource(R.drawable.ic_baseline_sms_24)
                itemView.rowTitle.text = "SMS"
                itemView.userRow.visibility = View.GONE;
                itemView.versionRow.visibility = View.GONE;
                itemView.paket.visibility = View.GONE


            }

            if(feedModel.cat == 2) {
                if (itemView.imageView != null) {itemView.imageView.setImageResource(R.drawable.nativedll)}
                itemView.rowImage.setImageResource(R.drawable.ic_baseline_refresh_24)
                itemView.rowTitle.text = "Güncelleme"
                itemView.userRow.visibility = View.GONE;
                itemView.versionRow.visibility = View.GONE;
                itemView.paket.visibility = View.GONE
            }

            if(feedModel.cat == 3) {
                if (itemView.imageView != null) {itemView.imageView.setImageResource(R.drawable.nativetfs)}
                itemView.rowImage.setImageResource(R.drawable.ic_baseline_code_24)
                itemView.rowTitle.text = "Azure DevOps"
                itemView.paket.text = " › " + feedModel.paket
            }

            if(feedModel.cat == 4) {
                if (itemView.imageView != null) {itemView.imageView.setImageResource(R.drawable.nativeall)}

                itemView.paket.visibility = View.GONE
            }

            if(feedModel.cat == 5) {
                if (itemView.imageView != null) {itemView.imageView.setImageResource(R.drawable.nativeall)}

                itemView.paket.visibility = View.GONE
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {
        val sharedPref: SharedPreferences? = parent.context?.getSharedPreferences(
            "com.univera.uni_port",
            Context.MODE_PRIVATE
        );
        val gorunumTip = sharedPref?.getInt("gorunumTip", 0)
        // println("fragment ids")
        // println(R.layout.fragment_feed_row) 2131492916
        // println(R.layout.feed_feed_row_izgara) 2131492914

        var rowStyle : Int = 2131492916
        if(gorunumTip == 1) {
            rowStyle = R.layout.feed_feed_row_izgara
        } else {
            rowStyle = R.layout.fragment_feed_row
        }

        val view = LayoutInflater.from(parent.context).inflate(
            rowStyle,
            parent,
            false
        )
        return RowHolder(view)
    }

    override fun onBindViewHolder(holder: RowHolder, position: Int) {

        holder.bind(feedList[position], position, listener)
        //zebra
        /*    if(position %2 == 1)
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        */

    }

    override fun getItemCount(): Int {
        return feedList.count()
    }
}
