package br.com.distribuidoradosapao.view.client

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import br.com.distribuidoradosapao.databinding.FragmentClientRequestFinishBinding
import br.com.distribuidoradosapao.model.Client
import br.com.distribuidoradosapao.view.client.adapter.ClientRequestAdapter
import br.com.distribuidoradosapao.view.request.RequestClientFinishActivity
import br.com.distribuidoradosapao.viewmodels.client.ClientViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import org.koin.android.ext.android.inject

class ClientRequestFinishFragment : Fragment() {

    private var _binding: FragmentClientRequestFinishBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ClientViewModel by inject()

    private var adapterClient: ClientRequestAdapter? = null
    private var options: FirestoreRecyclerOptions<Client>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientRequestFinishBinding.inflate(inflater, container, false)

        setViewModel()
        viewModel.loadClientDeleted()
        return binding.root
    }

    private fun setViewModel() {
        viewModel.loadClientDeleted.observe(viewLifecycleOwner) {

            options =
                FirestoreRecyclerOptions.Builder<Client>().setQuery(it, Client::class.java).build()

            adapterClient = ClientRequestAdapter(options!!, ::navigateRequestClientFragment)

            binding.rcClients.apply {
                adapter = adapterClient
                setHasFixedSize(true)
                layoutManager = GridLayoutManager(context, 2)
            }

            adapterClient?.startListening()
        }
    }

    private fun navigateRequestClientFragment(idClient: String, client: Client) {
        val intent = Intent(requireContext(), RequestClientFinishActivity::class.java)
        intent.putExtra("idClient", idClient)
        intent.putExtra("user", client)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): ClientRequestFinishFragment {
            return ClientRequestFinishFragment()
        }
    }
}