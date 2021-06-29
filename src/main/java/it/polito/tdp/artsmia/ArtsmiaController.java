package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.ResourceBundle;

import java.util.*;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.Artista;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {
	
	private Model model ;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnArtistiConnessi;

    @FXML
    private Button btnCalcolaPercorso;

    @FXML
    private ComboBox<String> boxRuolo;

    @FXML
    private TextField txtArtista;

    @FXML
    private TextArea txtResult;

    @FXML
    void doArtistiConnessi(ActionEvent event) {
    	if(this.model.isSetGrafo()) {
    		List<Adiacenza> result = this.model.getCongiutni();
    		if(result.size()==0) {
    			this.txtResult.setText("non vi sono artisti connessi!\n");
    			return;
    		}
    		this.txtResult.setText("il numero di artista connessi é "+ result.size()+" ed essi sono\n");
    		for(Adiacenza a : result) {
    			this.txtResult.appendText(a.toString());
    		}
    	}else {
    		this.txtResult.setText("CREA UN GRAFO !\n");
    	}
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	Integer id =null;
    	try { 
    		id = Integer.parseInt(this.txtArtista.getText());
    	}catch(NumberFormatException nfe) {
    		this.txtArtista.setText("inserire un numero come id artista\n");
    		return ;
    	}
    	Artista a = this.model.getActor(id);
    	if(a != null) {
    		List<Artista> result = this.model.getPercorso(a);
    		if(result.size()==0) {
    			this.txtResult.setText("il nodo é un nodo isolato\n");
    			return ;
    		}
    		this.txtResult.appendText("il numero di esposizioni per cui il percorso risulta massimo é : "+result.size());
    		this.txtResult.appendText("\npartendo da "+a.toString()+" si arriva con tale cammiono a:\n");
    		
    		for(Artista r : result ) {
    			this.txtResult.appendText(r.toString()+"\n");
    		}
    	}else {
    		this.txtResult.setText("inserire un numero come id Valido \n");
    		return ;
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	String ruolo = this.boxRuolo.getValue();
    	if(ruolo == null) {
    		this.txtResult.setText("Selezionare un ruolo!\n");
    		return ;
    	}
    	this.model.creaGrafo(ruolo);
    	this.txtResult.setText(this.model.infoGrafo());
    	
    	
    }

    public void setModel(Model model) {
    	this.model = model;
    	this.boxRuolo.getItems().addAll(this.model.getRuoli());
    }

    
    @FXML
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnArtistiConnessi != null : "fx:id=\"btnArtistiConnessi\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert boxRuolo != null : "fx:id=\"boxRuolo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtArtista != null : "fx:id=\"txtArtista\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

    }
}
