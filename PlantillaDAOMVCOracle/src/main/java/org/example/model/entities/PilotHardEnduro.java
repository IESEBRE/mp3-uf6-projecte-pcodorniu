package org.example.model.entities;

import java.util.Collection;
import java.util.TreeSet;

//Classe que representa un pilot de Hard Enduro
public class PilotHardEnduro {

    //Atributs
    private Long id;
    private String nom;
    private int edad;
    private boolean pilotMundial;

    //Col·lecció de competicions
    private Collection<Competicio> competicions;

    //Constructors
    public PilotHardEnduro(){}

    public PilotHardEnduro(String nom, int edad, boolean pilotMundial, Collection<Competicio> competicions) {
        this.nom = nom;
        this.edad = edad;
        this.pilotMundial = pilotMundial;
        this.competicions = competicions;
    }

    public PilotHardEnduro(Long id, String nom) {
        this.id = id;
        this.nom = this.nom;
    }

    public PilotHardEnduro(long id, String nom, int edad) {
        this.id = id;
        this.nom = nom;
        this.edad = edad;
    }

    public PilotHardEnduro(long id, String nom, int edad, boolean pilotMundial, TreeSet<Competicio> competicions) {
        this.id = id;
        this.nom = nom;
        this.edad = edad;
        this.pilotMundial = pilotMundial;
        this.competicions = competicions;
    }
    //Getters i setters
    public Collection<Competicio> getCompeticions() {
        return competicions;
    }

    private void setCompeticions(Collection<Competicio> competicions) {
        this.competicions = competicions;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public boolean isPilotMundial() {
        return pilotMundial;
    }

    public void setPilotMundial(boolean pilotMundial) {
        this.pilotMundial = pilotMundial;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    //Mètode per afegir una competició
    public static class Competicio implements Comparable<Competicio>{

        @Override
        public int compareTo(Competicio o) {return this.campeonat.compareTo(o.getCampeonat());
        }
        //Enum amb els diferents campeonats
        public static enum Campeonat {
            //Campeonats de Hard Enduro
            WESS,
            RedBullRomaniacs,
            ErzbergRodeo,
            HixpaniaHardEnduro,
            SeaToSky,
            RedBullTKO,
            RedBull111Megawatt,
            RedBullMinasRiders,
            RedBullOutliers,
            RedBullBrasil,
            RedBullMacedonia,
            RedBullPortugal,
            RedBullFrance,
            RedBullGermany,
            RedBullAustria,
            RedBullCzechRepublic,

        }

        //Atributs
        private Competicio.Campeonat campeonat;
        private int victories;

        public Competicio(Campeonat campeonat, int victories) {
            this.campeonat = campeonat;
            this.victories = victories;
        }

        //Getters i setters
        public Competicio.Campeonat getCampeonat() {
            return campeonat;
        }

        public void setCampeonat(Competicio.Campeonat campeonat) {
            this.campeonat = campeonat;
        }

        public int getVictories() {
            return victories;
        }
        public void setVictories(int victories) {
            this.victories = victories;
        }
    }


}

