package br.com.distribuidoradosapao.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver


class AdapterViewEmpty(view: View?, recyclerView: RecyclerView) :
    AdapterDataObserver() {
    var view: View?
    var recyclerView: RecyclerView

    init {
        this.view = view
        this.recyclerView = recyclerView
    }

    private fun checkIfEmpty() {
        if (view != null && recyclerView.adapter != null) {
            val emptyViewVisible = recyclerView.adapter!!.itemCount == 0
            view!!.visibility = if (emptyViewVisible) View.VISIBLE else View.GONE
            recyclerView.visibility = if (emptyViewVisible) View.GONE else View.VISIBLE
        }
    }

    override fun onChanged() {
        checkIfEmpty()
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        checkIfEmpty()
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        checkIfEmpty()
    }
}