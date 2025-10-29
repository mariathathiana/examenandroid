package com.example.peliculas.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.peliculas.data.Pelicula
import com.example.peliculas.data.PeliculaService
import com.example.peliculas.databinding.ActivityDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener el imdbID enviado desde MainActivity
        val imdbID = intent.getStringExtra("Pelicula_ID")
        if (imdbID != null) {
            fetchPeliculaDetails(imdbID)
        } else {
            Toast.makeText(this, "ID de película no recibido", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Click del botón para ver póster en grande
        binding.playButton.setOnClickListener {
            Toast.makeText(this, "Función de ver póster aún no implementada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchPeliculaDetails(imdbID: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val service = PeliculaService.getInstance()
                val response = service.getPeliculaPorId(imdbID)
                if (response.isSuccessful) {
                    val pelicula = response.body()
                    if (pelicula != null) {
                        withContext(Dispatchers.Main) {
                            bindDataToViews(pelicula)
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@DetailActivity, "Película no encontrada", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@DetailActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DetailActivity, "Error de red", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun bindDataToViews(pelicula: Pelicula) {
        binding.titleTextView.text = pelicula.title
        binding.yearRatedTextView.text = "${pelicula.year} | ${pelicula.rated}"
        binding.genreTypeTextView.text = "${pelicula.genre} | ${pelicula.type}"
        binding.releasedRuntimeTextView.text = "Estreno: ${pelicula.released} | ${pelicula.runtime}"
        binding.plotTextView.text = pelicula.plot
        binding.directorTextView.text =pelicula.director
        binding.paisTextView.text = pelicula.pais

        // Cargar el póster con Glide
        Glide.with(this)
            .load(pelicula.poster)
            .placeholder(android.R.drawable.ic_menu_report_image)
            .error(android.R.drawable.ic_menu_report_image)
            .into(binding.posterImageView)
    }
}
