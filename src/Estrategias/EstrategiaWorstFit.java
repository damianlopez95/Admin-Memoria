package Estrategias;

import java.util.ArrayList;

import Modelo.Particion;

public class EstrategiaWorstFit implements EstrategiaAsignacion {
	
	@Override
	public int obtenerParticion(ArrayList<Particion> particiones, int memRequerida, int valor) {
		//se busca la partici�n libre con tama�o m�s grande para contener al nuevo proceso
    	int indice = 0;
    	int particion = -1;
    	int tama�o = 0;
    	
    	while (indice < particiones.size())  {
    		if ((particiones.get(indice).getEstado().equals("Libre") && (particiones.get(indice).getTama�o() >= memRequerida))) {
    			if (particiones.get(indice).getTama�o() > tama�o) {
    				tama�o = particiones.get(indice).getTama�o();
    				particion = indice;
    			}
    		}
    		indice += 1;
    	}
    	return particion;
	}
}
