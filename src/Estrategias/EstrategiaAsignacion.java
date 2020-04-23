package Estrategias;

import java.util.ArrayList;

import Modelo.Particion;

public interface EstrategiaAsignacion {
	
	public int obtenerParticion(ArrayList<Particion> particiones, int memRequerida, int valor);
}
