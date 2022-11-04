package br.com.distribuidoradosapao.view.request

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.distribuidoradosapao.R
import br.com.distribuidoradosapao.databinding.InsertValueRecebidoParcialBottomSheetBinding
import br.com.distribuidoradosapao.model.PedidoRecebidoParcial
import br.com.distribuidoradosapao.viewmodels.request.RequestClientViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject

class InsertValueReceivedParcialBottomSheet(
    var idClient: String,
    var listener: (String) -> Unit,
    var recebido: Recebido
) :
    BottomSheetDialogFragment(),
    View.OnClickListener {

    private var _binding: InsertValueRecebidoParcialBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RequestClientViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = InsertValueRecebidoParcialBottomSheetBinding.inflate(inflater, container, false)

        listener()
        setupViewModel()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupViewModel() {
        viewModel.insertRequestPartial.observe(this) {
            if (it) {
                dismiss()
            } else {
                Log.i("Error", "Não foi possível inserir o valor parcial")
            }
        }

        viewModel.somaPedidosParcial.observe(this) {
            listener.invoke(it.toString())
        }
    }

    private fun listener() {
        binding.btReceberValor.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.bt_receber_valor -> verifyDataRequest()
        }
    }

    private fun verifyDataRequest() {
        binding.apply {
            when {
                edName.text.toString().isEmpty() -> edName.error =
                    "Preencha o nome"
                edValueRecebido.text.toString().isEmpty() -> edValueRecebido.error =
                    "Preencha o valor"
                else -> {
                    viewModel.receberPedidoParcial(
                        PedidoRecebidoParcial(
                            idClient = idClient,
                            name = edName.text.toString(),
                            value = edValueRecebido.text.toString().toFloat()
                        )
                    )
                    viewModel.somaReceberParcial(idClient)
                    recebido.onRecebido()
                }
            }
        }
    }

    interface Recebido {
        fun onRecebido()
    }

    companion object {
        fun newInstance(
            idClient: String,
            listener: (String) -> Unit,
            recebido: Recebido
        ): InsertValueReceivedParcialBottomSheet {
            return InsertValueReceivedParcialBottomSheet(idClient, listener, recebido)
        }
    }
}