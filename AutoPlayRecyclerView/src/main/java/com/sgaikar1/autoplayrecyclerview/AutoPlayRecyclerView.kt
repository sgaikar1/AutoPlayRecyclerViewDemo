package com.sgaikar1.autoplayrecyclerview

import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AutoPlayRecyclerView : RecyclerView {
    var listSize = 0
        set(value) {
            field = value
        }
    var autoPlayListener: AddAutoPlayListener? = null
        set(value) {
            field = value
        }
    private var itemViewDefaultHeight = 0
    private var screenDefaultHeight = 0
    private var playPosition = -1
    private var mContext: Context? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        mContext = context

        val display =
            (getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val point = Point()
        display.getSize(point)
        itemViewDefaultHeight = point.x
        screenDefaultHeight = point.y
        addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == SCROLL_STATE_IDLE) {
                    // When the end of the list has been reached.
                    // Need to handle that with this bit of logic
                    if (!recyclerView.canScrollVertically(1)) {
                        playItem(true)
                    } else {
                        playItem(false)
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    fun playItem(isEndOfList: Boolean) {
        val targetPosition: Int
        if (!isEndOfList) {
            val startPosition: Int =
                (layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
            var endPosition: Int =
                (layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()

            // if there is more than 2 list-items on the screen, set the difference to be 1
            if (endPosition - startPosition > 1) {
                endPosition = startPosition + 1
            }

            // something is wrong. return.
            if (startPosition < 0 || endPosition < 0) {
                return
            }

            // if there is more than 1 list-item on the screen
            targetPosition = if (startPosition != endPosition) {
                val startPositionItemHeight = getVisibleItemViewHeight(startPosition)
                val endPositionItemHeight = getVisibleItemViewHeight(endPosition)
                if (startPositionItemHeight > endPositionItemHeight) startPosition else endPosition
            } else {
                startPosition
            }
        } else {
            targetPosition = listSize - 1
        }
        Log.d(TAG, "Playing Item: target position: $targetPosition")

        // Item is already playing so return
        if (targetPosition == playPosition) {
            return
        }
        stopPlaying()

        // set the position of the recyclerview-item that is to be played
        updatePlayPosition(targetPosition)
        startPlaying(targetPosition)
    }

    /**
     * Returns the visible region of the item on the screen.
     * if some is cut off, it will return less than the @itemViewDefaultHeight
     *
     * @param playingPosition
     * @return
     */
    private fun getVisibleItemViewHeight(playingPosition: Int): Int {
        val visiblePostion: Int =
            playingPosition - (layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
        Log.d(TAG, "getVisibleItemViewHeight: at: $visiblePostion")
        val child = getChildAt(visiblePostion) ?: return 0
        val location = IntArray(2)
        child.getLocationInWindow(location)
        return if (location[1] < 0) {
            location[1] + itemViewDefaultHeight
        } else {
            screenDefaultHeight - location[1]
        }
    }

    fun stopPlaying() {
        autoPlayListener?.let { it.onStopPlayingItemAt(playPosition) }
    }

    fun startPlaying(position: Int) {
        autoPlayListener?.let { it.onStartPlayingItemAt(position) }
    }

    fun updatePlayPosition(position: Int) {
        playPosition = position
    }

    interface AddAutoPlayListener {
        fun onStartPlayingItemAt(position: Int)
        fun onStopPlayingItemAt(position: Int)
    }

    companion object {
        private const val TAG = "AutoPlayRecyclerView"
    }
}