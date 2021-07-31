package com.example.estoqueapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.estoqueapp.R;
import com.example.estoqueapp.helper.ConfiguracaoFirebase;
import com.example.estoqueapp.model.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ir.androidexception.datatable.DataTable;
import ir.androidexception.datatable.model.DataTableHeader;
import ir.androidexception.datatable.model.DataTableRow;

import static com.itextpdf.kernel.xmp.PdfConst.Date;


public class OldPrintActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("meus_produtos").child(ConfiguracaoFirebase.getIdUsuario());
    DatabaseReference retrieveRef = database.getReference("meus_produtos").child(ConfiguracaoFirebase.getIdUsuario());
    Button oldPrintButton;
    EditText oldPrintEditText;
    DataTable dataTable;
    DataTableHeader header;
    TextView nomeEmpresa, txtCelularEmpresa, txtTelefoneEmpresa, txtEmailEmpresa, txtCepEmpresa;
    SimpleDateFormat dataPatternFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");



    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    ArrayList<DataTableRow> rows = new ArrayList<>();
    private List<Produto> produtos = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_print);

        oldPrintButton = findViewById(R.id.oldPrintBtn);
        oldPrintEditText = findViewById(R.id.oldPrintEditText);
        dataTable = findViewById(R.id.data_table);


        header = new DataTableHeader.Builder().item("produto", 5)
                .item("cód", 5)
                .item("qtd", 5)
                .item("valUnit", 5)
                .item("valTotal", 5)
                .item("valVenda", 5).build();

        loadTable();



        myRef = ConfiguracaoFirebase.getFirebase().child("empresa").child(ConfiguracaoFirebase.getIdUsuario()).child("1");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nome= String.valueOf(snapshot.child("nomeempresa").getValue());
                String cel = String.valueOf(snapshot.child("celular").getValue());
                String tel = String.valueOf(snapshot.child("telefone").getValue());
                String email = String.valueOf(snapshot.child("email").getValue());
                String cep = String.valueOf(snapshot.child("cep").getValue());

                nomeEmpresa = findViewById(R.id.txtnomeEmpresaOld);
                nomeEmpresa.setText(nome);
                txtCelularEmpresa = findViewById(R.id.txtcelularEmpresaOld);
                txtCelularEmpresa.setText(cel);
                txtTelefoneEmpresa = findViewById(R.id.txtTelefoneEmpresaOld);
                txtTelefoneEmpresa.setText(tel);
                txtEmailEmpresa = findViewById(R.id.txtEmailEmpresaOld);
                txtEmailEmpresa.setText(email);
                txtCepEmpresa = findViewById(R.id.txtCepEmpresaOld);
                txtCepEmpresa.setText(cep);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

