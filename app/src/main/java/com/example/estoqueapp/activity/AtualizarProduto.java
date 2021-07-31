package com.example.estoqueapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.example.estoqueapp.R;
import com.example.estoqueapp.helper.ConfiguracaoFirebase;
import com.example.estoqueapp.helper.Permissoes;
import com.example.estoqueapp.model.Produto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;

public class AtualizarProduto extends AppCompatActivity {

    private EditText campoCodigo, campoProduto, campoQtdProduto, textInfoAdicional;
    private ImageView imagem1, imagem2, imagem3;
    private CurrencyEditText campoValor, campoValVenda;
    private String[] permissoes = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    private List<String> listaFotosRecuperadas = new ArrayList<>();
    private List<String> listaURLFotos = new ArrayList<>();
    private StorageReference storage;
    private AlertDialog dialog;
    private Produto produtoSelecionado;
    private Double QtdMult, ValMult, mult;
    private String formattedVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualizar_produto);

        //configurações iniciais
        storage = ConfiguracaoFirebase.getFirebaseStorage();
        //Validar permissões
        Permissoes.validarPermissoes(permissoes, this, 1);

        inicializarComponentes();

        produtoSelecionado =(Produto) getIntent().getSerializableExtra("produtoSelecionado");

        if( produtoSelecionado !=null ){
            campoCodigo.setText(produtoSelecionado.getCodigo());
            campoProduto.setText(produtoSelecionado.getProduto());
            campoQtdProduto.setText(produtoSelecionado.getQuantidade());
            campoValor.setText(produtoSelecionado.getValor());
            campoValVenda.setText(produtoSelecionado.getValVenda());
            textInfoAdicional.setText(produtoSelecionado.getInfo());


        }


    }


    public void inicializarComponentes(){
        campoCodigo = findViewById(R.id.atualizarCodigoProduto);
        campoProduto = findViewById(R.id.atualizarNomeProduto);
        campoQtdProduto = findViewById(R.id.atualizarQtdProduto);
        campoValor = findViewById(R.id.atualizarValor);
        campoValVenda = findViewById(R.id.atualizarValVenda);
        Locale locale = new Locale("pt", "BR");
        campoValor.setLocale(locale);
        campoValVenda.setLocale(locale);
        //imagem1 = findViewById(R.id.imageAtualizar1);
       // imagem2 = findViewById(R.id.imageAtualizar2);
        //imagem3 = findViewById(R.id.imageAtualizar3);
        //imagem1.setOnClickListener(this);
        //imagem2.setOnClickListener(this);
        //imagem3.setOnClickListener(this);
        textInfoAdicional = findViewById(R.id.atualizarInfoAdicional);


    }
/*
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imageAtualizar1:
                escolherImagem(1);
                break;
            case R.id.imageAtualizar2:
                escolherImagem(2);
                break;
            case R.id.imageAtualizar3:
                escolherImagem(3);
                break;
        }
    }

    public void escolherImagem(int requestCode){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){

            Uri imagemSelecionada = data.getData();
            String caminhoImagem = imagemSelecionada.toString();

            if(requestCode == 1){
                imagem1.setImageURI(imagemSelecionada);

            }else if(requestCode == 2){
                imagem2.setImageURI(imagemSelecionada);
            }else if(requestCode == 3){
                imagem3.setImageURI(imagemSelecionada);
            }
            listaFotosRecuperadas.add(caminhoImagem);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for( int permissaoResultado : grantResults ){
            if( permissaoResultado== PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }
    }
*/
    private void alertaValidacaoPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário permitir acesso à galeria");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void atualizarDadosProduto(View view){
        produtoSelecionado = configurarProduto();
        String valor = String.valueOf(campoValor.getRawValue());
        String valVenda = String.valueOf(campoValVenda.getRawValue());
        String quantidade = String.valueOf(campoQtdProduto.getText());

        //if(listaFotosRecuperadas.size() !=0 ){
            if(!produtoSelecionado.getCodigo().isEmpty()){
                if(!produtoSelecionado.getProduto().isEmpty()){
                    if(!produtoSelecionado.getQuantidade().isEmpty()) {
                        if (!valor.isEmpty() && !valor.equals("0")) {
                            if (!valVenda.isEmpty() && !valVenda.equals("0")) {
                                produtoSelecionado.atualizar();
                                finish();
                                Toast.makeText(this, "Produto/ Peça alterada com sucesso!", Toast.LENGTH_SHORT).show();

                            } else {
                                exibirMensagemErro("Preencha o campo valor de venda");
                            }
                        } else {
                            exibirMensagemErro("Preencha o campo valor unitário");
                        }
                    }else{
                        exibirMensagemErro("Preencha a quantidade");
                    }

                }else{
                    exibirMensagemErro("Preencha o campo produto");
                }
            }else{
                exibirMensagemErro("Preencha o campo código");
            }
      //  }else{
        //    exibirMensagemErro("Se desejar adicionar uma ou mais imagens ao Produto/Peça basta clicar na figura!");

       // }

    }

    private void exibirMensagemErro(String mensagem){
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }
/*
    public void salvarProduto(){
        dialog = new SpotsDialog.Builder().setContext(this).setMessage("Atualizando Produto/Peça").setCancelable(false).build();
        dialog.show();

        //SALVAR IMAGEM NO STORAGE
        for(int i=0; i < listaFotosRecuperadas.size(); i++){
            String urlImagem = listaFotosRecuperadas.get(i);
            int tamanhoLista = listaFotosRecuperadas.size();
            salvarFotoStorage(urlImagem, tamanhoLista, i );

        }

    } */
