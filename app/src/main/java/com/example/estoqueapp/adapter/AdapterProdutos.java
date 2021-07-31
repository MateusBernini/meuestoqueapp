package com.example.estoqueapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.estoqueapp.R;
import com.example.estoqueapp.model.Produto;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterProdutos extends RecyclerView.Adapter<AdapterProdutos.MyViewHolder> {

    private List<Produto>produtos;
    private Context contex;

    public AdapterProdutos(List<Produto> produtos, Context contex) {
        this.produtos = produtos;
        this.contex = contex;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_produto, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Produto produto = produtos.get(position);
        holder.codigo.setText( produto.getCodigo() );
        holder.produto.setText(produto.getProduto());
        holder.valVenda.setText(produto.getValVenda());
        holder.quantidade.setText(produto.getQuantidade());

        //PEGAR A PRIMEIRA IMAGEM CADASTRADA PARA EXIBIR
        //List<String> urlFotos = produto.getFotos();
        //String urlCapa = urlFotos.get(0);
        //Picasso.get().load(urlCapa).into(holder.foto);


    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView codigo;
        TextView produto;
        TextView valVenda;
        TextView quantidade;
        //ImageView foto;

        public MyViewHolder(View itemView){
            super(itemView);

            produto = itemView.findViewById(R.id.textProduto);
            codigo = itemView.findViewById(R.id.textCodigo);
            quantidade = itemView.findViewById(R.id.textQuantidade);
            valVenda = itemView.findViewById(R.id.textValVenda);
            //foto = itemView.findViewById(R.id.imageProduto);

        }



    }

}
