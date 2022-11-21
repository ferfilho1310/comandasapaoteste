package br.com.distribuidoradosapaoteste.view.requestforfinish.adapterRecebidosParcial

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import br.com.distribuidoradosapaoteste.R
import br.com.distribuidoradosapaoteste.model.RequestReceivedPartial
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class RequestParcialAdapter(
    options: FirestoreRecyclerOptions<RequestReceivedPartial>,
    private val listenerDeleteRequestReceived: ListenerDeleteRequestReceived? = null,
    private var isClientRequestFinish: Boolean = false
) : FirestoreRecyclerAdapter<RequestReceivedPartial, RecyclerView.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RequestParcialViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_pedidos_recebidos_parcial, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        model: RequestReceivedPartial
    ) {
        val clientViewHolder = holder as RequestParcialViewHolder
        clientViewHolder.bind(model)
        clientViewHolder.view.findViewById<ImageButton>(R.id.img_delete_request_received).apply {
            setOnClickListener {
                listenerDeleteRequestReceived?.onDeleteRequestReceived(
                    snapshots.getSnapshot(
                        position
                    ).id
                )
            }
            isVisible = !isClientRequestFinish
        }
    }

    interface ListenerDeleteRequestReceived {
        fun onDeleteRequestReceived(idRequestReceived: String)
    }
}