package com.example.estoqueapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.estoqueapp.R;
import com.example.estoqueapp.model.Produto;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class DetalhesProdutoActivity extends AppCompatActivity {
    //private CarouselView carouselView;
    private TextView nomeProduto;
    private TextView codigo;
    private TextView quantidade;
    private TextView valor;
    private TextView valTotal;
    private TextView valVenda;
    private TextView infoDetalhes;
    private Produto produtoSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_produto);

        inicializarComponentes();

        produtoSelecionado = (Produto) getIntent().getSerializableExtra("produtoSelecionado");

        if(produtoSelecionado !=null){


            nomeProduto.setText(produtoSelecionado.getProduto());
            codigo.setText(produtoSelecionado.getCodigo());
            quantidade.setText(produtoSelecionado.getQuantidade());
            valor.setText(produtoSelecionado.getValor() );
            valTotal.setText( produtoSelecionado.getMult() );
            valVenda.setText(produtoSelecionado.getValVenda() );
            infoDetalhes.setText(produtoSelecionado.getInfo());
        }

       // final ImageListener imageListener = new ImageListener() {
        //    @Override
        //    public void setImageForPosition(int position, ImageView imageView) {

       //         String urlString = produtoSelecionado.getFotos().get(position);
        //        Picasso.get().load(urlString).into(imageView);

       //     }
     //   };



           // carouselView.setPageCount(produtoSelecionado.getFotos().size());
            //carouselView.setImageListener(imageListener);



    }

    private void inicializarComponentes(){
        //carouselView = findViewById(R.id.carouselView);
        nomeProduto = findViewById(R.id.textProdutoDetalhe);
        codigo = findViewById(R.id.textCodigoDetalhe);
        quantidade = findViewById(R.id.textQuantidadeDetalhe);
        valor = findViewById(R.id.textValDetalhe);
        valTotal = findViewById(R.id.textValTotalDetalhe);
        valVenda = findViewById(R.id.TextValVendaDetalhe);
        infoDetalhes = findViewById(R.id.textInfoDetalhe);

    }



}