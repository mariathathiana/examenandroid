package com.example.peliculas.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.peliculas.R
import com.example.peliculas.adapters.PeliculaAdapter
import com.example.peliculas.data.Pelicula
import com.example.peliculas.data.PeliculaService
import com.example.peliculas.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    lateinit var adapter: PeliculaAdapter

    var filteredPeliculaList: List<Pelicula> = emptyList()
    var originalPeliculaList: List<Pelicula> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adapter = PeliculaAdapter(filteredPeliculaList) { position ->
            val pelicula = filteredPeliculaList[position]
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("Pelicula_ID", pelicula.imdbID)

            startActivity(intent)
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(this, 1)

        getPeliculaList()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)

        val searchView = menu.findItem(R.id.action_search).actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filteredPeliculaList = originalPeliculaList.filter { it.title.contains(newText, true) }
                adapter.updateItems(filteredPeliculaList)
                return true
            }
        })

        return true
    }

    fun getPeliculaList() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val service = PeliculaService.getInstance()
                val response = service.searchPeliculas("Guardians")
                if (response.isSuccessful) {
                    val peliculas = response.body()?.Search ?: emptyList()
                    originalPeliculaList = peliculas
                    filteredPeliculaList = peliculas

                    withContext(Dispatchers.Main) {
                        adapter.updateItems(filteredPeliculaList)
                    }
                } else {
                    // Manejo de error HTTP
                    println("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }
}