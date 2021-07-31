package com.example.estoqueapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.blackcat.currencyedittext.CurrencyEditText;
import com.example.estoqueapp.R;
import com.example.estoqueapp.model.Produto;
import java.util.Locale;

public class CadastrarProdutoActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText campoCodigo, campoProduto, campoQtdProduto, textInfoAdicional;
    private CurrencyEditText campoValor, campoValVenda;
    private Produto produto;
    private Double QtdMult, ValMult, mult;
    private String formattedVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_produto);

        inicializarComponentes();

    }

    @Override
    public void onClick(View v) {



        }


    private void inicializarComponentes(){
        campoCodigo = findViewById(R.id.editCodigoProduto);
        campoProduto = findViewById(R.id.editNomeProduto);
        campoQtdProduto = findViewById(R.id.editQtdProduto);

        campoQtdProduto.setText("0");

        campoValor = findViewById(R.id.editValor);
        campoValVenda = findViewById(R.id.editValVenda);
        Locale locale = new Locale("pt", "BR");
        campoValor.setLocale(locale);
        campoValVenda.setLocale(locale);


        textInfoAdicional = findViewById(R.id.editInfoAdicional);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for( int permissaoResultado : grantResults ){
            if( permissaoResultado==PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }
    }



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

    public void validarDadosProduto(View view){
            produto = configurarProduto();

            String valor = String.valueOf(campoValor.getRawValue());
            String valVenda = String.valueOf(campoValVenda.getRawValue());
           // String quantidade = String.valueOf(Integer.parseInt(campoQtdProduto.getText().toString()));


                if(!produto.getCodigo().isEmpty()){
                    if(!produto.getProduto().isEmpty()){
                        if(!produto.getQuantidade().isEmpty()){
                            if(!valor.isEmpty() && !valor.equals("0")){
                                if(!valVenda.isEmpty() && !valVenda.equals("0")){

                                    produto.salvar();
                                    Toast.makeText(CadastrarProdutoActivity.this, "Produto/ Peça Salva!", Toast.LENGTH_SHORT);
                                    finish();

                                }else{
                                    exibirMensagemErro("Preencha o campo valor de venda");
                                }


                            }else{
                                exibirMensagemErro("Preencha o campo valor unitário");
                            }


                        }else{
                            exibirMensagemErro("Preencha o campo Quantidade");
                        }

                    }else{
                        exibirMensagemErro("Preencha o campo Produto");
                    }

                }else{
                    exibirMensagemErro("Preencha o campo código");
                }


    }

    private void exibirMensagemErro(String mensagem){
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }




    private Produto configurarProduto(){
        String codigo = campoCodigo.getText().toString();
        String nomeproduto = campoProduto.getText().toString();
        String quantidade = campoQtdProduto.getText().toString();
        String valor = campoValor.getText().toString();
        String valVenda = campoValVenda.getText().toString();
        String info = textInfoAdicional.getText().toString();


        verificaQuantidade();

        /*
       // Double ValMult = Double.parseDouble(String.valueOf(campoValor.getRawValue()));
        ValMult = Double.parseDouble(String.valueOf(campoValor.getRawValue()));
        Double QtdMult = Double.parseDouble(quantidade);
        //Double mult = ValMult/10 * QtdMult;
        mult = ValMult/10 * QtdMult;
        formattedVal = campoValor.formatCurrency(String.valueOf(mult));

        //String formattedVal = campoValor.formatCurrency(String.valueOf(mult));
*/


        Produto produto = new Produto();
        produto.setCodigo( codigo );
        produto.setProduto( nomeproduto );
        produto.setQuantidade( quantidade );
        produto.setValor( valor );
        produto.setValVenda( valVenda );
        produto.setNome_filtro( nomeproduto );
        produto.setMult( formattedVal );
        produto.setInfo(info);
        produto.setCodigo_filtro(codigo);


        return produto;
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
            Toast.makeText(CadastrarProdutoActivity.this, "A Quantidade NÃO Foi Preenchida", Toast.LENGTH_LONG).show();
        }

    }


}