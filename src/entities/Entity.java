package entities;

// abstract base class for all entities
// holds position (row, col) and name
public abstract class Entity {

    protected int row;
    protected int col;
    protected String name;

    // constructs an entity at the given position with a name
    public Entity(String name, int row, int col) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Entity name cannot be blank or null.");
        }
        if (row < 0) {
            throw new IllegalArgumentException("Row cannot be negative.");
        }
        if (col < 0) {
            throw new IllegalArgumentException("Column cannot be negative.");
        }
        this.name = name;
        this.row  = row;
        this.col  = col;
    }

    public String getName() {
        return name;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
