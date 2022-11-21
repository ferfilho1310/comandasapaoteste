package br.com.distribuidoradosapaoteste.view.request

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import br.com.distribuidoradosapaoteste.R
import br.com.distribuidoradosapaoteste.databinding.ActivityRequestClientBinding
import br.com.distribuidoradosapaoteste.model.Client
import br.com.distribuidoradosapaoteste.view.main.MainActivity
import br.com.distribuidoradosapaoteste.view.requestfinish.RequestReceivedFinishFragment
import br.com.distribuidoradosapaoteste.view.requestfinish.RequestsClientFinishFragment
import br.com.distribuidoradosapaoteste.view.requestforfinish.ViewPagerAdapter
import br.com.distribuidoradosapaoteste.viewmodels.client.ClientViewModel
import org.koin.android.ext.android.inject


class RequestClientFinishActivity : AppCompatActivity() {

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

        setupViewModelDeleteClient()
        setupViewPager()
    }

    private fun getDataInformattionRequest(iDataClient: Intent): Boolean {
        return iDataClient.getBooleanExtra("isClientRequestFinish", false)
    }

    private fun verifyIfisClientRequestFinish(isClientRequestFinish: Boolean) {
        binding.tvFinalizarComanda.isVisible = !isClientRequestFinish
    }

    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(
            RequestsClientFinishFragment.newInstance(
                idClient = idClient,
                client = client
            ), "Anotados"
        )
        adapter.addFragment(
            RequestReceivedFinishFragment.newInstance(idClient, isClientRequestFinish),
            "Recebebido"
        )
        binding.viewPager.adapter = adapter
        binding.tabTablayout.setupWithViewPager(binding.viewPager)
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
                val i = Intent(this, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                i.putExtra("FinishActivity", true)
                startActivity(i)
                finish()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val i = Intent(this, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        i.putExtra("FinishActivity", true)
        startActivity(i)
        finish()
    }

    private fun setupViewModelDeleteClient() {
        viewModelClient.deleteClient.observe(this) {
            if (it == true) {
                val i = Intent(this, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                i.putExtra("FinishActivity", true)
                startActivity(i)
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