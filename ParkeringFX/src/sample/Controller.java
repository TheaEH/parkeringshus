package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class Bil{
    public String bilNummer;
    public Date startTid;
    //public int plass;
    public boolean kortTid; // true = korttids parkering

    public Bil(String bilNummer, Date startTid, boolean kortTid) {
        // konstruktør
        this.bilNummer = bilNummer;
        this.startTid = startTid;
        this.kortTid = kortTid;
    }

    public String formaterKvittering(){
        // formater kvitteringen etter oppgitt format
        String ut = "";
        ut += "Kvittering for bilnr: " + this.bilNummer + "\n";
        Date now = new Date();
        String enkelStarttid = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(this.startTid);
        String enkelSluttid = new SimpleDateFormat("dd.MM.yyyy HH.mm").format(now);
        ut += "Tid parkert: " + enkelStarttid + " til " + enkelSluttid + "\n";
        ut += "Betalt " + this.avgift() + "kr";
        return ut;
    }

    public double getPris(){
        // returner 10 eller 20 kroner avhengig av kortTid eller ikke
        if (kortTid) {return 10.00;}
        else {return 20.00;}
    }
    public double avgift(){
        // regner ut tiden som er gått i timer og ganger med prisen
        Date now = new Date();
        long varighet = now.getTime() - startTid.getTime();
        int timer = (int) (varighet/3_600_000);
        return timer*getPris() + 15;
    }
}

class Parkeringshus{
    // opprett arrayet av biler
    public ArrayList<Bil> biler = new ArrayList<>();

    public void reserverPlass(Bil enBil){
        // legg bilen i arrayet
        biler.add(enBil);
    }

    public String frigjorPlass(String bilNummeret){
        /*
         ** må finne bilen i arrayet
         ** når den er funnet slett den fra arrayet
         ** og formater kvitteringen som returneres
         ** dersom bilen ikke finnes skal man returnere en passenede tekst
         */

        String kvittering;
        for (Bil element : biler) {
            if (element.bilNummer.equals(bilNummeret)) {
                kvittering = element.formaterKvittering();
                System.out.println(element.formaterKvittering());
                biler.remove(element);
                return kvittering;
            }
        }
        return "Det fins ingen biler i p-huset med dette skiltet";
    }
}


public class Controller {
    // opprett parkeringshuset
    Parkeringshus amfi = new Parkeringshus();

    @FXML
    private Label lblAvgift;

    @FXML
    private TextField txtBilnummer;

    @FXML
    void kjorUt(ActionEvent event) {
        // kall frigjør plass og legg ut kvitteringen i lblAvgift
        lblAvgift.setText(amfi.frigjorPlass(txtBilnummer.getText()));
    }

    @FXML
    void regKorttid(ActionEvent event) {
        // opprett en bil
        // og kall på reserver plass
        Date nu = new Date();
        Bil nyBil = new Bil(txtBilnummer.getText(), nu, true);
        amfi.reserverPlass(nyBil);
    }

    @FXML
    void regLangtid(ActionEvent event) {
        // opprett en bil
        // og kall på reserver plass
        Date nu = new Date();
        Bil nyBil = new Bil(txtBilnummer.getText(), nu, false);
        amfi.reserverPlass(nyBil);
    }
}
