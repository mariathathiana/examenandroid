package com.example.peliculas.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.peliculas.data.Pelicula
import com.example.peliculas.databinding.ItemPeliculaBinding
import com.squareup.picasso.Picasso

class PeliculaAdapter(
    var items: List<Pelicula>,
    val onClickListener: (Int) -> Unit
) : RecyclerView.Adapter<PeliculaViewHolder>() {

    // Cual es la vista para los elementos
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeliculaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPeliculaBinding.inflate(layoutInflater, parent, false)
        return PeliculaViewHolder(binding)
    }

    // Cuales son los datos para el elemento
    override fun onBindViewHolder(holder: PeliculaViewHolder, position: Int) {
        val item = items[position]
        holder.render(item)
        holder.itemView.setOnClickListener {
            onClickListener(position)
        }
    }

    // Cuantos elementos se quieren listar?
    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(items: List<Pelicula>) {
        this.items = items
        notifyDataSetChanged()
    }
}

class PeliculaViewHolder(val binding: ItemPeliculaBinding) : RecyclerView.ViewHolder(binding.root) {

    fun render(pelicula: Pelicula) {
        binding.nameTextView.text = pelicula.title
        Picasso.get().load(pelicula.poster).into(binding.thumbnailImageView)
    }
}