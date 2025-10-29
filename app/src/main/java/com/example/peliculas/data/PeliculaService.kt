package com.example.peliculas.data

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface PeliculaService {

    // Buscar una película por título
    @GET("/")
    suspend fun getPeliculaPorTitulo(
        @Query("t") titulo: String,
        @Query("apikey") apiKey: String = "3d32ce0f"
    ): Response<Pelicula>

    // Buscar una película por ID de IMDb
    @GET("/")
    suspend fun getPeliculaPorId(
        @Query("i") id: String,
        @Query("apikey") apiKey: String = "3d32ce0f"
    ): Response<Pelicula>

    @GET("/")
    suspend fun searchPeliculas(
        @Query("s") query: String,
        @Query("apikey") apiKey: String = "3d32ce0f"
    ): Response<SearchResponse>


    companion object {
        fun getInstance(): PeliculaService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://www.omdbapi.com/") //
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(PeliculaService::class.java)
        }
    }
}
