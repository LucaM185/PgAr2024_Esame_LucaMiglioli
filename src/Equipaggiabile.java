package src;

public class Equipaggiabile extends Carta{
    private String nome;
    private Integer distanza;
    private String seme;
    private String valore;

    public Equipaggiabile(String nome, Integer distanza) {
        super(nome, "", false, "", "");
        this.nome = nome;
        this.distanza = distanza;
    }

    public void setSemeValore(String seme, String valore) {
        this.seme = seme;
        this.valore = valore;
    }

    public String toString() {
        return "Nome: " + this.nome + " Distanza: " + this.distanza + " Seme: " + this.seme + " Valore: " + this.valore;
    }
}
