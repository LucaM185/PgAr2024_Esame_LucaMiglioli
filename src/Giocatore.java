package src;
import java.util.ArrayList;
import java.util.Random;
import it.kibo.fp.lib.InputData;


public class Giocatore {
    Carta equipaggiata = new Carta("placeholder", "", false, "", "", 1);
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

        if (nome_ruolo.equals("Sceriffo")){
            PuntiFerita = 5;
        }

    }

    public Carta getEquipaggiata(){
        return equipaggiata;
    }

    public Integer getDistanza(){  
        // If equipaggiata has distanza, return it
        Integer dist = 1;
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

    public void pesca(Mazzo mazzo) {
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
            this.pesca(mazzo);
        }
    }

    public String getRuolo() {
        return nome_ruolo;
    }

    public void decrementaPF() {
        PuntiFerita--;
    }

    public void resetTurno() {
        
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
        return 0;  
    }

    public void equipaggia(Carta carta) {
        equipaggiata = carta;
    }

    public void bang(){
        ArrayList<Giocatore> giocatori = this.partita.getGiocatoriADistanza(1, this);
        System.out.println();
        System.out.println("Scegli chi vuoi colpire: ");
        for (int i = 0; i < giocatori.size(); i++) {
            System.out.println((i+1) + ": " + giocatori.get(i));
        }
        Integer scelta = InputData.readIntegerWithMaximum("Chi vuoi colpire? ", giocatori.size());
        giocatori.get(scelta-1).chiediMancato();
        
    }

    public void chiediMancato() {
        if (this.getEquipaggiata().getNome().equals("Barile")){
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
            this.decrementaPF();
        }

        // seleziona mancato 
        if (mancato){
            String risposta = "";
            System.out.println("Vuoi usare il mancato?"); 
            risposta = InputData.readString("Rispondi si o no: ", true);
            if (risposta.equals("si")){
                for (Carta carta : carte) {
                    if (carta.getNome().equals("Mancato")){
                        carte.remove(carta);
                        break;
                    }
                }
            }else{
                this.decrementaPF();
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
}


