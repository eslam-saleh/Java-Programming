package algproject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author cw
 */
public class Dijkstra extends javax.swing.JFrame {
    Image img;
    Graphics2D gfx;
    ArrayList<edge> e;
    ArrayList<node> n;
    Iterator<node> itr;
    int c;
    String s;
    boolean check;
    ArrayList<node>location1,location2;
    /**
    * Creates new form NewJFrame
    */
    public Dijkstra(ArrayList<node>n,ArrayList<edge>e,node src,node Dist,boolean check) {
        initComponents();
        location1 = new ArrayList<>();
        location2 = new ArrayList<>();
        this.e = e;
        this.n = n;
        this.c=0;
        this.s="";
        this.check=check;
        int dist[] = new int[n.size()];
        Queue<node> qn=new LinkedList<>();
        String disString[]=new String[n.size()];
        for (int i = 0; i < n.size(); i++) 
            dist[i] = Integer.MAX_VALUE; 
        qn.add(src);
        dist[src.id]=0;
        disString[src.id]="";
        while(!qn.isEmpty()){
        	node nx=qn.remove();
        	if(nx.selected)
        		continue;
        	else
        		nx.selected=true;
        	for (int j = 0; j <nx.nebours.size() ; j++) {
        		node nz=nx.nebours.get(j);
        		for (int k = 0; k < e.size(); k++) {
					if((nx==e.get(k).n1&&nz==e.get(k).n2)||(nx==e.get(k).n2&&nz==e.get(k).n1)) {
						if(dist[nx.id]+e.get(k).weight<dist[nz.id]) {
							dist[nz.id]=dist[nx.id]+e.get(k).weight;
							disString[nz.id]=disString[nx.id]+" "+nz.id;
						}
		        		break;
					}
				}
				qn.add(nx.nebours.get(j));
			}
		}
        String[] spli=disString[Dist.id].split(" ");
        spli[0]=""+src.id;
        if(check) {
	        for (int i = 0; i < spli.length; i++) {
	        	for (int j = 0; j < n.size(); j++) {
					if(spli[i].equals(Integer.toString(n.get(j).id))) {
						location2.add(n.get(j));
						break;
					}
				}
			}
        }
        else {
	        for (int i = 0; i < spli.length; i++) {
	        	for (int j = 0; j < n.size(); j++) {
					if(spli[i].equals(Integer.toString(n.get(j).id))) {
						location1.add(n.get(j));
						break;
					}
				}
			}
        }
        for (int i = 0; i < n.size(); i++) {
			n.get(i).selected=false;
		}
	    itr = location2.iterator();
	    if(itr.hasNext()){
	    	location1.add(itr.next());
	    }
        img= panel.createImage(panel.getWidth(),panel.getHeight());
        gfx = (Graphics2D) img.getGraphics();
    }
    public void draw()
    {
        gfx.setColor(Color.white);
        gfx.fillRect(0, 0, panel.getWidth(), panel.getHeight());
        ArrayList<edge> edges=new ArrayList<edge>();
        for (int i = 0; i < location1.size()-1; i++) {
            node n1=location1.get(i),n2=location1.get(i+1);
            edge FEdge=null;
            for (int j = 0; j < n1.nebours.size(); j++) {
				if(n1.nebours.get(j)==n2) {
					FEdge=n1.edges.get(j);
				}
			}
            edges.add(FEdge);
            if(i==0) {
				FEdge.drawWeight(gfx, Color.RED, ""+FEdge.tempWeight);
				c+=FEdge.weight;
            	s=FEdge.tempWeight+"="+c;				
            }
            else {
            	s=s.replaceFirst("="+c, "+"+FEdge.tempWeight+"="+(c+FEdge.weight));
            	c+=FEdge.weight;
				FEdge.drawWeight(gfx, Color.RED, ""+s);            	
            }
            FEdge.weight=0;
		}
        for (int j = 0; j < e.size() ; j++) {
        	if(edges.contains(e.get(j)))
        		continue;
            e.get(j).drawWeight(gfx, Color.BLACK, ""+e.get(j).weight);
        }
        for(int i=0; i < this.n.size(); i++){
            node n= (node) this.n.get(i);
            Point p= n.p;
            if(location1.contains(n)) {
            	gfx.setColor(Color.RED);
            }
            else {
            	gfx.setColor(Color.GREEN);
            }
            gfx.fillOval(n.b.x , n.b.y ,n.b.width/2,n.b.height/2);
            gfx.setColor(Color.BLACK);
            gfx.setFont(new Font("Arial", Font.BOLD, 15));
            gfx.drawString(n.name,p.x  ,p.y  );
        }
        panel.getGraphics().drawImage(img, 0, 0, this);
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        draw();
        if(!check) {
			JOptionPane.showMessageDialog(null, "Distance from Source to Distnation is "+c);
        }
    }
    /**
     
     
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new javax.swing.JPanel();
        panel.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mousePressed(MouseEvent e) {
                if(itr.hasNext()){
                	location1.add(itr.next());
                	draw();
                }
                else{
        			JOptionPane.showMessageDialog(null, "Distance from Source to Distnation is "+c);
                }
        	}
        });
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    /**
     * @param args the command line arguments
     */
    private javax.swing.JPanel panel;
    // End of variables declaration//GEN-END:variables
}
