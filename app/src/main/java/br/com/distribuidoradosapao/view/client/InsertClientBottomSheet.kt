package br.com.distribuidoradosapao.view.client


import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import br.com.distribuidoradosapao.R
import br.com.distribuidoradosapao.databinding.InsertClientBottomSheetBinding
import br.com.distribuidoradosapao.model.Client
import br.com.distribuidoradosapao.model.User
import br.com.distribuidoradosapao.viewmodels.client.ClientViewModel
import br.com.distribuidoradosapao.viewmodels.user.UserViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class InsertClientBottomSheet(
    val idClient: String? = null
) : BottomSheetDialogFragment(),
    View.OnClickListener {

    private var _binding: InsertClientBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ClientViewModel by inject()
    private val viewModelUser: UserViewModel by inject()

    private var user = User()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = InsertClientBottomSheetBinding.inflate(layoutInflater)

        binding.btInserirClient.setOnClickListener(this)
        binding.imgCloseBtsInsertClient.setOnClickListener(this)

        viewModelUser.searchUser(FirebaseAuth.getInstance().uid.toString())
        idClient?.let { viewModel.loadOneClient(it) }
        setViewModel()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.bt_inserir_client -> insertClient()
            R.id.img_close_bts_insert_client -> dismiss()
        }
    }

    private fun setViewModel() {
        idClient?.let {
            viewModel.loadOneClient.observe(this) {
                binding.edInsertClient.setText(it.name.toString())
            }
            viewModel.updateClient.observe(this) {
                if (it == true) {
                    dismiss()
                } else {
                    Toast.makeText(context, "Erro ao atualizar dados do cliente", Toast.LENGTH_LONG)
                        .show()
                }
            }
        } ?: run {
            viewModel.insertClient.observe(this) {
                if (it == true) {
                    dismiss()
                } else {
                    Toast.makeText(context, "Erro ao inserir cliente", Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModelUser.searchUser.observe(this) {
            if (it != null) {
                user = it
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun insertClient() {
        when {
            binding.edInsertClient.text.toString().isEmpty() -> binding.edInsertClient.error =
                "Informe o nome do cliente"
            else -> {
                idClient?.let {
                    viewModel.updateClient(
                        idClient,
                        Client(
                            name = binding.edInsertClient.text.toString()
                        )
                    )
                } ?: run {
                    viewModel.insertClient(
                        Client(
                            name = binding.edInsertClient.text.toString(),
                            date = dateFormat(),
                            nameAtendente = user.name
                        )
                    )
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

    companion object {
        fun newInstance(
            idClient: String?,
        ): InsertClientBottomSheet {
            return InsertClientBottomSheet(idClient)
        }
    }
}