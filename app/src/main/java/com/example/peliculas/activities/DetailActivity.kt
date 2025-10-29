package com.example.peliculas.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.peliculas.R
import com.example.peliculas.data.Pelicula
import com.example.peliculas.data.PeliculaService
import com.example.peliculas.databinding.ActivityDetailBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailBinding

    lateinit var pelicula: Pelicula

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val id = intent.getStringExtra("Pelicula_ID")!!

        getPelicula(id)
    }

    private fun loadData() {
        supportActionBar?.title = pelicula.title
        binding.descriptionTextView.text = pelicula.plot
        Picasso.get().load(pelicula.poster).into(binding.posterImageView)

        binding.playButton.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, pelicula.poster.toUri())
            startActivity(browserIntent)
        }
    }


    private fun getPelicula(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val service = PeliculaService.getInstance()
                val response = service.getPeliculaPorId(id)
                if (response.isSuccessful) {
                    pelicula = response.body()!!
                    CoroutineScope(Dispatchers.Main).launch {
                        loadData()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}