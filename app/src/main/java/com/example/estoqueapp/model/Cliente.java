package com.example.estoqueapp.model;

import com.example.estoqueapp.helper.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class Cliente implements Serializable {
    private String idCliente;
    private String nomeCliente;
    private String celular;
    private String email;
    private String endereco;
    private String info;
    private String nome_cli_filtro;


    public Cliente() {
        DatabaseReference produtoRef = ConfiguracaoFirebase.getFirebase().child("meus_clientes");
        setIdCliente( produtoRef.push().getKey() );
    }

    public void salvar(){
        String idUsuario = ConfiguracaoFirebase.getIdUsuario();
        DatabaseReference produtoRef = ConfiguracaoFirebase.getFirebase().child("meus_clientes");
        setIdCliente( produtoRef.push().getKey() );

        produtoRef.child(idUsuario).child(getIdCliente()).setValue(this);


    }

    public void atualizar(){
        String idUsuario = ConfiguracaoFirebase.getIdUsuario();
        DatabaseReference produtoRef = ConfiguracaoFirebase.getFirebase().child("meus_clientes").child(idUsuario).child(getIdCliente());
        produtoRef.setValue(this);

    }


    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getNome_cli_filtro() {
        return nome_cli_filtro;
    }

    public void setNome_cli_filtro(String nome_cli_filtro) {
        this.nome_cli_filtro = nome_cli_filtro.toLowerCase();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
