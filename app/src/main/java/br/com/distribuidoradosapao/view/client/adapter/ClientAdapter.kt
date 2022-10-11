package br.com.distribuidoradosapao.view.client.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import br.com.distribuidoradosapao.R
import br.com.distribuidoradosapao.model.Client
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ClientAdapter(
    options: FirestoreRecyclerOptions<Client>,
    private val listener: ListenerOnDataChanged,
    private val idClient: (String) -> Unit
) : FirestoreRecyclerAdapter<Client, RecyclerView.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ClientViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.client_view_holder, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, model: Client) {
        val clientViewHolder = holder as ClientViewHolder
        clientViewHolder.bind(model)
        holder.view.findViewById<Button>(R.id.bt_anotar_pedido).setOnClickListener {
            idClient.invoke(snapshots.getSnapshot(position).reference.id)
        }
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