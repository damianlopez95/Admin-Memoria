package Modelo;

import java.util.ArrayList;
import java.util.Map;

public class AdministradorMemoria {
	
	private static AdministradorMemoria adminMemoria;
	//Datos requeridos de entrada
	private int tiempoLiberacion;    //T. entre que termina el proceso y se lo libera
	private int tiempoSeleccion;     //T. entre que se elige el proceso y se lo agrega a memoria
	private int tiempoCargaPromedio; //T. sig al T. de selección (?)
	private Memoria memoria;
	private ArrayList<Proceso> procesos; //referencia a lista de procesos en la clase controladora
	
	//Variables necesarias para la administración de la memoria durante los ciclos
	private int ciclo = 0;
	boolean quedanProcesos = true;
	boolean tiemposActivos = false;
	boolean esperaLiberacion = false;
	boolean prioridadLiberacion = false;
	int indiceSigProcesoACargar = 0;
	int indiceSigProcesoALiberar = -1;
	int tSeleccionRestante = 0;
	int tLiberacionRestante = 0;
	int tCargaPromRestante = 0;
	int particionElegida = -2;
	int acumFragmentacion = 0;
	ArrayList<Proceso> procesosActivos = new ArrayList<Proceso>();
	
	//Registro de cada requerimiento pedido, documentado en listas distintas.
	private ArrayList<String> tiempos = new ArrayList<String>();
	private ArrayList<String> eventos = new ArrayList<String>();
	private ArrayList<String> particiones = new ArrayList<String>();
	private ArrayList<ArrayList<Particion>> regParticiones = new ArrayList<ArrayList<Particion>>();
	
	private AdministradorMemoria(Map<String, String> datos, ArrayList<Proceso> procesos) {
		this.tiempoLiberacion = Integer.parseInt(datos.get("tLiberacion"));
		this.tiempoSeleccion = Integer.parseInt(datos.get("tSeleccion"));
		this.tiempoCargaPromedio = Integer.parseInt(datos.get("tCargaProm"));
		this.procesos = procesos;
		memoria  = Memoria.crearInstancia(datos);
	}
	
    public static AdministradorMemoria crearInstancia(Map<String, String> datos, ArrayList<Proceso> procesos) {
    	if (adminMemoria == null){
    		adminMemoria = new AdministradorMemoria(datos, procesos);
        }
        return adminMemoria;
    }
    
    public void resetAdminMemoria() {
		adminMemoria = null;
	}
    
    public void gestionarMemoria() {
    	
    	registrarDatosIniciales();
    	
    	//Cada iteración representa un ciclo.
    	while (quedanProcesos) {
    		ciclo ++;
    		eventos.add("");
    		eventos.add("Inicia el ciclo: " + ciclo);
    		eventos.add("");
    		
    		if ((!procesosActivos.isEmpty()) && (indiceSigProcesoALiberar == -1)) {
    			indiceSigProcesoALiberar = evaluarProcesosActivos(procesosActivos, ciclo);
    		}
    		
    		if (!tiemposActivos) {
    			//Queda al menos un proceso sin cargar en memoria
    			if (indiceSigProcesoACargar < procesos.size()) {
    				//Se evalúa si el sig. proceso a cargar ha llegado
    				if (this.procesos.get(indiceSigProcesoACargar).getInstanteArribo() <= ciclo) {
    					//Hay al menos un proceso activo
    					if (!procesosActivos.isEmpty()) {
    						//Se evalua si algún proceso activo está listo para ser liberado
        					if (indiceSigProcesoALiberar != -1) {
        						//Se determina si el proceso a cargar o a liberar tiene prioridad
        						if ((esperaLiberacion) || ((this.procesos.get(indiceSigProcesoACargar).getInstanteArribo()) >= (procesosActivos.get(indiceSigProcesoALiberar).getCicloListo()))) {
                					tLiberacionRestante = this.tiempoLiberacion;
                					tiemposActivos = true;
                					prioridadLiberacion = true;
                				}
        					}
        					if ((!prioridadLiberacion) && (!esperaLiberacion)) {
            					tSeleccionRestante = this.tiempoSeleccion;
            					tCargaPromRestante = this.tiempoCargaPromedio;
            					procesos.get(indiceSigProcesoACargar).setCicloArribo(ciclo);
            					tiemposActivos = true;
            				}
            				prioridadLiberacion = false;
        				}
    					else {
    						tSeleccionRestante = this.tiempoSeleccion;
        					tCargaPromRestante = this.tiempoCargaPromedio;
        					procesos.get(indiceSigProcesoACargar).setCicloArribo(ciclo);
        					tiemposActivos = true;
    					}
    				}
    				else {
    					if (indiceSigProcesoALiberar != -1) {
							tLiberacionRestante = this.tiempoLiberacion;
        					tiemposActivos = true;
    					}
    				}
    			}
    			else {
    				if (!procesosActivos.isEmpty()) {
    					if (indiceSigProcesoALiberar != -1) {
    						tLiberacionRestante = this.tiempoLiberacion;
    						tiemposActivos = true;
    					}
    				}
    				else {
    					quedanProcesos = false;
    				}
    			}
    		}
    		if (tiemposActivos) {
    			if (tSeleccionRestante > 0) {
    				evaluacionTSeleccion();
        		}
        		else if (tCargaPromRestante > 0) {
        			evaluacionTCargaProm();
        		}
        		else if (tLiberacionRestante > 0) {
        			evaluacionTLiberacion();
        		}
    		}
    		registrarParticiones();
    		//si ya se cargó el último proceso en memoria, no se calcula la fragmentación
    		if (this.indiceSigProcesoACargar < this.procesos.size()) {
    			this.acumFragmentacion = this.acumFragmentacion + memoria.getFragmentacion();
    		}
    	}
    	eventos.add("Finaliza la simulación");
    	registrarTiempos();
    	memoria.resetMemoria();
    }
    
