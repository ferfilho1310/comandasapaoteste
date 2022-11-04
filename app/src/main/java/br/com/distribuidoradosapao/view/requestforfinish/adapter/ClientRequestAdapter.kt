package br.com.distribuidoradosapao.view.client.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import br.com.distribuidoradosapao.R
import br.com.distribuidoradosapao.model.Client
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestoreException

class ClientRequestAdapter(
    options: FirestoreRecyclerOptions<Client>,
    private val dataClient: (String, Client) -> Unit,
    private val onDataChangedListener: DataChangedListener = {},
    private val onErrorListener: ErrorListener = {}
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

    override fun onDataChanged() = onDataChangedListener.invoke(itemCount)

    override fun onError(e: FirebaseFirestoreException) = onErrorListener.invoke(e)
}