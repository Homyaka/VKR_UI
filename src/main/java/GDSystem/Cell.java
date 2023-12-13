package GDSystem;

public enum Cell {
    WALLS(-1),
    EMPTY(0),
    OBJECT (2),
    FREE(1),

    //Для отображения
    OBJ1 (11),
    OBJ2 (12),
    OBJ3 (13),
    OBJ4 (14),
    OBJ5(15);
    Cell(int i) {
    }
}
