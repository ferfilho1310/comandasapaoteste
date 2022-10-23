package br.com.distribuidoradosapao.view.client

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.distribuidoradosapao.databinding.FragmentClientBinding
import br.com.distribuidoradosapao.databinding.FragmentClientRequestFinishBinding
import br.com.distribuidoradosapao.model.Client
import br.com.distribuidoradosapao.view.client.adapter.ClientRequestFinishAdapter
import br.com.distribuidoradosapao.view.request.RequestClientActivity
import br.com.distribuidoradosapao.viewmodels.client.ClientViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import org.koin.android.ext.android.inject

class ClientRequestFinishFragment : Fragment() {

    private var _binding: FragmentClientRequestFinishBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ClientViewModel by inject()

    private var adapterClient: ClientRequestFinishAdapter? = null
    private var options: FirestoreRecyclerOptions<Client>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientRequestFinishBinding.inflate(inflater, container, false)

        setViewModel()
        viewModel.loadClients()
        return binding.root
    }

    private fun setViewModel() {
        viewModel.loadClient.observe(viewLifecycleOwner) {

            options =
                FirestoreRecyclerOptions.Builder<Client>().setQuery(it, Client::class.java).build()

            adapterClient = ClientRequestFinishAdapter(options!!, ::navigateRequestClientFragment)

            binding.rcClients.apply {
                adapter = adapterClient
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }

            adapterClient?.startListening()
        }
    }

    private fun navigateRequestClientFragment(idClient: String, client: Client) {
        val intent = Intent(requireContext(), RequestClientActivity::class.java)
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