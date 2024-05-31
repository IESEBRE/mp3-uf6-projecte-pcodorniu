package org.example.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

//Classe que representa la vista de la nostra aplicació
public class CompeticioView extends JFrame{
    //Atributs
    private JTabbedPane pestanyes;
    private JTable taula;
    private JScrollPane scrollPane1;
    private JButton insertarButton;
    private JButton modificarButton;
    private JButton borrarButton;
    private JTextField campNom;
    private JTextField campEdat;
    private JCheckBox pilotMundial;
    private JPanel panel;
    private JTable taulaComp;
    private JComboBox comboCampeonat;
    private JTextField campVictoria;
    //private JTabbedPane PanelPestanya;

    //Getters
    public JTable getTaulaComp() {
        return taulaComp;
    }

    public JComboBox getComboCampeonat() {
        return comboCampeonat;
    }

    public JTextField getCampVictoria() {
        return campVictoria;
    }

    public JTabbedPane getPestanyes() {
        return pestanyes;
    }

    public JTable getTaula() {
        return taula;
    }

    public JButton getBorrarButton() {
        return borrarButton;
    }

    public JButton getModificarButton() {
        return modificarButton;
    }

    public JButton getInsertarButton() {
        return insertarButton;
    }

    public JTextField getCampNom() {
        return campNom;
    }

    public JTextField getCampEdad() {
        return campEdat;
    }

    public JCheckBox getCaixaAlumne() {
        return pilotMundial;
    }


    //Constructor de la classe
    public CompeticioView() {


        //Per poder vore la finestra
        this.setContentPane(panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(false);
    }

        //Mètode per a mostrar missatges d'error
        private void createUIComponents() {
        // TODO: place custom component creation code here
        scrollPane1 = new JScrollPane();
        taula = new JTable();
        pestanyes = new JTabbedPane();
        taula.setModel(new DefaultTableModel());
        taula.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        scrollPane1.setViewportView(taula);

    }
}
