package com.example.estoqueapp.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.estoqueapp.R;
import com.example.estoqueapp.model.Produto;



import java.util.List;

public class AdapterProdutosOs extends RecyclerView.Adapter<AdapterProdutosOs.MyViewHolder>{

    private List<Produto> produtos;
    private Context context;

    public AdapterProdutosOs(List<Produto> produtos, Context contex) {
        this.produtos = produtos;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterProdutosOs.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_produto_os, parent, false);
        return new AdapterProdutosOs.MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Produto produto = produtos.get(position);
        holder.produto.setText(produto.getProduto());



    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView produto;


        public MyViewHolder(View itemView){
            super(itemView);
            produto = itemView.findViewById(R.id.nomeProdOs);

        }



    }

























}
