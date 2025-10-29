package com.example.peliculas.data

data class SearchResponse(
    val Search: List<Pelicula>,
    val totalResults: String,
    val Response: String
)