    private void evaluacionTSeleccion() {
    	eventos.add("Tiempo de Selección de partición para el proceso " + procesos.get(indiceSigProcesoACargar).getID());
		tSeleccionRestante --;
		if (tSeleccionRestante == 0) {
			this.particionElegida = memoria.seleccionarParticion(this.eventos, procesos.get(indiceSigProcesoACargar));
			switch (particionElegida) {
			case -2: 
			  indiceSigProcesoACargar ++;
			  tCargaPromRestante = 0;
			  tiemposActivos = false;
			  break;
			case -1: 
			  esperaLiberacion = true; //el proceso debe esperar a que termine un proceso para ingresar
			  tCargaPromRestante = 0;
			  tiemposActivos = false;
			  break;
			}
		}
    }
    
    private void evaluacionTCargaProm() {
    	eventos.add("Tiempo de Carga Promedio para el proceso " + procesos.get(indiceSigProcesoACargar).getID() + " en la partición libre " + String.valueOf(this.particionElegida));
		tCargaPromRestante --;
		if (tCargaPromRestante == 0) {
			//cargar proceso y apuntar índice a sig.
			memoria.agregarProceso(this.eventos, this.particiones, particionElegida, procesos.get(indiceSigProcesoACargar), ciclo);
			procesos.get(indiceSigProcesoACargar).setCiclos(ciclo);
			procesosActivos.add(procesos.get(indiceSigProcesoACargar));
			indiceSigProcesoACargar ++;
			tiemposActivos = false;
		}
    }
   
    private void evaluacionTLiberacion() {
    	eventos.add("Tiempo de Liberación del proceso " + procesosActivos.get(indiceSigProcesoALiberar).getID());
		tLiberacionRestante --;
		if (tLiberacionRestante == 0) {
			//liberar proceso de memoria y eliminarlo de la lista de activos
			memoria.terminarProceso(this.eventos, this.particiones, procesosActivos.get(indiceSigProcesoALiberar), ciclo);
			procesosActivos.get(indiceSigProcesoALiberar).setCicloSalida(ciclo);
			procesosActivos.remove(indiceSigProcesoALiberar);
			esperaLiberacion = false; //el proceso pendiente de ingresar puede volver a intentar
			indiceSigProcesoALiberar = -1;
			tiemposActivos = false;
		}
    }
    
    private int evaluarProcesosActivos(ArrayList<Proceso> procesosActivos, int ciclo) {
    	int aux = ciclo;
    	int proceso = -1;
    	
    	for (int i = 0; i < procesosActivos.size(); i++) {
    		if (aux >= procesosActivos.get(i).getCicloListo()) {
    			proceso = i;
    			aux = procesosActivos.get(i).getCicloListo();
    		}
    	}
    	if (proceso != -1) {
    		for (int i = 0; i < procesosActivos.size(); i++) {
    			if (aux == procesosActivos.get(i).getCicloListo()) {
    				if (procesosActivos.get(i).getCicloIngreso() < procesosActivos.get(proceso).getCicloIngreso()) {
	    				proceso = i;
    				}
    			}
    		}
    	}
    	return proceso;
    }
    
