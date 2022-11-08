package br.com.distribuidoradosapao.view.request

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import br.com.distribuidoradosapao.R
import br.com.distribuidoradosapao.databinding.FragmentPedidosRecebidosBinding
import br.com.distribuidoradosapao.model.PedidoRecebidoParcial
import br.com.distribuidoradosapao.view.requestforfinish.adapterRecebidosParcial.RequestParcialAdapter
import br.com.distribuidoradosapao.viewmodels.request.RequestClientViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import org.koin.android.ext.android.inject

class RequestReceivedFragment(
    var idClient: String
) : Fragment(), View.OnClickListener {

    private var _binding: FragmentPedidosRecebidosBinding? = null
    private val binding get() = _binding!!

    private var adapterRequest: RequestParcialAdapter? = null
    private var options: FirestoreRecyclerOptions<PedidoRecebidoParcial>? = null

    private val viewModel: RequestClientViewModel by inject()

    private var sumTotal = 0.00f
    private var sumParcial = 0.00f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPedidosRecebidosBinding.inflate(inflater, container, false)

        viewModel.loadSomaParcial(idClient)
        viewModel.somaReceberParcial(idClient)
        viewModel.somaRequestsClient(idClient)

        setupListeners()
        setupViewModel()
        setupViewModelSumParcial()
        sumTotalRequest()
        sumJaRecebido()

        return binding.root
    }

    private fun setupListeners() {
        binding.let {
            it.fabReceberParccial.setOnClickListener(this)
            it.fabSumRequestReceber.setOnClickListener(this)
        }
    }

    private fun setupViewModel() {
        viewModel.loadSomaParcial.observe(viewLifecycleOwner) {
            options = FirestoreRecyclerOptions.Builder<PedidoRecebidoParcial>()
                .setQuery(it, PedidoRecebidoParcial::class.java)
                .build()

            adapterRequest =
                RequestParcialAdapter(options!!)

            binding.recyclerView.apply {
                adapter = adapterRequest
                setHasFixedSize(true)
                layoutManager =
                    GridLayoutManager(context, 2)
            }

            adapterRequest?.startListening()
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.fab_receber_parccial -> {
                val bottomSheet =
                    InsertValueReceivedParcialBottomSheet.newInstance(
                        idClient,
                        ::listenerSumRececidoParcial
                    )
                bottomSheet.show(childFragmentManager, "TAG")
            }
            R.id.fab_sum_request_receber -> {
                sumJaRecebido()
            }
        }
    }

    private fun listenerSumRececidoParcial(sumRecebidoParcial: Float) {
        sumParcial = sumRecebidoParcial
        binding.tvRequestClientRecebido.text = "R$ ".plus("%.2f".format(sumRecebidoParcial))
    }

    private fun setupViewModelSumParcial() {
        viewModel.somaPedidosParcial.observe(viewLifecycleOwner) {
            sumParcial = it
            binding.tvRequestClientRecebido.text = "R$ ".plus("%.2f".format(it))
        }
    }

    private fun sumTotalRequest() {
        viewModel.somaRequestClient.observe(viewLifecycleOwner) {
            sumTotal = it
        }
    }

    private fun sumJaRecebido() {
        Handler(Looper.getMainLooper()).postDelayed(
            {
                binding.tvRequestClientReceber.text =
                    "R$ ".plus("%.2f".format(sumTotal - sumParcial))
            },
            1000
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(idClient: String) = RequestReceivedFragment(idClient)
    }
}