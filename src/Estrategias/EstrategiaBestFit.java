package Estrategias;

import java.util.ArrayList;

import Modelo.Particion;

public class EstrategiaBestFit implements EstrategiaAsignacion{

	@Override
	public int obtenerParticion(ArrayList<Particion> particiones, int memRequerida, int capacidad) {
		//se busca la partici�n libre con tama�o m�s cercano al requerido para contener al nuevo proceso
    	int indice = 0;
    	int particion = -1;
    	int tama�o = capacidad + 1;
    	
    	while (indice < particiones.size())  {
    		if ((particiones.get(indice).getEstado().equals("Libre") && (particiones.get(indice).getTama�o() >= memRequerida))) {
    			if (particiones.get(indice).getTama�o() < tama�o) {
    				tama�o = particiones.get(indice).getTama�o();
    				particion = indice;
    			}
    		}
    		indice += 1;
    	}
    	return particion;
	}
}
