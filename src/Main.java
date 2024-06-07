package src;

public class Main {
    public static void main(String[] args) {
        Integer n_giocatori = 7;
    
        Mazzo mazzo = new Mazzo();
        XMLParser parser = new XMLParser();
        parser.leggi("src/assets/listaCarte.xml", mazzo);

        GestionePartita partita = new GestionePartita(n_giocatori, mazzo);

        partita.distribuisci2();
        while (true) {
            cls();
            partita.gioca();
        }

        
    }

    public static void cls() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
}