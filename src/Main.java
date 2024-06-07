package src;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        Integer n_giocatori = 7;
    
        Mazzo mazzo = new Mazzo();
        XMLParser parser = new XMLParser();
        HashMap<String, String> personaggi_descrizioni = new HashMap<String, String>();
        parser.leggi("src/assets/listaCarte.xml", mazzo, personaggi_descrizioni);
        
        GestionePartita partita = new GestionePartita(n_giocatori, mazzo, personaggi_descrizioni);
        
        partita.distribuisci2();
        while (true) {
            cls();
            partita.gioca();
            if (partita.getRuoli().size() == 1){
                System.out.println("Il vincitore è: " + partita.getRuoli().get(0).getNome() + " con il ruolo di " + partita.getRuoli().get(0).getRuolo() + "!");
                break;
            }
        }

        
    }

    public static void cls() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
}