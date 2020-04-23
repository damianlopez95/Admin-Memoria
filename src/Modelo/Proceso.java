package Modelo;

public class Proceso {
	
	//Atributos iniciales
	private String ID;
	int instanteArribo;
	private int duracion;
	private int memRequerida;
	//Atributos necesarios de gesti�n
	private int cicloArribo; //el proceso entra en gesti�n
	private int cicloIngreso; //el proceso ingresa en memoria
	private int cicloListo; //el proceso est� listo para salir de memoria
	private int cicloSalida; //el proceso sale de memoria
	
	public Proceso(String ID, int instanteArribo, int duracion, int memRequerida) {
		this.ID = ID;
		this.instanteArribo = instanteArribo;
		this.duracion = duracion;
		this.memRequerida = memRequerida;
		
		this.cicloArribo = -1;
		this.cicloIngreso = -1; //todav�a no ingresa
		this.cicloSalida = -1;  //todav�a no termina
	}
	
	public String getID() {
		return this.ID;
	}
	
	public int getInstanteArribo() {
		return this.instanteArribo;
	}
	
	public int getDuracion() {
		return this.duracion;
	}
	
	public int getMemRequerida() {
		return this.memRequerida;
	}
	
	public int getCicloIngreso() {
		return this.cicloIngreso;
	}
	
	public int getCicloListo() {
		return this.cicloListo;
	}
	
	public int getCicloSalida() {
		return this.cicloSalida;
	}
	
	public void setCicloArribo(int ciclo) {
		this.cicloArribo = ciclo;
	}
	
	public int getCicloArribo() {
		return this.cicloArribo;
	}
	
	public void setCiclos(int ciclo) {
		this.cicloIngreso = ciclo;
		this.cicloListo = this.cicloIngreso + this.duracion;
	}
	
	public void setCicloSalida(int ciclo) {
		this.cicloSalida = ciclo;
	}
}
