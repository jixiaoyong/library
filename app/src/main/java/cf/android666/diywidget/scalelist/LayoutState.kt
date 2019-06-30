/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cf.android666.diywidget.scalelist

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Helper class that keeps temporary state while {LayoutManager} is filling out the empty
 * space.
 */
internal class LayoutState {

    /**
     * We may not want to recycle children in some cases (e.g. layout)
     */
    var mRecycle = true

    /**
     * Number of pixels that we should fill, in the layout direction.
     */
    var mAvailable: Int = 0

    /**
     * Current position on the adapter to get the next item.
     */
    var mCurrentPosition: Int = 0

    /**
     * Defines the direction in which the data adapter is traversed.
     * Should be [.ITEM_DIRECTION_HEAD] or [.ITEM_DIRECTION_TAIL]
     */
    var mItemDirection: Int = 0

    /**
     * Defines the direction in which the layout is filled.
     * Should be [.LAYOUT_START] or [.LAYOUT_END]
     */
    var mLayoutDirection: Int = 0

    /**
     * This is the target pixel closest to the start of the layout that we are trying to fill
     */
    var mStartLine = 0

    /**
     * This is the target pixel closest to the end of the layout that we are trying to fill
     */
    var mEndLine = 0

    /**
     * If true, layout should stop if a focusable view is added
     */
    var mStopInFocusable: Boolean = false

    /**
     * If the content is not wrapped with any value
     */
    var mInfinite: Boolean = false

    /**
     * @return true if there are more items in the data adapter
     */
    fun hasMore(state: RecyclerView.State): Boolean {
        return mCurrentPosition >= 0 && mCurrentPosition < state.itemCount
    }

    /**
     * Gets the view for the next element that we should render.
     * Also updates current item index to the next item, based on [.mItemDirection]
     *
     * @return The next element that we should render.
     */
    fun next(recycler: RecyclerView.Recycler): View {
        val view = recycler.getViewForPosition(mCurrentPosition)
        mCurrentPosition += mItemDirection
        return view
    }

    override fun toString(): String {
        return ("LayoutState{"
                + "mAvailable=" + mAvailable
                + ", mCurrentPosition=" + mCurrentPosition
                + ", mItemDirection=" + mItemDirection
                + ", mLayoutDirection=" + mLayoutDirection
                + ", mStartLine=" + mStartLine
                + ", mEndLine=" + mEndLine
                + '}'.toString())
    }

    companion object {

        val TAG = "LayoutState"

        val LAYOUT_START = -1

        val LAYOUT_END = 1

        val INVALID_LAYOUT = Integer.MIN_VALUE

        val ITEM_DIRECTION_HEAD = -1

        val ITEM_DIRECTION_TAIL = 1
    }
}
