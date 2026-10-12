package com.project.objectes;

public class Llibre {
    private int id;
    private String titol;
    private String autor;
    private int any;

    // Constructor per defecte (el necessiten les biblioteques de vinculació com Jackson)
    public Llibre() {
    }

    // Constructor amb paràmetres
    public Llibre(int id, String titol, String autor, int any) {
        this.id = id;
        this.titol = titol;
        this.autor = autor;
        this.any = any;
    }

    // Getters i setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitol() {
        return titol;
    }

    public void setTitol(String titol) {
        this.titol = titol;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getAny() {
        return any;
    }

    public void setAny(int any) {
        this.any = any;
    }

    // Sobreescriptura del mètode toString per a una representació més clara de l'objecte
    @Override
    public String toString() {
        return "Llibre{" +
                "id=" + id +
                ", titol='" + titol + '\'' +
                ", autor='" + autor + '\'' +
                ", any=" + any +
                '}';
    }
}
