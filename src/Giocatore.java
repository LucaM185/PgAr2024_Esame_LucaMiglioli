package src;
import java.util.ArrayList;
import java.util.Random;

import javax.sound.midi.Soundbank;

import it.kibo.fp.lib.InputData;


public class Giocatore {
    Carta equipaggiata;
    Integer ID_Counter = 0;
    String nome;
    Integer PuntiFerita = 4;
    String nome_ruolo;
    ArrayList<Carta> carte = new ArrayList<Carta>();
    GestionePartita partita;

    public Giocatore(ArrayList<String> nomi_giocatori, String nome_ruolo, GestionePartita partita) {
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

    }

    public Integer getDistanza(){  // TODO SUPPORTO MUSTANG E MIRINO
        return 1;
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
        if (nome_ruolo.equals("Sceriffo")) {
            return "'Sceriffo " + nome + "'";
        }
        else{
            return "'" + nome + "'";
        }
    }

    public void pesca(Mazzo mazzo) {
        carte.add(mazzo.pesca());
    }

    public String getRuolo() {
        return nome_ruolo;
    }

    public void decrementaPF() {
        PuntiFerita--;
    }

    public void resetTurno() {
        
    }


    public void gioca(){
        Integer scelta = 0;
        // Mostra carte (con indici da 1 a n)
        System.out.println("Le tue carte sono: ");
        for (int i = 0; i < carte.size(); i++) {
            System.out.println("Carta " + (i+1) + ": " + carte.get(i));
        }
        
        // Chiedi che carta usare (con indici da 1 a n, -1 per uscire)
        scelta = InputData.readIntegerWithMaximum("Quale carta vuoi usare? (-1 per uscire): ", carte.size());
        if (scelta == -1){
            return;
        }
        scelta--;
        carte.get(scelta).usa(this);   
    }

    public void equipaggia(Carta carta) {
        equipaggiata = carta;
    }

    public void bang(){
        ArrayList<Giocatore> giocatori = this.partita.getGiocatoriADistanza(1, this);
        System.out.println("Scegli chi vuoi colpire: ");
        for (Giocatore giocatore : giocatori) {
            System.out.println(giocatore);
        }
        Integer scelta = InputData.readIntegerWithMaximum("Chi vuoi colpire? ", giocatori.size());
        giocatori.get(scelta).chiediMancato();
        
    }

    public void chiediMancato() {
        // Controlla se presente un mancato nelle carte
        Boolean mancato = false;
        for (Carta carta : carte) {
            if (carta.getNome().equals("Mancato!")){
                mancato = true;
            }
        }

        // seleziona mancato 
        if (mancato){
            System.out.println("Vuoi usare il mancato?");
            String risposta = InputData.readString("Rispondi si o no: ", true);
            if (risposta.equals("si")){
                // Rimuovi mancato dalle carte
                for (Carta carta : carte) {
                    if (carta.getNome().equals("Mancato")){
                        carte.remove(carta);
                        break;
                    }
                }
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
        this.pesca(partita.getMazzo());
        this.pesca(partita.getMazzo());
    }

    public void wellsFargo() {
        this.pesca(partita.getMazzo());
        this.pesca(partita.getMazzo());
        this.pesca(partita.getMazzo());
    }

    public void panico(){
        Integer dist = this.getDistanza();
    }

    public void catBalou(){
        partita.catBalou(this);
    }

    public void scartaCartaRandom() {
        int index = (int) (Math.random() * carte.size());
        carte.remove(index);
    }

    public void gatling() {
        this.partita.gatling(this);
    }
}


