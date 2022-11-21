package br.com.distribuidoradosapaoteste.view.requestfinish

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.distribuidoradosapaoteste.databinding.FragmentRequestsFinishClientBinding
import br.com.distribuidoradosapaoteste.model.Client
import br.com.distribuidoradosapaoteste.model.Request
import br.com.distribuidoradosapaoteste.util.CustomGridLayoutManager
import br.com.distribuidoradosapaoteste.view.request.adapter.RequestFinishAdapter
import br.com.distribuidoradosapaoteste.viewmodels.request.RequestClientViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import org.koin.android.ext.android.inject

class RequestsClientFinishFragment(
    var idClient: String,
    var client: Client?
) : Fragment() {

    private var _binding: FragmentRequestsFinishClientBinding? = null
    private val binding get() = _binding!!

    private var adapterRequest: RequestFinishAdapter? = null
    private var options: FirestoreRecyclerOptions<Request>? = null

    private val viewModel: RequestClientViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRequestsFinishClientBinding.inflate(inflater, container, false)

        viewModel.loadRequests(idClient)
        viewModel.somaRequestsClient(idClient)

        setupViewModel()
        setupViewModelSum()

        return binding.root
    }

    private fun setupViewModel() {
        viewModel.loadRequestClient.observe(viewLifecycleOwner) {
            options = FirestoreRecyclerOptions.Builder<Request>()
                .setQuery(it, Request::class.java)
                .build()

            adapterRequest =
                RequestFinishAdapter(options!!)

            binding.recyclerView.apply {
                adapter = adapterRequest
                setHasFixedSize(true)
                layoutManager =
                    CustomGridLayoutManager(context,2)
            }

            adapterRequest?.startListening()
        }
    }

    private fun setupViewModelSum() {
        viewModel.somaRequestClient.observe(viewLifecycleOwner) {
            binding.tvTotalRequestClient.text = "R$ ".plus("%.2f".format(it))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(idClient: String, client: Client?) =
            RequestsClientFinishFragment(idClient, client)
    }
}