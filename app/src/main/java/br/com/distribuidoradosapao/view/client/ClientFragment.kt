package br.com.distribuidoradosapao.view.client

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.distribuidoradosapao.databinding.FragmentClientBinding
import br.com.distribuidoradosapao.model.Client
import br.com.distribuidoradosapao.model.User
import br.com.distribuidoradosapao.view.client.adapter.ClientAdapter
import br.com.distribuidoradosapao.view.client.request.RequestClientActivity
import br.com.distribuidoradosapao.viewmodels.client.ClientViewModel
import br.com.distribuidoradosapao.viewmodels.user.UserViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject

class ClientFragment : Fragment() {

    private var _binding: FragmentClientBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ClientViewModel by inject()

    private var adapterClient: ClientAdapter? = null
    private var options: FirestoreRecyclerOptions<Client>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientBinding.inflate(inflater, container, false)

        setViewModel()
        viewModel.loadClients()
        return binding.root
    }

    private fun setViewModel() {
        viewModel.loadClient.observe(viewLifecycleOwner) {

            options = FirestoreRecyclerOptions.Builder<Client>()
                .setQuery(it, Client::class.java)
                .build()

            adapterClient =
                ClientAdapter(options!!, object : ClientAdapter.ListenerOnDataChanged {
                    override fun onDataChangedEmpty(countData: Int) {

                    }

                    override fun onDataChangedIsNotEmpty(countData: Int) {

                    }
                }, ::navigateRequestClientFragment, ::editDataClient)

            binding.rcClients.apply {
                adapter = adapterClient
                setHasFixedSize(true)
                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }

            adapterClient?.startListening()
        }
    }

    private fun navigateRequestClientFragment(idClient: String, client: Client) {
        val intent = Intent(requireContext(), RequestClientActivity::class.java)
        intent.putExtra("idClient", idClient)
        intent.putExtra("user", client)
        startActivity(intent)
    }

    private fun editDataClient(idClient: String) {
        val bottomSheet = InsertClientBottomSheet.newInstance(idClient)
        bottomSheet.show(childFragmentManager,"TAG")
    }

    private fun setVisibility(
        isVisibleLottie: Int,
        isVisibleRecyclerView: Int,
    ) {
        binding.lnlLottie.visibility = isVisibleLottie
        binding.rcClients.visibility = isVisibleRecyclerView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}