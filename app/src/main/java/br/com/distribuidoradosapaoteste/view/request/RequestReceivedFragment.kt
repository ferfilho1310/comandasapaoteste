package br.com.distribuidoradosapaoteste.view.request

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import br.com.distribuidoradosapaoteste.R
import br.com.distribuidoradosapaoteste.databinding.FragmentPedidosRecebidosBinding
import br.com.distribuidoradosapaoteste.model.RequestReceivedPartial
import br.com.distribuidoradosapaoteste.util.CustomGridLayoutManager
import br.com.distribuidoradosapaoteste.view.requestforfinish.adapterRecebidosParcial.RequestParcialAdapter
import br.com.distribuidoradosapaoteste.viewmodels.request.RequestClientViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import org.koin.android.ext.android.inject

class RequestReceivedFragment(
    var idClient: String
) : Fragment(), View.OnClickListener {

    private var _binding: FragmentPedidosRecebidosBinding? = null
    private val binding get() = _binding!!

    private var adapterRequest: RequestParcialAdapter? = null
    private var options: FirestoreRecyclerOptions<RequestReceivedPartial>? = null

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

        setupListeners()
        setupViewModel()
        setupViewModelSumParcial()
        deleteRequestReceived()

        return binding.root
    }

    private fun setupListeners() {
        binding.let {
            it.fabReceberParccial.setOnClickListener(this)
        }
    }

    private fun setupViewModel() {
        viewModel.loadSomaParcial.observe(viewLifecycleOwner) {
            options = FirestoreRecyclerOptions.Builder<RequestReceivedPartial>()
                .setQuery(it, RequestReceivedPartial::class.java)
                .build()

            adapterRequest =
                RequestParcialAdapter(
                    options!!,
                    object : RequestParcialAdapter.ListenerDeleteRequestReceived {
                        override fun onDeleteRequestReceived(idRequestReceived: String) {
                            AlertDialog.Builder(requireActivity())
                                .setTitle("Atenção")
                                .setMessage("Deseja realmente deletar o recebimento?")
                                .setPositiveButton(
                                    "Sim"
                                ) { p0, _ ->
                                    viewModel.deleteRequestReceived(idRequestReceived)
                                    viewModel.somaReceberParcial(idClient)
                                    p0.dismiss()
                                }
                                .setNegativeButton(
                                    "Não"
                                ) { p0, _ -> p0?.dismiss() }.show()
                        }
                    })

            binding.recyclerView.apply {
                adapter = adapterRequest
                setHasFixedSize(true)
                layoutManager =
                    CustomGridLayoutManager(context,2)
            }

            adapterRequest?.startListening()
        }
    }

    private fun deleteRequestReceived() {
        viewModel.deleteRequest.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(
                    requireActivity(),
                    "Recebimento deletado com sucesso",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    requireActivity(), "Erro ao deletar recebimento", Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.fab_receber_parccial -> {
                val bottomSheet = InsertValueReceivedParcialBottomSheet.newInstance(
                    idClient,
                    ::listenerSumRececidoParcial
                )
                bottomSheet.show(childFragmentManager, "TAG")
            }
        }
    }

    private fun listenerSumRececidoParcial(sumRecebidoParcial: Float?) {
        binding.tvRequestClientRecebido.text = "R$ ".plus("%.2f".format(sumRecebidoParcial))
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
        fun newInstance(idClient: String) = RequestReceivedFragment(idClient)
    }
}