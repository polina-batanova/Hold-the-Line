package entities;

// abstract base class for all entities
// holds position (row, col) and name
public abstract class Entity {

    protected int row;
    protected int col;
    protected String name;

    // constructs an entity at the given position with a name
    public Entity(String name, int row, int col) {
        // todo: implement validation and assignment
    }

    public String getName() {
        // todo: implement
        return null;
    }

    public int getRow() {
        // todo: implement
        return 0;
    }

    public int getCol() {
        // todo: implement
        return 0;
    }
}
