package algproject;

import java.awt.*;

public class edge implements Comparable<edge>  {
	node n1,n2;
	int weight,tempWeight;
	boolean isDirected;
	public edge(node n1,node n2,int weight,boolean isDir) {
		// TODO Auto-generated constructor stub
		isDirected = isDir;
		this.n1=n1;
		this.n2=n2;
		this.weight=weight;
		this.tempWeight=weight;
        n1.nebours.add(n2);
        n1.edges.add(this);
        if (!isDirected) {
        	n2.nebours.add(n1);
            n2.edges.add(this);
        }
	}
    @Override
    public int compareTo(edge e2) {
        if (this.weight < e2.weight) return -1;
        else if (this.weight > e2.weight) return 1;
        return 0;
    }

    public void drawWeight(Graphics2D gfx, Color color, String str) {
        gfx.setFont(new Font("Arial", Font.BOLD, 15));
        gfx.setColor(color);
        gfx.drawString(str,((n1.p.x+n2.p.x)/2 + n2.p.x)/2  ,((n2.p.y+n1.p.y)/2 + n2.p.y)/2);
        Point pD = n2.p;
        Point pS = n1.p;
        gfx.setColor(color);
        gfx.drawLine(pS.x, pS.y, pD.x, pD.y);
        if(isDirected) {
            int d = 30;
            int h = 10;
            int dx = pD.x - pS.x, dy = pD.y - pS.y;
            double D = Math.sqrt(dx * dx + dy * dy);
            double xm = D - d, xn = xm, ym = h, yn = -h, x;
            double sin = dy / D, cos = dx / D;

            x = xm * cos - ym * sin + pS.x;
            ym = xm * sin + ym * cos + pS.y;
            xm = x;

            x = xn * cos - yn * sin + pS.x;
            yn = xn * sin + yn * cos + pS.y;
            xn = x;

            int[] xpoints = {pD.x, (int) xm, (int) xn};
            int[] ypoints = {pD.y, (int) ym, (int) yn};
            gfx.fillPolygon(xpoints, ypoints, 3);
        }
    }
}
