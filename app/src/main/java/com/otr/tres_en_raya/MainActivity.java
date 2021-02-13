package com.otr.tres_en_raya;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.BreakIterator;



public class MainActivity extends AppCompatActivity {


    private int jugadores;
    private RadioGroup grupo_radio;
    private int[] casillas;
    private Partida partida;
    SQLite helper;
    SQLiteDatabase db;

    private LinearLayout menu;
    private LinearLayout espacio1;

    //SONIDOS
    Button play, pause, stop;
    MediaPlayer media;
    MediaPlayer mediaVictoria;
    MediaPlayer mediaDerrota;
    MediaPlayer mediaEmpate;
    int pos_reproduccion;
    private final int REQUEST_CODE_PERMISSIONS = 1000;
    private String fichero = null;
    boolean conSonido = false;
    String reproducirAudios = "REPRODUCE";
    String silenciarAudios = "SILENCE";
    View v;

    // TABLA HISTORIAL
    String miNombreComoJugador;
    private String resultadoPartida;
    private String nivelDificultad;
    private String nombrePlayer2;

    //TABLA ESTADISTICAS
    private int partidasTotales = 0;
    private int puntosTotales = 0;
    private int puntosPartida = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      setContentView(R.layout.activity_main);

        pos_reproduccion = 0;
        play = findViewById(R.id.botonSonido);

        //ME PASO EL NOMBRE ELEGIDO EN EL OTRO LAYOUT
        Intent intent = getIntent();
        miNombreComoJugador = intent.getStringExtra("nombreUser");

        helper = new SQLite(this);
        db = helper.getWritableDatabase();

        casillas = new int[9];
        casillas[0] = R.id.a1; casillas[1] = R.id.a2; casillas[2] = R.id.a3;
        casillas[3] = R.id.b1; casillas[4] = R.id.b2; casillas[5] = R.id.b3;
        casillas[6] = R.id.c1; casillas[7] = R.id.c2; casillas[8] = R.id.c3;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent;

        switch (item.getItemId()) {

            case R.id.consultarRanking:

                irARanking();

            case R.id.consultarHistorial:

                irAHistorial();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void irARanking() {
        Intent i = new Intent(this, ListarRankingPartidas.class);
        startActivity(i);
    }

    private void irAHistorial() {

        Intent i = new Intent (this, ListarHistorialPartidas.class);
        startActivity(i);

    }

    private void mute() {

        AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);
    }

    public void unmute() {

        AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        amanager.setStreamMute(AudioManager.STREAM_MUSIC, false);
    }

    public void stopLocal (View view) {

        media.stop();
        pos_reproduccion = 0;
        media.release();
        media = null;

    }

    public void jugar(View view) {

        ImageView imagen;

        for(int i = 0; i < casillas.length; i++){
            imagen = findViewById(casillas[i]);
            imagen.setImageResource(R.drawable.casilla);
        }

        menu = findViewById(R.id.menu);
        espacio1 = findViewById(R.id.espacio1);

        //CUANDO JUEGA SOLO 1
        jugadores = 1;

        //NIVEL DE DIFICULTAD SELECCIONADO
        grupo_radio = findViewById(R.id.grupo_radio);
        int id = grupo_radio.getCheckedRadioButtonId();

        int dificultad = 0;
        nivelDificultad = getResources().getString(R.string.facil);
        puntosPartida = 1;

        if(id == R.id.radio_dificil) {
            dificultad = 1;
            puntosPartida = 2;
            nivelDificultad = getResources().getString(R.string.dificil);

        } else if(id == R.id.radio_extremo) {
            nivelDificultad = getResources().getString(R.string.extreme);
            puntosPartida = 3;
            dificultad = 2;

        }

        //SI ELEGIMOS DOS JUGADORES
        if(view.getId() == R.id.btn_dos_jugadores) {
            //RESETEO LA DIFICULTAD PORQUE NO HAY BOT
            jugadores = 2;
            nivelDificultad = "";
            //LE PASO LOS VALORES PARA QUE SE TRADUZCAN
            nombrePlayer2 = getResources().getString(R.string.j2);
            resultadoPartida = getResources().getString(R.string.j2);
            puntosPartida = 3;

        }

        else {

            //LE PASO LOS VALORES PARA QUE SE TRADUZCAN
            nombrePlayer2 = getResources().getString(R.string.machine);
            resultadoPartida = getResources().getString(R.string.machine);

        }

        //COMIENZA LA PARTIDA
        partida = new Partida(dificultad);

    }

