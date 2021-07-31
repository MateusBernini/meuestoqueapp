package com.example.estoqueapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.estoqueapp.R;
import com.example.estoqueapp.model.OrdemServico;

import java.util.List;

public class AdapterOs extends RecyclerView.Adapter<AdapterOs.MyViewHolder> {
    private List<OrdemServico> Oss;
    private Context context;

    public AdapterOs(List<OrdemServico> Oss, Context context) {
        this.Oss = Oss;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_os, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OrdemServico os = Oss.get(position);
        holder.nomeCliente.setText(os.getCliente());
        holder.celular.setText(os.getCelular());
        holder.data.setText(os.getDataEmissao());
        holder.descricao.setText(os.getDescricao());
        holder.valTotalOs.setText(os.getTotalOs());
        holder.tipo.setText(os.getTipoOs());


    }

    @Override
    public int getItemCount() {
        return Oss.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nomeCliente;
        TextView celular;
        TextView data;
        TextView descricao;
        TextView valTotalOs;
        TextView tipo;

        public MyViewHolder(View itemView){
            super(itemView);

            nomeCliente = itemView.findViewById(R.id.nomeClienteOs);
            celular = itemView.findViewById(R.id.celularclienteOs);
            data = itemView.findViewById(R.id.dataOs);
            descricao = itemView.findViewById(R.id.descricaoOs);
            valTotalOs = itemView.findViewById(R.id.valorTotalOs);
            tipo = itemView.findViewById(R.id.tipoOs);

        }

    }


}
