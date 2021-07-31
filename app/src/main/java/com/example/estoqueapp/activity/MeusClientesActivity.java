package com.example.estoqueapp.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.example.estoqueapp.adapter.AdapterClientes;
import com.example.estoqueapp.helper.ConfiguracaoFirebase;
import com.example.estoqueapp.helper.RecyclerItemClickListener;
import com.example.estoqueapp.model.Cliente;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Toast;
import com.example.estoqueapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;



public class MeusClientesActivity extends AppCompatActivity {

    private RecyclerView recyclerClientes;
    private List<Cliente> clientes = new ArrayList<>();
    private AdapterClientes adapterClientes;
    private DatabaseReference clienteUsuarioRef;
    private Cliente cliente;
    private SearchView clientesSearch;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_clientes);


        clienteUsuarioRef = ConfiguracaoFirebase.getFirebase().child("meus_clientes").child(ConfiguracaoFirebase.getIdUsuario());

        inicializarComponentes();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab2 = findViewById(R.id.fabClientes);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CadastrarClienteActivity.class));
            }
        });
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerClientes.setLayoutManager(new LinearLayoutManager(this));
        recyclerClientes.setHasFixedSize(true);

        adapterClientes = new AdapterClientes(clientes, this);
        recyclerClientes.setAdapter(adapterClientes);

        recuperarClientes();



        //chama swipe
        swipe();

        //APLICA EVENTO DE CLIQUE NOS ITENS DO RECYCLER VIEW

        recyclerClientes.addOnItemTouchListener( new RecyclerItemClickListener(this, recyclerClientes, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Cliente clienteSelecionado = clientes.get(position);
                Intent i = new Intent( MeusClientesActivity.this, DetalhesClienteActivity.class);
                i.putExtra("clienteSelecionado", clienteSelecionado);
                startActivity(i);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                Cliente clienteSelecionado = clientes.get(position);
                Intent i = new Intent( MeusClientesActivity.this, AtualizarClienteActivity.class);
                i.putExtra("clienteSelecionado", clienteSelecionado);
                startActivity(i);
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));



        //CONFIGURAÇÃO DO SEARCH VIEW
        clientesSearch.setQueryHint("Pesquisar Clientes");
        clientesSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                pesquisarClientes( newText );
                return true;
            }


        });


    }

    private void pesquisarClientes(String pesquisa){

        clienteUsuarioRef = ConfiguracaoFirebase.getFirebase().child("meus_clientes").child(ConfiguracaoFirebase.getIdUsuario());
        Query query2 = clienteUsuarioRef.orderByChild("nome_cli_filtro").startAt(pesquisa).endAt(pesquisa+ "\uf8ff");
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                clientes.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    clientes.add(ds.getValue(Cliente.class));

                }
                //Collections.reverse(clientes);
                adapterClientes.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void recuperarClientes(){
        clienteUsuarioRef = ConfiguracaoFirebase.getFirebase().child("meus_clientes").child(ConfiguracaoFirebase.getIdUsuario());
        Query query = clienteUsuarioRef.orderByChild("nome_cli_filtro").startAt("a").endAt("z"+ "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                clientes.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    clientes.add(ds.getValue(Cliente.class));

                }
                //Collections.reverse(clientes);
                   adapterClientes.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void inicializarComponentes(){
        recyclerClientes = findViewById(R.id.recyclerClientes);
        clientesSearch = findViewById(R.id.clientesSearch);


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
                excluirCliente( viewHolder );
            }
        };

        new ItemTouchHelper( itemTouch ).attachToRecyclerView(recyclerClientes);

    }

    public void excluirCliente(final RecyclerView.ViewHolder viewHolder){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Excluir Cliente do Cadastro");
        alertDialog.setMessage("Você tem certeza que deseja excluir o produto/peça selecionado?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();
                cliente = clientes.get(position);
                clienteUsuarioRef.child(cliente.getIdCliente()).removeValue();
                adapterClientes.notifyItemRemoved( position );




            }
        });
        alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MeusClientesActivity.this, "O item não foi deletado!", Toast.LENGTH_SHORT).show();
                adapterClientes.notifyDataSetChanged();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();


    }




}