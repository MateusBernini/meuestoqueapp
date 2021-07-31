package com.example.estoqueapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.estoqueapp.R;
import com.example.estoqueapp.helper.ConfiguracaoFirebase;
import com.example.estoqueapp.model.OrdemServico;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DetalhesOsActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("empresa").child(ConfiguracaoFirebase.getIdUsuario());
    private OrdemServico ordemSelecionada;
    private TextView tipoOs;
    private TextView clienteOs;
    private TextView celularOs;
    private TextView dataOs;
    private TextView emailOs;
    private TextView enderecoOs;
    private TextView maoObraOs;
    private TextView totalOs;
    private TextView descricaoOs;
    private String cliente, dataEmissao, totalOsString, celular, tipoOsString, endereco, descricao, valMaoObra, emailCliente;
    private ListView listViewDetailOs;
    EditText editNomePdf;
    SimpleDateFormat dataPatternFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
    private ArrayList<String> listaItensArray = new ArrayList<>();
    ArrayList<String> listaSoDeProdutos = new ArrayList<>();
    ArrayList<String> listaSoDeQtd = new ArrayList<>();
    ArrayList<String> listaSoDeValUnit = new ArrayList<>();
    ArrayList<String> listaSoDeValTotemString = new ArrayList<>();
    String oi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_os);

        inicializarComponentes();
        ordemSelecionada = (OrdemServico) getIntent().getSerializableExtra("ordemSelecionada");

        tipoOs.setText(ordemSelecionada.getTipoOs());
        clienteOs.setText(ordemSelecionada.getCliente());
        dataOs.setText(ordemSelecionada.getDataEmissao());
        celularOs.setText(ordemSelecionada.getCelular());
        emailOs.setText(ordemSelecionada.getEmail());
        enderecoOs.setText(ordemSelecionada.getEndereco());
        maoObraOs.setText(ordemSelecionada.getMaoObra());
        totalOs.setText(ordemSelecionada.getTotalOs());
        descricaoOs.setText(ordemSelecionada.getDescricao());
        //variaveis propdf
        cliente = ordemSelecionada.getCliente();
        dataEmissao = ordemSelecionada.getDataEmissao();
        totalOsString = ordemSelecionada.getTotalOs();
        celular = ordemSelecionada.getCelular();
        tipoOsString = ordemSelecionada.getTipoOs();
        endereco = ordemSelecionada.getEndereco();
        descricao = ordemSelecionada.getDescricao();
        valMaoObra = ordemSelecionada.getMaoObra();
        oi = ordemSelecionada.getTotalItens();
        emailCliente = ordemSelecionada.getEmail();

        //adicionar no pdf
        //listaItens = ordemSelecionada.getListaItens();

         listaSoDeProdutos = ordemSelecionada.getListaSodeProdutos();
         listaSoDeQtd = ordemSelecionada.getListaSoDeQtd();
         listaSoDeValUnit = ordemSelecionada.getListaSoDeValUnitario();
         listaSoDeValTotemString = ordemSelecionada.getListaSoDeValTotemString();

         listaItensArray = ordemSelecionada.getListaItensArray();


        final ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_2, android.R.id.text1, listaItensArray);

        listViewDetailOs.setAdapter(adaptador);






    }

    private void inicializarComponentes() {
            tipoOs = findViewById(R.id.tipoOsDetail);
            clienteOs = findViewById(R.id.clienteOsDetail);
            dataOs = findViewById(R.id.dataOsDetail);
            celularOs = findViewById(R.id.celularOsDetail);
            emailOs  =findViewById(R.id.emailOsdetail);
            enderecoOs = findViewById(R.id.enderecoOsDetail);
            maoObraOs = findViewById(R.id.maodeObraDetail);
            totalOs = findViewById(R.id.totalOsDetail);
            descricaoOs = findViewById(R.id.descricaoOsdetail);
            editNomePdf = findViewById(R.id.editNomePdf);
            listViewDetailOs = findViewById(R.id.lidtViewDetaiolOs);


    }



    public void btfff(View view){

        try {
            ffff();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }




    private void ffff()throws FileNotFoundException{

        myRef = ConfiguracaoFirebase.getFirebase().child("empresa").child(ConfiguracaoFirebase.getIdUsuario()).child("1");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nome= String.valueOf(snapshot.child("nomeempresa").getValue());
                String cel = String.valueOf(snapshot.child("celular").getValue());
                String tel = String.valueOf(snapshot.child("telefone").getValue());
                String email = String.valueOf(snapshot.child("email").getValue());
                String cep = String.valueOf(snapshot.child("cep").getValue());



                File file = new File(DetalhesOsActivity.this.getExternalFilesDir("/"),(editNomePdf.getText().toString() + "OS.pdf"));
                try {
                    OutputStream outputStream = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                PdfWriter writer = null;
                try {
                    writer = new PdfWriter(String.valueOf(file));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                PdfDocument pdfDocument = new PdfDocument(writer);
                Document document = new Document(pdfDocument);


                DeviceRgb verde = new DeviceRgb(51, 204, 51);
                DeviceRgb cinza = new DeviceRgb(220, 220, 220);


                float comunWidth1[] = {140,140, 140, 140};
                Table table1 = new Table(comunWidth1);

                //Insere a Logo:
                Drawable d1 = getDrawable(R.drawable.logo);
                Bitmap bitmap1 = ((BitmapDrawable)d1).getBitmap();
                ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                bitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream1);
                byte[] bitmapData1 = stream1.toByteArray();

                ImageData imageData1 = ImageDataFactory.create(bitmapData1);
                Image image1 = new Image(imageData1);
                image1.setHeight(120);

                //tabela 1-----01
                table1.addCell(new Cell(4, 1).add(image1).setBorder(Border.NO_BORDER));
                table1.addCell(new Cell(1, 2).add(new Paragraph("Ordem de Serviço").setFontSize(26f).setBold().setFontColor(Color.BLUE)).setBorder(Border.NO_BORDER));
                table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                //table1.addCell(new Cell(1, 2).add(new Paragraph("Ordem de Serviço").setFontSize(26f).setBold().setFontColor(Color.BLUE)).setBorder(Border.NO_BORDER));
                //table1.addCell(new Cell().add(new Paragraph("")));

                //tabela 1-----02
                //table1.addCell(new Cell().add(new Paragraph("")));
                table1.addCell(new Cell().add(new Paragraph("Data de Emissão")).setBorder(Border.NO_BORDER));
                table1.addCell(new Cell().add(new Paragraph(dataEmissao)).setBorder(Border.NO_BORDER));
                table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));

                //tabela 1-----03
                //table1.addCell(new Cell().add(new Paragraph("")));
                table1.addCell(new Cell().add(new Paragraph("Tipo da OS")).setBorder(Border.NO_BORDER));
                table1.addCell(new Cell().add(new Paragraph(tipoOsString)).setBorder(Border.NO_BORDER));
                table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));

                //tabela 1-----04
                table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                table1.addCell(new Cell().add(new Paragraph("Cliente").setBold()).setBorder(Border.NO_BORDER));

                //tabela 1-----05
                table1.addCell(new Cell().add(new Paragraph(cliente)).setBorder(Border.NO_BORDER));
                table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                table1.addCell(new Cell().add(new Paragraph("Celular").setBold()).setBorder(Border.NO_BORDER));

                //tabela 1-----06
                table1.addCell(new Cell().add(new Paragraph(celular)).setBorder(Border.NO_BORDER));
                table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                table1.addCell(new Cell().add(new Paragraph("Email").setBold()).setBorder(Border.NO_BORDER));

                //tabela 1-----07
                table1.addCell(new Cell().add(new Paragraph(emailCliente)).setBorder(Border.NO_BORDER));
                table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                table1.addCell(new Cell().add(new Paragraph("Endereço").setBold()).setBorder(Border.NO_BORDER));

                //tabela 1-----08
                table1.addCell(new Cell().add(new Paragraph(endereco)).setBorder(Border.NO_BORDER));
                table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                table1.addCell(new Cell(1,2).add(new Paragraph("")).setBorder(Border.NO_BORDER));
                //table1.addCell(new Cell().add(new Paragraph("")));

                //tabela 1-----09
                table1.addCell(new Cell().add(new Paragraph("Descrição do Serviço").setBold()).setBorder(Border.NO_BORDER));
                table1.addCell(new Cell(1,3).add(new Paragraph(descricao + "\n Valor da Mão de Obra:"+valMaoObra)).setBorder(Border.NO_BORDER));
                table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));

