package src;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import it.kibo.fp.lib.InputData;

public class GestionePartita {
    private ArrayList<Giocatore> ruoli;
    Integer posizione = 0;
    private Mazzo mazzo;
    private HashMap<String, String> personaggi_descrizioni = new HashMap<String, String>();
    private ArrayList<String> nomi;

    public GestionePartita(Integer n_giocatori, Mazzo mazzo, HashMap<String, String> personaggi_descrizioni) {  
        this.personaggi_descrizioni = personaggi_descrizioni;
        this.nomi = new ArrayList<String>(this.personaggi_descrizioni.keySet());

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
        this.ruoli.add(new Giocatore(nomi_giocatori, "Sceriffo", getNomePers(), this));
        this.ruoli.add(new Giocatore(nomi_giocatori, "Rinnegato", getNomePers(), this));
        this.ruoli.add(new Giocatore(nomi_giocatori, "Fuorilegge", getNomePers(), this));
        this.ruoli.add(new Giocatore(nomi_giocatori, "Fuorilegge", getNomePers(), this));
        
        switch (n_giocatori) {
            case 5:
                this.ruoli.add(new Giocatore(nomi_giocatori, "Vice", getNomePers(), this));
                break;
            case 6:
                this.ruoli.add(new Giocatore(nomi_giocatori, "Vice", getNomePers(), this));
                this.ruoli.add(new Giocatore(nomi_giocatori, "Fuorilegge", getNomePers(), this));
                break;
            case 7:
                this.ruoli.add(new Giocatore(nomi_giocatori, "Vice", getNomePers(), this));
                this.ruoli.add(new Giocatore(nomi_giocatori, "Fuorilegge", getNomePers(), this));
                this.ruoli.add(new Giocatore(nomi_giocatori, "Vice", getNomePers(), this));
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
    
    public String getNomePers() {
        Integer random_num = (int) (Math.random() * nomi.size());
        String nome_personaggio = nomi.get(random_num);
        nomi.remove(nome_personaggio);
        return nome_personaggio;
    }

    public int getIndexSceriffo() {
        for (int i = 0; i < this.ruoli.size(); i++) {
            if (this.ruoli.get(i).getRuolo().equals("Sceriffo")){
                return i;
            }
        }
        return -1;
    }

    public ArrayList<Giocatore> getRuoli(){
        return this.ruoli;
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
            ruolo.pesca();
            ruolo.pesca();
        }
    }

    public void gioca(){
        Giocatore giocatore = this.ruoli.get(posizione);
        ControllaMorti();
        VisualizzaGiocatori();
        System.out.println("\n\nE' il turno di " + giocatore);
        System.out.println("Hai " + giocatore.getPF() + " punti ferita");
;
        giocatore.resetTurno();
        
        giocatore.pesca();
        giocatore.pesca();

        
        while (giocatore.gioca() != -1)
        if (giocatore.getPersonaggio().equals("Sid Ketchum")) {
            System.out.println("Hai Sid Ketchum, vuoi scartare 2 carte per recuperare un punto ferita?");
            System.out.println("1. Si");
            System.out.println("2. No");
            Integer scelta = InputData.readIntegerWithMaximum("Scelta: ", 2);
            if (scelta == 1) {
                giocatore.scegliCartaDaScartare();
                giocatore.scegliCartaDaScartare();
                giocatore.incrementaPF();
            }
        }
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
                Integer mustangs = 0;
                if (this.ruoli.get(idx).getEquipaggiata().getNome().equals("Mustang"))
                    mustangs++; 
                if (this.ruoli.get(idx).getPersonaggio().equals("Paul Regret"))
                    mustangs++;
                if (distanza >= Math.abs(idx - posizione_giocatore)+mustangs) {
                    if (!giocatori.contains(this.ruoli.get(idx))){
                        giocatori.add(this.ruoli.get(idx));
                    }
                } else {
                    if (!giocatori.contains(this.ruoli.get(idx))){
                        giocatori.add(this.ruoli.get(idx));          
                    }      
                }
            }
        }
        

        return giocatori;
    }

    public void ControllaMorti() {
        for (Giocatore ruolo : this.ruoli) {
            if (ruolo.getPF() <= 0) {
                System.out.println("Il giocatore " + ruolo.getNome() + " è morto");
                this.ruoli.remove(ruolo);
            }
        }
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
            giocatore.bang(player);      
        }

    }
}
