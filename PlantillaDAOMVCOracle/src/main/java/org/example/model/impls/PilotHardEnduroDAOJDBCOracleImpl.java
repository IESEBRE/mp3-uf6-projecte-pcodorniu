package org.example.model.impls;

import org.example.model.daos.DAO;
import org.example.model.entities.PilotHardEnduro;
import org.example.model.exceptions.DAOException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;

public class PilotHardEnduroDAOJDBCOracleImpl implements DAO<PilotHardEnduro> {

    //Carreguem el fitxer de propietats
    Properties properties = new Properties();

    //Constructor
    @Override
    public PilotHardEnduro get(Long id) throws DAOException {

        //Declaració de variables del mètode
        Statement st = null;
        ResultSet rs = null;
        PilotHardEnduro pilot = null;

        //Carreguem el fitxer de propietats
        try (FileInputStream input = new FileInputStream("/home/alumne/Documents/MP03/UF6/mp3-uf6-projecte-pcodorniu/PlantillaDAOMVCOracle/src/main/resources/database.properties")) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al carregar el fitxer de properties");
        }

        //Obtenim les dades de la BD
        String url = properties.getProperty("ORACLE_URL");
        String user = properties.getProperty("ORACLE_USER");
        String password = properties.getProperty("ORACLE_PASSWORD");

