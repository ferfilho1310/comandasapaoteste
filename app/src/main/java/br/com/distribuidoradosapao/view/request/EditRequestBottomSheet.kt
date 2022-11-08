package br.com.distribuidoradosapao.view.request

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.distribuidoradosapao.R
import br.com.distribuidoradosapao.databinding.FragmentEditRequestBottomSheetBinding
import br.com.distribuidoradosapao.model.Request
import br.com.distribuidoradosapao.viewmodels.request.RequestClientViewModel
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
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.bt_insert_request_client -> verifyDataRequest()
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