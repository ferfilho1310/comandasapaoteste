package br.com.distribuidoradosapao

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.distribuidoradosapao.databinding.ActivityRequestClientBinding
import br.com.distribuidoradosapao.databinding.FragmentClientRequestFinishBinding
import br.com.distribuidoradosapao.databinding.FragmentRequestsClientBinding
import br.com.distribuidoradosapao.model.Client
import br.com.distribuidoradosapao.model.Request
import br.com.distribuidoradosapao.view.request.InsertRequestClientBottomSheet
import br.com.distribuidoradosapao.view.request.InsertValueRecebidoParcialBottomSheet
import br.com.distribuidoradosapao.view.request.adapter.RequestAdapter
import br.com.distribuidoradosapao.viewmodels.client.ClientViewModel
import br.com.distribuidoradosapao.viewmodels.request.RequestClientViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import org.koin.android.ext.android.inject

class FragmentRequestsClient(
    var idClient: String,
    var client: Client?
) : Fragment(), View.OnClickListener {

    private var _binding: FragmentRequestsClientBinding? = null
    private val binding get() = _binding!!

    private var adapterRequest: RequestAdapter? = null
    private var options: FirestoreRecyclerOptions<Request>? = null

    private val viewModel: RequestClientViewModel by inject()
    private val viewModelClient: ClientViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRequestsClientBinding.inflate(inflater, container, false)

        viewModel.loadRequests(idClient)

        setupViewModel()
        setupViewModelSum()
        setupViewModelSumParcial()

        setupListeners()

        return binding.root
    }

    private fun setupListeners() {
        binding.let {
            it.fabInsertRequestClient.setOnClickListener(this)
            it.fabReceberParccial.setOnClickListener(this)
        }
    }

    private fun setupViewModel() {
        viewModel.loadRequestClient.observe(viewLifecycleOwner) {
            options = FirestoreRecyclerOptions.Builder<Request>()
                .setQuery(it, Request::class.java)
                .build()

            adapterRequest =
                RequestAdapter(options!!, object : RequestAdapter.ListenerOnDataChanged {
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
                    GridLayoutManager(context,2)
            }

            adapterRequest?.startListening()
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.fab_receber_parccial -> {
                val bottomSheet =
                    InsertValueRecebidoParcialBottomSheet.newInstance(
                        idClient,
                        ::listenerSumRececidoParcial,
                        object : InsertValueRecebidoParcialBottomSheet.Recebido {
                            override fun onRecebido() {

                            }
                        }
                    )
                bottomSheet.show(childFragmentManager, "TAG")
            }
            R.id.fab_insert_request_client -> {
                val bottomSheet =
                    InsertRequestClientBottomSheet.newInstance(
                        idClient,
                        ::listenerSumTotal,
                        object : InsertRequestClientBottomSheet.Recebido {
                            override fun onRecebido() {

                            }
                        })
                bottomSheet.show(childFragmentManager, "TAG")
            }
            R.id.tv_finalizar_comanda -> {
                val alertDialog = AlertDialog.Builder(requireActivity())
                    .setTitle("Finalizar comanda")
                    .setMessage("Deseja realmente finalizar a comanda do cliente?")
                    .setPositiveButton("Sim") { p0, p1 ->
                        viewModelClient.searchClient(idClient)
                    }.setNegativeButton("Não") { p0, p1 ->
                        p0.dismiss()
                    }
                alertDialog.show()
            }
        }
    }

    private fun listenerSumTotal(sumRequest: String) {
        binding.tvTotalRequestClient.text = "R$ ".plus(sumRequest)
    }

    private fun listenerSumRececidoParcial(sumRecebidoParcial: String) {
        binding.tvRequestClientRecebido.text = "R$ ".plus(sumRecebidoParcial)
    }

    private fun setupViewModelSum() {
        viewModel.somaRequestClient.observe(viewLifecycleOwner) {
            binding.tvTotalRequestClient.text = "R$ ".plus(it)
        }
    }

    private fun setupViewModelSumParcial() {
        viewModel.somaPedidosParcial.observe(viewLifecycleOwner) {
            binding.tvRequestClientRecebido.text = "R$ ".plus(it)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.somaRequestsClient(idClient)
        viewModel.somaReceberParcial(idClient)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(
            idClient: String,
            client: Client?
        ) = FragmentRequestsClient(idClient, client)
    }
}