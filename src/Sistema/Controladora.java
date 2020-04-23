package Sistema;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import Modelo.AdministradorMemoria;
import Modelo.Proceso;
import Modelo.OrdenamientoProceso;
import Modelo.Particion;

public class Controladora {
	
	private AdministradorArchivo adminArchivo = new AdministradorArchivo();
	private AdministradorMemoria adminMemoria;
	private ArrayList<Proceso> procesos = new ArrayList<Proceso>();
	
	public void administrarProcesos(String archivo, Map<String, String> datos) {
		ArrayList<String> aux = new ArrayList<String>();
		aux = adminArchivo.obtenerDatos(archivo);
		cargarProcesos(aux);
		Collections.sort(procesos, new OrdenamientoProceso());
		adminMemoria = AdministradorMemoria.crearInstancia(datos, procesos); 
		adminMemoria.gestionarMemoria(); //Actividad principal
		registrarEventos(); //Una vez finalizada la gestión se crean archivos con los datos producidos
	}
	
	public void registrarEventos() {
		adminArchivo.crearRegistroArchivo(adminMemoria.obtenerRegistroDeEventos(), "Eventos");
		adminArchivo.crearRegistroArchivo(adminMemoria.obtenerRegistroDeParticiones(), "Particiones");
	}
	
	public void cargarProcesos(ArrayList<String> listaProcesos) {
		for (int i = 0; i < listaProcesos.size(); i++) {
			String[] proceso = listaProcesos.get(i).split(",");
			procesos.add(new Proceso(proceso[0], Integer.parseInt(proceso[1]), Integer.parseInt(proceso[2]), Integer.parseInt(proceso[3])));
		}
	}
	
	public ArrayList<String> obtenerProcesos(String archivo) {
		return adminArchivo.obtenerDatos(archivo);
	}
	
	public void actualizarProcesos(String archivo, ArrayList<String> procesos) {
		adminArchivo.actualizarArchivo(archivo, procesos);
	}
	
	public ArrayList<String> obtenerTiempos() {
		return adminMemoria.obtenerRegistroDeTiempos();
	}
	
	public ArrayList<String> obtenerEventos() {
		return adminMemoria.obtenerRegistroDeEventos();
	}
	
	public ArrayList<String> obtenerParticiones() {
		return adminMemoria.obtenerRegistroDeParticiones();
	}
	
	public ArrayList<ArrayList<Particion>> obtenerParticionesTotales() {
		return adminMemoria.obtenerParticionesTotales();
	}
	
	public void vaciarEstructuras() {
		adminMemoria.resetAdminMemoria();
		procesos = new ArrayList<Proceso>();
	}
}