    private void marcarCasilla(int casilla){

        ImageView imagen;

        imagen = findViewById(casillas[casilla]);

        if(partida.getJugador() == 1 && partida.getCasillasSeleccionadas(casilla) == 0){
            imagen.setImageResource(R.drawable.aspa);
            partida.setCasillasSeleccionadas(partida.getJugador(), casilla);
            partida.cambiarJugador();
        }
        else if(partida.getJugador() == 2 && partida.getCasillasSeleccionadas(casilla) == 0){
            imagen.setImageResource(R.drawable.circulo);
            partida.setCasillasSeleccionadas(partida.getJugador(), casilla);
            partida.cambiarJugador();
        }

        // CUANDO GANA EL JUGADOR 1
        if(partida.comprobarGanador()[0]==1) {

            partida.setTerminada(true);
            partidasTotales = 1 + partidasTotales;
            puntosTotales = puntosPartida + puntosTotales;
            resultadoPartida = miNombreComoJugador;
            insertarDatosPartidas(this.miNombreComoJugador, nombrePlayer2, this.nivelDificultad, resultadoPartida);

            insertarDatosJugadoresRanking(this.miNombreComoJugador, this.partidasTotales, this.puntosTotales);


            TextView mensaje = findViewById(R.id.resultadoPartida);
            mensaje.setText(R.string.gana_jugador1);
            playVictoria(v);
            mensaje.setVisibility(View.VISIBLE);

            //PARA RESETEAR TODOS LOS ELEMENTOS UNA VEZ GANA
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    setContentView(R.layout.activity_main);
                    //
                    RadioButton hard = findViewById(R.id.radio_dificil);
                    RadioButton extreme = findViewById(R.id.radio_extremo);
                    RadioButton easy = findViewById(R.id.radio_facil);

                    if(partida.getDificultad() == 1){
                        hard.setChecked(true);
                    }
                    else if(partida.getDificultad() == 2){
                        extreme.setChecked(true);
                    }
                    hard.jumpDrawablesToCurrentState();
                    extreme.jumpDrawablesToCurrentState();
                    easy.jumpDrawablesToCurrentState();
                }
            }, 2500);
        }

        //SI GANA EL JUGADOR 2 O LA MÁQUINA
        else if(partida.comprobarGanador()[0] == 2) {
            partida.setTerminada(true);
            partidasTotales = 1 + partidasTotales;
            puntosTotales = 0 + puntosTotales;

            insertarDatosPartidas(miNombreComoJugador, nombrePlayer2, nivelDificultad, resultadoPartida);

            insertarDatosJugadoresRanking(miNombreComoJugador, partidasTotales, puntosTotales);

            TextView mensaje = findViewById(R.id.resultadoPartida);
            mensaje.setText(R.string.gana_jugador2);
            playDerrota(v);
            mensaje.setVisibility(View.VISIBLE);

            //PARA RESETEAR TODOS LOS ELEMENTOS UNA VEZ GANA
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setContentView(R.layout.activity_main);
                    RadioButton hard = findViewById(R.id.radio_dificil);
                    RadioButton extreme = findViewById(R.id.radio_extremo);
                    RadioButton easy = findViewById(R.id.radio_facil);

                    if(partida.getDificultad()==1){
                        hard.setChecked(true);
                    }
                    else if(partida.getDificultad()==2){
                        extreme.setChecked(true);
                    }
                    hard.jumpDrawablesToCurrentState();
                    extreme.jumpDrawablesToCurrentState();
                    easy.jumpDrawablesToCurrentState();
                }
            }, 2500);

        } else if(partida.comprobarGanador()[0] == 3) {
            partida.setTerminada(true);

            resultadoPartida =  getResources().getString(R.string.resultadoEmpate);
            puntosTotales = 0 + puntosTotales;
            partidasTotales = 1 + partidasTotales;

            insertarDatosPartidas(miNombreComoJugador, nombrePlayer2, this.nivelDificultad, this.resultadoPartida);
            insertarDatosJugadoresRanking(miNombreComoJugador, partidasTotales, puntosTotales);

            TextView mensaje = findViewById(R.id.resultadoPartida);
            mensaje.setText(R.string.empate);

            playEmpate(v);

            mensaje.setVisibility(View.VISIBLE);

            //PARA RESETEAR TODOS LOS ELEMENTOS UNA VEZ SE EMPATA
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    setContentView(R.layout.activity_main);
                    RadioButton hard = findViewById(R.id.radio_dificil);
                    RadioButton extreme = findViewById(R.id.radio_extremo);
                    RadioButton easy = findViewById(R.id.radio_facil);

                    if(partida.getDificultad()==1){
                        hard.setChecked(true);
                    }
                    else if(partida.getDificultad()==2){
                        extreme.setChecked(true);
                    }
                    hard.jumpDrawablesToCurrentState();
                    extreme.jumpDrawablesToCurrentState();
                    easy.jumpDrawablesToCurrentState();
                }
            }, 2500);
        }

    }

    public void tocarCasilla(View view){

        if (conSonido == true) {

            playLocal(view);

        }

        if(partida == null)
            return;

        int casilla = 0;

        for(int i = 0; i < casillas.length; i++){
            if(casillas[i] == view.getId()){
                casilla = i;

                break;
            }
        }

        if(!partida.isTerminada() && partida.getCasillasSeleccionadas(casilla) == 0 && partida.getJugador() == 1 && jugadores == 1)  {

            marcarCasilla(casilla);

            if(!partida.isTerminada()){
                while(true) {

                    int casilla_ia = partida.ia();

                    if(partida.getCasillasSeleccionadas(casilla_ia) == 0) {
                        marcarCasilla(casilla_ia);
                        break;
                    }
                }
            }
        }

        else if(!partida.isTerminada() && partida.getCasillasSeleccionadas(casilla) == 0 && jugadores == 2)

            marcarCasilla(casilla);
    }

    private void insertarDatosPartidas(String nombreJ1, String nombreJ2, String dificultad, String resultado) {

        ContentValues values = new ContentValues();
        values.put("jugador1", nombreJ1);
        values.put("jugador2", nombreJ2);
        values.put("dificultad", dificultad);
        values.put("resultado", resultado);
        db.insert("datosPartidas", null, values);

    }

    public void listaPartidas (View view) {
        Intent i = new Intent(this, ListarHistorialPartidas.class);
        startActivity(i);
    }

    private void insertarDatosJugadoresRanking(String nombreJugador, int nPartidas, int nPuntos) {

        ContentValues values = new ContentValues();
        values.put("usuario", nombreJugador);
        values.put("partidasJugadas", nPartidas);
        values.put("puntos", nPuntos);
        db.insert("datosJugadores", null, values);

    }

    public void listaRanking (View view) {
        Intent i = new Intent(this, ListarRankingPartidas.class);
        startActivity(i);
    }


    public boolean playLocal(View view) {

        if (media == null) {

            media = MediaPlayer.create(this, R.raw.touch);

        }

        if (!media.isPlaying()) {

            media.start();
            media.seekTo(pos_reproduccion);
            conSonido = true;
            return conSonido;

        }

        if(media.isPlaying()) {

            pos_reproduccion = media.getCurrentPosition();
            media.pause();
            conSonido = false;
            return conSonido;

        }

        return conSonido;

    }

    public void reproduceSonido(View view) {

        final Button testButton = (Button) findViewById(R.id.botonSonido);
        testButton.setText(reproducirAudios);
        testButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Button btc = (Button)v;
                String bText = btc.getText().toString();

                if (bText == reproducirAudios) {
                    btc.setText(silenciarAudios);
                    unmute();
                    playLocal(v);
                }
                else if (bText == silenciarAudios) {
                    mute();
                    btc.setText(reproducirAudios);
                    stopLocal(v);
                }
            }
        });
    }

    public void playVictoria(View view) {

        if (mediaVictoria == null) {

            mediaVictoria = MediaPlayer.create(this, R.raw.victory);

        }

        if (!mediaVictoria.isPlaying()) {

            mediaVictoria.start();
            mediaVictoria.seekTo(pos_reproduccion);

        }
    }

    public void playDerrota(View view) {

        if (mediaDerrota == null) {

            mediaDerrota = MediaPlayer.create(this, R.raw.derrota);

        }

        if (!mediaDerrota.isPlaying()) {

            mediaDerrota.start();
            mediaDerrota.seekTo(pos_reproduccion);

        }
    }

    public void playEmpate(View view) {

        if (mediaEmpate == null) {

            mediaEmpate = MediaPlayer.create(this, R.raw.empate);

        }

        if (!mediaEmpate.isPlaying()) {

            mediaEmpate.start();
            mediaEmpate.seekTo(pos_reproduccion);

        }
    }


}

