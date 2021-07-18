package com.thesurix.gesturerecycler.util

import java.util.*

/**
 * @author thesurix
 */
class FixedSizeArrayDequeue<E>(private val maxSize: Int) : ArrayDeque<E>(maxSize) {

    override fun offer(e: E): Boolean {
        if (size == maxSize) {
            removeFirst()
        }

        return super.offer(e)
    }
}
