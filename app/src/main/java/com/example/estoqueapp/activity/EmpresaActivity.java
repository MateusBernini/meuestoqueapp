package com.example.estoqueapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.estoqueapp.R;
import com.example.estoqueapp.helper.ConfiguracaoFirebase;
import com.example.estoqueapp.model.Empresa;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmpresaActivity extends AppCompatActivity {
    EditText editNomeEmpresa, editCelEmpresa, editTelEmpresa, editEmailEmpresa, editCepEmpresa;
    Empresa empresa;
    TextView txtNomeEmpresa, txtCelEmpresa, txtTelempresa, txtEmailempresa, txtCepEmpresa;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("empresa").child(ConfiguracaoFirebase.getIdUsuario());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);

        inicializarComponentes();
        TextChanges();

        myRef = ConfiguracaoFirebase.getFirebase().child("empresa").child(ConfiguracaoFirebase.getIdUsuario()).child("1");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nome= String.valueOf(snapshot.child("nomeempresa").getValue());
                String cel = String.valueOf(snapshot.child("celular").getValue());
                String tel = String.valueOf(snapshot.child("telefone").getValue());
                String email = String.valueOf(snapshot.child("email").getValue());
                String cep = String.valueOf(snapshot.child("cep").getValue());

                txtNomeEmpresa.setText(nome);
                txtCelEmpresa.setText(cel);
                txtEmailempresa.setText(email);
                txtTelempresa.setText(tel);
                txtCepEmpresa.setText(cep);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void inicializarComponentes() {
        editNomeEmpresa = findViewById(R.id.editNomeEmpresa);
        editCelEmpresa = findViewById(R.id.editCelEmpresa);
        editTelEmpresa = findViewById(R.id.editTelEmpresa);
        editEmailEmpresa = findViewById(R.id.editEmailEmpresa);
        editCepEmpresa = findViewById(R.id.editCepEmpresa);

        txtNomeEmpresa = findViewById(R.id.txtNomeEmpresa);
        txtCelEmpresa = findViewById(R.id.txtCelEmpresa);
        txtTelempresa = findViewById(R.id.txtTelEmpresa);
        txtCepEmpresa = findViewById(R.id.txtCepEmpresa);
        txtEmailempresa = findViewById(R.id.txtEmailEmpresa);
    }

    public void addEmpresa(View view){
        empresa = configurarEmpresa();
        empresa.salvar();


    }

    private Empresa configurarEmpresa(){
        String nome = editNomeEmpresa.getText().toString();
        String cel = editCelEmpresa.getText().toString();
        String tel = editTelEmpresa.getText().toString();
        String email = editEmailEmpresa.getText().toString();
        String cep  = editCepEmpresa.getText().toString();

        Empresa empresa = new Empresa();

        empresa.setNomeempresa(nome);
        empresa.setCelular(cel);
        empresa.setTelefone(tel);
        empresa.setEmail(email);
        empresa.setCep(cep);
        return empresa;


    }


    private void TextChanges(){
        editNomeEmpresa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                txtNomeEmpresa.setText(editNomeEmpresa.getText().toString());
            }
        });

        editCelEmpresa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                txtCelEmpresa.setText(editCelEmpresa.getText().toString());
            }
        });

        editTelEmpresa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                txtTelempresa.setText(editTelEmpresa.getText().toString());
            }
        });

        editEmailEmpresa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                txtEmailempresa.setText(editEmailEmpresa.getText().toString());
            }
        });

        editCepEmpresa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                txtCepEmpresa.setText(editCepEmpresa.getText().toString());
            }
        });

    }




}