package br.com.distribuidoradosapao.view.client


import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import br.com.distribuidoradosapao.R
import br.com.distribuidoradosapao.databinding.InsertClientBottomSheetBinding
import br.com.distribuidoradosapao.model.Client
import br.com.distribuidoradosapao.model.User
import br.com.distribuidoradosapao.viewmodels.client.ClientViewModel
import br.com.distribuidoradosapao.viewmodels.user.UserViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class InsertClientBottomSheet : BottomSheetDialogFragment(), View.OnClickListener {

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
        viewModelUser.searchUser(FirebaseAuth.getInstance().uid.toString())
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
        }
    }

    private fun setViewModel() {
        viewModel.insertClient.observe(this) {
            if (it == true) {
                dismiss()
            } else {
                Toast.makeText(context, "Erro ao inserir cliente", Toast.LENGTH_LONG).show()
            }
        }

        viewModelUser.searchUser.observe(this) {
            if (it != null){
                user = it
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun insertClient() {
        when {
            binding.edInsertClient.text.toString().isEmpty() -> binding.edInsertClient.error =
                "Informe o nome do cliente"
            else -> {
                viewModel.insertClient(
                    Client(
                        name = binding.edInsertClient.text.toString(),
                        date = dateFormat(),
                        nameAtendente = user.email
                    )
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dateFormat(): String {
        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
        return current.format(formatter)
    }
}