/*
    try {
            createPdf();
            Toast.makeText(this, "PDF Salvo com sucesso!", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
*/
            oldPrintButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        createPdf();
                        Toast.makeText(OldPrintActivity.this, oldPrintEditText.getText().toString() + "Relatório.pdf Salvo com sucesso!", Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });

    }



    private void loadTable() {
        myRef = ConfiguracaoFirebase.getFirebase().child("meus_produtos").child(ConfiguracaoFirebase.getIdUsuario());
        Query query = myRef.orderByChild("nome_filtro").startAt("a").endAt("z"+ "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot myDataSnapshot : snapshot.getChildren()){
                    DataTableRow row = new DataTableRow.Builder()
                            .value(String.valueOf(myDataSnapshot.child("produto").getValue()))
                            .value(String.valueOf(myDataSnapshot.child("codigo").getValue()))
                            .value(String.valueOf(myDataSnapshot.child("quantidade").getValue()))
                            .value(String.valueOf(myDataSnapshot.child("valor").getValue()))
                            .value(String.valueOf(myDataSnapshot.child("mult").getValue()))
                            .value(String.valueOf(myDataSnapshot.child("valVenda").getValue())).build();
                    rows.add(row);

                }

                dataTable.setHeader(header);
                dataTable.setRows(rows);
                dataTable.inflate(OldPrintActivity.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




    private void createPdf() throws FileNotFoundException{


        myRef = ConfiguracaoFirebase.getFirebase().child("meus_produtos").child(ConfiguracaoFirebase.getIdUsuario());
        Query query = myRef.orderByChild("nome_filtro").startAt("a").endAt("z"+ "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String produto, codigo = "", qtd = "", valUnit ="", valMult = "", valVenda = "";

                produto = String.valueOf(snapshot.child("nome").getValue());



                    File file = new File(OldPrintActivity.this.getExternalFilesDir("/"),(oldPrintEditText.getText().toString()) + "Relatório.pdf");
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



                    Paragraph paragraph = new Paragraph("Relatório Meus Produtos/ Peças");
                    paragraph.setFontSize(30f);
                    paragraph.setTextAlignment(TextAlignment.CENTER);

                    Paragraph p1 = new Paragraph("Gerado em: "+ dataPatternFormat.format(new Date().getTime()));
                    document.add(p1);

                    
                    float columnWidthtb1[] = {600f};
                    Table tb1 = new Table(columnWidthtb1);

                    tb1.addCell(new Cell().add(new Paragraph(nomeEmpresa.getText().toString())).setBorder(Border.NO_BORDER).setFontSize(26f).setFontColor(Color.BLUE));
                    tb1.addCell(new Cell().add(new Paragraph(txtEmailEmpresa.getText().toString())).setBorder(Border.NO_BORDER).setFontColor(Color.BLACK));
                    tb1.addCell(new Cell().add(new Paragraph(txtCelularEmpresa.getText().toString())).setBorder(Border.NO_BORDER).setFontColor(Color.BLACK));
                    tb1.addCell(new Cell().add(new Paragraph(txtTelefoneEmpresa.getText().toString())).setBorder(Border.NO_BORDER).setFontColor(Color.BLACK));
                    tb1.addCell(new Cell().add(new Paragraph(txtCepEmpresa.getText().toString())).setBorder(Border.NO_BORDER).setFontColor(Color.BLACK));

                    float columnWidth[] = {200f, 200f, 100f, 150f, 150f, 150f};
                    Table table = new Table(columnWidth);
                    table.addCell(new Cell().add(new Paragraph("Código")).setFontColor(Color.WHITE).setBackgroundColor(Color.BLUE).setBold());
                    table.addCell(new Cell().add(new Paragraph("Produto")).setFontColor(Color.WHITE).setBackgroundColor(Color.BLUE).setBold());
                    table.addCell(new Cell().add(new Paragraph("Quantidade")).setFontColor(Color.WHITE).setBackgroundColor(Color.BLUE).setBold());
                    table.addCell(new Cell().add(new Paragraph("Valor Unitário")).setFontColor(Color.WHITE).setBackgroundColor(Color.BLUE).setBold());
                    table.addCell(new Cell().add(new Paragraph("Valor Total")).setFontColor(Color.WHITE).setBackgroundColor(Color.BLUE).setBold());
                    table.addCell(new Cell().add(new Paragraph("Valor de Venda")).setFontColor(Color.WHITE).setBackgroundColor(Color.BLUE).setBold());

                for(DataSnapshot myDataSnapshot : snapshot.getChildren()){
                    codigo = (String.valueOf(myDataSnapshot.child("codigo").getValue()));
                    produto = (String.valueOf(myDataSnapshot.child("produto").getValue()));
                    qtd = (String.valueOf(myDataSnapshot.child("quantidade").getValue()));
                    valUnit = (String.valueOf(myDataSnapshot.child("valor").getValue()));
                    valMult = (String.valueOf(myDataSnapshot.child("mult").getValue()));
                    valVenda = (String.valueOf(myDataSnapshot.child("valVenda").getValue()));

                    table.addCell(produto);
                    table.addCell(codigo);
                    table.addCell(qtd);
                    table.addCell(valUnit);
                    table.addCell(valMult);
                    table.addCell(valVenda);


                }

                    document.add(paragraph);
                    document.add(tb1);
                    document.add(table);
                    document.close();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*
        Código base da criação de pdf com tabela no itext7
        File file = new File(OldPrintActivity.this.getExternalFilesDir("/"),("aa") + "OS.pdf");
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(String.valueOf(file));
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);
        Paragraph paragraph = new Paragraph("Hello");

        document.add(paragraph);
        float columnWidth[] = {200f, 200f, 100f, 100f, 100f, 100f};
        Table table = new Table(columnWidth);
        table.addCell("Name ");
        table.addCell("Age");

        table.addCell("RajaRum");
        table.addCell("32");

        document.add(table);


        document.close();

        Toast.makeText(this, "Pdf Foi Criado", Toast.LENGTH_SHORT).show();
*/

    }







}