package org.example.controller;

import org.example.model.entities.PilotHardEnduro;
import org.example.model.exceptions.DAOException;
import org.example.model.entities.PilotHardEnduro.Competicio;
import org.example.view.ModelComponentsVisuals;
import org.example.model.impls.PilotHardEnduroDAOJDBCOracleImpl;
import org.example.view.CompeticioView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

public class Controller implements PropertyChangeListener { //1. Implementació de interfície PropertyChangeListener


    private ModelComponentsVisuals modelComponentsVisuals =new ModelComponentsVisuals();
    private PilotHardEnduroDAOJDBCOracleImpl dadesPilot;
    private CompeticioView view;

    public Controller(PilotHardEnduroDAOJDBCOracleImpl dadesPilot, CompeticioView view) {
        this.dadesPilot = dadesPilot;
        this.view = view;

        //Executem el mètode per a carregar les dades de la BD
        try {
            this.dadesPilot.creaTaulaPilots();
        } catch (DAOException e) {
            e.printStackTrace();
        }

        //5. Necessari per a que Controller reaccione davant de canvis a les propietats lligades
        canvis.addPropertyChangeListener(this);

        //Lliguem la vista amb el model
        lligaVistaModel();

        //Afegim els listeners als components de la vista
        afegirListeners();

        //Si no hem tingut cap poroblema amb la BD, mostrem la finestra
        view.setVisible(true);

    }

    //Mètode per a lligar la vista amb el model
    private void lligaVistaModel() {

        //Carreguem la taula d'alumnes en les dades de la BD
        try {
            setModelTaulaAlumne(modelComponentsVisuals.getModelTaulaPilot(), dadesPilot.getAll());
        } catch (DAOException e) {
            this.setExcepcio(e);
        }

        //Fixem el model de la taula dels alumnes
        JTable taula = view.getTaula();
        taula.setModel(this.modelComponentsVisuals.getModelTaulaPilot());
        //Amago la columna que conté l'objecte alumne
        taula.getColumnModel().getColumn(3).setMinWidth(0);
        taula.getColumnModel().getColumn(3).setMaxWidth(0);
        taula.getColumnModel().getColumn(3).setPreferredWidth(0);

        //Fixem el model de la taula de matrícules
        JTable taulaMat = view.getTaulaComp();
        taulaMat.setModel(this.modelComponentsVisuals.getModelTaulaComp());

        //Posem valor a el combo d'MPs
        view.getComboCampeonat().setModel(modelComponentsVisuals.getComboBoxModel());

        //Desactivem la pestanya de la matrícula
        view.getPestanyes().setEnabledAt(1, false);
        view.getPestanyes().setTitleAt(1, "Competicions de ...");

    }

    //Mètode per a omplir el model de la taula d'alumnes
    private void setModelTaulaAlumne(DefaultTableModel modelTaulaAlumne, List<PilotHardEnduro> all) {

        // Fill the table model with data from the collection
        for (PilotHardEnduro pilot : all) {
            modelTaulaAlumne.addRow(new Object[]{pilot.getNom(), pilot.getEdad(), pilot.isPilotMundial(), pilot});
        }
    }