    private void registrarDatosIniciales() {
    	this.particiones.add("Ciclo 1: ");
    	this.particiones.add("");
    	memoria.registrarParticiones(this.particiones); //Se registra la partición inicial
    	this.eventos.add("Administrador de Memoria");
    	this.eventos.add("");
    	this.eventos.add("Capacidad máxima de memoria: " + String.valueOf(memoria.getCapacidad()) + "MB");
    	this.eventos.add("Estrategia utilizada: " + memoria.getEstrategia());
    	this.eventos.add("Tiempo de selección de partición: " + String.valueOf(this.tiempoSeleccion) + " ciclo/s");
    	this.eventos.add("Tiempo de liberación de partición: " + String.valueOf(this.tiempoLiberacion) + " ciclo/s");
    	this.eventos.add("Tiempo de carga promedio: " + String.valueOf(this.tiempoCargaPromedio) + " ciclo/s");
    	this.eventos.add("");
    	this.eventos.add("-------------------------------------------------------------");
    }
    
    private void registrarParticiones() {
    	ArrayList<Particion> aux = new ArrayList<Particion>();
    	ArrayList<Particion> original = memoria.getParticiones();
    	for (int i = 0; i < original.size(); i++) {
    		aux.add(new Particion(Integer.parseInt(original.get(i).getID().substring(1)), original.get(i).getDirInicial(), original.get(i).getDirFinal(), original.get(i).getEstado(), null));
    	}
    	this.regParticiones.add(aux);
    }
    
    private void registrarTiempos() {
    	
    	int primerArribo = 100000;
    	int ultimaSalida = -1;
    	float cantProcesos = 0;
    	float acumTR = 0;
    	this.tiempos.add("Procesos:");
    	this.tiempos.add("------------------------------");
    	this.tiempos.add("");
    	
    	for (int i = 0; i < this.procesos.size(); i++) {
    		this.tiempos.add("Proceso " + this.procesos.get(i).getID());
    		if (this.procesos.get(i).getCicloIngreso() > -1) {
    			cantProcesos ++;
    			int aux = this.procesos.get(i).getCicloSalida() - this.procesos.get(i).getCicloArribo() + 1;
    			this.tiempos.add("Tiempo de retorno: " + String.valueOf(aux) + " ciclo/s");
    			acumTR = acumTR + this.procesos.get(i).getCicloSalida();
    			if (this.procesos.get(i).getCicloArribo() < primerArribo) {
    				primerArribo = this.procesos.get(i).getCicloArribo();
    			}
    			if (this.procesos.get(i).getCicloSalida() > ultimaSalida) {
    				ultimaSalida = this.procesos.get(i).getCicloSalida();
    			}
    		}
    		else {
    			this.tiempos.add("Tiempo de retorno: --, debido a que no se almacenó en memoria");
    		}
    		this.tiempos.add("");
    	}
    	
    	this.tiempos.add("Tanda de Procesos:");
    	this.tiempos.add("------------------------------");
    	this.tiempos.add("");
    	if (cantProcesos > 0) {
    		this.tiempos.add("Tiempo de Retorno: " + String.valueOf(ultimaSalida - primerArribo) + " ciclo/s");
        	this.tiempos.add("Tiempo Medio de Retorno: " + String.valueOf(acumTR / cantProcesos) + " ciclo/s");
        	this.tiempos.add("Indice de Fragmentación Externa: " + String.valueOf(this.acumFragmentacion));
    	}
    	else {
    		this.tiempos.add("Tiempo de Retorno: --");
        	this.tiempos.add("Tiempo Medio de Retorno: --");
        	this.tiempos.add("Indice de Fragmentación Externa: --");
    	}
    }
    
    public ArrayList<String> obtenerRegistroDeTiempos() {
    	return tiempos;
    }
    
    public ArrayList<String> obtenerRegistroDeEventos() {
    	return eventos;
    }
    
    public ArrayList<String> obtenerRegistroDeParticiones() {
    	return particiones;
    }
    
    public ArrayList<ArrayList<Particion>> obtenerParticionesTotales() {
    	return regParticiones;
    }
}
