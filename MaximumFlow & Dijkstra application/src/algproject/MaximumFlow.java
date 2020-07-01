package algproject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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

import javax.swing.JOptionPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author cw
 */
public class MaximumFlow extends javax.swing.JFrame {
    Image img;
    Graphics2D gfx;
    int current,q,w;
    ArrayList<edge>e;
    Iterator<node> itr;
    ArrayList<node>location1,location2;
   node src, Dist;
   ArrayList<node>n;
	int maximumFlow ;
	node parent[] ;
    boolean check;
   int minDistance(int dist[], Boolean sptSet[]) 
   { 
       // Initialize min value 
       int min = Integer.MAX_VALUE, min_index = -1; 
 
       for (int i = 0; i < dist.length; i++) 
           if (sptSet[i] == false && dist[i] <= min) { 
               min = dist[i]; 
               min_index = i; 
           } 
 
       return min_index; 
   }
    

    /**
     * Creates new form NewJFrame
     */
    public MaximumFlow(ArrayList<node>n,ArrayList<edge>e,node src,node Dist,boolean check) {
        initComponents();
        location1 = new ArrayList<>();
        location2 = new ArrayList<>();
        this.src=src;
        this.Dist=Dist;
        this.n=n;
        this.e=e;
        this.check=check;
        parent = new node[n.size()];
        maximumFlow = 0;
        this.w=1;
        if(!check) {
			while(bfs(n,src, Dist, parent)) { 
	        	if(src==Dist) {
	        		for (int i = 0; i < src.nebours.size(); i++) {
						if(src==src.nebours.get(i)) {
							maximumFlow+=src.edges.get(i).weight;
							break;
						}
					}
	        		break;
	        	}
		        int path_flow = Integer.MAX_VALUE; 
		        int i;
		        for (node v=Dist; v!=src; v=parent[v.id]) {
		        	node u = parent[v.id];
		        	for (i = 0; i < u.nebours.size(); i++) {
		        		if(u.nebours.get(i).id==v.id) {
		        			break;
		        		}
		        	}
		        	path_flow = Math.min(path_flow, u.edges.get(i).weight);
		        }
		        for (node v=Dist; v!=src; v=parent[v.id]) {
		        	node u = parent[v.id];
		        	for (i = 0; i < u.nebours.size(); i++) {
		        		if(u.nebours.get(i).id==v.id) {
		        			break;
		        		}
		        	}
		        	u.edges.get(i).weight-=path_flow;
		        }
		        maximumFlow += path_flow; 
		    }
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
        for (int j = 0; j < e.size() ; j++) {
            if(e.get(j).weight<=0) {
                gfx.setColor(Color.RED);
                gfx.drawLine(e.get(j).n1.p.x, e.get(j).n1.p.y, e.get(j).n2.p.x, e.get(j).n2.p.y);
                e.get(j).drawWeight(gfx, Color.RED, ""+e.get(j).tempWeight+"/"+(e.get(j).weight-e.get(j).tempWeight));
            }
            else {
                gfx.setColor(Color.BLACK);
                gfx.drawLine(e.get(j).n1.p.x, e.get(j).n1.p.y, e.get(j).n2.p.x, e.get(j).n2.p.y);
                e.get(j).drawWeight(gfx, Color.BLACK, ""+e.get(j).tempWeight+"/"+(e.get(j).weight-e.get(j).tempWeight));
            }
        }
        for (int i = 0; i < location1.size()-1; i++) {
            node n1=location1.get(i),n2=location1.get(i+1);
            for (int j = 0; j < n2.nebours.size(); j++) {
				if(n2.nebours.get(j)==n1) {
					n2.edges.get(j).drawWeight(gfx, Color.BLUE, n2.edges.get(j).tempWeight+"/"+(n2.edges.get(j).weight-n2.edges.get(j).tempWeight));
				}
			}
		}
        for(int i=0; i < this.n.size(); i++){
        	node n= (node) this.n.get(i);
            Point p= (Point) n.p;
            
            if(location1.contains(n)) {
            	gfx.setColor(Color.BLUE);
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
    boolean bfs(ArrayList<node>n,node s, node e, node parent[]) 
    { 
        Queue<node> qn=new LinkedList<>();
        boolean visited[] = new boolean[n.size()]; 
        for (int i = 0; i < visited.length; i++) {
			visited[i]=false;
		}
        visited[s.id]=true;
        parent[s.id]=null;
        qn.add(s);
    	while(!qn.isEmpty()) {
    		node v=qn.poll();
    		for (int i = 0; i < v.nebours.size(); i++) {
				if(!visited[v.nebours.get(i).id]&&v.edges.get(i).weight>0) {
					qn.add(v.nebours.get(i));
					parent[v.nebours.get(i).id]=v;
					visited[v.nebours.get(i).id]=true;
				}
			}
    	}
    	return(visited[e.id]);
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        draw();
        if(!check) {
			JOptionPane.showMessageDialog(null, "the maximum flow is "+maximumFlow);
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
        		boolean a;
        		ArrayList<node>path=new ArrayList<node>();
        		if((a=bfs(n,src, Dist, parent))) { 
    	        	if(src==Dist) {
    	        		for (int i = 0; i < src.nebours.size(); i++) {
    						if(src==src.nebours.get(i)) {
    							maximumFlow+=src.edges.get(i).weight;
    							break;
    						}
    					}
    	        		return;
    	        	}
     	           int path_flow = Integer.MAX_VALUE; 
     	           int i;
     	           for (node v=Dist; v!=src; v=parent[v.id]) { 
     	            	node u = parent[v.id]; 
						path.add(v);
     	            	for (i = 0; i < u.nebours.size(); i++) {
     						if(u.nebours.get(i).id==v.id) {
     							break;
     						}
     					}
     	                path_flow = Math.min(path_flow, u.edges.get(i).weight); 
     	           } 
     	           for (node v=Dist; v!=src; v=parent[v.id]) { 
     	            	node u = parent[v.id]; 
     	            	for (i = 0; i < u.nebours.size(); i++) {
     						if(u.nebours.get(i).id==v.id) {
     							break;
     						}
     					}
     	            	u.edges.get(i).weight-=path_flow;
     	           } 
     	           maximumFlow += path_flow; 
     	        } 
        		if(a)
            		path.add(src);
        		location1=path;
        		draw();
        		if(!a) {
        			JOptionPane.showMessageDialog(null, "the maximum flow is "+maximumFlow);
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
