package src;

public class Main {
    public static void main(String[] args) {
        Integer n_giocatori = 4;
    
        Mazzo mazzo = new Mazzo();
        XMLParser parser = new XMLParser();
        parser.leggi("src/assets/listaCarte.xml", mazzo);

        GestionePartita partita = new GestionePartita(n_giocatori, mazzo);
        partita.VisualizzaGiocatori();

        while (true) {
            partita.gioca();
        }
    }
}