package br.com.polenflorestal.qrcodecmpc.activitys;


import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

import br.com.polenflorestal.qrcodecmpc.R;
import br.com.polenflorestal.qrcodecmpc.utils.ToastUtil;

public class Croqui extends AppCompatActivity {
    private int arvorePos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_croqui);

        arvorePos = getIntent().getIntExtra("arvore_pos", 0);

        if(arvorePos < 1 || arvorePos > 88){
            ToastUtil.show(this, "Croqui não encontrado!", Toast.LENGTH_LONG);
            finish();
        }

        String cID = "c"+arvorePos;

        findViewById( getResources().getIdentifier(cID, "id", getPackageName())).setBackgroundColor(Color.parseColor("#ff0000"));

        configuraToolbar();
    }

    private void configuraToolbar(){
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_18_1x);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }
}