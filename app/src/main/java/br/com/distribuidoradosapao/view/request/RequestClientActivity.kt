package br.com.distribuidoradosapao.view.request

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.distribuidoradosapao.R
import br.com.distribuidoradosapao.databinding.ActivityRequestClientBinding
import br.com.distribuidoradosapao.model.Client
import br.com.distribuidoradosapao.model.PedidoRecebidoParcial
import br.com.distribuidoradosapao.model.Request
import br.com.distribuidoradosapao.view.MainActivity
import br.com.distribuidoradosapao.view.request.adapter.RequestAdapter
import br.com.distribuidoradosapao.viewmodels.client.ClientViewModel
import br.com.distribuidoradosapao.viewmodels.request.RequestClientViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import org.koin.android.ext.android.inject


class RequestClientActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRequestClientBinding
    private var idClient: String = String()
    private var adapterRequest: RequestAdapter? = null
    private var options: FirestoreRecyclerOptions<Request>? = null

    private val viewModel: RequestClientViewModel by inject()
    private val viewModelClient: ClientViewModel by inject()

    private var client: Client? = null

    private var sumParcial = 0f
    private var sumtotal = 0f
    private var valorAReceber = 0f

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
        client = getDataUser(intent)

        supportActionBar?.title = client?.name

        viewModel.loadRequests(idClient)

        setupViewModel()
        setupViewModelSum()
        setupViewModelDeleteClient()
        setupViewModelSumParcial()

        setupListeners()
    }

    private fun setupListeners() {
        binding.let {
            it.fabInsertRequestClient.setOnClickListener(this)
            it.tvFinalizarComanda.setOnClickListener(this)
            it.fabReceberParccial.setOnClickListener(this)
        }
    }

    private fun getDataClient(iDataClient: Intent): String? {
        return iDataClient.getStringExtra("idClient")
    }

    private fun getDataUser(iDataClient: Intent): Client? {
        return iDataClient.getParcelableExtra("user")
    }

    private fun setupViewModel() {
        viewModel.loadRequestClient.observe(this) {
            options = FirestoreRecyclerOptions.Builder<Request>()
                .setQuery(it, Request::class.java)
                .build()

            adapterRequest =
                RequestAdapter(options!!, object : RequestAdapter.ListenerOnDataChanged {
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
                startActivity(
                    Intent(
                        this,
                        MainActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
                finish()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(
            Intent(
                this,
                MainActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
        finish()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.fab_receber_parccial -> {
                val bottomSheet =
                    InsertValueRecebidoParcialBottomSheet.newInstance(
                        idClient,
                        ::listenerSumRececidoParcial,
                        object : InsertValueRecebidoParcialBottomSheet.Recebido {
                            override fun onRecebido() {

                            }
                        }
                    )
                bottomSheet.show(supportFragmentManager, "TAG")
            }
            R.id.fab_insert_request_client -> {
                val bottomSheet =
                    InsertRequestClientBottomSheet.newInstance(
                        idClient,
                        ::listenerSumTotal,
                        object : InsertRequestClientBottomSheet.Recebido {
                            override fun onRecebido() {

                            }
                        })
                bottomSheet.show(supportFragmentManager, "TAG")
            }
            R.id.tv_finalizar_comanda -> {
                val alertDialog = AlertDialog.Builder(this)
                    .setTitle("Finalizar comanda")
                    .setMessage("Deseja realmente finalizar a comanda do cliente?")
                    .setPositiveButton("Sim") { p0, p1 ->
                        viewModelClient.searchClient(idClient)
                    }.setNegativeButton("Não") { p0, p1 ->
                        p0.dismiss()
                    }
                alertDialog.show()
            }
        }
    }

    private fun setupViewModelDeleteClient() {
        viewModelClient.deleteClient.observe(this) {
            if (it == true) {
                startActivity(
                    Intent(
                        this,
                        MainActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
                finish()
                Toast.makeText(this, "Comanda Finalizada com sucesso", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(
                    this,
                    "Comanda já finalizada ou houve outro problema",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun listenerSumTotal(sumRequest: String) {
        binding.tvTotalRequestClient.text = "R$ ".plus(sumRequest)
    }

    private fun listenerSumRececidoParcial(sumRecebidoParcial: String) {
        binding.tvRequestClientRecebido.text = "R$ ".plus(sumRecebidoParcial)
    }

    private fun setupViewModelSum() {
        viewModel.somaRequestClient.observe(this) {
            binding.tvTotalRequestClient.text = "R$ ".plus(it)
        }
    }

    private fun setupViewModelSumParcial() {
        viewModel.somaPedidosParcial.observe(this) {
            binding.tvRequestClientRecebido.text = "R$ ".plus(it)
        }
    }

    private fun valorReceber() {
     viewModel.recebido.observe(this){
         binding.tvRequestClientReceber.text = "R$ ".plus(it.toString())
     }
    }

    override fun onStart() {
        super.onStart()
        viewModel.somaRequestsClient(idClient)
        viewModel.somaReceberParcial(idClient)
        valorReceber()
    }
}