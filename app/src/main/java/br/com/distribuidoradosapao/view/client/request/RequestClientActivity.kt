package br.com.distribuidoradosapao.view.client.request

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.distribuidoradosapao.R
import br.com.distribuidoradosapao.databinding.ActivityRequestClientBinding
import br.com.distribuidoradosapao.model.Request
import br.com.distribuidoradosapao.view.MainActivity
import br.com.distribuidoradosapao.view.client.request.adapter.RequestAdapter
import br.com.distribuidoradosapao.viewmodels.request.RequestClientViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import org.koin.android.ext.android.inject


class RequestClientActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRequestClientBinding
    private var idClient: String = String()
    private val viewModel: RequestClientViewModel by inject()
    private var adapterRequest: RequestAdapter? = null
    private var options: FirestoreRecyclerOptions<Request>? = null

    @SuppressLint("RestrictedApi", "UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestClientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setListenerToolbar()

        supportActionBar?.apply {
            setDefaultDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        val intent = intent
        idClient = getDataClient(intent).orEmpty()

        viewModel.loadRequests(idClient)

        setupViewModel()
        setupViewModelSum()
        binding.fabInsertRequestClient.setOnClickListener(this)
    }

    private fun getDataClient(iDataClient: Intent): String? {
        return iDataClient.getStringExtra("idClient")
    }

    private fun setupViewModel() {
        viewModel.loadRequestClient.observe(this) {
            options = FirestoreRecyclerOptions.Builder<Request>()
                .setQuery(it, Request::class.java)
                .build()

            adapterRequest = RequestAdapter(options!!, object : RequestAdapter.ListenerOnDataChanged {
                override fun onDataChanged(countData: Int) {
                    if (countData == 0) {
                        /*   setVisibility(
                               isVisibleLottie = true,
                               isVisibleRecyclerView = false,
                               isVisibleTextLottie = true
                           )*/
                    } else {
                        /*setVisibility(
                            isVisibleLottie = false,
                            isVisibleRecyclerView = true,
                            isVisibleTextLottie = false
                        )*/
                    }
                }
            })

            binding.recyclerView.apply {
                adapter = adapterRequest
                setHasFixedSize(true)
                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }

            adapterRequest?.startListening()
        }
    }

    private fun setListenerToolbar() {
        binding.toolbar.let {
            it.navigationIcon = resources.getDrawable(R.drawable.ic_back)
            it.setNavigationOnClickListener {
                startActivity(Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.fab_insert_request_client -> {
                val bottomSheet = InsertRequestClientBottomSheet.newInstance(idClient, ::listenerSumTotal)
                bottomSheet.show(supportFragmentManager, "TAG")
            }
        }
    }

    private fun listenerSumTotal(sumRequest: String) {
        binding.tvTotalRequestClient.text = sumRequest
    }

    private fun setupViewModelSum() {
        viewModel.somaRequestClient.observe(this) {
            binding.tvTotalRequestClient.text = it.toString()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.somaRequestsClient(idClient)
    }
}