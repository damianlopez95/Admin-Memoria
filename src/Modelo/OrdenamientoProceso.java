package Modelo;

import java.util.Comparator;

public class OrdenamientoProceso implements Comparator<Proceso>  {

	public int compare(Proceso a, Proceso b) 
    { 
        return a.instanteArribo - b.instanteArribo; 
    }
}
