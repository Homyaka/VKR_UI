package GDSystem;

public enum Cell {
    WALLS(-1),
    EMPTY(0),
    OBJECT (2),
    FREE(1),

    //Для отображения
    OBJ1 (10),
    OBJ2 (11),
    OBJ3 (12);

    Cell(int i) {
    }
}
