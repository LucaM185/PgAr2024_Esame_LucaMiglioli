package src;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.catalog.Catalog;

import it.kibo.fp.lib.InputData;


public class Giocatore {
    Carta equipaggiata = new Carta("placeholder", "", false, "", "", 1);
    Integer ID_Counter = 0;
    String nome;
    Integer PuntiFerita = 4;
    String nome_ruolo;
    ArrayList<Carta> carte = new ArrayList<Carta>();
    GestionePartita partita;
    Boolean bangUtilizzato = false;
    String personaggio;
    Integer PFMax = 0;

    public Giocatore(ArrayList<String> nomi_giocatori, String nome_ruolo, String personaggio, GestionePartita partita) {
        this.personaggio = personaggio;
        
        // Prendi e rimuovi un nome RANDOM
        Random rand = new Random();
        Integer index = rand.nextInt(nomi_giocatori.size());
        nome = nomi_giocatori.get(index);
        nomi_giocatori.remove(nome);
        
        // Assegna l'ID 
        Integer ID = ID_Counter;
        ID_Counter++;
        
        this.nome_ruolo = nome_ruolo;
        this.partita = partita;
        this.bangUtilizzato = false;

        if (personaggio.equals("El Gringo") || personaggio.equals("Paul Regret")){
            PuntiFerita = 3;
        }

        if (nome_ruolo.equals("Sceriffo")){
            PuntiFerita++;
        }
        this.PFMax = PuntiFerita;
    }

    public String getPersonaggio(){
        return this.personaggio;
    }

    public Carta getEquipaggiata(){
        return equipaggiata;
    }

    public Integer getDistanza(){  
        // If equipaggiata has distanza, return it
        Integer dist = 1;
        if (personaggio.equals("Rose Doolan")){
            dist++;
        }
        if (equipaggiata.getDistanza() != null){
            dist = equipaggiata.getDistanza();
        }
        if (equipaggiata.getNome().equals("Mirino")){
            return dist+1;
        }
        return dist;
    }

    public void incrementaPF(){
        PuntiFerita++;
    }

    public Integer getPF() {
        return PuntiFerita;
    }

    public String getNome() {
        return nome;
    }

    public String toString() {
        // if sceriffo
        String close = "'";
        close = close + " (" + PuntiFerita + ")";
        if (!equipaggiata.getNome().equals("placeholder"))
            close = close + " - " + equipaggiata.getNome();

        if (nome_ruolo.equals("Sceriffo")) {
            return "'Sceriffo " + nome + close ;
        }
        else{
            return "'" + nome + close;
        }
    }

    public void pesca() {
        Mazzo mazzo = partita.getMazzo();
        Carta pescata = mazzo.pesca();
        Boolean giapresente = false;
        for (Carta carta : carte) {
            if (carta.getNome().equals(pescata.getNome())){
                giapresente = true;
            }
        }
        if (!giapresente){
            carte.add(pescata);
        }
        else {
            mazzo.scarta(pescata);
            this.pesca();
        }
    }


    public String getRuolo() {
        return nome_ruolo;
    }

    public void decrementaPF(Giocatore attaccante) {
        if (this.personaggio.equals("Bart Cassidy") && PuntiFerita > 0){
            this.pesca();
        }
        if (this.personaggio.equals("El Gringo")){
            if (attaccante.getCarte().size() != 0){
                carte.add(attaccante.getRandomCarta());
            }
        }

        PuntiFerita--;
    }


    public void resetTurno() {
        this.bangUtilizzato = false;
    }

    public ArrayList<Carta> getCarte(){
        return carte;
    }

    public void scegliCartaDaScartare() {
        System.out.println("Le tue carte sono: ");
        for (int i = 0; i < carte.size(); i++) {
            System.out.println("Carta " + (i+1) + ": " + carte.get(i));
        }

        Integer scelta = InputData.readIntegerWithMaximum("Quale carta vuoi scartare? ", carte.size());
        scelta--;
        scarta(carte.get(scelta));
    }

