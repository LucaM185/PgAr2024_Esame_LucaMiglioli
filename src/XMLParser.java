package src;
import javax.xml.stream.*;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class XMLParser {
    public XMLParser() {
    
    }

    public void leggi(String file, Mazzo mazzo, HashMap<String, String> personaggi_descritioni) {
        ArrayList<String> nomiruoli = new ArrayList<String>();
        ArrayList<Carta> carte = new ArrayList<Carta>();

        try {
            XMLInputFactory xmlif = null;
            XMLStreamReader xmlr = null;
            xmlif = XMLInputFactory.newInstance();
            xmlr = xmlif.createXMLStreamReader(file, new FileInputStream(file));
            Integer i = 0;
            while (xmlr.hasNext()) {
                i++;
                xmlr.next();
                if (xmlr.getEventType() == XMLStreamConstants.START_ELEMENT) {
                    String localname = xmlr.getLocalName();
                    // Ruolo
                    if (localname.equals("ruolo")) {
                        xmlr.next();
                        nomiruoli.add(xmlr.getText());
                    }
                    // Arma
                    else if (localname.equals("arma")) {
                        xmlr.next();
                        xmlr.next();
                        xmlr.next();                        
                        String nome = xmlr.getText();
                        xmlr.next();
                        xmlr.next();
                        xmlr.next();
                        xmlr.next();
                        Integer distanza = Integer.parseInt(xmlr.getText());
                    
                        // Finche non si chiude arma
                        String valore = "";
                        while (xmlr.hasNext()) {  // LEGGISEMEVALORE
                            if (xmlr.getEventType() == XMLStreamConstants.END_ELEMENT && xmlr.getLocalName().equals("arma")) {
                                break;
                            }
                            if (xmlr.getEventType() == XMLStreamConstants.START_ELEMENT) {
                                if (xmlr.getLocalName().equals("valore")) {
                                    xmlr.next();
                                    valore = xmlr.getText();
                                }
                                else if (xmlr.getLocalName().equals("seme")) {
                                    xmlr.next();
                                    String seme = xmlr.getText();
                                    Arma arma = new Arma(nome, distanza, seme, valore);
                                    carte.add(arma);
                                }

                            }
                            xmlr.next();
                        }
                    }
                    else if (localname.equals("personaggio")){
                        // get attribute pf 
                        Integer pf = Integer.parseInt(xmlr.getAttributeValue(null, "pf"));
                        String nome = "";
                        String descrizione = "";
                        // Get nome and descrizione one after the other easy
                        xmlr.next();
                        xmlr.next();
                        xmlr.next();
                        nome = xmlr.getText();
                        xmlr.next();
                        xmlr.next();
                        xmlr.next();
                        xmlr.next();
                        descrizione = xmlr.getText();
                        personaggi_descritioni.put(nome, descrizione);
                    }
                    else if (localname.equals("carta")) {
                        // check equipaggiabile <carta equipaggiabile="false">
                        Boolean equipaggiabile = xmlr.getAttributeValue(null, "equipaggiabile").equals("true");
                        String nome = "";

                        while (xmlr.hasNext()) {
                            if (xmlr.getEventType() == XMLStreamConstants.END_ELEMENT && xmlr.getLocalName().equals("carta")) {
                                break;
                            }
                            if (xmlr.getEventType() == XMLStreamConstants.START_ELEMENT) {
                                if (xmlr.getLocalName().equals("nome")) {
                                    xmlr.next();
                                    nome = xmlr.getText();
                                    
                                    xmlr.next();
                                    xmlr.next();
                                    xmlr.next();
                                    xmlr.next();
                                    String descrizione = xmlr.getText();
                                    String valore = "";
                                    while (xmlr.hasNext()) {
                                        if (xmlr.getEventType() == XMLStreamConstants.END_ELEMENT && xmlr.getLocalName().equals("carta")) {
                                            break;
                                        }
                                        if (xmlr.getEventType() == XMLStreamConstants.START_ELEMENT) {
                                            if (xmlr.getLocalName().equals("valore")){
                                                xmlr.next();
                                                valore = xmlr.getText();
                                            }
                                            else if (xmlr.getLocalName().equals("seme")) {
                                                xmlr.next();
                                                String seme = xmlr.getText();
                                                Carta carta = new Carta(nome, descrizione, equipaggiabile, seme, valore, 1);
                                                carte.add(carta);
                                            }
                                        }
                                        xmlr.next();
                                    }                                    
                                }
                            }
                            xmlr.next();
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Errore nell'inizializzazione del parser:");
            System.out.println(e.getMessage());
        }

        mazzo.setCarte(carte);
        
    }

}