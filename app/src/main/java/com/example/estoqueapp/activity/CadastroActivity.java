package com.example.estoqueapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.estoqueapp.R;
import com.example.estoqueapp.helper.ConfiguracaoFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import okhttp3.internal.Util;

public class CadastroActivity extends AppCompatActivity {
    private Button botaoAcesso;
    private EditText campoEmail, campoSenha;
    private Switch tipoAcesso;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
            inicializarComponentes();
            autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

            botaoAcesso.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String email = campoEmail.getText().toString();
                    String senha = campoSenha.getText().toString();

                    if(!email.isEmpty()){
                        if(!senha.isEmpty()){
                            //Verifica o estado do switch
                            if(tipoAcesso.isChecked()){//Será feito o cadastro
                                autenticacao.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            verificarUsuarioLogado();
                                            Toast.makeText(CadastroActivity.this, "Cadastro Efetuado!", Toast.LENGTH_SHORT).show();

                                        }else{
                                            String erroExcecao = "";
                                            try{
                                                throw task.getException();
                                            }catch(FirebaseAuthWeakPasswordException e){
                                                erroExcecao = "Crie uma senha mais forte!";
                                            }catch(FirebaseAuthInvalidCredentialsException e){
                                                erroExcecao = "Email inválido!";
                                            }catch(FirebaseAuthUserCollisionException e){
                                                erroExcecao = "Esta conta já existe!";
                                            }catch(Exception e){
                                                erroExcecao = "ao cadastrar usuário:" + e.getMessage();
                                                e.printStackTrace();
                                            }
                                            Toast.makeText(CadastroActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                            }else{//Será feito o Login
                                    autenticacao.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                verificarUsuarioLogado();

                                                Toast.makeText(CadastroActivity.this, "Sucesso ao Logar!", Toast.LENGTH_SHORT).show();

                                            }else{
                                                Toast.makeText(CadastroActivity.this, "Erro ao Logar :" + task.getException(), Toast.LENGTH_SHORT).show();
                                            }



                                        }
                                    });
                            }


                        }else{Toast.makeText(CadastroActivity.this, "Preencha a senha!", Toast.LENGTH_SHORT).show();}


                    }else{
                        Toast.makeText(CadastroActivity.this, "Preencha o email!", Toast.LENGTH_SHORT).show();

                    }


                }
            });
        }

    protected void onStart(){super.onStart();
        verificarUsuarioLogado();
    }


    private void inicializarComponentes(){
        campoEmail = findViewById(R.id.editCadastroEmail);
        campoSenha = findViewById(R.id.editCadastroSenha);
        botaoAcesso = findViewById(R.id.buttonAcesso);
        tipoAcesso = findViewById(R.id.switchAcesso);}

    public void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        if(autenticacao.getCurrentUser() != null){
            abrirTelaPrincipal();

        }
    }

    public void abrirTelaPrincipal(){
        startActivity(new Intent(this, TelamainActivity.class));

    }

//Métodos recuperar senha abaixo! remover caso dê erro
    public void recuperarSenha(View view){

        String email = campoEmail.getText().toString();

            if(email.isEmpty()){
                Toast.makeText(getBaseContext(), "Insira o email para recuperar sua senha!", Toast.LENGTH_LONG).show();
            }else{
                enviarEmail(email);
            }


    }

    private void enviarEmail(String email){
        autenticacao.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getBaseContext(), "Uma mensagem foi enviada ao seu Email, verifique-o para redefinir sua senha!", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getBaseContext(), "Erro, email inválido!", Toast.LENGTH_LONG).show();
            }
        });

    }



    }