    private void afegirListeners() {

        ModelComponentsVisuals modelo = this.modelComponentsVisuals;
        DefaultTableModel model = modelo.getModelTaulaPilot();
        DefaultTableModel modelComp = modelo.getModelTaulaComp();
        JTable taula = view.getTaula();
        JTable taulaComp = view.getTaulaComp();
        JButton insertarButton = view.getInsertarButton();
        JButton modificarButton = view.getModificarButton();
        JButton borrarButton = view.getBorrarButton();
        JTextField campNom = view.getCampNom();
        JTextField campEdad = view.getCampEdad();
        JCheckBox caixaAlumne = view.getCaixaAlumne();
        JTabbedPane pestanyes = view.getPestanyes();
        JTextField campVictories = view.getCampVictoria();
        JComboBox comboCampeonat = view.getComboCampeonat();

        //Botó insertar
        view.getInsertarButton().addActionListener(
                new ActionListener() {
                    /**
                     * ActionListener per gestionar els esdeveniments de clic al botó d'inserció.
                     * Implementa l'acció d'inserir una nova fila a la taula.
                     *
                     * @param e l'esdeveniment de clic que desencadena l'acció, en aquest cas, al botó d'inserció
                     */
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        try {
                            String nom = campNom.getText();
                            int edad = Integer.parseInt(campEdad.getText());
                            boolean pilotMundial = caixaAlumne.isSelected();

                            if (nom.isBlank()) {
                                throw new DAOException(2);
                            } else if (nomPilotRepetit(nom)) {
                                throw new DAOException(1);
                            } else {
                                PilotHardEnduro pilot = new PilotHardEnduro(nom, edad, pilotMundial, new TreeSet<Competicio>());
                                dadesPilot.insertPilot(pilot);
                                model.addRow(new Object[]{pilot.getNom(), pilot.getEdad(), pilot.isPilotMundial(), pilot});
                                actualitzaTaulaPilot();

                                //Camps en blanc
                                campNom.setText("");
                                campEdad.setText("");
                                campNom.requestFocus();
                            }
                        } catch (NumberFormatException ex) {
                            setExcepcio(new DAOException(3));
                        } catch (DAOException ex) {
                            setExcepcio(ex);
                        }

                    }
                }
        );

        //Botó modificar

