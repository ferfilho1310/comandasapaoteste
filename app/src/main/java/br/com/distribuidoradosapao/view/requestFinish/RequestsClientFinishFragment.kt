package br.com.distribuidoradosapao.view.requestFinish

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import br.com.distribuidoradosapao.R
import br.com.distribuidoradosapao.databinding.FragmentRequestsClientBinding
import br.com.distribuidoradosapao.databinding.FragmentRequestsFinishClientBinding
import br.com.distribuidoradosapao.model.Client
import br.com.distribuidoradosapao.model.Request
import br.com.distribuidoradosapao.view.request.InsertRequestClientBottomSheet
import br.com.distribuidoradosapao.view.request.adapter.RequestFinishAdapter
import br.com.distribuidoradosapao.viewmodels.client.ClientViewModel
import br.com.distribuidoradosapao.viewmodels.request.RequestClientViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
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
                RequestFinishAdapter(options!!, object : RequestFinishAdapter.ListenerOnDataChanged {
                    override fun onDataChanged(countData: Int) {
                        if (countData == 0) {
                            /*   setVisibility(
                                   isVisibleLottie = true,
                                   isVisibleRecyclerView = false,
                                   isVisibleTextLottie = true
                               )*/
                        } else {
                            /*setVisibility(
                                isVisibleLottie = false,
                                isVisibleRecyclerView = true,
                                isVisibleTextLottie = false
                            )*/
                        }
                    }
                })

            binding.recyclerView.apply {
                adapter = adapterRequest
                setHasFixedSize(true)
                layoutManager =
                    GridLayoutManager(context, 2)
            }

            adapterRequest?.startListening()
        }
    }

    private fun setupViewModelSum() {
        viewModel.somaRequestClient.observe(viewLifecycleOwner) {
            binding.tvTotalRequestClient.text = "R$ ".plus(it)
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