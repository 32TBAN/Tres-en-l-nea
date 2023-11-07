package uta.fisei.ejercicio05;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView textViewGanador;
    Integer[] buttons;
    int[] table = new int[]{
            0,0,0,
            0,0,0,
            0,0,0,
    };
    int status = 0;
    int marckput;
    int turno = 1;
    boolean isTwo;
    private long startTime;
    private long endTime;

    int [] posWin = new int[]{-1,-1,-1};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewGanador = findViewById(R.id.textViewGanador);
        textViewGanador.setVisibility(View.INVISIBLE);

        buttons = new Integer[]{R.id.button1,R.id.button2,R.id.button3,R.id.button4,R.id.button5,
                R.id.button6,R.id.button7,R.id.button8,R.id.button9};


        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        isTwo = sharedPreferences.getBoolean("isTwo", false);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isTwo", isTwo);
        super.onSaveInstanceState(outState);
    }
    public void twoPerson(View view){
        finish();
        startActivity(new Intent(MainActivity.this, MainActivity.class));
        isTwo = true;

        // Guardar el valor de isTwo en las preferencias compartidas
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isTwo", isTwo);
        editor.apply();
    }
    public void onePerson(View view){
        finish();
        startActivity(new Intent(MainActivity.this, MainActivity.class));
        isTwo = false;

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isTwo", isTwo);
        editor.apply();
    }
    public void mark(View view){
        int numButton = Arrays.asList(buttons).indexOf(view.getId());
        if (status == 0){
            turno = 1;

            if (startTime == 0) {
                startTime = System.currentTimeMillis();
            }

            if (table[numButton] == 0) {
                view.setBackgroundResource(R.drawable.x);
                table[numButton] = 1;
                marckput += 1;
                status = comprobate();
                finishGame();
                if (status == 0 && !isTwo) {
                    turno = -1;
                    computer();
                    marckput += 1;
                    status = comprobate();
                    finishGame();
                }else{
                    status = 3;
                }
            }
        }else if (status == 3){
            turno = 3;

            if (table[numButton] == 0) {
                view.setBackgroundResource(R.drawable.circulo);
                table[numButton] = -1;
                marckput += 1;
                status = comprobate();
                finishGame();
            }
        }

    }

    public void computer(){
        Random random = new Random();
        int pos = random.nextInt(table.length);
        while (table[pos] != 0)
            pos = random.nextInt(table.length);
        Button b = findViewById(buttons[pos]);
        b.setBackgroundResource(R.drawable.circulo);
        table[pos] = -1;

    }

    public int comprobate(){
        for (int i =0; i <= 6; i+=3){
            if (Math.abs(table[i]+table[i+1]+table[i+2]) == 3) {
                posWin = new int[]{i, i + 1, i + 2};
                return  turno;
            }
        }
        for (int i =0; i < 3; i++){
            if (Math.abs(table[i]+table[i+3]+table[i+6]) == 3) {
                posWin = new int[]{i, i + 3, i + 6};
                return turno;
            }
        }
        if (Math.abs(table[0]+table[4]+table[8]) == 3) {
            posWin = new int[]{0,4,8};
            return  turno;
        }else if (Math.abs(table[2]+table[4]+table[6]) == 3){
            posWin = new int[]{2,4,6};
            return turno;
        }else if(marckput ==9){
            return 2;
        }

        return 0;
    }

    public void finishGame(){
        int colotWin = R.drawable.x_ganadora;
        // Calcula la duración de la partida

        if (status != 0) {
            endTime = System.currentTimeMillis();
        }

        long duration = Math.abs((endTime - startTime)/1000);
        String textDuration = "\nDuración de la partida: " + (duration) + " SEG";

        if (status == 1 ){
            textViewGanador.setVisibility(View.VISIBLE);
            textViewGanador.setTextColor(Color.GREEN);
            textViewGanador.setText("Ha ganado X"+textDuration);
        }else if(status == -1){
            textViewGanador.setVisibility(View.VISIBLE);
            textViewGanador.setText("Has perdido"+textDuration);
            textViewGanador.setTextColor(Color.RED);
            colotWin = R.drawable.circulo_ganador;
        }else if(status == 2){
            textViewGanador.setVisibility(View.VISIBLE);
            textViewGanador.setText("Empate"+textDuration);
        }else if(status == 3){
            textViewGanador.setVisibility(View.VISIBLE);
            textViewGanador.setText("Ha ganado circulo"+textDuration);
            textViewGanador.setTextColor(Color.GREEN);
            colotWin = R.drawable.circulo_ganador;
        }

        if(status != 2 && status != 0){
            for (int i=0;i<posWin.length; i++){
                Button b = findViewById(buttons[posWin[i]]);
                b.setBackgroundColor(Color.GREEN);
                b.setBackgroundResource(colotWin);
            }
        }


    }
}