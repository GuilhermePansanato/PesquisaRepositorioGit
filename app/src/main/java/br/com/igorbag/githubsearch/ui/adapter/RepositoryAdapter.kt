package br.com.igorbag.githubsearch.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.igorbag.githubsearch.R
import br.com.igorbag.githubsearch.domain.Repository

class RepositoryAdapter(private val repositories: List<Repository>) :
    RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder>() {

    var clickItemLister: (Repository) -> Unit = {}
    var btnShareLister: (Repository) -> Unit = {}

    // Cria uma nova view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.repository_item, parent, false)
        return RepositoryViewHolder(view)
    }

    // Pega o conteudo da view e troca pela informacao de item de uma lista
    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        holder.bindView(repositories[position], btnShareLister, clickItemLister)
        //@TODO 8 -  Realizar o bind do viewHolder
        //Exemplo de Bind
        //holder.preco.text = repositories[position].atributo

        // Exemplo de click no item
        //holder.itemView.setOnClickListener {
        // carItemLister(repositores[position])
        //}

        // Exemplo de click no btn Share
        //holder.favorito.setOnClickListener {
        //    btnShareLister(repositores[position])
        //}
    }

    // Pega a quantidade de repositorios da lista
    //@TODO 9 - realizar a contagem da lista
    override fun getItemCount(): Int = repositories.size

    class RepositoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        //@TODO 10 - Implementar o ViewHolder para os repositorios
        val name: TextView
        val btnShare: ImageView

        init {
            name = view.findViewById(R.id.tv_nomerep)
            btnShare = view.findViewById(R.id.iv_share)
        }

        fun bindView(
            repository: Repository,
            btnShareLister: (Repository) -> Unit,
            clickItemLister: (Repository) -> Unit
        ) {
            itemView.setOnClickListener { clickItemLister(repository) }
            name.text = repository.name
            btnShare.setOnClickListener {
                btnShareLister(repository)
            }
        }

    }
}


