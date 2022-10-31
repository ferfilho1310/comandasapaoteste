package br.com.distribuidoradosapao.view.request

import android.os.Bundle
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

class PedidosRecebidosFragment(
    var idClient: String
) : Fragment(), View.OnClickListener {

    private var _binding: FragmentPedidosRecebidosBinding? = null
    private val binding get() = _binding!!

    private var adapterRequest: RequestParcialAdapter? = null
    private var options: FirestoreRecyclerOptions<PedidoRecebidoParcial>? = null

    private val viewModel: RequestClientViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPedidosRecebidosBinding.inflate(inflater, container, false)

        viewModel.loadSomaParcial(idClient)

        setupListeners()
        setupViewModel()
        setupViewModelSumParcial()

        return binding.root
    }

    private fun setupListeners() {
        binding.let {
            it.fabReceberParccial.setOnClickListener(this)
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
        }
    }

    private fun listenerSumRececidoParcial(sumRecebidoParcial: String) {
        binding.tvRequestClientRecebido.text = "R$ ".plus("%.2f".format(sumRecebidoParcial.toFloat()))
    }

    private fun setupViewModelSumParcial() {
        viewModel.somaPedidosParcial.observe(viewLifecycleOwner) {
            binding.tvRequestClientRecebido.text = "R$ ".plus(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(idClient: String) = PedidosRecebidosFragment(idClient)
    }
}