package Model.Cards;
import Model.Player;
import Model.Tile.Tile;
import Model.Tile.type;

public class CC_03 implements CCStrategy {
    public boolean isCompleted(Player p) {
        Tile[][] curr_shelf=p.getShelf();
		int num_row = curr_shelf.length;
		int num_col = curr_shelf[0].length;
        boolean[][] Seen=new boolean[num_row][num_col];
        for (int i = 0; i < num_row; i++) {
            for (int j = 0; j < num_col; j++) {
                Seen[i][j]=false;
            }

        }
        int adj=0,limit=0;
        for (int i = 0; i < num_row; i++) {
            for (int j = 0; j < num_col; j++) {
               type curr=curr_shelf[i][j].getCategory();
			   if(!Seen[i][j]) {
				   Seen[i][j]=true;
				   adj = adj(curr, i, j, curr_shelf, Seen);
			   }
               if(adj>=4){
                   limit++;
               }
            }

        }
        return limit >= 4;
    }

    private int adj(type c,int x,int y, Tile[][] shelf,boolean[][] Seen) {
        int p = 1;
		try{
			if (shelf[x+1][y].getCategory() == c && !Seen[x+1][y]) {
				Seen[x+1][y]=true;
				p = p + adj(c, x+1, y, shelf, Seen);
			}
		}catch (IndexOutOfBoundsException ignored){}
		try{
			if (shelf[x][y+1].getCategory() == c && !Seen[x][y+1]) {
					Seen[x][y+1]=true;
					p = p + adj(c, x, y+1, shelf, Seen);
			}
		}catch (IndexOutOfBoundsException ignored){}
        return p;
    }
}
