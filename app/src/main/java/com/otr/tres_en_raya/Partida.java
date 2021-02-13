package com.otr.tres_en_raya;

import java.util.Random;

public class Partida {

    private final int dificultad;
    private int[] casillas_seleccionadas;
    private int jugador;
    private boolean terminada;

    private final int[][] combinaciones = {{0,1,2}, {3,4,5}, {6,7,8},
            {0,3,6}, {1,4,7}, {2,5,8},
            {0,4,8}, {2,4,6}};

    public Partida(int dificultad){

        this.dificultad = dificultad;
        jugador = 1;
        terminada = false;
        casillas_seleccionadas = new int[9];
    }

    public int getJugador(){
        return jugador;
    }

    public void cambiarJugador(){

        if(this.jugador == 1) {
            this.jugador = 2;
        }  else {
            this.jugador = 1;
        }
    }


    public int numeroCasillasSeleccionadas(int jugador){
        int contador = 0;
        for(int i = 0; i < casillas_seleccionadas.length; i++){
            if(casillas_seleccionadas[i]==jugador)
                contador++;
        }
        return contador;
    }

    public int dosEnRaya(int jugador){

        int [][] combinaciones = getCombinaciones();
        int casilla = -1;
        int cuantas_lleva = 0;

        for(int i = 0; i < getCombinaciones().length; i++){
            cuantas_lleva = 0;
            for(int j = 0; j < 3; j++){
                if(casillas_seleccionadas[combinaciones[i][j]]==jugador)
                    cuantas_lleva++;
            }
            if(cuantas_lleva==2){
                for(int j = 0; j < 3; j++){
                    if(casillas_seleccionadas[combinaciones[i][j]]==0)
                        return combinaciones[i][j];
                }
            }
        }
        return casilla;
    }

    public int combinacionesConAspa(){

        int [][] combinaciones = getCombinaciones();
        int casilla = -1;
        int [] combinaciones_aspa = new int[8];

        int casilla0 = 0;
        int casilla2 = 0;
        int casilla6 = 0;
        int casilla8 = 0;

        for(int i = 0; i < getCombinaciones().length; i++){
            for(int j = 0; j < 3; j++){
                if(casillas_seleccionadas[combinaciones[i][j]] == 1)
                    combinaciones_aspa[i] = 1;
            }
        }

        //COMBINACIONES POSIBLES
        for(int i = 0; i < getCombinaciones().length; i++){
            if(combinaciones_aspa[i] == 1){
                for(int j = 0; j < 3; j++){
                    if(combinaciones[i][j] == 0 && casillas_seleccionadas[combinaciones[i][j]] == 0)
                        casilla0++;
                    else if(combinaciones[i][j] == 2 && casillas_seleccionadas[combinaciones[i][j]] == 0)
                        casilla2++;
                    else if(combinaciones[i][j] == 6 && casillas_seleccionadas[combinaciones[i][j]] == 0)
                        casilla6++;
                    else if(combinaciones[i][j] == 8 && casillas_seleccionadas[combinaciones[i][j]] == 0)
                        casilla8++;
                }
            }
        }

        if(casilla0 == 2)
            return 0;
        else if(casilla2 == 2)
            return 2;
        else if(casilla6 == 2)
            return 6;
        else if(casilla8 == 2)
            return 8;

        return -1;
    }

    public int dosEnRayaGanar(){

        int [][] combinaciones = getCombinaciones();
        int casilla = -1;
        int cuantas_lleva = 0;

        for(int i = 0; i < getCombinaciones().length; i++){
            cuantas_lleva = 0;
            for(int j = 0; j < 3; j++){
                if(casillas_seleccionadas[combinaciones[i][j]] == 2)
                    cuantas_lleva++;
                if(casillas_seleccionadas[combinaciones[i][j]] == 1)
                    cuantas_lleva--;
            }
            if(cuantas_lleva==1){
                for(int j = 0; j < 3; j++){
                    if(casillas_seleccionadas[combinaciones[i][j]] == 0)
                        return combinaciones[i][j];
                }
            }
        }
        return casilla;
    }