/*
    private void salvarFotoStorage(String urlString, final int totalFotos, int contador){
        //criar nó no storage
        final StorageReference imagemProduto = storage.child("imagens").child("produtos").child(produtoSelecionado.getIdProduto()).child("imagem"+contador);

        UploadTask uploadTask = imagemProduto.putFile( Uri.parse(urlString) );
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagemProduto.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri firebaseUrl = task.getResult();
                        String urlConvertida = firebaseUrl.toString();
                        listaURLFotos.add(urlConvertida);

                        if(totalFotos == listaURLFotos.size()){
                            produtoSelecionado.setFotos(listaURLFotos);

                            produtoSelecionado.atualizar();

                            dialog.dismiss();
                            finish();
                        }

                    }
                });

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                exibirMensagemErro("Falha ao fazer upload");
                Log.i("INFO", "Falha ao fazer upload:" + e.getMessage());
            }
        });
    }
*/
    private Produto configurarProduto(){
        String codigo = campoCodigo.getText().toString();
        String nomeproduto = campoProduto.getText().toString();
        String quantidade = campoQtdProduto.getText().toString();
        String valor = campoValor.getText().toString();
        String valVenda = campoValVenda.getText().toString();
        String info = textInfoAdicional.getText().toString();


/*
        Double ValMult = Double.parseDouble(String.valueOf(campoValor.getRawValue()));
        Double QtdMult = Double.parseDouble(quantidade);
        Double mult = ValMult/100 * QtdMult;

        String multt = String.valueOf(mult.doubleValue());

  */

        verificaQuantidade();


        Produto produto = new Produto();
        produtoSelecionado.setCodigo( codigo );
        produtoSelecionado.setProduto( nomeproduto );
        produtoSelecionado.setQuantidade( quantidade );
        produtoSelecionado.setValor( valor );
        produtoSelecionado.setValVenda( valVenda );
        produtoSelecionado.setNome_filtro( nomeproduto );
        produtoSelecionado.setMult( formattedVal );
        produtoSelecionado.setInfo(info);





        return produtoSelecionado;



    }

    public void verificaQuantidade(){
        if(!campoQtdProduto.getText().toString().isEmpty()){
            // Double ValMult = Double.parseDouble(String.valueOf(campoValor.getRawValue()));
            ValMult = Double.parseDouble(String.valueOf(campoValor.getRawValue()));
            Double QtdMult = Double.parseDouble(campoQtdProduto.getText().toString());
            //Double mult = ValMult/10 * QtdMult;
            mult = ValMult/10 * QtdMult;
            formattedVal = campoValor.formatCurrency(String.valueOf(mult));

            //String formattedVal = campoValor.formatCurrency(String.valueOf(mult));
        }else{
            Toast.makeText(AtualizarProduto.this, "A Quantidade NÃO Foi Preenchida", Toast.LENGTH_LONG).show();
        }

    }




}