package com.example.estoqueapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.estoqueapp.R;
import com.example.estoqueapp.model.Cliente;


public class DetalhesClienteActivity extends AppCompatActivity {
    private TextView nomeDet, celDet, emailDet, enderecoDet, infoDetail;
    private Cliente clienteSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_cliente);
        getSupportActionBar().setTitle("Detalhes do Cliente");
        inicializarComponentes();

        clienteSelecionado =(Cliente) getIntent().getSerializableExtra("clienteSelecionado");

        if(clienteSelecionado!=null){
            nomeDet.setText(clienteSelecionado.getNomeCliente());
            celDet.setText(clienteSelecionado.getCelular());
            emailDet.setText(clienteSelecionado.getEmail());
            enderecoDet.setText(clienteSelecionado.getEndereco());
            infoDetail.setText(clienteSelecionado.getInfo());


        }

    }

    public void visualizarCelular(View view){
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", clienteSelecionado.getCelular(), null));
        startActivity(i);
    }


    private void inicializarComponentes(){
        nomeDet = findViewById(R.id.nomedetail);
        celDet = findViewById(R.id.celDetail);
        emailDet = findViewById(R.id.emailDetail);
        enderecoDet = findViewById(R.id.enderecoDetail);
        infoDetail = findViewById(R.id.infoCliDetail);

    }

}