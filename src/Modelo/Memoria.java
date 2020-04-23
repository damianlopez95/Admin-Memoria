package Modelo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Estrategias.ContextoEstrategia;
import Estrategias.EstrategiaBestFit;
import Estrategias.EstrategiaFirstFit;
import Estrategias.EstrategiaNextFit;
import Estrategias.EstrategiaWorstFit;

public class Memoria {
	
	private static Memoria memoria;
	private static int idParticionGral = 1;
	
	private ContextoEstrategia algoritmoEstrategia;
	
	private int capacidad;
	private int punteroNextFit;
	private String estrategia;
	private Map<String, Integer> tablaProcesos;
	private ArrayList<Particion> particiones;
	
	private Memoria(Map<String, String> datos) {
		this.punteroNextFit = 0;
		this.estrategia = datos.get("estrategia");
		this.capacidad = Integer.parseInt(datos.get("tamañoMem"));
		particiones = new ArrayList<Particion>();
		tablaProcesos = new HashMap<String, Integer>();
		algoritmoEstrategia = new ContextoEstrategia();
		inicializarEstrategia();
		particiones.add(new Particion(idParticionGral, 0, this.capacidad -1, "Libre", null));
		idParticionGral++;
	}
	
	private void inicializarEstrategia() {
		switch(estrategia) {
    	  case "first-fit": algoritmoEstrategia.setEstrategia(new EstrategiaFirstFit());
    	    break;
    	  case "best-fit": algoritmoEstrategia.setEstrategia(new EstrategiaBestFit());
    	    break;
    	  case "next-fit": algoritmoEstrategia.setEstrategia(new EstrategiaNextFit());
    		break;
    	  case "worst-fit": algoritmoEstrategia.setEstrategia(new EstrategiaWorstFit());
    		break;
		}
	}
	
	public void resetMemoria() {
		idParticionGral = 0;
		particiones.clear();
		memoria = null;
	}
	
    public static Memoria crearInstancia(Map<String, String> datos) {
    	if (memoria == null){
            memoria = new Memoria(datos);
        }
        return memoria;
    }
    
    public int seleccionarParticion(ArrayList<String> eventos, Proceso proceso) {
		int indParticion = -1;
    	if (proceso.getMemRequerida() <= capacidad) {
    		switch(this.estrategia) {
	      	  case "first-fit": indParticion = algoritmoEstrategia.obtenerParticion(this.particiones, proceso.getMemRequerida(), 0);
	      	    break;
	      	  case "best-fit": indParticion = algoritmoEstrategia.obtenerParticion(this.particiones, proceso.getMemRequerida(), this.capacidad);
	      	    break;
	      	  case "next-fit": indParticion = algoritmoEstrategia.obtenerParticion(this.particiones, proceso.getMemRequerida(), this.punteroNextFit);
	      		break;
	      	  case "worst-fit": indParticion = algoritmoEstrategia.obtenerParticion(this.particiones, proceso.getMemRequerida(), 0);
	      		break;
    		}
    	}
    	else {
    		eventos.add("El proceso " + proceso.getID() + " no puede ser almacenado debido a que requiere más memoria de la existente");
    		return -2;  //-2 significa que la memoria no tiene capacidad para contener al proceso, mas allá de las particiones.
    	}
    	
    	if (indParticion == -1) {
    		eventos.add("Actualmente el proceso " + proceso.getID() + " no puede ser almacenado debido a que no hay una partición suficiente disponible");
    		return -1; //-1 significa que en este momento no hay espacio para contener al proceso.
    	}
    	else {
    		eventos.add("El proceso " + proceso.getID() + " será posicionado en la partición " + String.valueOf(indParticion));
    		return indParticion;
    	}
    }
    
    public void agregarProceso(ArrayList<String> eventos, ArrayList<String> particiones, int particion, Proceso proceso, int ciclo) {
    	//agregar partición
    	particiones.add("Ciclo: " + ciclo);
    	particiones.add("");
		int dInicial = this.particiones.get(particion).getDirInicial();
		int dFinal = (proceso.getMemRequerida() + dInicial) - 1;
		this.particiones.add(particion, new Particion(idParticionGral,dInicial, dFinal, "En uso", proceso));
		idParticionGral ++;
		this.tablaProcesos.put(proceso.getID(), particion);
		if (proceso.getMemRequerida() == this.particiones.get(particion+1).getTamaño()) { //en caso de que el nuevo proceso ocupe lo mismo que la partición anterior
			this.particiones.remove(particion+1);
		}
		else {
			this.punteroNextFit = ((particion + 1) % particiones.size());
			this.particiones.get(particion+1).setID(idParticionGral); //al espacio libre se le asigna nuevo ID
			idParticionGral ++;
			this.particiones.get(particion+1).setDirInicial(dFinal+1);
		}
		recalcularTablaIndices(particion+1);
    	registrarParticiones(particiones);
    }
    
