package br.com.igorbag.githubsearch.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import br.com.igorbag.githubsearch.R
import br.com.igorbag.githubsearch.data.GitHubService
import br.com.igorbag.githubsearch.domain.Repository
import br.com.igorbag.githubsearch.ui.adapter.RepositoryAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var nomeUsuario: EditText
    lateinit var btnConfirmar: Button
    lateinit var listaRepositories: RecyclerView
    lateinit var githubApi: GitHubService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupView()
        setupRetrofit()
        setupListeners()
        val name = showUserName()
        nomeUsuario.setText(name)
        getAllReposByUserName()
    }

    // Metodo responsavel por realizar o setup da view e recuperar os Ids do layout
    fun setupView() {
        //@TODO 1 - Recuperar os Id's da tela para a Activity com o findViewById
        nomeUsuario = findViewById(R.id.et_nome_usuario)
        btnConfirmar = findViewById(R.id.btn_confirmar)
        listaRepositories = findViewById(R.id.rv_lista_repositories)

    }

    //metodo responsavel por configurar os listeners click da tela
    private fun setupListeners() {
        //@TODO 2 - colocar a acao de click do botao confirmar
        btnConfirmar.setOnClickListener {
            saveUserLocal(nomeUsuario.text.toString())
            getAllReposByUserName()

        }
    }


    // salvar o usuario preenchido no EditText utilizando uma SharedPreferences
    private fun saveUserLocal(resultado: String) {
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(getString(R.string.saveUser), resultado)
            apply()
        }
        //@TODO 3 - Persistir o usuario preenchido na editText com a SharedPref no listener do botao salvar
    }

    private fun showUserName(): String? {
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return ""
        return sharedPref.getString(getString(R.string.saveUser), "")

        //@TODO 4- depois de persistir o usuario exibir sempre as informacoes no EditText  se a sharedpref possuir algum valor, exibir no proprio editText o valor salvo
    }

    override fun onResume() {
        super.onResume()

    }

    //Metodo responsavel por fazer a configuracao base do Retrofit
    fun setupRetrofit() {
        val retrofit = Retrofit.Builder().baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        githubApi = retrofit.create(GitHubService::class.java)
        /*
           @TODO 5 -  realizar a Configuracao base do retrofit
           Documentacao oficial do retrofit - https://square.github.io/retrofit/
           URL_BASE da API do  GitHub= "https://api.github.com/"
           lembre-se de utilizar o GsonConverterFactory mostrado no curso
        */
    }

    //Metodo responsavel por buscar todos os repositorios do usuario fornecido
    fun getAllReposByUserName() {

        githubApi.getAllRepositoriesByUser(nomeUsuario.text.toString())
            .enqueue(object : Callback<List<Repository>> {
                override fun onResponse(
                    call: Call<List<Repository>>, response: Response<List<Repository>>
                ) {
                    if (response.isSuccessful) {

                        response.body()?.let {
                            setupAdapter(it)
                        }


                    } else {
                        print("Erro")
                    }


                }

                override fun onFailure(call: Call<List<Repository>>, t: Throwable) {

                    print("erro")
                }


                // TODO 6 - realizar a implementacao do callback do retrofit e chamar o metodo setupAdapter se retornar os dados com sucesso
            })

        // Metodo responsavel por realizar a configuracao do adapter
    }

    private fun setupAdapter(it: List<Repository>) {
        val adapter = RepositoryAdapter(it)
        adapter.clickItemLister = {
            openBrowser(it.htmlUrl)
        }
        adapter.btnShareLister = {
            shareRepositoryLink(it.htmlUrl)
        }
        listaRepositories.adapter = adapter
    }


    /*
        @TODO 7 - Implementar a configuracao do Adapter , construir o adapter e instancia-lo
        passando a listagem dos repositorios
     */


    // Metodo responsavel por compartilhar o link do repositorio selecionado
    // @Todo 11 - Colocar esse metodo no click do share item do adapter
    fun shareRepositoryLink(urlRepository: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, urlRepository)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    // Metodo responsavel por abrir o browser com o link informado do repositorio

    // @Todo 12 - Colocar esse metodo no click item do adapter
    fun openBrowser(urlRepository: String) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW, Uri.parse(urlRepository)
            )
        )

    }

}