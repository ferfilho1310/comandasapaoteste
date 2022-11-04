package br.com.distribuidoradosapao.view.client.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import br.com.distribuidoradosapao.R
import br.com.distribuidoradosapao.model.Client
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestoreException

typealias DataChangedListener = (count: Int) -> Unit
typealias ErrorListener = (error: FirebaseFirestoreException) -> Unit

class ClientAdapter(
    options: FirestoreRecyclerOptions<Client>,
    private val dataClient: (String, Client) -> Unit,
    private val idClient: (String) -> Unit,
    private val onDataChangedListener: DataChangedListener = {},
    private val onErrorListener: ErrorListener = {}
) : FirestoreRecyclerAdapter<Client, RecyclerView.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ClientViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.client_view_holder, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, model: Client) {
        val clientViewHolder = holder as ClientViewHolder
        clientViewHolder.bind(model)
        holder.view.findViewById<Button>(R.id.bt_anotar_pedido).apply {
            setOnClickListener {
                dataClient.invoke(snapshots.getSnapshot(position).reference.id, model)
            }
        }
        holder.view.findViewById<ImageButton>(R.id.bt_editar_cliente).apply {
            setOnClickListener {
                idClient.invoke(snapshots.getSnapshot(position).reference.id)
            }
        }
    }

    override fun onDataChanged() = onDataChangedListener.invoke(itemCount)

    override fun onError(e: FirebaseFirestoreException) = onErrorListener.invoke(e)
}