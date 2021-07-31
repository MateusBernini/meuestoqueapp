package com.example.estoqueapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.estoqueapp.R;
import com.example.estoqueapp.model.Cliente;
import com.santalu.maskara.widget.MaskEditText;

public class CadastrarClienteActivity extends AppCompatActivity {

    private EditText campoNomecliente, campoEmailCliente, campoEnderecoCliente, campoInfoCliente;
    private Cliente cliente;
    private MaskEditText campoCelularCliente, campoTelefoneCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_cliente);
        getSupportActionBar().setTitle("Cadastrar Cliente");

        inicializarcomponentes();

    }

    public void inicializarcomponentes(){
        campoNomecliente = findViewById(R.id.txtNomeCliente);
        campoCelularCliente = findViewById(R.id.txtCelularCliente);
        campoEmailCliente = findViewById(R.id.txtEmailCliente);
        campoEnderecoCliente = findViewById(R.id.txtEnderecoCliente);
        campoInfoCliente = findViewById(R.id.txtInfoCliente);

    }

    public void salvarCliente(View view){
        cliente = configurarCliente();
        String cel = "";
        if(campoCelularCliente.getUnMasked() !=null){
            cel = campoCelularCliente.getUnMasked();
        }



        if(!cliente.getNomeCliente().isEmpty()){
            if(!cliente.getCelular().isEmpty() && cel.length() >=10 ){
                cliente.salvar();
                finish();
            }else{
                exibirMensagemErro("O campo Celular não foi Preenchido \n\n Digite ao menos 10 números!");
            }

        }else{
            exibirMensagemErro("O Campo nome não foi Preenchido");
        }


    }

    private void exibirMensagemErro(String mensagem){
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }

    private Cliente configurarCliente(){
        String nome = campoNomecliente.getText().toString();
        String celular = campoCelularCliente.getText().toString();
        String email = campoEmailCliente.getText().toString();
        String endereco = campoEnderecoCliente.getText().toString();
        String info = campoInfoCliente.getText().toString();


        Cliente cliente = new Cliente();
        cliente.setNome_cli_filtro(nome);
        cliente.setNomeCliente(nome);
        cliente.setCelular(celular);
        cliente.setEmail(email);
        cliente.setEndereco(endereco);
        cliente.setInfo(info);


        return cliente;
    }


}