package com.sgaikar1.autoplayrecyclerview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import java.util.*


class MainActivity : AppCompatActivity() {
    private var mRecyclerView: AutoPlayRecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRecyclerView = findViewById(R.id.recyclerView)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        mRecyclerView?.setLayoutManager(layoutManager)
//        val itemDecorator = VerticalSpacingItemDecorator(10)
//        mRecyclerView.addItemDecoration(itemDecorator)
        val mediaObjects: ArrayList<MediaObject> = Resources.MEDIA_OBJECTS
        mRecyclerView?.listSize = mediaObjects.size
        val adapter = DemoRecyclerAdapter(mediaObjects, initGlide())
        mRecyclerView?.adapter = adapter
        mRecyclerView?.autoPlayListener = object : AutoPlayRecyclerView.AddAutoPlayListener {
            override fun onStartPlayingItemAt(position: Int) {
                if(position>-1) {
                    val obj = mediaObjects[position]
                    obj.isPlaying = true
                    adapter.notifyItemChanged(position)
                }
            }

            override fun onStopPlayingItemAt(position: Int) {
                if(position>-1) {
                    val obj = mediaObjects[position]
                    obj.isPlaying = false
                    adapter.notifyItemChanged(position)
                }
            }

        }
    }

    private fun initGlide(): RequestManager {
        val options: RequestOptions = RequestOptions()
            .placeholder(R.color.white)
            .error(R.color.purple_200)
        return Glide.with(this)
            .setDefaultRequestOptions(options)
    }


}