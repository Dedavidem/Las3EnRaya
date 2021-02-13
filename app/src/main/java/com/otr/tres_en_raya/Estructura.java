package com.otr.tres_en_raya;

import android.provider.BaseColumns;

public class Estructura {

    public static final String SQL_CREATE_DATOS_PARTIDAS =
            "CREATE TABLE IF NOT EXISTS " + EstructuraDatosPartidas.TABLE_NAME_DATOS_PARTIDAS +
                    "(" + EstructuraDatosPartidas._ID + " integer PRIMARY KEY, " + EstructuraDatosPartidas.COLUMN_NAME_JUGADOR1 +
                    " text, " + EstructuraDatosPartidas.COLUMN_NAME_JUGADOR2 + " text, " +
                    EstructuraDatosPartidas.COLUMN_DIFICULTAD + " text, " +
                    EstructuraDatosPartidas.COLUMN_RESULTADO + " text);";

    public static final String SQL_DELETE_DATOS_PARTIDAS =
            "DROP TABLE IF EXISTS " + EstructuraDatosPartidas.TABLE_NAME_DATOS_PARTIDAS;

    public static class EstructuraDatosPartidas implements BaseColumns {

        public static final String TABLE_NAME_DATOS_PARTIDAS = "datosPartidas";
        public static final String COLUMN_NAME_JUGADOR1 = "jugador1";
        public static final String COLUMN_NAME_JUGADOR2 = "jugador2";
        public static final String COLUMN_DIFICULTAD = "dificultad";
        public static final String COLUMN_RESULTADO = "resultado";

    }

    public static final String SQL_CREATE_DATOS_USUARIOS =
            "CREATE TABLE IF NOT EXISTS " + EstructuraDatosJugadores.TABLE_NAME_DATOS_JUGADORES +
                    "(" + EstructuraDatosJugadores._ID + " integer PRIMARY KEY, " + EstructuraDatosJugadores.COLUMN_NAME_USUARIO +
                    " text, " + EstructuraDatosJugadores.COLUMN_NAME_N_PARTIDAS + " integer, " +
                    EstructuraDatosJugadores.COLUMN_PUNTOS + " integer);";

    public static final String SQL_DELETE_DATOS_USUARIOS =
            "DROP TABLE IF EXISTS " + EstructuraDatosJugadores.TABLE_NAME_DATOS_JUGADORES;

    public static class EstructuraDatosJugadores implements BaseColumns {

        public static final String TABLE_NAME_DATOS_JUGADORES = "datosJugadores";
        public static final String COLUMN_NAME_USUARIO = "usuario";
        public static final String COLUMN_NAME_N_PARTIDAS= "partidasJugadas";
        public static final String COLUMN_PUNTOS = "puntos";

    }

    }
