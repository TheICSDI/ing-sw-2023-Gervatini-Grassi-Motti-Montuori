package main.java.it.polimi.ingsw.model.Cards;
import main.java.it.polimi.ingsw.model.Tile.type;
import main.java.it.polimi.ingsw.model.Player;
import main.java.it.polimi.ingsw.model.Tile.Tile;

public class CC_03 implements CCStrategy {
    Tile[][] shelf;
    boolean[][] Seen;
    public boolean isCompleted(Player p) {
         shelf= p.getShelf();
		int num_row = shelf.length;
		int num_col = shelf[0].length;
        Seen=new boolean[num_row][num_col];

        for (int i = 0; i < num_row; i++) {
            for (int j = 0; j < num_col; j++) {
                Seen[i][j] = false;
            }

        }
        int limit = 0;
        for (int i = 0; i < num_row; i++) {
            for (int j = 0; j < num_col; j++) {
               type curr=shelf[i][j].getCategory();
               int adj=0;
			   if(!Seen[i][j] && !curr.equals(type.EMPTY)) {
				   Seen[i][j] = true;
				   adj = adj(curr, i, j);
			   }
               if(adj >= 4){
                   limit++;
               }
            }

        }
        return limit >= 4;
    }

    private int adj(type c,int x,int y) {
        int p = 1;

		try{
			if (shelf[x+1][y].getCategory().equals(c) && !Seen[x+1][y]) {
				Seen[x+1][y]=true;
				p = p + adj(c, x+1, y);
			}
		}catch (IndexOutOfBoundsException ignored){}

		try{
			if (shelf[x][y+1].getCategory().equals(c) && !Seen[x][y+1]) {
					Seen[x][y+1]=true;
					p = p + adj(c, x, y+1);
			}
		}catch (IndexOutOfBoundsException ignored){}
        try{
            if (shelf[x-1][y].getCategory().equals(c) && !Seen[x+1][y]) {
                Seen[x-1][y]=true;
                p = p + adj(c, x-1, y);
            }
        }catch (IndexOutOfBoundsException ignored){}

        try{
            if (shelf[x][y-1].getCategory().equals(c) && !Seen[x][y+1]) {
                Seen[x][y-1]=true;
                p = p + adj(c, x, y-1);
            }
        }catch (IndexOutOfBoundsException ignored){}

        return p;
    }
}
