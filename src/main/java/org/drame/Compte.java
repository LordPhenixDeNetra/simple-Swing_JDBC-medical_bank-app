package org.drame;

public class Compte {

    private int numeroCompte;
    private double solde;
    private String prenom;
    private String nom;
    private double decouvert;


    public Compte(int numeroCompte, double solde, String prenom, String nom, double decouvert) {
        this.numeroCompte = numeroCompte;
        this.solde = solde;
        this.prenom = prenom;
        this.nom = nom;
        this.decouvert = decouvert;
    }

    public Compte() {
    }


    public int getNumeroCompte() {
        return numeroCompte;
    }

    public void setNumeroCompte(int numeroCompte) {
        this.numeroCompte = numeroCompte;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getDecouvert() {
        return decouvert;
    }

    public void setDecouvert(double decouvert) {
        this.decouvert = decouvert;
    }

    @Override
    public String toString() {
        return "Compte{" +
                "numeroCompte=" + numeroCompte +
                ", solde=" + solde +
                ", prenom='" + prenom + '\'' +
                ", nom='" + nom + '\'' +
                ", decouvert=" + decouvert +
                '}';
    }
}
