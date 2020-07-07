package br.com.polenflorestal.qrcodecmpc.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import br.com.polenflorestal.qrcodecmpc.R;
import br.com.polenflorestal.qrcodecmpc.utils.DataBaseOnlineUtil;
import br.com.polenflorestal.qrcodecmpc.utils.DataBaseUtil;
import br.com.polenflorestal.qrcodecmpc.utils.ToastUtil;

import static br.com.polenflorestal.qrcodecmpc.utils.Constants.EMPRESA_NOME;

public class ExibeArvore extends AppCompatActivity {
    private String codigo;
    private int arvorePos = 0;
    private ListenerRegistration listenerComentarios;
    String addComentarioTxt = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        codigo = getIntent().getStringExtra("qr_code");
        DataBaseUtil.abrir(this);
        Cursor cursor = DataBaseUtil.buscar("Arvore", new String[]{}, "codigo = '"+codigo+"'", "");

        if (cursor.getCount() <= 0) {
            ToastUtil.show(this, "Código não encontrado no Banco de Dados!", Toast.LENGTH_LONG);
            finish();
        }

        while(cursor.moveToNext()){
            int tipo = cursor.getInt(cursor.getColumnIndex("tipo"));
            String local = cursor.getString(cursor.getColumnIndex("local"));
            int parcela = cursor.getInt(cursor.getColumnIndex("parcela"));
            int linha = cursor.getInt(cursor.getColumnIndex("linha"));
            int bloco = cursor.getInt(cursor.getColumnIndex("bloco"));
            arvorePos = cursor.getInt(cursor.getColumnIndex("arvore_pos"));
            String codigo_geno = cursor.getString(cursor.getColumnIndex("codigo_geno"));
            String genitor_fem = cursor.getString(cursor.getColumnIndex("genitor_fem"));
            String genitor_mas = cursor.getString(cursor.getColumnIndex("genitor_mas"));
            String data_plantio = cursor.getString(cursor.getColumnIndex("data_plantio"));
            String ult_medicao = cursor.getString(cursor.getColumnIndex("ult_medicao"));
            String dap = cursor.getString(cursor.getColumnIndex("dap"));
            String altura = cursor.getString(cursor.getColumnIndex("altura"));
            String vol = cursor.getString(cursor.getColumnIndex("vol"));
            String procedencia = cursor.getString(cursor.getColumnIndex("procedencia"));
            String historico = cursor.getString(cursor.getColumnIndex("historico"));
            String especie_comp = cursor.getString(cursor.getColumnIndex("especie_comp"));

            if (tipo == 0) {
                setContentView(R.layout.activity_exibe_arvore);

                ((TextView)findViewById(R.id.local)).setText(local);

                try {
                    ((TextView)findViewById(R.id.data_plantio)).setText(
                            new SimpleDateFormat("dd/MM/yyyy").format(
                                    new SimpleDateFormat("MM/dd/yyyy").parse(data_plantio)
                            ));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                ((TextView)findViewById(R.id.talhao)).setText(""+bloco);
                ((TextView)findViewById(R.id.individuo)).setText(""+arvorePos);
                ((TextView)findViewById(R.id.especie)).setText(codigo_geno);
                ((TextView)findViewById(R.id.genitores)).setText(genitor_fem);

                try {
                    ((TextView)findViewById(R.id.ult_medicao)).setText(
                            new SimpleDateFormat("dd/MM/yyyy").format(
                                    new SimpleDateFormat("MM/dd/yyyy").parse(ult_medicao)
                            ));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                ((TextView)findViewById(R.id.dap)).setText(dap+" cm");
                ((TextView)findViewById(R.id.altura)).setText(altura+" m");
                ((TextView)findViewById(R.id.vol)).setText(vol+" m³");

                listenerComentarios = DataBaseOnlineUtil.getInstance().getCollectionReferenceWhereEqualTo(
                        "Empresa/"+ EMPRESA_NOME+"/Comentario",
                        "arvoreCodigo",
                        codigo,
                        "criacaoTimestamp",
                        Query.Direction.DESCENDING
                )
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                if (e != null) {
                                    Log.w("MY_FIREBASE", "Listen failed.", e);
                                    return;
                                }
                                if (queryDocumentSnapshots != null) {
                                    limpaComentariosView();
                                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                        exibeComentario(doc.getString("dataCriacao"), doc.getString("texto"));
                                    }
                                }
                                Log.i("MY_FIREBASE_EXIBEARVORE", "listener recebido!");
                            }
                        });

            } else {
                setContentView(R.layout.activity_exibe_arvore1);

                ((ImageView)findViewById(R.id.empresa_logo)).setImageDrawable(getDrawable(R.drawable.empresa_icon2));

                String uri = "@drawable/" + codigo.toLowerCase() + "_1";
                int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                ((ImageView)findViewById(R.id.arvore_img_1)).setImageResource(imageResource);

                ((TextView)findViewById(R.id.local)).setText(local);
                try {
                    ((TextView)findViewById(R.id.data_plantio)).setText(
                            new SimpleDateFormat("dd/MM/yyyy").format(
                                    new SimpleDateFormat("MM/dd/yyyy").parse(data_plantio)
                            ));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                ((TextView)findViewById(R.id.individuo)).setText(""+arvorePos);
                ((TextView)findViewById(R.id.especie)).setText(codigo_geno);
                ((TextView)findViewById(R.id.especie_comp)).setText(especie_comp);
                ((TextView)findViewById(R.id.procedencia)).setText(procedencia);
                ((TextView)findViewById(R.id.historico)).setText(historico);
            }
            break;
        }

        configuraToolbar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(listenerComentarios != null)
            listenerComentarios.remove();
        Log.i("MY_FIREBASE_EXIBEARVORE", "listener removido");
    }

    private void configuraToolbar(){
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void abrirCroqui(View view){
        Intent intent = new Intent(this, Croqui.class);
        intent.putExtra("arvore_pos", arvorePos);
        startActivity(intent);
    }

    private void exibeComentario(String data, String com){
        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        params.topMargin = 5;

        TextView com1 = new TextView(this);
        String temp = "- "+data+": "+com+"";
        com1.setText(temp);
        com1.setTextColor(Color.parseColor("#1433DC"));
        com1.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        com1.setLayoutParams(params);
        ((LinearLayout)findViewById(R.id.linear_comentarios)).addView(com1);
    }

    private void limpaComentariosView(){
        ((LinearLayout)findViewById(R.id.linear_comentarios)).removeAllViews();
    }

    public void adicionarComentario(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Adicionar Comentário");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                addComentarioTxt = input.getText().toString();

                if(addComentarioTxt.equals("") || addComentarioTxt.length() <= 0){
                    ToastUtil.show(ExibeArvore.this, "Erro ao inserir comentário: comentário vazio!", Toast.LENGTH_LONG);
                }
                else {
                    Map<String, Object> comentarioData = new HashMap<>();
                    comentarioData.put("texto", addComentarioTxt);
                    comentarioData.put("arvoreCodigo", codigo);
                    comentarioData.put("criacaoTimestamp", System.currentTimeMillis());
                    comentarioData.put("dataCriacao", new SimpleDateFormat("dd/MM/yyyy").format(new Timestamp(System.currentTimeMillis())));

                    DataBaseOnlineUtil.getInstance().insertDocument("Empresa/"+EMPRESA_NOME+"/Comentario", comentarioData);
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }
}
