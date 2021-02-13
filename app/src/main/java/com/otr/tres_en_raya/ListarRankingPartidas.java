package com.otr.tres_en_raya;

import
        android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class ListarRankingPartidas extends AppCompatActivity implements AdapterView.OnItemClickListener {

    SQLiteDatabase db;
    SQLite helper;
    ListView lv;
    TextView txtTexto;
    EditText editarNombre;
    String nombrePlayer1;
    int _id;
    TextView textTexto1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_ranking_partidas);

        editarNombre = (EditText) findViewById(R.id.editTextModificarNombre);
        textTexto1 = (TextView) findViewById(R.id.textView1);
        txtTexto = findViewById(R.id.miTextViewRanking2);
        txtTexto.setMovementMethod(new ScrollingMovementMethod());

        lv = findViewById(R.id.listaRanking);

        consultaDatosRanking();

        helper = new SQLite(this);
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        //    Cursor cursor = db.query("datosPartidas", null, null, null, null, null, null);
        //     mostrarTable(cursor);

        lv.setOnItemClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_ranking, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.volverMainRanking:

                vueltaPrincipalRanking2();

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void vueltaPrincipalRanking2 () {

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);

    }

    private void mostrarTable(Cursor cursor) {

        txtTexto.append("\n Tabla Datos \n-------------------");
        cursor.moveToFirst();

        int nfilas = cursor.getCount();
        int ncolumnas = cursor.getColumnCount();
        String fila = "\n";

        for (int i = 0; i < nfilas; i++) {

            fila = "\n";

            for (int j = 0; j < ncolumnas; j++) {

                fila = fila + cursor.getString(j) + " ";

            }

            txtTexto.append(fila);
            cursor.moveToNext();
        }
    }


    private void consultaDatosRanking() {

        helper = new SQLite(this);

        db = helper.getReadableDatabase();

        /*
          public static final String TABLE_NAME_DATOS_JUGADORES = "datosJugadores";
        public static final String COLUMN_NAME_USUARIO = "usuario";
        public static final String COLUMN_NAME_N_PARTIDAS= "partidasJugadas";
        public static final String COLUMN_PUNTOS = "puntos";
         */

        Cursor cursor =
                db.query(Estructura.EstructuraDatosJugadores.TABLE_NAME_DATOS_JUGADORES, null, null, null,
                        null, null, null);

        //adaptamos el cursor a nuestro ListView
        String[] from = {Estructura.EstructuraDatosJugadores.COLUMN_NAME_USUARIO,
                Estructura.EstructuraDatosJugadores.COLUMN_NAME_N_PARTIDAS,
                Estructura.EstructuraDatosJugadores.COLUMN_PUNTOS};

        int[] to = {R.id.textViewUser, R.id.textViewPartidas, R.id.textViewPuntos}; // SON DEL LAYOUT LINEAR_LAYOUT

        SimpleCursorAdapter adaptador = new SimpleCursorAdapter(this,
                R.layout.linear_layout2, cursor, from, to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        lv.setAdapter(adaptador);

        db.close();
    }

/*
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
*/
    public void vueltaPrincipalRanking(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Cursor cursor = (Cursor) lv.getItemAtPosition(position);
        _id = cursor.getInt(0);
        nombrePlayer1 = cursor.getString(1) ;
        //mostramos los datos en los cuadros de texto de la parte superior del
        //layout
        editarNombre.setText(nombrePlayer1);
        textTexto1.setText(String.valueOf(_id));

    }

    public void actualizarNombre(View view) {

        String nuevoNombre = editarNombre.getText().toString();
        ContentValues con = new ContentValues();
        con.put("usuario",  nuevoNombre);
        db.update(Estructura.EstructuraDatosJugadores.TABLE_NAME_DATOS_JUGADORES, con, Estructura.EstructuraDatosJugadores._ID + " = " + _id, null);
        Toast.makeText(this, nombrePlayer1 + " " + String.valueOf(_id), Toast.LENGTH_LONG).show();

        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);

    }
}