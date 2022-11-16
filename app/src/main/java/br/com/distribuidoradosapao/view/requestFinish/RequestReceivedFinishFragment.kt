package br.com.distribuidoradosapao.view.requestFinish

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import br.com.distribuidoradosapao.databinding.FragmentPedidosRecebidosFinalizadosBinding
import br.com.distribuidoradosapao.model.PedidoRecebidoParcial
import br.com.distribuidoradosapao.util.CustomGridLayoutManager
import br.com.distribuidoradosapao.view.requestforfinish.adapterRecebidosParcial.RequestParcialAdapter
import br.com.distribuidoradosapao.viewmodels.request.RequestClientViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import org.koin.android.ext.android.inject

class RequestReceivedFinishFragment(
    var idClient: String,
    var isClientRequestFinish: Boolean
) : Fragment() {

    private var _binding: FragmentPedidosRecebidosFinalizadosBinding? = null
    private val binding get() = _binding!!

    private var adapterRequest: RequestParcialAdapter? = null
    private var options: FirestoreRecyclerOptions<PedidoRecebidoParcial>? = null

    private val viewModel: RequestClientViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPedidosRecebidosFinalizadosBinding.inflate(inflater, container, false)

        viewModel.loadSomaParcial(idClient)
        viewModel.somaReceberParcial(idClient)

        setupViewModel()
        setupViewModelSumParcial()

        return binding.root
    }

    private fun setupViewModel() {
        viewModel.loadSomaParcial.observe(viewLifecycleOwner) {
            options = FirestoreRecyclerOptions.Builder<PedidoRecebidoParcial>()
                .setQuery(it, PedidoRecebidoParcial::class.java)
                .build()

            adapterRequest =
                RequestParcialAdapter(options!!, isClientRequestFinish = isClientRequestFinish)

            binding.recyclerView.apply {
                adapter = adapterRequest
                setHasFixedSize(true)
                layoutManager =
                    CustomGridLayoutManager(context,2)
            }

            adapterRequest?.startListening()
        }
    }

    private fun setupViewModelSumParcial() {
        viewModel.somaPedidosParcial.observe(viewLifecycleOwner) {
            binding.tvRequestClientRecebido.text = "R$ ".plus("%.2f".format(it))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(idClient: String, isClientRequestFinish: Boolean) =
            RequestReceivedFinishFragment(idClient, isClientRequestFinish)
    }
}