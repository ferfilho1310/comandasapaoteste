package br.com.distribuidoradosapao.view.request

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.com.distribuidoradosapao.FragmentRequestsClient
import br.com.distribuidoradosapao.R
import br.com.distribuidoradosapao.databinding.ActivityRequestClientBinding
import br.com.distribuidoradosapao.model.Client
import br.com.distribuidoradosapao.view.MainActivity
import br.com.distribuidoradosapao.view.requestfinish.FragmentPedidosRecebidos
import br.com.distribuidoradosapao.view.requestfinish.ViewPagerAdapter
import br.com.distribuidoradosapao.viewmodels.client.ClientViewModel
import org.koin.android.ext.android.inject


class RequestClientActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRequestClientBinding

    private val viewModelClient: ClientViewModel by inject()

    private var client: Client? = null
    private var idClient: String = String()

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

        setupListeners()
        setupViewModelDeleteClient()
        setupViewPager()
    }

    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(
            FragmentRequestsClient.newInstance(
                idClient = idClient,
                client = client
            ), "Pedidos"
        )
        adapter.addFragment(
            FragmentPedidosRecebidos(),
            "Recebidos Parcial"
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
}