package com.example.estoqueapp.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfiguracaoFirebase {
    private static DatabaseReference referenciaFirebase;
    private static FirebaseAuth referenciaAutenticacao;
    private static StorageReference referenciaStorage;


    public static String getIdUsuario(){
        FirebaseAuth autenticacao = getFirebaseAutenticacao();
        return autenticacao.getCurrentUser().getUid();

    }


    //MÉTODO QUE RETORNA A REFERÊNCIA DO FIREBASE
    public static DatabaseReference getFirebase(){
        if(referenciaFirebase==null){
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }
        return referenciaFirebase;
    }

    //RETORNA A INSTANCIA DO FIREBASE AUTH
    public static FirebaseAuth getFirebaseAutenticacao(){
        if(referenciaAutenticacao==null){
            referenciaAutenticacao = FirebaseAuth.getInstance();
        }
        return referenciaAutenticacao;

    }

    public static StorageReference getFirebaseStorage(){
        if(referenciaStorage==null){
            referenciaStorage = FirebaseStorage.getInstance().getReference();
        }
        return referenciaStorage;
    }

}
