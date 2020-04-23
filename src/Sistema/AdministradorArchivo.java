package Sistema;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AdministradorArchivo {
	
	private String fileName;
	private String filePath;
	
	public ArrayList<String> obtenerDatos(String filePath) {
		ArrayList<String> procesos = new ArrayList<String>();
		
		File file = new File(filePath);
		this.fileName = file.getName().toString();
		this.filePath = file.getParent().toString() + "\\";
		
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filePath));
			String line = reader.readLine();
			while (line != null) {
				if (line.length() > 6) {
					procesos.add(line);
				}
				line = reader.readLine(); //salto de línea
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return procesos;
	}
	
	public void actualizarArchivo(String filePath, ArrayList<String> procesos) {
		
		File file = new File(filePath);
		
		try {
			FileOutputStream fos = new FileOutputStream(file);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		
			for (int i = 0; i < procesos.size(); i++) {
				bw.write(procesos.get(i));
				bw.newLine();
			}
		 
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}     
	}
	
	public void crearRegistroArchivo(ArrayList<String> eventos, String tipo) {
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yy HH.mm.ss");
		LocalDateTime now = LocalDateTime.now();
		String filePath = this.filePath + dtf.format(now) + " " + tipo + " " + this.fileName;
		
		actualizarArchivo(filePath, eventos);
	}
}
