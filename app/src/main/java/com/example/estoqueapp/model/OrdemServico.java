package com.example.estoqueapp.model;

import com.example.estoqueapp.helper.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.ArrayList;

public class OrdemServico implements Serializable {
    private String idOs;
    private String dataEmissao;
    private String cliente;
    private String celular;
    private String telefone;
    private String endereco;
    private String descricao;
    private String email;
    private String valor;
    //produto:
    //mao obra
    private String maoObra;
    private String totalOs;
    private String tipoOs;
    private String cliente_filtro;

    //listas!!
    //LISTA ACUMULADAS
    private String listaItens;
    private String listaTotalCadaItem;
    //LISTA DE CADA VALOR

    //private String listaSoDeProdutos;
    private ArrayList<String> listaSodeProdutos;
    private ArrayList<String> listaSoDeValUnitario;
    private ArrayList<String> listaSoDeValTotemString;
    private ArrayList<String> listaSoDeQtd;

    private String totalItens;


    private ArrayList<String> listaItensArray = new ArrayList<>();

    private ArrayList<Double> listatotItensdouble = new ArrayList<>();


    // Lista produtos, valunit ..completa



    public OrdemServico() {


        DatabaseReference osRef = ConfiguracaoFirebase.getFirebase().child("minhas_os");
        setIdOs(osRef.push().getKey());



    }

    public void salvar(){
        String idOs = ConfiguracaoFirebase.getIdUsuario();
        DatabaseReference osRef = ConfiguracaoFirebase.getFirebase().child("minhas_os");
        setIdOs( osRef.push().getKey() );

        osRef.child(idOs).child(getIdOs()).setValue(this);

    }

    public void atualizar(){
        String idUsuario = ConfiguracaoFirebase.getIdUsuario();
        DatabaseReference osRef = ConfiguracaoFirebase.getFirebase().child("minhas_os").child(idUsuario).child(getIdOs());
        osRef.setValue(this);

    }


    public String getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(String dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getIdOs() {
        return idOs;
    }

    public void setIdOs(String idOs) {
        this.idOs = idOs;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMaoObra() {
        return maoObra;
    }

    public void setMaoObra(String maoObra) {
        this.maoObra = maoObra;
    }

    public String getTotalOs() {
        return totalOs;
    }

    public void setTotalOs(String totalOs) {
        this.totalOs = totalOs;
    }

    public String getTipoOs() {
        return tipoOs;
    }

    public void setTipoOs(String tipoOs) {
        this.tipoOs = tipoOs;
    }

    public String getCliente_filtro() {
        return cliente_filtro;
    }

    public void setCliente_filtro(String cliente_filtro) {
        this.cliente_filtro = cliente_filtro.toLowerCase();
    }

    public String getListaItens() {
        return listaItens;
    }

    public void setListaItens(String listaItens) {
        this.listaItens = listaItens;
    }

    public String getListaTotalCadaItem() {
        return listaTotalCadaItem;
    }

    public void setListaTotalCadaItem(String listaTotalCadaItem) {
        this.listaTotalCadaItem = listaTotalCadaItem;
    }


    public ArrayList<String> getListaSodeProdutos() {
        return listaSodeProdutos;
    }

    public void setListaSodeProdutos(ArrayList<String> listaSodeProdutos) {
        this.listaSodeProdutos = listaSodeProdutos;
    }

    public ArrayList<String> getListaSoDeValUnitario() {
        return listaSoDeValUnitario;
    }

    public void setListaSoDeValUnitario(ArrayList<String> listaSoDeValUnitario) {
        this.listaSoDeValUnitario = listaSoDeValUnitario;
    }

    public ArrayList<String> getListaSoDeValTotemString() {
        return listaSoDeValTotemString;
    }

    public void setListaSoDeValTotemString(ArrayList<String> listaSoDeValTotemString) {
        this.listaSoDeValTotemString = listaSoDeValTotemString;
    }

    public ArrayList<String> getListaSoDeQtd() {
        return listaSoDeQtd;
    }

    public void setListaSoDeQtd(ArrayList<String> listaSoDeQtd) {
        this.listaSoDeQtd = listaSoDeQtd;
    }

    public String getTotalItens() {
        return totalItens;
    }

    public void setTotalItens(String totalItens) {
        this.totalItens = totalItens;
    }

    public ArrayList<String> getListaItensArray() {
        return listaItensArray;
    }

    public void setListaItensArray(ArrayList<String> listaItensArray) {
        this.listaItensArray = listaItensArray;
    }

    public ArrayList<Double> getListatotItensdouble() {
        return listatotItensdouble;
    }

    public void setListatotItensdouble(ArrayList<Double> listatotItensdouble) {
        this.listatotItensdouble = listatotItensdouble;
    }
}
