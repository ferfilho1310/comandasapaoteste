package br.com.distribuidoradosapao.view.client.request

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.distribuidoradosapao.R
import br.com.distribuidoradosapao.databinding.InsertRequestClientBottomSheetBinding
import br.com.distribuidoradosapao.model.Request
import br.com.distribuidoradosapao.viewmodels.request.RequestClientViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject

class InsertRequestClientBottomSheet(
    var idClient: String,
    var listener: (String) -> Unit
) : BottomSheetDialogFragment(), View.OnClickListener {

    private var _binding: InsertRequestClientBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RequestClientViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = InsertRequestClientBottomSheetBinding.inflate(inflater, container, false)

        listener()
        setupViewModel()
        setupViewModelSum()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun listener() {
        binding.btInsertRequestClient.setOnClickListener(this)
    }

    private fun setupViewModelSum() {
        viewModel.somaRequestClient.observe(this) {
            listener.invoke(it.toString())
        }
    }

    private fun setupViewModel() {
        viewModel.insertRequestClient.observe(this) {
            if (it == true) {
                dismiss()
            } else {
                Log.e("TAG", "Erro ao inserir o pedido")
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
                edRequestClientValue.text.toString().isEmpty() -> edRequestClientQuantity.error =
                    "Preencha o valor"
                edProductRequestClient.text.toString().isEmpty() -> edProductRequestClient.error =
                    "Preencha o produto"
                else -> {
                    viewModel.insertRequestClient(
                        Request(
                            idClient = idClient,
                            amount = edRequestClientQuantity.text.toString(),
                            nameProduct = edProductRequestClient.text.toString(),
                            value = edRequestClientValue.text.toString()
                        )
                    )
                    viewModel.somaRequestsClient(idClient)
                }
            }
        }
    }

    companion object {
        fun newInstance(
            idClient: String,
            listener: (String) -> Unit
        ): InsertRequestClientBottomSheet {
            return InsertRequestClientBottomSheet(idClient, listener)
        }
    }
}