package cash.z.ecc.android.ui.zircle

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import cash.z.ecc.android.R
import cash.z.ecc.android.sdk.db.entity.ConfirmedTransaction

class ZircleAdapter<T : Zircler> :
    ListAdapter<T, ZirclerViewHolder>(
        object : DiffUtil.ItemCallback<T>() {
            override fun areItemsTheSame(
                oldItem: T,
                newItem: T
            ) = oldItem.minedHeight == newItem.minedHeight && oldItem.noteId == newItem.noteId
                    // bugfix: distinguish between self-transactions so they don't overwrite each other in the UI // TODO confirm that this is working, as intended
                    && ((oldItem.raw == null && newItem.raw == null) || (oldItem.raw != null && newItem.raw != null && oldItem.raw!!.contentEquals(newItem.raw!!)))

            override fun areContentsTheSame(
                oldItem: T,
                newItem: T
            ) = oldItem == newItem
        }
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ZirclerViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
    )

    override fun onBindViewHolder(
        holder: ZirclerViewHolder<T>,
        position: Int
    ) = holder.bindTo(getItem(position))
}

class ZirclerViewHolder {

}

data class Zircler(val nickname: String)
