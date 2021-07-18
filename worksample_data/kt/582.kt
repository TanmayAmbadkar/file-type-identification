package com.thesurix.gesturerecycler.transactions

import com.thesurix.gesturerecycler.util.swap

/**
 * @author thesurix
 */
class SwapTransaction<T>(private val firstIndex: Int,
                         private val secondIndex: Int) : Transaction<T> {

    override fun perform(transactional: Transactional<T>): Boolean {
        transactional.data.swap(firstIndex, secondIndex)
        notifyChanged(transactional)
        return true
    }

    override fun revert(transactional: Transactional<T>): Boolean {
        transactional.data.swap(secondIndex, firstIndex)
        notifyChanged(transactional)
        return true
    }

    private fun notifyChanged(transactional: Transactional<T>) {
        transactional.notifyChanged(firstIndex)
        transactional.notifyChanged(secondIndex)
    }
}