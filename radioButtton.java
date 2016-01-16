/*
 * Nikita Erokhine, Riley Freeborn, Arnab Dey
 * Cutten Defence
 * RadioButton Class
 * Places selectable radio buttons on the map which are manupulated in the main class (Radio Buttons are created for tower selection)
 */
package cuttendefence;

public class radioButtton {
    //variables for active/incactive, position and dimensions of the radio buttons

    protected boolean selected;
    protected int x, y, height, width;

    public radioButtton(boolean a, int x, int y) {//constructor
        selected = a;
        height = 10;
        width = 10;
        this.x = x;
        this.y = y;
    }
    //getters and setters

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    //to string

    @Override
    public String toString() {
        return "radioButtton{" + "selected=" + selected + ", x=" + x + ", y=" + y + ", height=" + height + ", width=" + width + '}';
    }
}
