package br.com.distribuidoradosapao.view.request.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import br.com.distribuidoradosapao.R
import br.com.distribuidoradosapao.model.Request
import br.com.distribuidoradosapao.view.client.adapter.DataChangedListener
import br.com.distribuidoradosapao.view.client.adapter.ErrorListener
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestoreException

class RequestAdapter(
    options: FirestoreRecyclerOptions<Request>,
    private val listernerEditRequest: ListenerEditRequest,
    private val onDataChangedListener: DataChangedListener = {},
) : FirestoreRecyclerAdapter<Request, RecyclerView.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RequestViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.request_client_view_holder, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, model: Request) {
        val requestViewHolder = holder as RequestViewHolder
        requestViewHolder.bind(model)
        requestViewHolder.view.findViewById<ImageButton>(R.id.img_edit_request).setOnClickListener {
            listernerEditRequest.onEditRequest(snapshots.getSnapshot(position).reference.id, model)
        }
    }

    override fun onDataChanged() = onDataChangedListener.invoke(itemCount)

    interface ListenerEditRequest {
        fun onEditRequest(idRequest: String,  model: Request)
    }
}