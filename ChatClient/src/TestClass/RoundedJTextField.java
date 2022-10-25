package TestClass;

import javax.swing.JTextField;

import java.awt.Shape;
import java.awt.Graphics;
import java.awt.geom.RoundRectangle2D;

public class RoundedJTextField extends JTextField {

    private Shape shape;

    public RoundedJTextField() {
        super();
        setOpaque(false);
    }

    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 15, 15);
        super.paintComponent(g);
    }

    protected void paintBorder(Graphics g) {
        g.setColor(getForeground());
        g.drawRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 15, 15);
    }

    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new RoundRectangle2D.Float(0, 0, getWidth() - 2, getHeight() - 2, 15, 15);
        }
        return shape.contains(x, y);
    }
}
