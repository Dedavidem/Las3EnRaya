package com.otr.tres_en_raya;

import android.content.ContentValues;
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
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toolbar;

public class ListarHistorialPartidas extends AppCompatActivity implements AdapterView.OnItemClickListener {

    SQLiteDatabase db;
    SQLite helper;
    ListView lv;
    TextView txtTexto;
    View view;
    String miNombreComoJugador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_historial_partidas);

        txtTexto = findViewById(R.id.miTextView);
        txtTexto.setMovementMethod(new ScrollingMovementMethod());

        lv = findViewById(R.id.lista);

        consultaDatosPartidas();

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
        inflater.inflate(R.menu.menu_historial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.volverMainHistorial:

                vueltaPrincipalHistorial2();

            default:
                return super.onOptionsItemSelected(item);
        }
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



    private void consultaDatosPartidas() {

        helper = new SQLite(this);

        db = helper.getReadableDatabase();

        Cursor cursor =
                db.query(Estructura.EstructuraDatosPartidas.TABLE_NAME_DATOS_PARTIDAS, null, null, null,
                        null, null, null);

        //adaptamos el cursor a nuestro ListView
        String[] from = {Estructura.EstructuraDatosPartidas.COLUMN_NAME_JUGADOR1,
                Estructura.EstructuraDatosPartidas.COLUMN_NAME_JUGADOR2,
                Estructura.EstructuraDatosPartidas.COLUMN_DIFICULTAD,
                Estructura.EstructuraDatosPartidas.COLUMN_RESULTADO};

        int[] to = { R.id.textViewNombreJ1, R.id.textViewNombreJ2, R.id.textViewDificultad, R.id.textViewResultado}; // SON DEL LAYOUT LINEAR_LAYOUT

        SimpleCursorAdapter adaptador = new SimpleCursorAdapter(this,
                R.layout.linear_layout, cursor, from, to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        lv.setAdapter(adaptador);

        db.close();
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    public void vueltaPrincipal (View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }


    public void vueltaPrincipalHistorial2 () {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

}