package Estrategias;

import java.util.ArrayList;

import Modelo.Particion;

public class EstrategiaFirstFit implements EstrategiaAsignacion{

	@Override
	public int obtenerParticion(ArrayList<Particion> particiones, int memRequerida, int valor) {
		//se busca la primer partici�n libre con tama�o suficiente para contener al nuevo proceso de manera secuencial
    	int indice = 0;
    	
    	while (indice < particiones.size())  {
    		if ((particiones.get(indice).getEstado().equals("Libre") && (particiones.get(indice).getTama�o() >= memRequerida))) {
    			return indice;
    		}
    		else {
    			indice += 1;
    		}
    	}
    	return -1; //no hay disponibilidad
	}
}
