package com.example.a1d;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity5 extends AppCompatActivity {
    Button ButtonA;
    Button ButtonB;
    Button ButtonC;
    Button ButtonD;
    TextView txtView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        txtView = (TextView) findViewById(R.id.name5);
        ButtonA = (Button) findViewById(R.id.addlocation);
        ButtonB = (Button) findViewById(R.id.button3);
        ButtonC = (Button) findViewById(R.id.button4);
        ButtonD = (Button) findViewById(R.id.days);

        ButtonD.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity5.this,MainActivity6.class);
                startActivity(intent);
            }}
        );
        ButtonA.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity5.this,MainActivity4.class);
                startActivity(intent);
            }}
        );
        ButtonB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity5.this,MainActivity.class);
                startActivity(intent);
            }}
        );

        ButtonC.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity5.this,MainActivity2.class);
                startActivity(intent);
            }}
        );
    }
}