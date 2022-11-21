package br.com.distribuidoradosapaoteste.view.request

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import br.com.distribuidoradosapaoteste.R
import br.com.distribuidoradosapaoteste.databinding.FragmentEditRequestBottomSheetBinding
import br.com.distribuidoradosapaoteste.model.Request
import br.com.distribuidoradosapaoteste.viewmodels.request.RequestClientViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject

class EditRequestBottomSheet(
    var idRequest: String,
    var idClient: String,
    var listener: (Float) -> Unit,
    var model: Request
) : BottomSheetDialogFragment(), View.OnClickListener {

    private var _binding: FragmentEditRequestBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RequestClientViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditRequestBottomSheetBinding.inflate(inflater, container, false)

        listener()
        setupViewModel()
        setupViewModelSum()
        setDataRequestView(model)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setDataRequestView(model: Request) {
        binding.apply {
            edRequestClientQuantity.setText(model.amount.toString())
            edRequestClientValue.setText(model.valueUnit.toString())
            edProductRequestClient.setText(model.nameProduct.toString())
        }
    }

    private fun listener() {
        binding.btInsertRequestClient.setOnClickListener(this)
        binding.imgCloseBtsEditRequest.setOnClickListener(this)
    }

    private fun setupViewModelSum() {
        viewModel.somaRequestClient.observe(this) {
            listener.invoke(it)
        }
    }

    private fun setupViewModel() {
        viewModel.updateRequest.observe(this) {
            if (it == true) {
                dismiss()
            } else {
                Log.e("TAG", "Erro ao atualizar o pedido")
                Toast.makeText(
                    requireActivity(),
                    "Erro ao atualizar o pedido",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.bt_insert_request_client -> verifyDataRequest()
            R.id.img_close_bts_edit_request -> dismiss()
        }
    }

    private fun verifyDataRequest() {
        binding.apply {
            when {
                edRequestClientQuantity.text.toString().isEmpty() -> edRequestClientQuantity.error =
                    "Preencha a quantidade"
                edRequestClientValue.text.toString().isEmpty() -> edRequestClientValue.error =
                    "Preencha o valor"
                edProductRequestClient.text.toString().isEmpty() -> edProductRequestClient.error =
                    "Preencha o produto"
                else -> {
                    viewModel.updateRequest(
                        idRequest,
                        Request(
                            amount = edRequestClientQuantity.text.toString(),
                            nameProduct = edProductRequestClient.text.toString(),
                            valueUnit = edRequestClientValue.text.toString().toFloat(),
                        )
                    )
                    viewModel.somaRequestsClient(idClient)
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
        @JvmStatic
        fun newInstance(
            idClient: String,
            idRequest: String,
            listener: (Float) -> Unit,
            model: Request
        ): EditRequestBottomSheet {
            return EditRequestBottomSheet(idClient, idRequest, listener, model)
        }
    }
}