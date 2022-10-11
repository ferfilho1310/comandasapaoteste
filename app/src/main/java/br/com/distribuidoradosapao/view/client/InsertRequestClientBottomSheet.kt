package br.com.distribuidoradosapao.view.client

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.distribuidoradosapao.databinding.InsertRequestClientBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class InsertRequestClientBottomSheet : BottomSheetDialogFragment() {

    private var _binding: InsertRequestClientBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = InsertRequestClientBottomSheetBinding.inflate(inflater, container, false)

        binding.btInsertRequestClient.setOnClickListener {
            dismiss()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}