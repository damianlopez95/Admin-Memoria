package Modelo;

public class Particion {
	
	private String ID;
	private int dirInicial;
	private int dirFinal;
	private String estado;
	private Proceso proceso;
	
	public Particion(int id, int dirInicial, int dirFinal, String estado, Proceso proceso) {
		this.ID = "P0" + String.valueOf(id);
		this.dirInicial = dirInicial;
		this.dirFinal = dirFinal;
		this.estado = estado;
		this.proceso = proceso;
	}
	
	public String getID() {
		return this.ID;
	}
	
	public void setID(int id) {
		this.ID = "P0" + String.valueOf(id);
	}
	
	public String getEstado() {
		return this.estado;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public int getTamaño() {
		return (this.dirFinal - this.dirInicial) +1;
	}
	
	public void setDirInicial(int dir) {
		this.dirInicial = dir;
	}
	
	public int getDirInicial() {
		return this.dirInicial;
	}
	
	public void setDirFinal(int dir) {
		this.dirFinal = dir;
	}
	
	public int getDirFinal() {
		return this.dirFinal;
	}
	
	public Proceso getProceso() {
		return this.proceso;
	}
	
	public void setProceso() {
		this.estado = null;
	}
}