//ESTÁ PRONTO!! --->

                float columnWidth2[] = {100f, 100f, 100f, 100f};
//Table 1 left
                Table table = new Table(columnWidth2);
                table.setHorizontalAlignment(HorizontalAlignment.CENTER);

                table.addCell(new Cell().add(new Paragraph("Itens Utilizados").setFontSize(18f).setFontColor(Color.BLACK)).setBackgroundColor(Color.WHITE).setBorder(Border.NO_BORDER));
                table.addCell(new Cell().add(new Paragraph("")).setBackgroundColor(Color.WHITE).setBorder(Border.NO_BORDER));
                table.addCell(new Cell().add(new Paragraph("")).setBackgroundColor(Color.WHITE).setBorder(Border.NO_BORDER));
                table.addCell(new Cell().add(new Paragraph("")).setBackgroundColor(Color.WHITE).setBorder(Border.NO_BORDER));

                table.addCell(new Cell().add(new Paragraph("Produto/ Peça").setFontColor(Color.WHITE)).setBold().setBackgroundColor(verde));
                table.addCell(new Cell().add(new Paragraph("Quantidade")).setFontColor(Color.WHITE)).setBold().setBackgroundColor(verde);
                table.addCell(new Cell().add(new Paragraph("Valor de Venda Unitário")).setFontColor(Color.WHITE)).setBold().setBackgroundColor(verde);
                table.addCell(new Cell().add(new Paragraph("Total")).setFontColor(Color.WHITE)).setBold().setBackgroundColor(verde);

                for(int i = 0; i<listaSoDeProdutos.size(); i++){

                    //table.addCell(listaSoDeProdutos.get(i));
                    table.addCell(new Cell().add(new Paragraph(listaSoDeProdutos.get(i))).setBackgroundColor(Color.WHITE));

                }

                table.addCell(new Cell().add(new Paragraph("")).setBackgroundColor(Color.WHITE).setBorder(Border.NO_BORDER));
                // table.addCell(new Cell(1,2).add(new Paragraph("Terms & Conditions")));
                table.addCell(new Cell().add(new Paragraph("")).setBackgroundColor(Color.WHITE).setBorder(Border.NO_BORDER));
                table.addCell(new Cell().add(new Paragraph("Total Itens:")).setFontColor(Color.WHITE).setBackgroundColor(verde));
                table.addCell(new Cell().add(new Paragraph(oi)).setFontColor(Color.BLACK).setBackgroundColor(cinza));

                table.addCell(new Cell(1,2).add(new Paragraph("")).setBackgroundColor(Color.WHITE).setBorder(Border.NO_BORDER));
                table.addCell(new Cell().add(new Paragraph("Mão de Obra")).setFontColor(Color.WHITE));
                table.addCell(new Cell().add(new Paragraph(valMaoObra)).setFontColor(Color.BLACK).setBackgroundColor(cinza));
                //table.addCell(new Cell().add(new Paragraph("")));
                // table.addCell(new Cell().add(new Paragraph("")));

                table.addCell(new Cell(1,2).add(new Paragraph("")).setBackgroundColor(Color.WHITE).setBorder(Border.NO_BORDER));
                // table.addCell(new Cell(1,2).add(new Paragraph("Terms & Conditions")));
                table.addCell(new Cell().add(new Paragraph("TOTAL")).setFontColor(Color.WHITE).setBackgroundColor(Color.BLUE));
                table.addCell(new Cell().add(new Paragraph(totalOsString)).setFontColor(Color.WHITE).setBackgroundColor(Color.BLUE));
                //table.addCell(new Cell().add(new Paragraph("")));


                //DADOS DA EMPRESA!!ABAIXO

                float columnWidth5[] = {50, 250};
                Table table3 = new Table(columnWidth5);
