package com.example.peliculas.data

import com.google.gson.annotations.SerializedName

data class Pelicula(
    @SerializedName("Title") val title: String,
    @SerializedName("Year") val year: String,
    @SerializedName("Rated") val rated: String,
    @SerializedName("Released") val released: String,
    @SerializedName("Runtime") val runtime: String,
    @SerializedName("Type") val type: String,
    @SerializedName("Plot") val plot: String,
    @SerializedName("Poster") val poster: String,
    @SerializedName("Genre") val genre: String,
    @SerializedName("imdbID") val imdbID: String,
    @SerializedName("Country") val pais: String,
    @SerializedName("Director") val director: String,

)
