package com.sgaikar1.autoplayrecyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager


class DemoRecyclerAdapter(
    val mediaObjects: ArrayList<MediaObject>,
    val requestManager: RequestManager,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        return PlayingViewHolder(
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.layout_list_item, viewGroup, false)
        )
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, i: Int) {
        (viewHolder as PlayingViewHolder).onBind(mediaObjects[i], requestManager)
    }

    override fun getItemCount(): Int {
        return mediaObjects.size
    }

    class PlayingViewHolder(var parent: View) : RecyclerView.ViewHolder(
        parent
    ) {
        private var title: TextView = itemView.findViewById(R.id.title)
        var thumbnail: ImageView = itemView.findViewById(R.id.thumbnail)
        var requestManager: RequestManager? = null

        fun onBind(mediaObject: MediaObject, requestManager: RequestManager?) {
            this.requestManager = requestManager
            parent.tag = this
            title.text = mediaObject.title
            title.isSelected = mediaObject.isPlaying
            this.requestManager?.let {
                it.load(mediaObject.thumbnail)
                    .into(thumbnail)
            }
        }
    }

}
