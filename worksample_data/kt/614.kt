package xyz.hisname.fireflyiii.ui.base

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.launch

// Code taken from: https://geoffreymetais.github.io/code/diffutil-threading/
abstract class DiffUtilAdapter<D, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    protected var dataSet: List<D> = listOf()
    private val diffCallback by lazy(LazyThreadSafetyMode.NONE) { DiffCallback() }
    private val eventActor by lazy { GlobalScope.actor<List<D>>(Dispatchers.IO, capacity = Channel.CONFLATED,
            block = {
                for (list in channel) internalUpdate(list)
            })
    }

    fun update (list: List<D>) = eventActor.offer(list)

    private suspend fun internalUpdate(list: List<D>) {
        GlobalScope.launch(Dispatchers.IO, CoroutineStart.LAZY) {
            dataSet = list
            DiffUtil.calculateDiff(diffCallback.apply { newList = list },false)
                    .dispatchUpdatesTo(this@DiffUtilAdapter)
        }.join()

    }

    private inner class DiffCallback : DiffUtil.Callback() {
        lateinit var newList: List<D>
        override fun getOldListSize() = dataSet.size
        override fun getNewListSize() = newList.size
        override fun areContentsTheSame(oldItemPosition : Int, newItemPosition : Int) = true
        override fun areItemsTheSame(oldItemPosition : Int, newItemPosition : Int) =
                dataSet[oldItemPosition] == newList[newItemPosition]
    }
}
