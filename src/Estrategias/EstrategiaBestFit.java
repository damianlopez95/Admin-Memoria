package Estrategias;

import java.util.ArrayList;

import Modelo.Particion;

public class EstrategiaBestFit implements EstrategiaAsignacion{

	@Override
	public int obtenerParticion(ArrayList<Particion> particiones, int memRequerida, int capacidad) {
		//se busca la partición libre con tamaño más cercano al requerido para contener al nuevo proceso
    	int indice = 0;
    	int particion = -1;
    	int tamaño = capacidad + 1;
    	
    	while (indice < particiones.size())  {
    		if ((particiones.get(indice).getEstado().equals("Libre") && (particiones.get(indice).getTamaño() >= memRequerida))) {
    			if (particiones.get(indice).getTamaño() < tamaño) {
    				tamaño = particiones.get(indice).getTamaño();
    				particion = indice;
    			}
    		}
    		indice += 1;
    	}
    	return particion;
	}
}
