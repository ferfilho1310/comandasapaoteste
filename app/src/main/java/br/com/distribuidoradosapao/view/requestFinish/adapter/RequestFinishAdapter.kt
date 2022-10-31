package br.com.distribuidoradosapao.view.request.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.distribuidoradosapao.R
import br.com.distribuidoradosapao.model.Request
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class RequestFinishAdapter(
    options: FirestoreRecyclerOptions<Request>,
    private val listener: ListenerOnDataChanged,
) : FirestoreRecyclerAdapter<Request, RecyclerView.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RequestFinishViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.request_finish_client_view_holder, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, model: Request) {
        val requestViewHolder = holder as RequestFinishViewHolder
        requestViewHolder.bind(model)
    }

    override fun onDataChanged() {
        super.onDataChanged()
        if (itemCount == 0) {
            listener.onDataChanged(0)
            return
        } else {
            listener.onDataChanged(itemCount)
            return
        }
    }

    interface ListenerOnDataChanged {
        fun onDataChanged(countData: Int)
    }
}