        view.getModificarButton().addActionListener(
                new ActionListener() {
                    /**
                     * ActionListener per gestionar els esdeveniments de clic al botó de modificació.
                     * Implementa l'acció de modificar la fila seleccionada de la taula.
                     *
                     * @param e l'esdeveniment de clic que desencadena l'acció, en aquest cas, al botó de modificació
                     */
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        //Obtenim la fila seleccionada
                        int filaSel = taula.getSelectedRow();

                            if (filaSel != -1) {
                                if (campNom != null && campEdad != null && caixaAlumne != null) {
                                    try {
                                        PilotHardEnduro pilot = (PilotHardEnduro) model.getValueAt(filaSel, 3);
                                        if (pilot != null) {
                                            String nomText = campNom.getText();
                                            String edadText = campEdad.getText();
                                            if (!nomText.isBlank() && !edadText.isBlank()) {
                                                pilot.setNom(nomText);
                                                pilot.setEdad(Integer.parseInt(edadText));
                                                pilot.setPilotMundial(caixaAlumne.isSelected());
                                                try {
                                                    dadesPilot.update(pilot);
                                                    model.setValueAt(nomText, filaSel, 0);
                                                    model.setValueAt(Integer.parseInt(edadText), filaSel, 1);
                                                    model.setValueAt(caixaAlumne.isSelected(), filaSel, 2);
                                                    actualitzaTaulaPilot();
                                                } catch (DAOException ex) {
                                                    throw new RuntimeException(ex);
                                                }
                                            }
                                        }
                                    } catch (NumberFormatException ex) {
                                        setExcepcio(new DAOException(3));
                                    }
                                }
                        } else setExcepcio(new DAOException(6));
                    }
                }

        );

        //Botó borrar
        view.getBorrarButton().addActionListener(
                new ActionListener() {
                    /**
                     * ActionListener per gestionar els esdeveniments de clic al botó de borrat.
                     * Implementa l'acció de suprimir la fila seleccionada de la taula.
                     *
                     * @param e l'esdeveniment de clic que desencadena l'acció, en aquest cas, al botó de borrat
                     */
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //Mirem si tenim una fila de la taula seleccionada
                        int filaSel = taula.getSelectedRow();

                            if (filaSel != -1) {
                                PilotHardEnduro pilot = (PilotHardEnduro) model.getValueAt(filaSel, 3);
                                model.removeRow(filaSel);
                                //Borrem la fila seleccionada de la base de dades
                                try {
                                    dadesPilot.delete(pilot.getId());
                                } catch (DAOException ex) {
                                    setExcepcio(ex);
                                }
                                actualitzaTaulaPilot();
                                //Una vegada borrada la fila seleccionem la fila a continuació
                                if (filaSel < model.getRowCount()) taula.setRowSelectionInterval(filaSel, filaSel);
                                else if (model.getRowCount() > 0)
                                    taula.setRowSelectionInterval(filaSel - 1, filaSel - 1);
                                //modificacionesRealizadas = true;
                                //Desactivem pestanyes
                                view.getPestanyes().setEnabledAt(1, false);
                                view.getPestanyes().setTitleAt(1, "Competicions de ...");
                            } else setExcepcio(new DAOException(4));
                    }
                }
        );

        //Afegim un MouseListener a la taula de pilots per a que els camps de la taula es posen als camps de text
        taula.addMouseListener(new MouseAdapter() {
           /**
             * MouseListener per gestionar els esdeveniments de ratolí a la taula de pilots.
             * Implementa l'acció de completar els camps de text amb els valors de la fila seleccionada al clicar-hi.
             *
             * @param e l'esdeveniment de ratolí que desencadena l'acció, en aquest cas, un clic a la taula de pilots
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                //Obtenim el número de la fila seleccionada
                int filaSel = taula.getSelectedRow();

                if (filaSel != -1) {        //Tenim una fila seleccionada
                    //Posem els valors de la fila seleccionada als camps respectius
                    campNom.setText(model.getValueAt(filaSel, 0).toString());
                    campEdad.setText(model.getValueAt(filaSel, 1).toString());
                    caixaAlumne.setSelected((Boolean) model.getValueAt(filaSel, 2));

                    //Activem la pestanya de la matrícula de l'alumne seleccionat
                    //view.getPestanyes().setEnabledAt(1, true);
                    //view.getPestanyes().setTitleAt(1, "Competicions de " + campNom.getText());

                    //Posem valor a el combo d'MPs
                    //view.getComboMP().setModel(modelo.getComboBoxModel());
                    //ompliCompeticio((PilotHardEnduro) model.getValueAt(filaSel, 3),modelComp);
                } else {                  //Hem deseleccionat una fila
                    //Posem els camps de text en blanc
                    campNom.setText("");
                    campEdad.setText("");

                    //Desactivem pestanyes
                    view.getPestanyes().setEnabledAt(1, false);
                    view.getPestanyes().setTitleAt(1, "Competicions de ...");
                }
            }
        });

        //Afegim un MouseListener a la taula de competicions per a que els camps de la taula es posen als camps de text
        taulaComp.addMouseListener(new MouseAdapter() {
            /**
             * MouseListener per gestionar els esdeveniments de ratolí a la taula de competicions.
             * Implementa l'acció de completar els camps de text amb els valors de la fila seleccionada al clicar-hi.
             *
             * @param e l'esdeveniment de ratolí que desencadena l'acció, en aquest cas, un clic a la taula de competicions
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                //Al seleccionar la taula omplim els camps de text en els valors de la fila seleccionada
                int filaSelComp = taulaComp.getSelectedRow();
                if (filaSelComp != -1) {        //Tenim una fila seleccionada
                    //Posem els valors de la fila seleccionada als camps respectius
                    view.getComboCampeonat().setSelectedItem(modelComp.getValueAt(filaSelComp, 0));
                    view.getCampVictoria().setText(modelComp.getValueAt(filaSelComp, 1).toString());
                } else {                  //Hem deseleccionat una fila
                    //Posem els camps de text en blanc
                    view.getComboCampeonat().setSelectedIndex(0);
                    view.getCampVictoria().setText("");
                }
            }
        });

        //Afegim un FocusListener al camp de text de l'edat per a controlar que el valor sigui correcte
        campEdad.addFocusListener(new FocusAdapter() {
            /**
             * Invoked when a component loses the keyboard focus.
             *
             * @param e
             */
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                String regex1="\\d+";

                if(campEdad.getText().isBlank() || !campEdad.getText().matches(regex1)){
                    setExcepcio(new DAOException(7));
                }
            }
        });
        //throw new LaMeuaExcepcio(1,"Ha petat la base de dades");
    }


    //Mètode per actualitzar la tuala pilot
    private void actualitzaTaulaPilot() {
        //Obtenim el model de la taula
        DefaultTableModel model = modelComponentsVisuals.getModelTaulaPilot();

        try {
            //Buidem el model
            model.setRowCount(0);
            //Omplim el model amb les dades de la BD
            setModelTaulaAlumne(model, dadesPilot.getAll());
        } catch (DAOException e) {
            setExcepcio(e);
        }
    }

    //Mètode per a omplir la taula de competicions del pilot seleccionat
