package src;

public class Carta {
    private String nome;
    private String descrizione;
    private Boolean equipaggiabile;
    private String seme;
    private String valore;
    private Integer distanza;
    

    public Carta(String nome, String descrizione, Boolean equipaggiabile, String seme, String valore, Integer distanza) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.equipaggiabile = equipaggiabile;
        this.seme = seme;
        this.valore = valore;
        this.distanza = distanza;
    }

    public Integer getDistanza(){
        return this.distanza;
    }

    public String getNome(){
        return this.nome;
    }

    public String getSeme(){
        return this.seme;
    }

    public void usa(Giocatore giocatore){
        System.out.println("Hai usato la carta: " + this.nome );
        if (this.equipaggiabile){
            giocatore.equipaggia(this);
        }
        if (this.nome.equals("BANG!")){
            giocatore.bang();
        }
        if (this.nome.equals("Mancato")){
            System.out.println("Non puoi usare il mancato in questo momento");
            return;
        }
        if (this.nome.equals("Birra")){
            giocatore.birra();
        }
        if (this.nome.equals("Saloon")){
            giocatore.saloon();
        }
        if (this.nome.equals("Diligenza")){
            giocatore.diligenza();
        }
        if (this.nome.equals("Wells Fargo")){
            giocatore.wellsFargo();
        }
        if (this.nome.equals("Panico!")){
            giocatore.panico();
        }
        if (this.nome.equals("Cat Balou")){
            giocatore.catBalou();
        }
        if (this.nome.equals("Gatling")){
            giocatore.gatling();
        }
        
        giocatore.scarta(this);
    }

    public String toString(){
        return "Nome: " + this.nome;
    }

}
