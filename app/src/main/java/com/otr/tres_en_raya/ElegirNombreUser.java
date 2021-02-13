package com.otr.tres_en_raya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import javax.net.ssl.SSLSession;

public class ElegirNombreUser extends AppCompatActivity {


    String s1;
    EditText et1;
    SQLiteDatabase db;
    Cursor cursor;
    SQLite helper;
    View view;
    String selectQuery;

    TextView saberJ2;
    TextView saberDificultad;
    TextView saberJ1;
    TextView saberGanador;

    String recuperarDificultad;
    String recuperarJ2;
    String recuperarJ1;
    String recuperarGanador;

    TableRow miFila;
    TableRow misDatos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elegir_nombre_user);

        Button validar = (Button) findViewById(R.id.button2);

        saberJ2 = (TextView) findViewById(R.id.recuperarJ2);
        saberDificultad = (TextView) findViewById(R.id.recuperarDificultad);
        saberJ1 = (TextView) findViewById(R.id.recuperarJ1);
        saberGanador = (TextView) findViewById(R.id.recuperarGanador);
        saberGanador.setText(recuperarGanador);
        saberJ1.setText(recuperarJ1);
        saberJ2.setText(recuperarJ2);
        saberDificultad.setText(recuperarDificultad);

        miFila = (TableRow) findViewById(R.id.tablaUltimaPartida);
        misDatos = (TableRow) findViewById(R.id.tablaDatosUltimaPartida);

        et1 = (EditText) findViewById(R.id.miNombreDeUsuario);
        helper = new SQLite(this);
        db = helper.getWritableDatabase();

        MainActivity guardarDato = new MainActivity();
        s1 = et1.getText().toString();
        guardarDato.miNombreComoJugador = s1;

        // set listeners
        et1.addTextChangedListener(mTextWatcher);


        // run once to disable if empty
        checkFieldsForEmptyValues();
    }


    @Override
    protected void onPause() {
        super.onPause();
        //creamos un objeto sharedPreferences y obtenemos el sharedPreferences
        //por defecto de nuestra app
        SharedPreferences datos =
                PreferenceManager.getDefaultSharedPreferences(this);
        //hacemos editable creando un objeto de tipo Editor
        SharedPreferences.Editor miEditor = datos.edit();
        //establecemos la información a almacenar (clave-valor)
        //  insertarDatosPartidas(miNombreComoJugador, nombrePlayer2, this.nivelDificultad, this.resultadoPartida);

        //BUSCAME EL NIVEL DE DIFICULTAD
        selectQuery = "SELECT DIFICULTAD FROM DATOSPARTIDAS";
        cursor = db.rawQuery(selectQuery, null);
        cursor.moveToLast();
        recuperarDificultad = cursor.getString(cursor.getColumnIndex("dificultad"));

        selectQuery = "SELECT JUGADOR2 FROM DATOSPARTIDAS";
        cursor = db.rawQuery(selectQuery, null);
        cursor.moveToLast();
        recuperarJ2 = cursor.getString(cursor.getColumnIndex("jugador2"));

        selectQuery = "SELECT JUGADOR1 FROM DATOSPARTIDAS";
        cursor = db.rawQuery(selectQuery, null);
        cursor.moveToLast();
        recuperarJ1 = cursor.getString(cursor.getColumnIndex("jugador1"));


        selectQuery = "SELECT RESULTADO FROM DATOSPARTIDAS";
        cursor = db.rawQuery(selectQuery, null);
        cursor.moveToLast();
        recuperarGanador = cursor.getString(cursor.getColumnIndex("resultado"));

     //   if (!recuperarDificultad.isEmpty() || !recuperarJ2.isEmpty() || recuperarJ1.isEmpty() || recuperarGanador.isEmpty()) {

            //GUARDAME LOS VALORES
            miEditor.putString("dificultad", recuperarDificultad);
            miEditor.putString("jugador2", recuperarJ2);
            miEditor.putString("jugador1", recuperarJ1);
            miEditor.putString("resultado", recuperarGanador);

            //transferimos la informacion al objeto sharedpreference
            miEditor.apply();

    //    }

    }


    @Override
    protected void onResume() {
        super.onResume();
        //creamos un objeto sharedPreferences y obtenemos el sharedPreferences
        //por defecto de nuestra app
        SharedPreferences datos =
                PreferenceManager.getDefaultSharedPreferences(this);
        //recuperamos la informacion
        //debemos poner el valor por defecto si no encontrase la clave

        recuperarDificultad = datos.getString("dificultad", "unknown");
        saberDificultad.setText(recuperarDificultad);

        recuperarJ2 = datos.getString("jugador2", "unknown");
        saberJ2.setText(recuperarJ2);

        recuperarJ1 = datos.getString("jugador1", "unknown");
        saberJ1.setText(recuperarJ1);

        recuperarGanador = datos.getString("resultado", "unknown");
        saberGanador.setText(recuperarGanador);


    }


    public void vamosMain(View view) {

        Intent myIntent = new Intent(view.getContext(), MainActivity.class);
        myIntent.putExtra("nombreUser", s1);
        startActivityForResult(myIntent, 0);

    }

    //  create a textWatcher member
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // check Fields For Empty Values
            checkFieldsForEmptyValues();
        }
    };

    void checkFieldsForEmptyValues() {
        Button b = (Button) findViewById(R.id.botonValidar);

        s1 = et1.getText().toString();

        if (s1.equals("")) {
            b.setEnabled(false);
        } else {
            b.setEnabled(true);
        }
    }


    public void mostrarPartida(View view) {

        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);


        }



}