//    private static void ompliCompeticio(PilotHardEnduro al, DefaultTableModel modelComp) {
//        //Omplim el model de la taula de competicions del pilot seleccionat
//        modelComp.setRowCount(0);
//        //Fill de taula amb dades de la col·lecció
//        for (PilotHardEnduro.Competicio competicio : al.getCompeticions()) {
//            modelComp.addRow(new Object[]{competicio.getCampeonat(),competicio.getVictories()});
//        }
//
//
//    }



    //Mètode per a comprovar que el nom del pilot no es repeteix
    private boolean nomPilotRepetit(String nomPilot) {
        JTable taula = view.getTaula();
        DefaultTableModel model = (DefaultTableModel) taula.getModel();
        //Recorrem totes les files de la taula
        for (int i = 0; i < model.getRowCount(); i++) {
            //Comprovem si el nom del pilot ja existeix
            if (model.getValueAt(i, 0).equals(nomPilot)) {
                String nomActual = (String) model.getValueAt(i, 0);
                if (nomActual.equals(nomPilot)) {
                    return true; //Si el nom del pilot ja existeix retornem true
                }
            }
        }
        return false; //Si el nom del pilot no existeix retornem false
    }

    //TRACTAMENT D'EXCEPCIONS

    //2. Propietat lligada per controlar quan genero una excepció
    public static final String PROP_EXCEPCIO="excepcio";

    //Propietat per a guardar l'excepció
    private DAOException excepcio;

    public DAOException getExcepcio() {
        return excepcio;
    }

    //Mètode per a canviar l'excepció
    public void setExcepcio(DAOException excepcio) {
        DAOException valorVell=this.excepcio;
        this.excepcio = excepcio;
        canvis.firePropertyChange(PROP_EXCEPCIO, valorVell,excepcio);
    }


    //3. Propietat PropertyChangesupport necessària per poder controlar les propietats lligades
    PropertyChangeSupport canvis=new PropertyChangeSupport(this);


    //4. Mètode on posarem el codi de tractament de les excepcions --> generat per la interfície PropertyChangeListener
    /**
     * PropertyChangeListener per gestionar els esdeveniments de canvi de propietat.
     * Implementa l'acció de tractar les excepcions generades.
     *
     * @param evt l'esdeveniment de canvi de propietat que desencadena l'acció, en aquest cas, una excepció
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        //Obtenim l'excepció generada
        DAOException rebuda=(DAOException)evt.getNewValue();

        //Tractament de l'excepció
        try {
            throw rebuda;
        } catch (DAOException e) {
            //Aquí farem ele tractament de les excepcions de l'aplicació
            switch(evt.getPropertyName()){
                case PROP_EXCEPCIO:

                    //Mostrem un missatge d'error
                    switch (rebuda.getTipo()) {
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                            JOptionPane.showMessageDialog(null, rebuda.getMessage());
                            break;
                    }


            }
        }
    }

}
