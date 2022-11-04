package br.com.distribuidoradosapao.view.salesReport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import br.com.distribuidoradosapao.databinding.FragmentSalesReportBinding
import br.com.distribuidoradosapao.viewmodels.request.RequestClientViewModel
import org.koin.android.ext.android.inject

class SalesReportFragment : Fragment() {

    private var _binding: FragmentSalesReportBinding? = null
    val binding get() = _binding!!

    val viewModel: RequestClientViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSalesReportBinding.inflate(inflater, container, false)
        viewModel.loadAllRequest()
        setListenerSpinner()
        setViewModel()
        return binding.root
    }

    private fun setViewModel() {
        viewModel.loadAllRequest.observe(viewLifecycleOwner) {
            val adapter = ArrayAdapter<String>(requireActivity(),android.R.layout.simple_spinner_dropdown_item)

            adapter.add("Selecione uma data")
            adapter.addAll(it.orEmpty())
            binding.spDateRequests.adapter = adapter
        }
        viewModel.filterRequestForDate.observe(viewLifecycleOwner) {
            binding.tvTotalPedidos.text = "R$ ".plus("%.2f".format(it))
        }
    }

    private fun setListenerSpinner() {
        binding.spDateRequests.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    viewModel.filterRequestForDate(p0?.getItemAtPosition(p2).toString())
                }

                override fun onNothingSelected(p0: AdapterView<*>?) = Unit

            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SalesReportFragment()
    }
}