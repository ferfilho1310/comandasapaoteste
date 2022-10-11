package br.com.distribuidoradosapao.view.client


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.distribuidoradosapao.R
import br.com.distribuidoradosapao.databinding.InsertClientBottomSheetBinding
import br.com.distribuidoradosapao.model.Client
import br.com.distribuidoradosapao.viewmodels.client.ClientViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject
import java.util.*

class InsertClientBottomSheet : BottomSheetDialogFragment(), View.OnClickListener {

    private var _binding: InsertClientBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ClientViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = InsertClientBottomSheetBinding.inflate(layoutInflater)

        binding.btInserirClient.setOnClickListener(this)
        viewModel.insertClient.observe(this) {
            if (it == true) {
                dismiss()
            } else {
                Toast.makeText(context, "Erro ao inserir cliente", Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.bt_inserir_client -> insertClient()
        }
    }

    private fun insertClient() {
        when {
            binding.edInsertClient.text.toString().isEmpty() -> binding.edInsertClient.error =
                "Informe o nome do cliente"
            else -> {
                viewModel.insertClient(
                    Client(
                        name = binding.edInsertClient.text.toString(),
                        date = Date().toString()
                    )
                )
            }
        }
    }
}