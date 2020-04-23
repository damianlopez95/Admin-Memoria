package Estrategias;

import java.util.ArrayList;

import Modelo.Particion;

public class ContextoEstrategia {
	
	private EstrategiaAsignacion estrategia;
	
	public void setEstrategia(EstrategiaAsignacion estrategia) {
		this.estrategia = estrategia;
	}
	
	public int obtenerParticion(ArrayList<Particion> particiones, int memRequerida, int valor) {
		return estrategia.obtenerParticion(particiones, memRequerida, valor);
	}
}