    public void terminarProceso(ArrayList<String> eventos, ArrayList<String> particiones, Proceso proceso, int ciclo) {
    	int indProceso = this.tablaProcesos.get(proceso.getID());
    	particiones.add("Ciclo: " + ciclo);
    	particiones.add("");
    	eventos.add("El proceso " + proceso.getID() + " finaliza liberando " + String.valueOf(proceso.getMemRequerida()) + "MB");
    	this.tablaProcesos.remove(proceso.getID());
    	if ((this.punteroNextFit >= indProceso) && (this.punteroNextFit > 0)) {
			this.punteroNextFit --;
		}
    	evaluarParticionesLibresContiguas(eventos, indProceso);
    	registrarParticiones(particiones);
    }
    
    public void registrarParticiones(ArrayList<String> regParticiones) {
    	String aux;
    	for (int i = 0; i < this.particiones.size(); i++) {
			aux = particiones.get(i).getID() + " =  Tamaño: " + String.valueOf(particiones.get(i).getTamaño()) + "MB - Dir. Inicial: "
			+ particiones.get(i).getDirInicial() + " - Dir. Final: " + particiones.get(i).getDirFinal() + " - Estado: " + particiones.get(i).getEstado();
			if (particiones.get(i).getProceso() != null) {
				aux = aux + " - Proceso: " + particiones.get(i).getProceso().getID();
			}
			regParticiones.add(aux);
			regParticiones.add("");
			aux = "";
		}
    }
    
    private boolean pIzqLibre(int indice) {
    	return ((indice > 0) && (this.particiones.get(indice - 1).getEstado().equals("Libre")));
    }
    
    private boolean pDerLibre(int indice) {
    	return ((indice < this.particiones.size() - 1) && (this.particiones.get(indice + 1).getEstado().equals("Libre")));
    }
    
    private void evaluarParticionesLibresContiguas(ArrayList<String> eventos, int indParticion) {
    	
    	if (pIzqLibre(indParticion) && pDerLibre(indParticion)) {
    		eventos.add("Como ambas particiones contiguas del proceso estan libres, se procede a combinar ambas particiones libres");
    		this.particiones.get(indParticion - 1).setDirFinal(this.particiones.get(indParticion + 1).getDirFinal());
    		this.particiones.remove(indParticion + 1);
    		this.particiones.get(indParticion - 1).setID(idParticionGral);
    	}
    	else if (pIzqLibre(indParticion)) {
    		eventos.add("Como la partición izquierda del proceso está libre, se procede a combinar ambas particiones");
    		this.particiones.get(indParticion - 1).setDirFinal(this.particiones.get(indParticion).getDirFinal());
    		this.particiones.get(indParticion - 1).setID(idParticionGral);
    	}
    	else if (pDerLibre(indParticion)) {
			eventos.add("Como la partición derecha del proceso está libre, se procede a combinar ambas particiones");
			this.particiones.get(indParticion + 1).setDirInicial(this.particiones.get(indParticion).getDirInicial());
			this.particiones.get(indParticion + 1).setID(idParticionGral);
    	}
    	else {
    		eventos.add("No se encontraron particiones libres contiguas, por lo tanto no hay combinación");
    		this.particiones.add(indParticion, new Particion(idParticionGral,this.particiones.get(indParticion).getDirInicial(), this.particiones.get(indParticion).getDirFinal(),"Libre", null));
    		indParticion ++;
    	}
    	idParticionGral++;
    	this.particiones.remove(indParticion);
    	recalcularTablaIndices(indParticion);
    }
    
    private void recalcularTablaIndices(int indice) {
    	
    	for (int i = indice; i < this.particiones.size(); i++) {
    		if ((particiones.get(i).getEstado()).equals("En uso")) {
    			this.tablaProcesos.put(particiones.get(i).getProceso().getID(), i);
    		}
    	}
    }
    
    public boolean hayProcesoEnMemoria() {
    
    	for (int i = 0; i < this.particiones.size(); i++) {
    		if ((particiones.get(i).getEstado()).equals("En uso")) {
    			return true;
    		}
    	}
    	return false;
    }
    //Sirve para calcular el índice de fragmentación externa
    public int getFragmentacion() {
    	int aux = 0;
    	for (int i = 0; i < this.particiones.size(); i++) {
    		if ((particiones.get(i).getEstado()).equals("Libre")) {
    			aux = aux + particiones.get(i).getTamaño();
    		}
    	}
    	return aux;
    }
    
    public int getCapacidad() {
    	return this.capacidad;
    }
    
    public String getEstrategia() {
    	return this.estrategia;
    }
    //Retorna las particiones de un ciclo dado. Sirve para visualizar las particiones en cada uno de los ciclos.
    public ArrayList<Particion> getParticiones() {
    	return this.particiones;
    }
}
