package br.com.distribuidoradosapao.view.request

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import br.com.distribuidoradosapao.R
import br.com.distribuidoradosapao.databinding.FragmentRequestsClientBinding
import br.com.distribuidoradosapao.model.Client
import br.com.distribuidoradosapao.model.Request
import br.com.distribuidoradosapao.view.request.adapter.RequestAdapter
import br.com.distribuidoradosapao.view.request.adapter.RequestAdapter.ListenerEditRequest
import br.com.distribuidoradosapao.viewmodels.client.ClientViewModel
import br.com.distribuidoradosapao.viewmodels.request.RequestClientViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import org.koin.android.ext.android.inject

class RequestsClientFragment(
    var idClient: String,
    var client: Client?
) : Fragment(), View.OnClickListener {

    private var _binding: FragmentRequestsClientBinding? = null
    private val binding get() = _binding!!

    private var adapterRequest: RequestAdapter? = null
    private var options: FirestoreRecyclerOptions<Request>? = null

    private val viewModel: RequestClientViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRequestsClientBinding.inflate(inflater, container, false)

        viewModel.loadRequests(idClient)
        viewModel.somaRequestsClient(idClient)

        setupViewModel()
        setupViewModelSum()

        setupListeners()

        return binding.root
    }

    private fun setupListeners() {
        binding.let {
            it.fabInsertRequestClient.setOnClickListener(this)
        }
    }

    private fun setupViewModel() {
        viewModel.loadRequestClient.observe(viewLifecycleOwner) {
            options = FirestoreRecyclerOptions.Builder<Request>()
                .setQuery(it, Request::class.java)
                .build()

            adapterRequest =
                RequestAdapter(options!!,
                    object : ListenerEditRequest {
                    override fun onEditRequest(idRequest: String, model: Request) {
                        editRequest(idRequest, model)

                    }
                }
                ) { count ->
                    showHideNoData(count > 0)
                }

            binding.recyclerView.apply {
                adapter = adapterRequest
                setHasFixedSize(true)
                layoutManager =
                    GridLayoutManager(context, 2)
            }

            adapterRequest?.startListening()
        }
    }

    private fun showHideNoData(isHaveData: Boolean) {
        binding.apply {
            recyclerView.isVisible = isHaveData
            llVazio.isVisible = !isHaveData
        }
    }

    private fun editRequest(idRequest: String, model: Request) {
        val bottomSheet =
            EditRequestBottomSheet.newInstance(idRequest, idClient, ::listenerSumTotal, model)
        bottomSheet.show(childFragmentManager, "TAG")
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.fab_insert_request_client -> {
                val bottomSheet =
                    InsertRequestClientBottomSheet.newInstance(idClient, ::listenerSumTotal)
                bottomSheet.show(childFragmentManager, "TAG")
            }
        }
    }

    private fun listenerSumTotal(sumRequest: String) {
        binding.tvTotalRequestClient.text = "R$ ".plus("%.2f".format(sumRequest))
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
            RequestsClientFragment(idClient, client)
    }
}