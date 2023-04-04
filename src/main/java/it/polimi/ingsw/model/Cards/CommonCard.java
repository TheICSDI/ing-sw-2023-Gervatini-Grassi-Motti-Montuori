package main.java.it.polimi.ingsw.model.Cards;

import main.java.it.polimi.ingsw.model.Player;

public class CommonCard{
    private int points = 8;
    private boolean first_card;
    private CCStrategy strategy;

    public CommonCard(CCStrategy current_strategy, boolean first){
        this.strategy = current_strategy;
        this.first_card = first;
    }
    public boolean DoControl(Player p){
        return strategy.isCompleted(p);
    }

    public void CalculatePoints(Player p){
        if(getPoints()>0){
            if(DoControl(p)){
                if(isFirst_card()){
                    if(p.getScoreToken1() == 0){
                        p.setScoreToken1(points);
                        setPoints(points-2);       //Diviso in due righe uguali per far si che abbassi i punti solo la prima volta che
                                                    // completa la common
                    }
                }
                else{
                    if(p.getScoreToken2()==0){
                        p.setScoreToken2(points);
                        setPoints(points-2);
                    }
                }


            }
        }
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean isFirst_card() {
        return first_card;
    }

    public void setFirst_card(boolean first_card) {
        this.first_card = first_card;
    }
}
