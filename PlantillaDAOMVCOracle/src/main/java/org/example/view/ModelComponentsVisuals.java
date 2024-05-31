package org.example.view;

import org.example.model.entities.PilotHardEnduro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ModelComponentsVisuals {

    //Atributs
    private DefaultTableModel modelTaulaPilot;
    private DefaultTableModel modelTaulaComp;
    private ComboBoxModel<PilotHardEnduro.Competicio.Campeonat> comboBoxModel;

    //Getters
    public ComboBoxModel<PilotHardEnduro.Competicio.Campeonat> getComboBoxModel() {
        return comboBoxModel;
    }

    public DefaultTableModel getModelTaulaPilot() {
        return modelTaulaPilot;
    }

    public DefaultTableModel getModelTaulaComp() {
        return modelTaulaComp;
    }

    //Constructor
    public ModelComponentsVisuals() {


        //Anem a definir l'estructura de la taula dels alumnes
        modelTaulaPilot = new DefaultTableModel(new Object[]{"Nom","Edat","És pilot mundial?","Object"},0){
            /**
             * Retorna true independentment dels valors dels paràmetres.
             *
             * @param row    la fila de la qual es vol consultar el valor
             * @param column la columna de la qual es vol consultar el valor
             * @return true
             * @see #setValueAt
             */
            @Override
            public boolean isCellEditable(int row, int column) {

                //Fem que TOTES les cel·les de la columna 1 de la taula es puguen editar
                //if(column==1) return true;
                return false;
            }



            //Permet definir el tipo de cada columna
            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return String.class;
                    case 1:
                        return Integer.class;
                    case 2:
                        return Boolean.class;
                    default:
                        return Object.class;
                }
            }
        };




        //Anem a definir l'estructura de la taula de les matrícules
        modelTaulaComp =new DefaultTableModel(new Object[]{"Competicio","Victoria"},0){
            /**
             * Retorna true independentment dels valors dels paràmetres.
             *
             * @param row    la fila de la qual es vol consultar el valor
             * @param column la columna de la qual es vol consultar el valor
             * @return true
             * @see #setValueAt
             */
            @Override
            public boolean isCellEditable(int row, int column) {

                //Fem que TOTES les cel·les de la columna 1 de la taula es puguen editar
                //if(column==1) return true;
                return false;
            }

            //Permet definir el tipo de cada columna
            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return PilotHardEnduro.Competicio.Campeonat.class;
                    case 1:
                        return Integer.class;
                    default:
                        return Object.class;
                }
            }
        };

        //Estructura del comboBox
        comboBoxModel=new DefaultComboBoxModel<>(PilotHardEnduro.Competicio.Campeonat.values());



    }
}
