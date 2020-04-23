package Interfaz;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import Modelo.Particion;

@SuppressWarnings("serial")
public class Panel extends JPanel {

	private ArrayList<Particion> particiones;
	private int memoria;
	
	public Panel(ArrayList<Particion> particiones, int memoria) {
		this.particiones = particiones;
		this.memoria = memoria; //sirve como proporción
	}
	
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        g.setColor(Color.black);
        Rectangle2D r2d = new Rectangle2D.Float(3, 60, 623, 110);
        g2d.draw(r2d);
        g.setFont(new Font("Tahoma", Font.BOLD, 8));
        
        float acumTamaño = 0;
        float posX = 3;
        
        for (int i = 0; i < particiones.size()-1; i++) {
        	float limite = (((623)*(particiones.get(i).getTamaño()))/ memoria); //se busca la proporción del tamaño
        	acumTamaño = acumTamaño + limite;
        	Rectangle2D rect = new Rectangle2D.Float(posX, 60, limite, 110);
        	if ((particiones.get(i).getEstado()).equals("Libre")) {
        		g.setColor(Color.white);
        		g2d.fill(rect);
        	}
        	else {
        		g.setColor(Color.CYAN);
        		g2d.fill(rect);
        	}
        	g.setColor(Color.black);
        	g2d.drawString(String.valueOf(particiones.get(i).getTamaño()) + "MB", ((limite-30)/2)+posX, 120);
        	Rectangle2D rect1 = new Rectangle2D.Float(posX, 60, limite, 110);
        	g2d.draw(rect1);
        	posX = posX + limite;
        }
        
        int limite = (623)*(particiones.get(particiones.size()-1).getTamaño())/memoria;
        Rectangle2D rect;
        Rectangle2D rect1;
        if ((limite + posX) > 623) {
        	rect = new Rectangle2D.Float(posX, 60, 623 - posX + 3, 110);
        	rect1 = new Rectangle2D.Float(posX, 60, 623 - posX + 3, 110);
        }
        else {
        	rect = new Rectangle2D.Double(posX, 60, limite, 110);
        	rect1 = new Rectangle2D.Double(posX, 60, limite, 110);
        }
        if ((particiones.get(particiones.size()-1).getEstado()).equals("Libre")) {
    		g.setColor(Color.white);
    		g2d.fill(rect);
    	}
    	else {
    		g.setColor(Color.CYAN);
    		g2d.fill(rect);
    	}
        g.setColor(Color.black);
        g2d.drawString(String.valueOf(particiones.get(particiones.size()-1).getTamaño()) + "MB", ((626-acumTamaño-30)/2)+posX, 120);
        
        g2d.draw(rect1);
        
        g.drawRect(3, 195, 9, 9);
        g.drawRect(3, 215, 9, 9);
        g.drawString("Partición ocupada", 20, 205);
        g.drawString("Partición libre", 20, 225);
        
        g.setFont(new Font("Tahoma", Font.BOLD, 12));
        g.drawString("Memoria total: " + String.valueOf(memoria) + "MB", 480, 205);
        
        g.setColor(Color.CYAN);
		g.fillRect(3, 195, 9, 9);
		g.setColor(Color.white);
		g.fillRect(3, 215, 9, 9);
    }
}
