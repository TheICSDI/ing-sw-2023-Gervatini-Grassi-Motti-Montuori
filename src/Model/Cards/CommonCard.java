package Model.Cards;

import Model.Player;

public class CommonCard{
    private int points = 8;
    private int first_card;
    private CCStrategy strategy;

    public CommonCard(CCStrategy current_strategy, int first){
        this.strategy = current_strategy;
        this.first_card = first;
    }
    public int DoControl(){
        return strategy.isCompleted();
    }

    public void CalculatePoints(Player p){
        if(getPoints()>0){
            if(DoControl() == 1){
                if(getFirst_card()==1){
                    if(p.isScoreToken1() == 0){
                        p.setScoreToken1(points);
                    }
                }
                else{
                    if(p.isScoreToken2()==0){
                        p.setScoreToken2(points);
                    }
                }
                setPoints(points-2);

            }
        }
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getFirst_card() {
        return first_card;
    }

    public void setFirst_card(int first_card) {
        this.first_card = first_card;
    }
}
