package Estrategias;

import java.util.ArrayList;

import Modelo.Particion;

public class EstrategiaNextFit implements EstrategiaAsignacion{

	@Override
	public int obtenerParticion(ArrayList<Particion> particiones, int memRequerida, int punteroNextFit) {
		//se busca la primer partici�n libre con tama�o suficiente para contener al nuevo proceso, pero comenzando desde la �ltima inserci�n
    	int indice = punteroNextFit;
    	
    	while (indice < particiones.size())  {
    		if ((particiones.get(indice).getEstado().equals("Libre") && (particiones.get(indice).getTama�o() >= memRequerida))) {
    			return indice;
    		}
    		else {
    			indice += 1;
    		}
    	}
    	if (punteroNextFit > 0) {
    		indice = 0;
    		while (indice <= punteroNextFit)  {
    			if ((particiones.get(indice).getEstado().equals("Libre") && (particiones.get(indice).getTama�o() >= memRequerida))) {
        			return indice;
        		}
        		else {
        			indice += 1;
        		}
    		}
    	}
    	return -1; //no hay disponibilidad
	}
}
