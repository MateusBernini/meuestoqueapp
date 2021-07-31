package com.example.estoqueapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;
import com.blackcat.currencyedittext.CurrencyEditText;
import com.example.estoqueapp.R;
import com.example.estoqueapp.adapter.AdapterClientesOs;
import com.example.estoqueapp.adapter.AdapterProdutosOs;
import com.example.estoqueapp.helper.ConfiguracaoFirebase;
import com.example.estoqueapp.helper.RecyclerItemClickListener;
import com.example.estoqueapp.model.Cliente;
import com.example.estoqueapp.model.OrdemServico;
import com.example.estoqueapp.model.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.santalu.maskara.widget.MaskEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CadastrarOsActivity extends AppCompatActivity {
    private EditText campoCliente, campoEmail, campoEndereco, campoProdNomeOs, campoQtdProdOs, campoDescricaoServico;
    private MaskEditText campoCelular, editDataEmissao;
    private CurrencyEditText txtTotalOs;
    private CurrencyEditText campoValOs, campoValQtdMult, campoValMaoObra;
    private OrdemServico ordemServico;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("minhas_os").child(ConfiguracaoFirebase.getIdUsuario());
    private RecyclerView recyclerClientes;
    private List<Cliente> clientes = new ArrayList<>();
    private AdapterClientesOs adapterClientes;
    private DatabaseReference clienteUsuarioRef;
    private RecyclerView recyclerProdOs;
    private List<Produto> produtos = new ArrayList<>();
    private AdapterProdutosOs adapterProdutos;
    private DatabaseReference produtoUsuarioRef;
    private SearchView searchCliente, searchProduto;

    private Double qtd;

    private Spinner tipoOs;
    private Button limpar1;

    private Button limpar3;

    //VIEWS
    //private TextView listaItensView;
   // private TextView listaTotais;
   // private TextView totalItensdaListaview;
        //ARRAYS
    ArrayList<String>listaItensArray;
    ArrayList<Double>listaValTotItensArray;

    //ARRAYS PARA SALVAR NO MODEL
    private ArrayList<String>listaSoDeProdutos = new ArrayList<>();
    //String [] listaSoDeProdutos = {};
    private ArrayList<String>listaSoDeValUnitario = new ArrayList<>();
    private ArrayList<String>listaSoDeValTotemString = new ArrayList<>();
    private ArrayList<String>listaSoDeQtd = new ArrayList<>();


    //cURRENCY
    private CurrencyEditText listaFormatted;

    //Table array



    ListView listview;










    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_os);

        inicializarComponentes();


        listview = findViewById(R.id.listView);

        Locale locale = new Locale("pt", "BR");
        campoValOs.setLocale(locale);
        campoValQtdMult.setLocale(locale);
        campoValMaoObra.setLocale(locale);
        txtTotalOs.setLocale(locale);

        listaFormatted.setLocale(locale);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String valorSelecionado = listview.getItemAtPosition(position).toString();
                //valorSelecionado = "";


            }
        });


        listaItensArray = new ArrayList<>();
        listaValTotItensArray = new ArrayList<>();


        //USAR DEPOIS PARA TESTES
        //totalItensdaListaview = findViewById(R.id.listaTotais);

        //listaItensView = findViewById(R.id.listaItensViewRaw); //itens completos
        //listaTotais = findViewById(R.id.listaTotais);   //itens sem formatação, em double


        final ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_2, android.R.id.text1, listaItensArray);


        carregarDadosSpinner();

        recyclerClientes.setLayoutManager(new LinearLayoutManager(this));
        recyclerClientes.setHasFixedSize(true);

        adapterClientes = new AdapterClientesOs(clientes, this);
        recyclerClientes.setAdapter(adapterClientes);

        recuperarClientes();

        //recup produtos para recycler

        recyclerProdOs.setLayoutManager(new LinearLayoutManager(this));
        recyclerProdOs.setHasFixedSize(true);
        adapterProdutos = new AdapterProdutosOs(produtos, this);
        recyclerProdOs.setAdapter(adapterProdutos);



        recuperarProdutos();

            //funcionando
            limpar1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listaItensArray.add(" Produto/Peça:    "+campoProdNomeOs.getText().toString() + "\n" + " ValUnitário:    " +campoValOs.getText().toString() + "\n" +" Qtd:    " +campoQtdProdOs.getText().toString() + "\n"+" Total:    " +campoValQtdMult.getText().toString());
                    listaSoDeProdutos.add(campoProdNomeOs.getText().toString());
                    //um a um no lista so de produtos
                    listaSoDeProdutos.add(campoQtdProdOs.getText().toString());
                    listaSoDeProdutos.add(campoValOs.getText().toString());
                    listaSoDeProdutos.add(campoValQtdMult.getText().toString());

                    listaSoDeValUnitario.add(campoValOs.getText().toString());
                    listaSoDeValTotemString.add(campoValQtdMult.getText().toString());
                    listaSoDeQtd.add(campoQtdProdOs.getText().toString());

                    listview.setAdapter(adaptador);
                    adaptador.notifyDataSetChanged();

                    double o = campoValQtdMult.getRawValue();
                    double op = Double.parseDouble(String.valueOf(o/10));

                    listaValTotItensArray.add(op);

                    double total= 0;
                    for(int i = 0; i< listaValTotItensArray.size(); i++){ total += listaValTotItensArray.get(i); }

                    listaFormatted.setText(String.valueOf(total));


                    for(String lista: listaSoDeProdutos){
                        System.out.println(lista.toString());

                    }


                }
            });


            limpar3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listaItensArray.clear();
                    listaValTotItensArray.clear();
                    listaSoDeProdutos.clear();
                    listaSoDeValUnitario.clear();
                    listaSoDeValTotemString.clear();
                    listaSoDeQtd.clear();

                    adaptador.notifyDataSetChanged();

                }
            });



        //APLICA EVENTO DE CLIQUE NOS ITENS DO RECYCLER VIEW

        recyclerClientes.addOnItemTouchListener( new RecyclerItemClickListener(this, recyclerClientes, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Cliente clienteSelecionado = clientes.get(position);

                campoCliente.setText(clienteSelecionado.getNomeCliente());
                campoCelular.setText(clienteSelecionado.getCelular());
                campoEmail.setText(clienteSelecionado.getEmail());
                campoEndereco.setText(clienteSelecionado.getEndereco());

            }

            @Override
            public void onLongItemClick(View view, int position) {
                Cliente clienteSelecionado = clientes.get(position);

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));


        //Aplica evento de clique no recycler de produtos
        recyclerProdOs.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerProdOs, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final Produto produtoSelecionado = produtos.get(position);
                    campoProdNomeOs.setText(produtoSelecionado.getProduto());
                    campoValOs.setText(produtoSelecionado.getValVenda());

            }


            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));


       ListenerDoText();
       ListenerDoTextTotal();




        searchCliente.setQueryHint("Pesquisar Clientes");
        searchCliente.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                pesquisarClientes( newText );
                return true;
            }


        });


        //CONFIGURAÇÃO DO SEARCH VIEW
        searchProduto.setQueryHint("Pesquisar Produtos/ Peças");
        searchProduto.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                pesquisarProdutos( newText );
                return true;
            }
        });




    }

    public void inicializarComponentes() {
        campoCliente = findViewById(R.id.clienteOs);
        recyclerClientes = findViewById(R.id.recyclerCliOs);
        campoCelular = findViewById(R.id.celularOs);
        campoEmail = findViewById(R.id.emailOs);
        campoEndereco = findViewById(R.id.enderecoOs);
        recyclerProdOs = findViewById(R.id.recyclerProdOs);
        campoProdNomeOs = findViewById(R.id.campoProdNomeOs);
        campoValOs = findViewById(R.id.campoValOs);
        campoQtdProdOs = findViewById(R.id.campoQtdProdOs);
        campoValQtdMult = findViewById(R.id.campoValQtdMult);
        campoDescricaoServico = findViewById(R.id.campoDescricaoServico);
        campoValMaoObra = findViewById(R.id.valMaoObra);
        txtTotalOs = findViewById(R.id.txtTotalOs);
        editDataEmissao = findViewById(R.id.editDataEmissao);
        tipoOs = findViewById(R.id.spinnerTipo);
        listaFormatted = findViewById(R.id.listaItensViewFormatted); //jogar nesse pra pegar formatação
        searchCliente = findViewById(R.id.searchCliente);
        searchProduto = findViewById(R.id.searchProduto);

        //btsLimpar ====>

        limpar1 = findViewById(R.id.btLimpar1);

        limpar3 = findViewById(R.id.btLimpar3);

    }


    public void salvarOs(View view){

        ordemServico = configurarOs();
        String a = String.valueOf(txtTotalOs.getText().toString());
        String cel = "";
        if(campoCelular.getUnMasked() !=null ){
            cel = campoCelular.getUnMasked();
        }
        String dataVerif = "";
        if(editDataEmissao.getUnMasked() !=null){
            dataVerif = editDataEmissao.getUnMasked();
        }


        if(!ordemServico.getCliente().isEmpty()){
            if(!ordemServico.getCelular().isEmpty() && cel.length() >=10){
                if(!a.isEmpty() && !a.equals("0")){
                    if(!ordemServico.getDataEmissao().isEmpty() && dataVerif.length() >=6){
                            ordemServico.salvar();
                            Toast.makeText(this, "Odem de Serviço Cadastrada com Sucesso!", Toast.LENGTH_SHORT).show();
                            finish();

                    }else{
                        exibirMensagemErro("A data de emissão não foi preenchida! \n\n Preencha pelo menos 6 dígitos!");
                    }

                }else{
                    exibirMensagemErro("O campo valor total não foi preenchido!");
                }

            }else{
                exibirMensagemErro("Preencha o celular do Cliente! \n\n Preencha pelo menos 10 números");
            }

        }else{
            exibirMensagemErro("Preencha o nome do Cliente!");
        }







    }

    private void exibirMensagemErro(String mensagem){
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }

    private OrdemServico configurarOs(){

        String cliente =  campoCliente.getText().toString();
        String celular = campoCelular.getText().toString();
        String email = campoEmail.getText().toString();
        String endereco = campoEndereco.getText().toString();
        String descricao = campoDescricaoServico.getText().toString();
        String maoObra = campoValMaoObra.getText().toString();
        String totalOs = txtTotalOs.getText().toString();
        String data = editDataEmissao.getText().toString();
        String tipo = tipoOs.getSelectedItem().toString();

        //ALERANDO AQUI==============================================
        String totListaItens = listaFormatted.getText().toString();
        //===========================================================
        String listaItens = listaItensArray.toString();

        String lsdp = listaSoDeProdutos.toString();
        String lsdvu = listaSoDeValUnitario.toString();
        String lsdvts = listaSoDeValTotemString.toString();
        String lsdq = listaSoDeQtd.toString();

        OrdemServico ordemServico = new OrdemServico();
        ordemServico.setCliente(cliente);
        ordemServico.setCelular(celular);
        ordemServico.setEmail(email);
        ordemServico.setEndereco(endereco);
        ordemServico.setDescricao(descricao);
        ordemServico.setMaoObra(maoObra);
        ordemServico.setTotalOs(totalOs);
        ordemServico.setDataEmissao(data);
        ordemServico.setTipoOs(tipo);
        ordemServico.setCliente_filtro(cliente);
        ordemServico.setListaSodeProdutos(listaSoDeProdutos);
        ordemServico.setListaSoDeValUnitario(listaSoDeValUnitario);
        ordemServico.setListaSoDeValTotemString(listaSoDeValTotemString);
        ordemServico.setListaSoDeQtd(listaSoDeQtd);

        ordemServico.setListatotItensdouble(listaValTotItensArray);

        ordemServico.setListaItens(listaItens);

        //ALTERANDO AQUI==============================
        ordemServico.setTotalItens(totListaItens);
        ordemServico.setListaItensArray(listaItensArray);
        //===========================================


        return ordemServico;

    }

    private void recuperarClientes(){
        clienteUsuarioRef = ConfiguracaoFirebase.getFirebase().child("meus_clientes").child(ConfiguracaoFirebase.getIdUsuario());
        Query query = clienteUsuarioRef.orderByChild("nome_cli_filtro").startAt("a").endAt("z"+ "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                clientes.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    clientes.add(ds.getValue(Cliente.class));

                }
                //Collections.reverse(clientes);
                adapterClientes.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void recuperarProdutos(){
        produtoUsuarioRef = ConfiguracaoFirebase.getFirebase().child("meus_produtos").child(ConfiguracaoFirebase.getIdUsuario());
        Query query2 = produtoUsuarioRef.orderByChild("nome_filtro").startAt("a").endAt("z"+ "\uf8ff");
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                produtos.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    produtos.add(ds.getValue(Produto.class));

                }
                adapterProdutos.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void ListenerDoText(){
        //ITEM1
        campoQtdProdOs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                if(!campoQtdProdOs.getText().toString().isEmpty() && !campoValOs.getText().toString().isEmpty()){
                qtd = Double.parseDouble(String.valueOf(campoQtdProdOs.getText()));


                double a = campoValOs.getRawValue();

                double val = Double.parseDouble(String.valueOf(a/10));

                double mult = qtd * val;


                    campoValQtdMult.setText(String.valueOf(mult));

                }
            }
        });


    }

    private void ListenerDoTextTotal(){

        campoValMaoObra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                        double g = campoValMaoObra.getRawValue();
                        double h = listaFormatted.getRawValue();

                        double gg = Double.parseDouble(String.valueOf(g/10));
                        double hh = Double.parseDouble(String.valueOf(h/10));

                        double soma = gg + hh;

                        txtTotalOs.setText(String.valueOf(soma));


            }
        });




    }


    private void carregarDadosSpinner(){
            String[] tipos = new String[]{
                "1 - Serviço", "2 - Venda", "3 - Serviço e Venda"

            };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                tipoOs.setAdapter(adapter);

    }




    private void pesquisarClientes(String pesquisa){

        clienteUsuarioRef = ConfiguracaoFirebase.getFirebase().child("meus_clientes").child(ConfiguracaoFirebase.getIdUsuario());
        Query query2 = clienteUsuarioRef.orderByChild("nome_cli_filtro").startAt(pesquisa).endAt(pesquisa+ "\uf8ff");
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                clientes.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    clientes.add(ds.getValue(Cliente.class));

                }
                //Collections.reverse(clientes);
                adapterClientes.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void pesquisarProdutos(String pesquisa){

        produtoUsuarioRef = ConfiguracaoFirebase.getFirebase().child("meus_produtos").child(ConfiguracaoFirebase.getIdUsuario());
        Query query = produtoUsuarioRef.orderByChild("nome_filtro").startAt(pesquisa).endAt(pesquisa + "\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                produtos.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    produtos.add( ds.getValue(Produto.class) );

                }

                adapterProdutos.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }




}