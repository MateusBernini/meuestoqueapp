package com.example.estoqueapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.estoqueapp.R;
import com.example.estoqueapp.model.Cliente;
import java.util.List;

public class AdapterClientes extends RecyclerView.Adapter<AdapterClientes.MyViewHolder> {
    private List<Cliente> clientes;
    private Context context;

    public AdapterClientes(List<Cliente> clientes, Context context) {
        this.clientes = clientes;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cliente, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Cliente cliente = clientes.get(position);
        holder.nomeCliente.setText(cliente.getNomeCliente());
        holder.celular.setText(cliente.getCelular());
        holder.email.setText(cliente.getEmail());
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nomeCliente;
        TextView celular;
        TextView email;

        public MyViewHolder(View itemView){
            super(itemView);

            nomeCliente = itemView.findViewById(R.id.textNomeCli);
            celular = itemView.findViewById(R.id.textCelularCli);
            email = itemView.findViewById(R.id.textEmailCli);

        }

    }




}
