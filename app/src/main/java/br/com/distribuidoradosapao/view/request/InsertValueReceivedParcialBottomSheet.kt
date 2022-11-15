package br.com.distribuidoradosapao.view.request

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import br.com.distribuidoradosapao.R
import br.com.distribuidoradosapao.databinding.InsertValueRecebidoParcialBottomSheetBinding
import br.com.distribuidoradosapao.model.PedidoRecebidoParcial
import br.com.distribuidoradosapao.viewmodels.request.RequestClientViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject

class InsertValueReceivedParcialBottomSheet(
    var idClient: String,
    var listener: (Float?) -> Unit
) : BottomSheetDialogFragment(),
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

        viewModel.somaReceberParcial(idClient)

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
                Toast.makeText(
                    requireActivity(),
                    "Não foi possível inserir o valor parcial",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        viewModel.somaPedidosParcial.observe(this) {
            listener.invoke(it)
        }
    }

    private fun listener() {
        binding.btReceberValor.setOnClickListener(this)
        binding.imgCloseBtsValueRequest.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.bt_receber_valor -> verifyDataRequest()
            R.id.img_close_bts_value_request -> dismiss()
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
                    viewModel.insertValueReceivedPartial(
                        PedidoRecebidoParcial(
                            idClient = idClient,
                            name = edName.text.toString(),
                            value = edValueRecebido.text.toString().toFloat()
                        )
                    )
                    viewModel.somaReceberParcial(idClient)
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener {
            val bottomSheet = bottomSheetDialog
                .findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)

            if (bottomSheet != null) {
                val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
                behavior.isDraggable = false
            }
        }
        return bottomSheetDialog
    }

    companion object {
        fun newInstance(
            idClient: String,
            listener: (Float?) -> Unit
        ): InsertValueReceivedParcialBottomSheet {
            return InsertValueReceivedParcialBottomSheet(idClient, listener)
        }
    }
}