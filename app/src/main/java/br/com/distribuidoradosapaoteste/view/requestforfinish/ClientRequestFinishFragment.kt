package br.com.distribuidoradosapaoteste.view.client

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.distribuidoradosapaoteste.databinding.FragmentClientRequestFinishBinding
import br.com.distribuidoradosapaoteste.model.Client
import br.com.distribuidoradosapaoteste.util.CustomGridLayoutManager
import br.com.distribuidoradosapaoteste.view.client.adapter.ClientRequestAdapter
import br.com.distribuidoradosapaoteste.view.request.RequestClientFinishActivity
import br.com.distribuidoradosapaoteste.viewmodels.client.ClientViewModel
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

            adapterClient = ClientRequestAdapter(
                options!!,
                ::navigateRequestClientFragment
            )

            binding.rcClients.apply {
                adapter = adapterClient
                setHasFixedSize(true)
                layoutManager = CustomGridLayoutManager(context,2)
            }

            adapterClient?.startListening()
        }
    }

    private fun navigateRequestClientFragment(idClient: String, client: Client) {
        val intent = Intent(requireContext(), RequestClientFinishActivity::class.java)
        intent.putExtra("idClient", idClient)
        intent.putExtra("user", client)
        intent.putExtra("isClientRequestFinish", true)
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