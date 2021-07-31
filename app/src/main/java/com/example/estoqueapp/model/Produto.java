package com.example.estoqueapp.model;

import com.example.estoqueapp.helper.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import java.io.Serializable;
import java.util.List;

public class Produto implements Serializable {
    private String idProduto;
    private String codigo;
    private String produto;
    private String quantidade;
    private String valor;
    private String valVenda;
    private List<String> fotos;
    private String nome_filtro;
    private String mult;
    private String info;
    private String codigo_filtro;


    public Produto() {
        DatabaseReference produtoRef = ConfiguracaoFirebase.getFirebase().child("meus_produtos");
        setIdProduto( produtoRef.push().getKey() );

    }

    public void salvar(){
        String idUsuario = ConfiguracaoFirebase.getIdUsuario();
        DatabaseReference produtoRef = ConfiguracaoFirebase.getFirebase().child("meus_produtos");
        setIdProduto( produtoRef.push().getKey() );

        produtoRef.child(idUsuario).child(getIdProduto()).setValue(this);


    }

    public void atualizar(){
        String idUsuario = ConfiguracaoFirebase.getIdUsuario();
        DatabaseReference produtoRef = ConfiguracaoFirebase.getFirebase().child("meus_produtos").child(idUsuario).child(getIdProduto());
        produtoRef.setValue(this);

    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getValVenda() {
        return valVenda;
    }

    public void setValVenda(String valVenda) {
        this.valVenda = valVenda;
    }

    public List<String> getFotos() {
        return fotos;
    }

    public void setFotos(List<String> fotos) {
        this.fotos = fotos;
    }

    public String getNome_filtro() {
        return nome_filtro;
    }

    public void setNome_filtro(String nome_filtro) {
        this.nome_filtro = nome_filtro.toLowerCase();
    }

    public String getMult() {
        return mult;
    }

    public void setMult(String mult) {
        this.mult = mult;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCodigo_filtro() {
        return codigo_filtro;
    }

    public void setCodigo_filtro(String codigo_filtro) {
        this.codigo_filtro = codigo_filtro.toLowerCase();
    }
}
