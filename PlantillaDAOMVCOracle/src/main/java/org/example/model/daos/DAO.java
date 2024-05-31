package org.example.model.daos;


import org.example.model.entities.PilotHardEnduro;
import org.example.model.exceptions.DAOException;

import java.util.List;

public interface DAO <T>{

    //T és el tipus de dades amb el que treballarem
    T get(Long id) throws DAOException;

    //Retorna una llista de tots els objectes de la BD
    List<T> getAll() throws DAOException;

    //Guarda un objecte a la BD
    void save(T obj) throws DAOException;

    //Actualitza un objecte a la BD
    void update(T pilot) throws DAOException;

    //Esborra un objecte de la BD
    void delete(Long id) throws DAOException;

    //Inserta un pilot a la BD
    void insertPilot (PilotHardEnduro pilot) throws DAOException;

    //Tots els mètodes necessaris per interactuar en la BD

}
