package org.example.app;

import org.example.controller.Controller;
import org.example.model.impls.PilotHardEnduroDAOJDBCOracleImpl;
import org.example.view.CompeticioView;

import javax.swing.*;
import java.util.Locale;

public class Main {


    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //Definim la cultura de la nostra aplicació
                Locale.setDefault(new Locale("ca","ES"));
               new Controller(new PilotHardEnduroDAOJDBCOracleImpl(), new CompeticioView());

            }
        });
    }
}
