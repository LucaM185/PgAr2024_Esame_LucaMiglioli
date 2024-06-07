package src;

public class Arma extends Carta{
    private String nome;
    private Integer distanza;
    private String seme;
    private String valore;

    public Arma(String nome, Integer distanza, String seme, String valore) {
        super(nome, "", false, seme, valore, distanza);
        this.nome = nome;
        this.distanza = distanza;
    }

    public String toString() {
        return "Nome: " + this.nome + " -  Distanza: " + this.distanza ;
    }
}
