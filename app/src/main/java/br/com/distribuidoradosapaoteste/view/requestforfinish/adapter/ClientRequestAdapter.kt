package br.com.distribuidoradosapaoteste.view.client.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import br.com.distribuidoradosapaoteste.R
import br.com.distribuidoradosapaoteste.model.Client
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ClientRequestAdapter(
    options: FirestoreRecyclerOptions<Client>,
    private val dataClient: (String, Client) -> Unit
) : FirestoreRecyclerAdapter<Client, RecyclerView.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ClientRequestFinishViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.client_request_finish_view_holder, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, model: Client) {
        val clientViewHolder = holder as ClientRequestFinishViewHolder
        clientViewHolder.bind(model)

        holder.view.findViewById<Button>(R.id.bt_ver_pedido).setOnClickListener {
            dataClient.invoke(model.idClient.orEmpty(), model)
        }
    }
}