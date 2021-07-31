package com.example.estoqueapp.model;

import com.example.estoqueapp.helper.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

public class Empresa {
    private String idEmpresa;
    private String nomeempresa, celular, email, cep, telefone;

    public Empresa() {
        DatabaseReference empresaRef = ConfiguracaoFirebase.getFirebase().child("empresa");
        //setIdEmpresa( empresaRef.push().getKey() );
        setIdEmpresa( "1" );
    }

    public void salvar(){
        String idUsuario = ConfiguracaoFirebase.getIdUsuario();
        DatabaseReference empresaRef = ConfiguracaoFirebase.getFirebase().child("empresa");
        //setIdEmpresa( empresaRef.push().getKey() );
        setIdEmpresa( "1" );

        empresaRef.child(idUsuario).child(getIdEmpresa()).setValue(this);


    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNomeempresa() {
        return nomeempresa;
    }

    public void setNomeempresa(String nomeempresa) {
        this.nomeempresa = nomeempresa;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
