package com.example.estoqueapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import com.example.estoqueapp.adapter.AdapterOs;
import com.example.estoqueapp.helper.ConfiguracaoFirebase;
import com.example.estoqueapp.helper.RecyclerItemClickListener;
import com.example.estoqueapp.model.OrdemServico;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Switch;
import android.widget.Toast;
import com.example.estoqueapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import dmax.dialog.SpotsDialog;



public class OsActivity extends AppCompatActivity {
    private RecyclerView recyclerOs;
    private AdapterOs adapterOs;
    private List<OrdemServico> os = new ArrayList<>();
    private android.app.AlertDialog dialog;
    private DatabaseReference OsUsuarioRef;
    private OrdemServico ordemServico;
    private SearchView searchOs;
    private Switch switchOs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_os);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CadastrarOsActivity.class));
            }
        });

        inicializarComponentes();

        //Configurar o RecyclerView
        recyclerOs.setLayoutManager(new LinearLayoutManager(this));
        recyclerOs.setHasFixedSize(true);
        adapterOs = new AdapterOs(os, this);
        recyclerOs.setAdapter( adapterOs );

        //Recupera as Os
        recuperarOs();

        swipe();

        //CONFIGURAÇÃO DO SEARCH VIEW
        searchOs.setQueryHint("Pesquisar Ordens de Serviço");
        searchOs.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                pesquisarOs( newText );
                return true;
            }
        });


        //aplica evento de clique no recycler os

        recyclerOs.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerOs, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                OrdemServico ordemSelecionada = os.get(position);
                Intent i = new Intent(OsActivity.this, DetalhesOsActivity.class);
                i.putExtra("ordemSelecionada", ordemSelecionada);
                startActivity(i);
                adapterOs.notifyDataSetChanged();


            }

            @Override
            public void onLongItemClick(View view, int position) {
                OrdemServico ordemSelecionada = os.get(position);
                Intent i = new Intent(OsActivity.this, EditarOsActivity.class);
                i.putExtra("ordemSelecionada", ordemSelecionada);
                startActivity(i);
                adapterOs.notifyDataSetChanged();


            }
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));




    }





    private void recuperarOs() {
        dialog = new SpotsDialog.Builder().setContext(this).setMessage("Carregando").setCancelable(false).build();
        dialog.show();

        //ORDENAR DE A a Z
        OsUsuarioRef = ConfiguracaoFirebase.getFirebase().child("minhas_os").child(ConfiguracaoFirebase.getIdUsuario());
        Query query = OsUsuarioRef.orderByChild("tipoOs").startAt("1").endAt("3" + "\uf8ff");

        //oRDENAR DE a A z
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                os.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    os.add( ds.getValue(OrdemServico.class) );

                }

                adapterOs.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void inicializarComponentes() {
        recyclerOs = findViewById(R.id.recyclerOs);
        searchOs = findViewById(R.id.searchOs);
        switchOs = findViewById(R.id.switchOs);
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
                excluirOs( viewHolder );
            }
        };

        new ItemTouchHelper( itemTouch ).attachToRecyclerView(recyclerOs);

    }

    public void excluirOs(final RecyclerView.ViewHolder viewHolder){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Excluir Ordem de Serviço do Cadastro");
        alertDialog.setMessage("Você tem certeza que deseja excluir a Ordem de Serviço selecionado?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();
                ordemServico = os.get(position);
                OsUsuarioRef.child(ordemServico.getIdOs()).removeValue();
                adapterOs.notifyItemRemoved( position );



            }
        });
        alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(OsActivity.this, "O item não foi deletado!", Toast.LENGTH_SHORT).show();
                adapterOs.notifyDataSetChanged();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();


    }






    private void pesquisarOs(String pesquisa){

        if(switchOs.isChecked()){
            OsUsuarioRef = ConfiguracaoFirebase.getFirebase().child("minhas_os").child(ConfiguracaoFirebase.getIdUsuario());
            Query query2 = OsUsuarioRef.orderByChild("dataEmissao").startAt(pesquisa).endAt(pesquisa + "\uf8ff");
            query2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    os.clear();
                    for(DataSnapshot ds : snapshot.getChildren()){
                        os.add( ds.getValue(OrdemServico.class) );

                    }

                    adapterOs.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }else{
            OsUsuarioRef = ConfiguracaoFirebase.getFirebase().child("minhas_os").child(ConfiguracaoFirebase.getIdUsuario());
            Query query = OsUsuarioRef.orderByChild("cliente_filtro").startAt(pesquisa).endAt(pesquisa + "\uf8ff");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    os.clear();
                    for(DataSnapshot ds : snapshot.getChildren()){
                        os.add( ds.getValue(OrdemServico.class) );

                    }

                    adapterOs.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }


    }






//Remover se der Errado>>>>>>->>>>>->>>>






}