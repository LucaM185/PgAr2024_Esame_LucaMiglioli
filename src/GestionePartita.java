package src;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import it.kibo.fp.lib.InputData;

public class GestionePartita {
    private ArrayList<Giocatore> ruoli;
    Integer posizione = 0;
    private Mazzo mazzo;

    public GestionePartita(Integer n_giocatori, Mazzo mazzo) {
        // Creo il mazzo
        this.mazzo = mazzo;

        // Creo i giocatori
        ArrayList<String> nomi_giocatori = new ArrayList<String>();
        for (int i = 0; i < n_giocatori; i++) {
            nomi_giocatori.add(InputData.readString("Inserisci il tuo nome: ", true));
        }
        ArrayList<String> nomi_giocatori_copia = new ArrayList<String>(nomi_giocatori);

        // Assegno i ruoli
        this.ruoli = new ArrayList<Giocatore>();
        this.ruoli.add(new Giocatore(nomi_giocatori, "Sceriffo", this));
        this.ruoli.add(new Giocatore(nomi_giocatori, "Rinnegato", this));
        this.ruoli.add(new Giocatore(nomi_giocatori, "Fuorilegge", this));
        this.ruoli.add(new Giocatore(nomi_giocatori, "Fuorilegge", this));
        
        switch (n_giocatori) {
            case 5:
                this.ruoli.add(new Giocatore(nomi_giocatori, "Vice", this));
                break;
            case 6:
                this.ruoli.add(new Giocatore(nomi_giocatori, "Vice", this));
                this.ruoli.add(new Giocatore(nomi_giocatori, "Fuorilegge", this));
                break;
            case 7:
                this.ruoli.add(new Giocatore(nomi_giocatori, "Vice", this));
                this.ruoli.add(new Giocatore(nomi_giocatori, "Fuorilegge", this));
                this.ruoli.add(new Giocatore(nomi_giocatori, "Vice", this));
                break;
        }

        // Riordina secondo nomi_giocatori_copia
        ArrayList<Giocatore> ruoli_ordinati = new ArrayList<Giocatore>();
        for (String nome : nomi_giocatori_copia) {
            for (Giocatore ruolo : this.ruoli) {
                if (ruolo.getNome().equals(nome)) {
                    ruoli_ordinati.add(ruolo);
                }
            }
        }
        this.ruoli = ruoli_ordinati;
    }

    public int getIndexSceriffo() {
        for (int i = 0; i < this.ruoli.size(); i++) {
            if (this.ruoli.get(i).getRuolo().equals("Sceriffo")){
                return i;
            }
        }
        return -1;
    }

    public void VisualizzaGiocatori() {
        System.out.println("I giocatori sono: ");
        for (Giocatore ruolo : this.ruoli) {
            System.out.println("" + ruolo + " ");
        }
    }

    public void distribuisci2() {
        // Assegna carte
        for (Giocatore ruolo : this.ruoli) {
            ruolo.pesca(mazzo);
            ruolo.pesca(mazzo);
        }
    }

    public void gioca(){
        Giocatore giocatore = this.ruoli.get(posizione);
        VisualizzaGiocatori();
        System.out.println("\n\nE' il turno di " + giocatore);
        System.out.println("Hai " + giocatore.getPF() + " punti ferita");
;
        giocatore.resetTurno();
        
        giocatore.pesca(mazzo);
        giocatore.pesca(mazzo);

        
        while (giocatore.gioca() != -1)
        while (giocatore.getCarte().size() > giocatore.getPF()) {
            System.out.println("\nHai troppe carte, scartane una");
            giocatore.scegliCartaDaScartare();
        }


        posizione = (posizione - 1 + this.ruoli.size()) % this.ruoli.size();
    }

    public ArrayList<Giocatore> getGiocatoriADistanza(Integer distanza, Giocatore giocatore) {
        ArrayList<Giocatore> giocatori = new ArrayList<Giocatore>();
        Integer posizione_giocatore = this.ruoli.indexOf(giocatore);
        for (int i = posizione_giocatore-distanza; i <= posizione_giocatore+distanza; i++) {
            Integer idx = i;
            idx = (idx + this.ruoli.size()) % this.ruoli.size();
            if (idx != posizione_giocatore) {    
                // Controlla che non ci siano Mustang
                if (this.ruoli.get(idx).getEquipaggiata().getNome().equals("Mustang")) {
                    if (distanza >= Math.abs(idx - posizione_giocatore)+1) {
                        giocatori.add(this.ruoli.get(idx));
                    }
                } else {
                    giocatori.add(this.ruoli.get(idx));
                    
                }
            }
        }
        return giocatori;
    }
    
    public void saloon() {
        for (Giocatore ruolo : this.ruoli) {
            ruolo.incrementaPF();
        }
    }

    public Mazzo getMazzo(){
        return this.mazzo;
    }

    public void catBalou(Giocatore player){
        // Scarta carta random eccetto player
        ArrayList<Giocatore> giocatorieccettoplayer = new ArrayList<Giocatore>();
        
        for (Giocatore giocatore : this.ruoli) {
            if (!giocatore.equals(player)) {
                giocatorieccettoplayer.add(giocatore);
            }
        }

        int index = (int) (Math.random() * giocatorieccettoplayer.size());
        Giocatore giocatore = giocatorieccettoplayer.get(index);

        giocatore.scartaCartaRandom();
    }

    public void gatling(Giocatore player){
        // Scarta carta random eccetto player
        ArrayList<Giocatore> giocatorieccettoplayer = new ArrayList<Giocatore>();
        
        for (Giocatore giocatore : this.ruoli) {
            if (!giocatore.equals(player)) {
                giocatorieccettoplayer.add(giocatore);
            }
        }

        for (Giocatore giocatore : giocatorieccettoplayer) {
            giocatore.bang();      
        }

    }
}
