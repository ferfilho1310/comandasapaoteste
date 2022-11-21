package br.com.distribuidoradosapaoteste.view.request

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import br.com.distribuidoradosapaoteste.R
import br.com.distribuidoradosapaoteste.databinding.ActivityRequestClientBinding
import br.com.distribuidoradosapaoteste.model.Client
import br.com.distribuidoradosapaoteste.view.main.MainActivity
import br.com.distribuidoradosapaoteste.view.requestforfinish.ViewPagerAdapter
import br.com.distribuidoradosapaoteste.viewmodels.client.ClientViewModel
import org.koin.android.ext.android.inject


class RequestClientActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRequestClientBinding

    private val viewModelClient: ClientViewModel by inject()

    private var client: Client? = null
    private var idClient: String = String()
    private var isClientRequestFinish: Boolean = false

    @SuppressLint("RestrictedApi")
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
        isClientRequestFinish = getDataInformattionRequest(intent)
        verifyIfisClientRequestFinish(isClientRequestFinish)

        supportActionBar?.title = "Pedidos do(a) ".plus(client?.name)

        setupListeners()
        setupViewModelDeleteClient()
        setupViewPager()
    }

    private fun verifyIfisClientRequestFinish(isClientRequestFinish: Boolean) {
        binding.tvFinalizarComanda.isVisible = !isClientRequestFinish
    }

    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(
            RequestsClientFragment.newInstance(
                idClient = idClient,
                client = client
            ), "Anotados"
        )
        adapter.addFragment(
            RequestReceivedFragment.newInstance(idClient),
            "Recebido Parcial"
        )
        binding.viewPager.adapter = adapter
        binding.tabTablayout.setupWithViewPager(binding.viewPager)
    }

    private fun setupListeners() {
        binding.tvFinalizarComanda.setOnClickListener(this)
    }

    private fun getDataClient(iDataClient: Intent): String? {
        return iDataClient.getStringExtra("idClient")
    }

    private fun getDataUser(iDataClient: Intent): Client? {
        return iDataClient.getParcelableExtra("user")
    }

    private fun getDataInformattionRequest(iDataClient: Intent): Boolean {
        return iDataClient.getBooleanExtra("isClientRequestFinish", false)
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
            R.id.tv_finalizar_comanda -> {
                val alertDialog = AlertDialog.Builder(this)
                    .setTitle("Finalizar comanda")
                    .setMessage("Deseja realmente finalizar a comanda do cliente?")
                    .setPositiveButton("Sim") { p0, p1 ->
                        viewModelClient.searchClient(idClient, true)
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
}