package com.example.estoqueapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.example.estoqueapp.adapter.AdapterProdutos;
import com.example.estoqueapp.helper.ConfiguracaoFirebase;
import com.example.estoqueapp.helper.RecyclerItemClickListener;
import com.example.estoqueapp.model.Produto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Switch;
import android.widget.Toast;
import com.example.estoqueapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class TelamainActivity extends AppCompatActivity {
    private FirebaseAuth autenticacao;
private RecyclerView recyclerProdutos;
private List<Produto> produtos = new ArrayList<>();
private AdapterProdutos adapterProdutos;
private DatabaseReference produtoUsuarioRef;
private Produto produto;
private android.app.AlertDialog dialog;
private SearchView searchView;
private Switch switchBusca;
private List<Produto> produtoSemFotos = new ArrayList<>();
private Produto produtoSemFoto;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telamain);

        //Configurações iniciais
        produtoUsuarioRef = ConfiguracaoFirebase.getFirebase().child("meus_produtos").child(ConfiguracaoFirebase.getIdUsuario());

        inicializarComponentes();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), CadastrarProdutoActivity.class));
            }
        });
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Configurar o RecyclerView
        recyclerProdutos.setLayoutManager(new LinearLayoutManager(this));
        recyclerProdutos.setHasFixedSize(true);
        adapterProdutos = new AdapterProdutos(produtos, this);
        recyclerProdutos.setAdapter( adapterProdutos );

        //Recupera os produtos
        recuperarProdutos();

        //CHAMA O SWIPE
            swipe();

            //CONFIGURAÇÃO DO SEARCH VIEW
            searchView.setQueryHint("Pesquisar Produtos/ Peças");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    pesquisarProdutos( newText );
                    return true;
                }
            });

            //APLICA EVENTO DE CLIQUE NOS ITENS DO RECYCLER VIEW
            recyclerProdutos.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerProdutos, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Produto produtoSelecionado = produtos.get(position);

                            Intent i = new Intent(TelamainActivity.this, DetalhesProdutoActivity.class);
                            i.putExtra("produtoSelecionado", produtoSelecionado);
                            startActivity(i);
                            adapterProdutos.notifyDataSetChanged();



                }

                @Override
                public void onLongItemClick(View view, int position) {
                    Produto produtoSelecionado = produtos.get(position);
                    Intent i = new Intent( TelamainActivity.this, AtualizarProduto.class);
                    i.putExtra("produtoSelecionado", produtoSelecionado);
                    startActivity(i);
                }

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            }));


}

    private void pesquisarProdutos(String pesquisa){

        if(switchBusca.isChecked()){
            produtoUsuarioRef = ConfiguracaoFirebase.getFirebase().child("meus_produtos").child(ConfiguracaoFirebase.getIdUsuario());
            Query query2 = produtoUsuarioRef.orderByChild("codigo_filtro").startAt(pesquisa).endAt(pesquisa + "\uf8ff");
            query2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    produtos.clear();
                    for(DataSnapshot ds : snapshot.getChildren()){
                        produtos.add( ds.getValue(Produto.class) );

                    }

                    adapterProdutos.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }else{
            produtoUsuarioRef = ConfiguracaoFirebase.getFirebase().child("meus_produtos").child(ConfiguracaoFirebase.getIdUsuario());
            Query query = produtoUsuarioRef.orderByChild("nome_filtro").startAt(pesquisa).endAt(pesquisa + "\uf8ff");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    produtos.clear();
                    for(DataSnapshot ds : snapshot.getChildren()){
                        produtos.add( ds.getValue(Produto.class) );

                    }

                    adapterProdutos.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }


    }



    public void swipe(){

        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                excluirProduto( viewHolder );
            }
        };

        new ItemTouchHelper( itemTouch ).attachToRecyclerView(recyclerProdutos);

    }

    public void excluirProduto(final RecyclerView.ViewHolder viewHolder){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Excluir Produto/Peça do Cadastro");
            alertDialog.setMessage("Você tem certeza que deseja excluir o produto/peça selecionado?");
            alertDialog.setCancelable(false);

            alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int position = viewHolder.getAdapterPosition();
                    produto = produtos.get(position);
                    produtoUsuarioRef.child(produto.getIdProduto()).removeValue();
                    adapterProdutos.notifyItemRemoved( position );

                    

                }
            });
            alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(TelamainActivity.this, "O item não foi deletado!", Toast.LENGTH_SHORT).show();
                    adapterProdutos.notifyDataSetChanged();
                }
            });

            AlertDialog alert = alertDialog.create();
            alert.show();


    }


    private void recuperarProdutos(){
        dialog = new SpotsDialog.Builder().setContext(this).setMessage("Carregando").setCancelable(false).build();
        dialog.show();

        //ORDENAR DE A a Z
        produtoUsuarioRef = ConfiguracaoFirebase.getFirebase().child("meus_produtos").child(ConfiguracaoFirebase.getIdUsuario());
        Query query = produtoUsuarioRef.orderByChild("nome_filtro").startAt("a").endAt("z"+ "\uf8ff");

        //oRDENAR DE a A z
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                produtos.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    produtos.add( ds.getValue(Produto.class) );

                }

                adapterProdutos.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {


        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case  R.id.sair:
                autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
                autenticacao.signOut();
                startActivity(new Intent(this, CadastroActivity.class));
                finish();

                break;

            case R.id.clientes:
                Intent i = new Intent(getApplicationContext(), MeusClientesActivity.class);
                startActivity(i);

                break;

            case R.id.relatorio:
                startActivity(new Intent(TelamainActivity.this, OldPrintActivity.class));

                break;

            case R.id.orden:
                startActivity(new Intent(this, OsActivity.class));

                break;

            case R.id.empresa:
                startActivity(new Intent(this, EmpresaActivity.class));

                break;

            case R.id.info:
                Toast.makeText(TelamainActivity.this, "Para excluir cadastros arraste o item desejado para ao lado! \n\n " +
                        "Para editar um item, mantenha o item desejado pressionado!", Toast.LENGTH_LONG ).show();

                break;




        }
        return super.onOptionsItemSelected(item);
    }

    public void inicializarComponentes(){
        recyclerProdutos = findViewById(R.id.recyclerProdutos);
        searchView = findViewById(R.id.searchView);
        switchBusca = findViewById(R.id.switchBusca);
    }




}