/*
        Drawable d2 = getDrawable(R.drawable.logo);
        Bitmap bitmap2 = ((BitmapDrawable)d2).getBitmap();
        ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
        bitmap2.compress(Bitmap.CompressFormat.PNG, 100, stream2);
        byte[] bitmapData2 = stream2.toByteArray();

        ImageData imageData2 = ImageDataFactory.create(bitmapData2);
        Image image2 = new Image(imageData2);
        image2.setHeight(120);
*/
                //table3.addCell(new Cell().add(new Paragraph("Empresa")));
                //table3.addCell(new Cell().add(new Paragraph(nome)));
                table3.addCell(new Cell().add(new Paragraph("Contato & Fone")));
                table3.addCell(new Cell().add(new Paragraph(cel +"    /    "+tel)));
                table3.addCell(new Cell().add(new Paragraph("Email")));
                table3.addCell(new Cell().add(new Paragraph(email)));
                table3.addCell(new Cell().add(new Paragraph("Cep/ Edereço")));
                table3.addCell(new Cell().add(new Paragraph(cep)));

                float columnWidth7[] = {300};
                Table table4 = new Table(columnWidth7);
                table4.addCell(new Cell().add(new Paragraph(nome)).setFontSize(18f).setFontColor(Color.BLACK).setBold().setBorder(Border.NO_BORDER));



                document.add(table1);
                document.add(new Paragraph("\n"));
                document.add(table);
                document.add(new Paragraph("\n").setTextAlignment(TextAlignment.RIGHT));
                document.add(table4);
                document.add(table3);
                document.add(new Paragraph("\nGerado em:" + dataPatternFormat.format(new Date().getTime())+"\n").setTextAlignment(TextAlignment.RIGHT));

                document.close();
                Toast.makeText(DetalhesOsActivity.this, editNomePdf.getText().toString()+ "OS.pdf Salvo com Sucesso!", Toast.LENGTH_LONG).show();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


}