    public Integer gioca(){
        Integer scelta = 0;
        if (carte.size() > this.PuntiFerita){
            System.out.println("RICORDA: Non puoi avere più carte dei punti ferita, se fai -1 dovrai scartare!");
        }

        // Mostra carte (con indici da 1 a n)
        System.out.println("Le tue carte sono: ");
        for (int i = 0; i < carte.size(); i++) {
            System.out.println("Carta " + (i+1) + ": " + carte.get(i));
        }
        
        // Chiedi che carta usare (con indici da 1 a n, -1 per uscire)
        scelta = InputData.readIntegerWithMaximum("Quale carta vuoi usare? (-1 per uscire): ", carte.size());
        if (scelta == -1){
            return -1;
        }
        scelta--;
        carte.get(scelta).usa(this); 
        if (carte.size() == 0){
            pesca();
        }
        return 0;  
    }

    public boolean isBangUtilizzato() {
        return bangUtilizzato;
    }

    public void equipaggia(Carta carta) {
        equipaggiata = carta;
    }

    public void bang(Giocatore attaccante){
        this.bangUtilizzato = true;
        ArrayList<Giocatore> giocatori = this.partita.getGiocatoriADistanza(getDistanza(), this);
        System.out.println();
        System.out.println("Scegli chi vuoi colpire: ");
        for (int i = 0; i < giocatori.size(); i++) {
            System.out.println((i+1) + ": " + giocatori.get(i));
        }
        Integer scelta = InputData.readIntegerWithMaximum("Chi vuoi colpire? ", giocatori.size());
        giocatori.get(scelta-1).chiediMancato(attaccante);
        
    }

    public void chiediMancato(Giocatore attaccante) {
        if (this.getEquipaggiata().getNome().equals("Barile")){
            Carta pescata = this.partita.getMazzo().pesca();
            if (pescata.getSeme().equals("CUORI")){
                System.out.println("Hai pescato " + pescata + " di " + pescata.getSeme() + "quindi il barile ti ha salvato!! ");
                return;
            }
        }
        if (this.personaggio.equals("Jourdonnais")){
            Carta pescata = this.partita.getMazzo().pesca();
            if (pescata.getSeme().equals("CUORI")){
                System.out.println("Hai pescato " + pescata + " di " + pescata.getSeme() + "quindi il barile ti ha salvato!! ");
                return;
            }
        }

        // Controlla se presente un mancato nelle carte
        Boolean mancato = false;
        for (Carta carta : carte) {
            if (carta.getNome().equals("Mancato!")){
                mancato = true;
            }
        }

        if (!mancato){
            this.decrementaPF(attaccante);
        }

        // seleziona mancato 
        if (mancato){
            System.out.println("Vuoi usare il mancato?"); 
            System.out.println("1: Si");
            System.out.println("2: No");
            Integer risposta = InputData.readIntegerWithMaximum("Rispondi: ", 2);
            if (risposta == 1){
                for (Carta carta : carte) {
                    if (carta.getNome().equals("Mancato")){
                        carte.remove(carta);
                        break;
                    }
                }
            }else{
                this.decrementaPF(attaccante);
            }
        }
    }

    public void birra() {
        PuntiFerita++;
    }

    public void saloon() {
        partita.saloon();
    }

    public void diligenza() {
        this.pesca();
        this.pesca();
    }

    public void wellsFargo() {
        this.pesca();
        this.pesca();
        this.pesca();
    }

    public void panico(){
        Integer dist = this.getDistanza();
    }

    public void catBalou(){  // TODO aggiungere scelta carta invece che random
        partita.catBalou(this);
    }

    public void scartaCartaRandom() {
        int index = (int) (Math.random() * carte.size());
        carte.remove(carte.get(index));
    }

    public void gatling() {
        this.partita.gatling(this);
    }

    public void rimuoviCarta(Carta carta) {
        carte.remove(carta);    
    }

    public void scarta(Carta carta) {
        this.partita.getMazzo().scarta(carta);
        carte.remove(carta);
    }

    public Carta getRandomCarta() {
        int index = (int) (Math.random() * carte.size());
        Carta carta = carte.get(index);
        carte.remove(carta);
        return carta;
    }
}


