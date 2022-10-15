package br.com.distribuidoradosapao.view.client

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.distribuidoradosapao.databinding.FragmentClientBinding
import br.com.distribuidoradosapao.model.Client
import br.com.distribuidoradosapao.view.client.adapter.ClientAdapter
import br.com.distribuidoradosapao.view.client.request.RequestClientActivity
import br.com.distribuidoradosapao.viewmodels.client.ClientViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import org.koin.android.ext.android.inject

class ClientFragment : Fragment() {

    private var _binding: FragmentClientBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ClientViewModel by inject()
    private var adapterClient: ClientAdapter? = null
    private var options: FirestoreRecyclerOptions<Client>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientBinding.inflate(inflater, container, false)

        setViewModel()
        viewModel.loadClient()

        return binding.root
    }

    private fun setViewModel() {
        viewModel.loadClient.observe(viewLifecycleOwner) {
            options = FirestoreRecyclerOptions.Builder<Client>()
                .setQuery(it, Client::class.java)
                .build()

            adapterClient = ClientAdapter(options!!, object : ClientAdapter.ListenerOnDataChanged {
                override fun onDataChanged(countData: Int) {
                    if (countData == 0) {
                        setVisibility(
                            isVisibleLottie = true,
                            isVisibleRecyclerView = false,
                            isVisibleTextLottie = true
                        )
                    } else {
                        setVisibility(
                            isVisibleLottie = false,
                            isVisibleRecyclerView = true,
                            isVisibleTextLottie = false
                        )
                    }
                }
            }, ::navigateRequestClientFragment)

            binding.rcClients.apply {
                adapter = adapterClient
                setHasFixedSize(true)
                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }

            adapterClient?.startListening()
        }
    }

    private fun navigateRequestClientFragment(idClient: String) {
        val intent = Intent(requireContext(), RequestClientActivity::class.java)
        intent.putExtra("idClient",idClient)
        startActivity(intent)
    }

    private fun setVisibility(
        isVisibleLottie: Boolean,
        isVisibleRecyclerView: Boolean,
        isVisibleTextLottie: Boolean
    ) {
        binding.ltClientEmpty.isVisible = isVisibleLottie
        binding.rcClients.isVisible = isVisibleRecyclerView
        binding.tvTextLottie.isVisible = isVisibleTextLottie
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}