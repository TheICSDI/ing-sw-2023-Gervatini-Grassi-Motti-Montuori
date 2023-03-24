enum type {
    CATS,
    BOOKS,
    GAMES,
    FRAMES,
    TROPHIES,
    PLANTS;
}

public class Tiles {
    private final type category;
    //andra aggiunto il collegamento a IMG
    /* probabilmente non serve il costruttore
       nb va creato un file json con le tiles.*/
    public Tiles(type any){
        this.category= any;
    }

    public type getCategory(){
        return category;
    }
}
