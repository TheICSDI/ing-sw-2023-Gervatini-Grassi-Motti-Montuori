enum type {
    CATS,
    BOOKS,
    GAMES,
    FRAMES,
    TROPHIES,
    PLANTS;
}

public class Tile {
    private final type category;
    //andra aggiunto il collegamento a IMG
    /* probabilmente non serve il costruttore
       nb va creato un file json con le tiles.*/
    public Tile(type any){
        this.category= any;
    }

    public type getCategory(){
        return category;
    }
}
