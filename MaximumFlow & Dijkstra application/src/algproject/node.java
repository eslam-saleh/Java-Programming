package algproject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class node implements Comparable<node> {
	String name;
    List<node> nebours = new ArrayList<>();
    List<edge> edges = new ArrayList<>();
    Point p;
    int r;
    int id;
    boolean selected;
    Rectangle b;
    Color color;
	public node(int id,String name,ArrayList<node>n, int totalSize) {
		this.name=name;
		selected = false;
		b = new Rectangle();
		color=Color.GREEN;
        int x = 400 + (int)(200 * Math.cos((2 * Math.PI / totalSize) * (id + 1)));
        int y = 250 + (int)(200 * Math.sin((2 * Math.PI / totalSize) * (id + 1)));
        Point p = new Point(x,y);
        this.id=id;
        r=15;
        this.p=p;
        setBoundary(b);
	}
    private void setBoundary(Rectangle b) {
        b.setBounds(p.x -r, p.y -r , 4 * r, 4 * r);
    }
    public boolean isnebour(node n) {
		for (int i = 0; i < nebours.size(); i++) {
			if(nebours.get(i).name.equals(n.name)) {
				return true;
			}
		}
		return false;
	}

    Point getLocation() {
        return p;
    }

    /**
     * Return true if this node contains p.
     */
    private boolean contains(Point oldP) {
        double x1 = p.getX();
        double y1 = p.getY();
        double x2 = oldP.getX();
        double y2 = oldP.getY();
        double distance = Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
        if(distance <= 70)
            return false;
        return true;
    }

    private double getDistance(Point oldP) {
        double x1 = p.getX();
        double y1 = p.getY();
        double x2 = oldP.getX();
        double y2 = oldP.getY();
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }

    boolean isSelected() {
        return selected;
    }
    /**
     * Mark this node as selected.
     */
    void setSelected(boolean selected) {
        this.selected = selected;
    }
    @Override
    public int compareTo(node n2) {
        if (this.id < n2.id) return -1;
        else if (this.id > n2.id) return 1;
        return 0;
    }
}
