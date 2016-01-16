/*
 * Nikita Erokhine, Riley Freeborn, Arnab Dey
 * Cutten Defence
 * RButton Class (Buttons are used for menus)
 */
package cuttendefence;

public class RButton {
    //variables for the position, dimensions and text displayed by the button

    int x, y, height, width;
    String text;

    public RButton(int x, int y, int height, int width, String text) {//constructor
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.text = text;
    }
    //getters and setters

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
        return "RButton{" + "x=" + x + ", y=" + y + ", height=" + height + ", width=" + width + ", text=" + text + '}';
    }
}
