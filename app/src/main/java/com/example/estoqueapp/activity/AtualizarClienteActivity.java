package com.example.estoqueapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.example.estoqueapp.R;
import com.example.estoqueapp.model.Cliente;
import com.santalu.maskara.widget.MaskEditText;

public class AtualizarClienteActivity extends AppCompatActivity {

    private EditText nomeClienteAtualizar, emailAtualizar, enderecoAtualizar, infoAdicAtualizar;
    private Cliente clienteSelecionado;
    private MaskEditText celularClienteAtualizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualizar_cliente);
        getSupportActionBar().setTitle("Editar Dados do Cliente");

        clienteSelecionado = (Cliente) getIntent().getSerializableExtra("clienteSelecionado");

        inicializarComponentes();

        nomeClienteAtualizar.setText(clienteSelecionado.getNomeCliente());
        celularClienteAtualizar.setText(clienteSelecionado.getCelular());
        emailAtualizar.setText(clienteSelecionado.getEmail());
        enderecoAtualizar.setText(clienteSelecionado.getEndereco());
        infoAdicAtualizar.setText(clienteSelecionado.getInfo());





    }

    private void inicializarComponentes() {
        nomeClienteAtualizar =findViewById(R.id.nomeAtualizar);
        celularClienteAtualizar = findViewById(R.id.celularAtualizar);
        emailAtualizar = findViewById(R.id.emailAtualizar);
        enderecoAtualizar = findViewById(R.id.enderecoAtualizar);
        infoAdicAtualizar = findViewById(R.id.infoAdicAtualizar);

    }

    public void atualizarCliente(View view){
        clienteSelecionado = configurarCliente();
        String cel = "";
            if(celularClienteAtualizar.getUnMasked() !=null){
                cel = celularClienteAtualizar.getUnMasked().toString();
            }


        if(!clienteSelecionado.getNomeCliente().isEmpty()){
            if(!clienteSelecionado.getCelular().isEmpty() && cel.length() >=10 ){
                    clienteSelecionado.atualizar();
                    finish();
                Toast.makeText(this, "Alterações realizadas com sucesso!", Toast.LENGTH_SHORT).show();

            }else{
                exibirMensagemErro("O campo Celular não foi Preenchido \n\n Digite ao menos 10 números!");
            }

        }else{
            exibirMensagemErro("O Campo nome não foi Preenchido");
        }


    }

    private Cliente configurarCliente() {
        String nome = nomeClienteAtualizar.getText().toString();
        String celular = celularClienteAtualizar.getText().toString();
        String email = emailAtualizar.getText().toString();
        String endereco = enderecoAtualizar.getText().toString();
        String info = infoAdicAtualizar.getText().toString();

        clienteSelecionado.setNomeCliente(nome);
        clienteSelecionado.setCelular(celular);
        clienteSelecionado.setEmail(email);
        clienteSelecionado.setEndereco(endereco);
        clienteSelecionado.setInfo(info);

        return clienteSelecionado;
    }

    private void exibirMensagemErro(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }

}