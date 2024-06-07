package src;

import java.util.ArrayList;

public class Mazzo {
    private ArrayList<Carta> carte;
    private ArrayList<Carta> scarti;

    public Mazzo() {
        this.carte = new ArrayList<Carta>();
        this.scarti = new ArrayList<Carta>();
    }

    public void mescola() {
        //TODO Auto-generated method stub
    }

    public Carta pesca() {
        // random index from 0 to carte.size()
        int index = (int) (Math.random() * this.carte.size());
        Carta carta = this.carte.get(index);
        return carta;
    }

    public void setCarte(ArrayList<Carta> cards) {
        this.carte = cards;
    }

    public ArrayList<Carta> getCarte() {
        return this.carte;
    }
    
    public String printlen() {
        return "Il mazzo contiene: " + carte.size() + " carte";
    }


}
