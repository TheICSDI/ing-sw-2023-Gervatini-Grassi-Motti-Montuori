package Cards;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import Tile.*;

public class PersonalCard implements Card
{
    private int id;       //each card has unique id
    public Tile[][] card;

    /*
    All'inizio della partita vengono generate #giocatori interi DIVERSI che tramite questo costruttore crea la carta
    dalla pool di carte presenti nel file personal_card.json per poi assegnarli al giocatore
     */
    public PersonalCard(int uid)
    {
        this.id = uid;
        this.card = new Tile[6][5]; //fixed size

        personalCardParser();
    }
    private void personalCardParser()   //using the json file
    {

        JSONParser parser = new JSONParser();
        JSONArray personalCardFile = null;
        Tile temp;

        try
        {
            FileInputStream pathFile = new FileInputStream("JSON/personal_card.json");
            personalCardFile = (JSONArray) parser.parse(new InputStreamReader(pathFile));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

        JSONObject card = (JSONObject) personalCardFile.get(this.id);
        JSONArray cardTiles = (JSONArray) card.get("tiles");
        for(int j = 0; j < cardTiles.size(); j++)
        {
            JSONObject coordinate = (JSONObject) cardTiles.get(j);
            temp = new Tile(coordinate.get("type").toString());
            this.card[Integer.parseInt(coordinate.get("x").toString())][Integer.parseInt(coordinate.get("y").toString())] = temp;
        }
    }
    public int calculatePoints(){return 0;}
	/*
	da fare una serie di check tra la carta e la matrice della carta e la library del giocatore
	per controlare quante sono uguale e assegnare il punteggio.
	Probabilmente possiamo fare a meno della matrice e usare una rappresentazione come nel fie
	Json ma è da vedere
	 */


}
