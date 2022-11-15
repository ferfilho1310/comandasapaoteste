package br.com.distribuidoradosapao.view.request

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import br.com.distribuidoradosapao.R
import br.com.distribuidoradosapao.databinding.InsertRequestClientBottomSheetBinding
import br.com.distribuidoradosapao.model.Request
import br.com.distribuidoradosapao.viewmodels.request.RequestClientViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class InsertRequestClientBottomSheet(
    var idClient: String,
    var listener: (Float) -> Unit,
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
        binding.imgCloseBtsRequestClient.setOnClickListener(this)
    }

    private fun setupViewModelSum() {
        viewModel.somaRequestClient.observe(this) {
            listener.invoke(it)
        }
    }

    private fun setupViewModel() {
        viewModel.insertRequestClient.observe(this) {
            if (it == true) {
                dismiss()
            } else {
                Log.e("TAG", "Erro ao inserir o pedido")
                Toast.makeText(
                    requireActivity(),
                    "Erro ao inserir o pedido",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.bt_insert_request_client -> verifyDataRequest()
            R.id.img_close_bts_request_client -> dismiss()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
                            valueUnit = edRequestClientValue.text.toString().toFloat(),
                            date = dateFormat()
                        )
                    )
                    viewModel.somaRequestsClient(idClient)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dateFormat(): String {
        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return current.format(formatter)
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
            listener: (Float) -> Unit,
        ): InsertRequestClientBottomSheet {
            return InsertRequestClientBottomSheet(idClient, listener)
        }
    }
}