    public int ia(){

        int casilla;

        switch(dificultad) {
            case 0: {

                Random aleatorio = new Random();
                casilla = aleatorio.nextInt(9);

                return casilla;
            }

            case 1: {
                casilla = dosEnRaya(1);
                if(casilla != -1) {
                    return casilla;
                }

                Random aleatorio = new Random();

                casilla = aleatorio.nextInt(9);

                return casilla;
            }

            case 2: {

                if(casillas_seleccionadas[1] == 1 &&casillas_seleccionadas[8] == 1 && casillas_seleccionadas[0] == 0)
                    return 0;
                else if(casillas_seleccionadas[1] == 1 &&casillas_seleccionadas[6] == 1 && casillas_seleccionadas[0] == 0)
                    return 0;

                if(numeroCasillasSeleccionadas(1) == 1) {
                    if(casillas_seleccionadas[1] == 1 && casillas_seleccionadas[7] == 0)
                        return 7;
                    else  if(casillas_seleccionadas[3] == 1 && casillas_seleccionadas[5] == 0)
                        return 5;
                    else  if(casillas_seleccionadas[5] == 1 && casillas_seleccionadas[3] == 0)
                        return 3;
                    else  if(casillas_seleccionadas[7] == 1 && casillas_seleccionadas[1] == 0)
                        return 1;
                }

                //SI NO SE PUEDE GANAR, RELLENA LAS CASILLAS NECESARIAS PARA EMPATAR
                casilla = dosEnRaya(1);
                if(casilla !=- 1) {
                    return casilla;
                }

                // EN CASO DE QUE PUEDA GANAR
                casilla = dosEnRaya(2);
                if(casilla != -1) {
                    return casilla;
                }

                if(casillas_seleccionadas[4] == 2 && (casillas_seleccionadas[6] == 1 || casillas_seleccionadas[8] == 1) &&
                        casillas_seleccionadas[7] == 0){
                    return 7;
                }

                if(combinacionesConAspa() != -1)
                    return combinacionesConAspa();

                if(casillas_seleccionadas[4] == 0)
                    return 4;

                if(casillas_seleccionadas[0] == 0)
                    return 0;
                if(casillas_seleccionadas[2] == 0)
                    return 2;
                if(casillas_seleccionadas[6] == 0)
                    return 6;
                if(casillas_seleccionadas[8] == 0)
                    return 8;

                Random aleatorio = new Random();

                casilla = aleatorio.nextInt(9);

                return casilla;
            }
        }
        return 0;
    }

    public boolean isTerminada(){
        return terminada;
    }

    public void setTerminada(boolean terminada){
        this.terminada = terminada;
    }

    public int getCasillasSeleccionadas(int casilla){
        return casillas_seleccionadas[casilla];
    }

    public void setCasillasSeleccionadas(int valor, int casilla){
        this.casillas_seleccionadas[casilla] = valor;
    }

    public int[] comprobarGanador(){

        int array[] = new int[2];

        final int[][] combinaciones = getCombinaciones();

        for(int i = 0; i < 8; i++){
            if(casillas_seleccionadas[combinaciones[i][0]] == 1 &&
               casillas_seleccionadas[combinaciones[i][1]] == 1 &&
               casillas_seleccionadas[combinaciones[i][2]] == 1) {

                array[0] = 1;
                array[1] = i;
                return array;
            }
            else if(casillas_seleccionadas[combinaciones[i][0]] == 2 &&
                    casillas_seleccionadas[combinaciones[i][1]] == 2 &&
                    casillas_seleccionadas[combinaciones[i][2]] == 2){

                array[0] = 2;
                array[1] = i;
                return array;
            }
        }

        int contador = 0;
        for(int i = 0; i < 9; i++){

            if(casillas_seleccionadas[i] != 0)
                contador++;
        }
        if(contador == 9)  {

            array[0] = 3;
            array[1] = 3;
            return array;
        }

        array[0] = 0;
        array[1] = 0;
        return array;
    }

    public int[][] getCombinaciones(){

        return combinaciones;
    }

    public int getDificultad(){

        return dificultad;
    }
}
