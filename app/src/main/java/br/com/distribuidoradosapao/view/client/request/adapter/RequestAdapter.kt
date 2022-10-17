package br.com.distribuidoradosapao.view.client.request.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import br.com.distribuidoradosapao.R
import br.com.distribuidoradosapao.model.Client
import br.com.distribuidoradosapao.model.Request
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class RequestAdapter(
    options: FirestoreRecyclerOptions<Request>,
    private val listener: ListenerOnDataChanged,
) : FirestoreRecyclerAdapter<Request, RecyclerView.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RequestViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.request_client_view_holder, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, model: Request) {
        val clientViewHolder = holder as RequestViewHolder
        clientViewHolder.bind(model)
    }

    override fun onDataChanged() {
        super.onDataChanged()
        if (itemCount == 0) {
            listener.onDataChanged(0)
        } else {
            listener.onDataChanged(itemCount)
        }
    }

    interface ListenerOnDataChanged {
        fun onDataChanged(countData: Int)
    }
}