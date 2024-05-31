package org.example.model.exceptions;

import java.util.HashMap;
import java.util.Map;

public class DAOException extends Exception{

    //Map amb els missatges
    private static final Map<Integer, String> missatges = new HashMap<>();
    //Num i retorna string, el map
    static {
        missatges.put(0, "Error al connectar a la BD!!");
        missatges.put(1, "Restricció d'integritat violada - clau primària duplicada");
        missatges.put(2, "El nom del pilot ja existeix");
        missatges.put(3, "Falta omplir alguna dada o arreglar-la!!");
        missatges.put(4, "Per borrar una fila l'has de seleccionar a la taula");
        missatges.put(5, "Algun camp no és correcte!!");
        missatges.put(6, "Per modificar una fila l'has de seleccionar a la taula");
        missatges.put(7, "Has d'introduir una edat correcta!!");
        missatges.put(8, "Error al borrar les dades a la BD!!");
    }

    //Atribut
    private int tipo;

    //Constructor al q pasem tipo
    public DAOException(int tipo){
        this.tipo=tipo;
    }

    //Sobreescrivim el get message
        @Override
    public String getMessage(){
        return missatges.get(this.tipo); //el missatge del tipo
    }

    //getter1
    public int getTipo() {
        return tipo;
    }
}