        //Connexió a la BD
        try (Connection con = DriverManager.getConnection(url, user, password);
        ) {
            //Creem la sentència SQL
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM PILOTS");

            //Recorrem el resultat de la consulta
            if (rs.next()) {
                pilot = new PilotHardEnduro(Long.valueOf(rs.getString(1)), rs.getString(2));
            }
        } catch (SQLException throwables) {
            throw new DAOException(1);
        } finally {
            try {
                //Tanquem els recursos
                if (rs != null) rs.close();
                if (st != null) st.close();
            } catch (SQLException e) {
                throw new DAOException(1);
            }

        }
        //Retornem el pilot
        return pilot;
    }


    //Mètode per a obtenir tots els pilots de la BD
    @Override
    public List<PilotHardEnduro> getAll() throws DAOException {
        //Declaració de variables del mètode
        List<PilotHardEnduro> pilot = new ArrayList<>();

        try (FileInputStream input = new FileInputStream("/home/alumne/Documents/MP03/UF6/mp3-uf6-projecte-pcodorniu/PlantillaDAOMVCOracle/src/main/resources/database.properties")) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al carregar el fitxer de properties");
        }

        String url = properties.getProperty("ORACLE_URL");
        String user = properties.getProperty("ORACLE_USER");
        String password = properties.getProperty("ORACLE_PASSWORD");

        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement st = con.prepareStatement("SELECT * FROM PILOTS");
             ResultSet rs = st.executeQuery();
        ) {

            while (rs.next()) {
                pilot.add(new PilotHardEnduro(rs.getLong("id"), rs.getString("nom"), rs.getInt("edad"), rs.getInt("mundial") == 1 ? true : false,
                        new TreeSet<PilotHardEnduro.Competicio>()));
            }
        } catch (SQLException throwables) {
            int tipoError = throwables.getErrorCode();
            //System.out.println(tipoError+" "+throwables.getMessage());
            switch(throwables.getErrorCode()){
                case 17002: //l'he obtingut posant un sout en el throwables.getErrorCode()
                    tipoError = 0;
                    break;
                default:
                    tipoError = 1;  //error desconegut
            }
            throw new DAOException(tipoError);
        }
        return pilot;
    }


    //Mètode per a crea la taula de pilots a la BD
    public void creaTaulaPilots() throws DAOException {
        String checkTableExists = "BEGIN " +
                "   DECLARE " +
                "       t_count NUMBER; " +
                "   BEGIN " +
                "       SELECT COUNT(*) INTO t_count FROM user_tables WHERE table_name = 'PILOTS'; " +
                "       IF t_count = 0 THEN " +
                "           EXECUTE IMMEDIATE 'CREATE TABLE PILOTS ( " +
                "               ID NUMBER(10) NOT NULL, " +
                "               NOM VARCHAR2(50) NOT NULL, " +
                "               EDAD NUMBER(10) NOT NULL, " +
                "               MUNDIAL NUMBER(1) NOT NULL, " +
                "               PRIMARY KEY (ID) " +
                "           )'; " +
                "       END IF; " +
                "   END; " +
                "END;";

        try (FileInputStream input = new FileInputStream("/home/alumne/Documents/MP03/UF6/mp3-uf6-projecte-pcodorniu/PlantillaDAOMVCOracle/src/main/resources/database.properties")) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al carregar el fitxer de properties");
        }
        String url = properties.getProperty("ORACLE_URL");
        String user = properties.getProperty("ORACLE_USER");
        String password = properties.getProperty("ORACLE_PASSWORD");

        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement st = con.prepareStatement(checkTableExists);
        ) {
            st.execute();
        } catch (SQLException throwables) {
            throw new DAOException(1);
        }

    }

    //Mètode per a guardar un pilot a la BD
    //@Override
    public void insertPilot(PilotHardEnduro pilot) throws DAOException {

        try (FileInputStream input = new FileInputStream("/home/alumne/Documents/MP03/UF6/mp3-uf6-projecte-pcodorniu/PlantillaDAOMVCOracle/src/main/resources/database.properties")) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al carregar el fitxer de properties");
        }

        String url = properties.getProperty("ORACLE_URL");
        String user = properties.getProperty("ORACLE_USER");
        String password = properties.getProperty("ORACLE_PASSWORD");

        String sql = "INSERT INTO PILOTS (NOM, EDAD, MUNDIAL) VALUES (?, ?, ?)";

        try (Connection con = DriverManager.getConnection(url, user, password)) {
             PreparedStatement st = con.prepareStatement(sql, new String[]{"ID"});
            //No cal posar l'id perquè és autoincremental
            st.setString(1, pilot.getNom());
            st.setInt(2, pilot.getEdad());
            st.setInt(3, pilot.isPilotMundial() ? 1 : 0);

            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    Long id = rs.getLong(1);
                    pilot.setId(id);
                }
            }

            st.executeUpdate();

        } catch (SQLException throwables) {
            throw new DAOException(1);
        }

    }

    @Override
    public void save(PilotHardEnduro pilot) throws DAOException {
//        String sql = "INSERT INTO PILOTS (NOM, EDAD, MUNDIAL) VALUES (?, ?, ?)";
//
//        try (Connection con = DriverManager.getConnection(
//                "jdbc:oracle:thin:@//localhost:1521/xe",
//                "C##HR",
//                "HR"
//        );
//             PreparedStatement st = con.prepareStatement(sql);
//        ) {
//            //No cal posar l'id perquè és autoincremental
//            st.setString(1, pilot.getNom());
//            st.setDouble(2, pilot.getEdad());
//            st.setInt(3, pilot.isPilotMundial() ? 1 : 0);
//
//            st.executeUpdate();
//
//        } catch (SQLException throwables) {
//            throw new DAOException(1);
//        }
    }

    //Mètode per a actualitzar un pilot a la BD
    @Override
    public void update(PilotHardEnduro pilot) throws DAOException {

        try (FileInputStream input = new FileInputStream("/home/alumne/Documents/MP03/UF6/mp3-uf6-projecte-pcodorniu/PlantillaDAOMVCOracle/src/main/resources/database.properties")) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al carregar el fitxer de properties");
        }

        String url = properties.getProperty("ORACLE_URL");
        String user = properties.getProperty("ORACLE_USER");
        String password = properties.getProperty("ORACLE_PASSWORD");


        String sql = "UPDATE PILOTS SET NOM = ?, EDAD = ?, MUNDIAL = ? WHERE ID = ?";
        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement st = con.prepareStatement(sql);
        ) {
            st.setString(1, pilot.getNom());
            st.setInt(2, pilot.getEdad());
            st.setInt(3, pilot.isPilotMundial() ? 1 : 0);
            st.setLong(4, pilot.getId());

            st.executeUpdate();

        } catch (SQLException throwables) {
            throw new DAOException(1);
        }

    }

    //Mètode per a esborrar un pilot de la BD
    @Override
    public void delete(Long id) throws DAOException {

        try (FileInputStream input = new FileInputStream("/home/alumne/Documents/MP03/UF6/mp3-uf6-projecte-pcodorniu/PlantillaDAOMVCOracle/src/main/resources/database.properties")) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al carregar el fitxer de properties");
        }

        String url = properties.getProperty("ORACLE_URL");
        String user = properties.getProperty("ORACLE_USER");
        String password = properties.getProperty("ORACLE_PASSWORD");

        String sql = "DELETE FROM PILOTS WHERE ID = ?";

        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement st = con.prepareStatement(sql);
        ) {
            st.setLong(1, id);

            st.executeUpdate();

        } catch (SQLException throwables) {
            throw new DAOException(1);
        }
    }
}
