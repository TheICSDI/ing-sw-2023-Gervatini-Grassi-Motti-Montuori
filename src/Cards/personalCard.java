package Cards;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import src.Tiles.*;  // for now ".*" and I don't understand how to import a class in his parent folder
public class personalCard implements Card
{
    private final int id;       //each card has unique id
    public Tiles[][] card;

    public personalCard(int uid)
    {
        this.id = uid;
        this.card = new Tiles[6][5]; //fixed size
        personalCardParser();
    }
    private void personalCardParser()   //using the json file to card
    {

        JSONParser parser = new JSONParser();
        JSONArray personalCardFile = null;

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
            //need to fix the type of coordinate.get("type") from Object to Tiles
            this.card[Integer.parseInt(coordinate.get("x").toString())][Integer.parseInt(coordinate.get("y").toString())] = coordinate.get("type");
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
