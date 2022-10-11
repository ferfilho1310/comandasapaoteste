package br.com.distribuidoradosapao.view.client

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.com.distribuidoradosapao.R
import br.com.distribuidoradosapao.databinding.ActivityRequestClientBinding
import br.com.distribuidoradosapao.databinding.InsertRequestClientBottomSheetBinding
import br.com.distribuidoradosapao.view.MainActivity


class RequestClientActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRequestClientBinding

    @SuppressLint("RestrictedApi", "UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestClientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setListenerToolbar()

        val intent = intent
        val idClient = getDataClient(intent)

        supportActionBar?.apply {
            setDefaultDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        binding.fabInsertRequestClient.setOnClickListener(this)
    }

    private fun getDataClient(iDataClient: Intent): String? {
        return iDataClient.getStringExtra("idClient")
    }

    private fun setListenerToolbar() {
        binding.toolbar.let {
            it.navigationIcon = resources.getDrawable(R.drawable.ic_back)
            it.setNavigationOnClickListener {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.fab_insert_request_client -> {
                val bottomSheet = InsertRequestClientBottomSheet()
                bottomSheet.show(supportFragmentManager, "TAG")
            }
        }
    }
}