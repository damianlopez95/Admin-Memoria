package Interfaz;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JComboBox;
import java.awt.Color;

import javax.swing.SwingConstants;
import javax.swing.JSeparator;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import java.awt.CardLayout;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import Modelo.Particion;
import Sistema.Controladora;

import javax.swing.JTextArea;
import javax.swing.JTabbedPane;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;

public class Interfaz {
	
	private JFrame frame;
	
	private Controladora controladora = new Controladora();
	
	private final JTextField textField = new JTextField();
	private JTextField tf_fileDisplay;
	private String filePath;
	private JTextField txtTLiberacion;
	private JTextField txtTSeleccion;
	private JTextField txtTCarga;
	private JTextField txtTamañoMem;
	private JButton btnEditar; //se escribe aquí para tener referencia desde el método getFile()
	private DefaultTableModel model;
	private JTable table;
	private JLayeredPane layeredPane;
	
	private int cicloElegido = 1;
	private int particionesTot = 0;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Interfaz window = new Interfaz();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public Interfaz() {
		initialize();
	}
	
	private void initialize() {
		
		textField.setColumns(10);
		frame = new JFrame();
		frame.setBounds(100, 100, 692, 477);
		frame.setResizable(false);
		frame.setTitle("TPI - Simulador: Administrador de Memoria");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new CardLayout(0, 0));
		
		JPanel panelSeleccion = new JPanel();
		panelSeleccion.setVisible(true);
		frame.getContentPane().add(panelSeleccion, "name_1228638203910700");
		panelSeleccion.setLayout(null);
		
		JPanel panelEdicion = new JPanel();
		panelEdicion.setVisible(false);
		frame.getContentPane().add(panelEdicion, "name_1228638239002500");
		panelEdicion.setLayout(null);
		
		JButton btnGuardarCambios = new JButton("Guardar cambios");
		btnGuardarCambios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> procesos = new ArrayList<String>();
				for (int i = 0; i < model.getRowCount(); i++) {
					String proceso = "";
					for (int j = 0; j < model.getColumnCount()-1; j++) {
						proceso = proceso + model.getValueAt(i, j).toString() + ",";
					}
					proceso = proceso + model.getValueAt(i, model.getColumnCount()-1).toString();
					procesos.add(proceso);
				}
				controladora.actualizarProcesos(tf_fileDisplay.getText(), procesos);
				JOptionPane.showMessageDialog(frame.getContentPane(), "Las modificaciones se han realizados exitosamente", "Aviso", JOptionPane.PLAIN_MESSAGE);
			}
		});
		btnGuardarCambios.setBackground(Color.LIGHT_GRAY);
		btnGuardarCambios.setBounds(53, 370, 144, 21);
		panelEdicion.add(btnGuardarCambios);
		
		JButton btnVolver = new JButton("Volver");
		btnVolver.setBackground(Color.LIGHT_GRAY);
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelEdicion.setVisible(false);
				panelSeleccion.setVisible(true);
			}
		});
		btnVolver.setBounds(523, 370, 85, 21);
		panelEdicion.add(btnVolver);
		
		JLabel lblContenidoDelArchivo = new JLabel("Contenido del archivo:");
		lblContenidoDelArchivo.setBounds(53, 32, 192, 13);
		panelEdicion.add(lblContenidoDelArchivo);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(53, 90, 555, 236);
		panelEdicion.add(scrollPane);
		
		model = new DefaultTableModel(); 
		table = new JTable(model);
		model.addColumn("ID Proceso"); 
		model.addColumn("Instante de arribo");
		model.addColumn("Duración total"); 
		model.addColumn("Cant. mem. requerida");
		table.getColumnModel().getColumn(1).setPreferredWidth(101);
		table.getColumnModel().getColumn(2).setPreferredWidth(86);
		table.getColumnModel().getColumn(3).setPreferredWidth(118);
		scrollPane.setViewportView(table);
		
		JButton btnAgregarProceso = new JButton("Agregar Proceso");
		btnAgregarProceso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.addRow(new Object[]{"", "", "", ""});
			}
		});
		btnAgregarProceso.setBounds(53, 336, 144, 21);
		panelEdicion.add(btnAgregarProceso);
		
		JButton btnEliminarProcesoSeleccionado = new JButton("Eliminar proceso");
		btnEliminarProcesoSeleccionado.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow() > -1) {
					model.removeRow(table.getSelectedRow());
				}
			}
		});
		btnEliminarProcesoSeleccionado.setBounds(207, 336, 144, 21);
		panelEdicion.add(btnEliminarProcesoSeleccionado);
		
		JButton btnSubir = new JButton("Subir");
		btnSubir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow() > 0) {
					model.moveRow(table.getSelectedRow(), table.getSelectedRow(), table.getSelectedRow()-1);
					table.setRowSelectionInterval(table.getSelectedRow()-1, table.getSelectedRow()-1);
				}
			}
		});
		btnSubir.setBounds(426, 336, 85, 21);
		panelEdicion.add(btnSubir);
		
		JButton btnBajar = new JButton("Bajar");
		btnBajar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow() > -1) {
					if (table.getSelectedRow() < model.getRowCount()-1) {
						model.moveRow(table.getSelectedRow(), table.getSelectedRow(), table.getSelectedRow()+1);
						table.setRowSelectionInterval(table.getSelectedRow()+1, table.getSelectedRow()+1);
					}
				}
			}
		});
		btnBajar.setBounds(523, 336, 85, 21);
		panelEdicion.add(btnBajar);
		
		btnEditar = new JButton("Ver");
		btnEditar.setBounds(492, 21, 85, 21);
		panelSeleccion.add(btnEditar);
		btnEditar.setEnabled(false);
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelEdicion.setVisible(true);
				panelSeleccion.setVisible(false);
			}
		});
		btnEditar.setForeground(SystemColor.textHighlight);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(0, 0, 2, 235);
		panelSeleccion.add(separator_2);
		separator_2.setOrientation(SwingConstants.VERTICAL);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(0, 0, 110, -5);
		panelSeleccion.add(separator_1);
		
		JLabel lblArchivoElegido = new JLabel("Archivo elegido:");
		lblArchivoElegido.setBounds(77, 5, 118, 13);
		panelSeleccion.add(lblArchivoElegido);
		lblArchivoElegido.setFont(new Font("Tahoma", Font.BOLD, 10));
		
		txtTamañoMem = new JTextField();
		txtTamañoMem.setBounds(389, 99, 96, 19);
		panelSeleccion.add(txtTamañoMem);
		txtTamañoMem.setColumns(10);
		
		JLabel lblTiempoDeLiberacin = new JLabel("Tiempo de liberaci\u00F3n de partici\u00F3n:");
		lblTiempoDeLiberacin.setBounds(92, 195, 251, 13);
		panelSeleccion.add(lblTiempoDeLiberacin);
		lblTiempoDeLiberacin.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		txtTCarga = new JTextField();
		txtTCarga.setBounds(389, 288, 96, 19);
		panelSeleccion.add(txtTCarga);
		txtTCarga.setColumns(10);
		
		JLabel lblTiempoDeCarga = new JLabel("Tiempo de carga promedio: ");
		lblTiempoDeCarga.setBounds(92, 287, 194, 19);
		panelSeleccion.add(lblTiempoDeCarga);
		lblTiempoDeCarga.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		txtTSeleccion = new JTextField();
		txtTSeleccion.setBounds(389, 240, 96, 19);
		panelSeleccion.add(txtTSeleccion);
		txtTSeleccion.setColumns(10);
		
		JLabel lblTiempoDeSeleccin = new JLabel("Tiempo de selecci\u00F3n de partici\u00F3n:");
		lblTiempoDeSeleccin.setBounds(92, 238, 222, 21);
		panelSeleccion.add(lblTiempoDeSeleccin);
		lblTiempoDeSeleccin.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		JLabel lblEstrategiaDeAsignacin = new JLabel("Estrategia de asignaci\u00F3n:");
		lblEstrategiaDeAsignacin.setBounds(92, 145, 194, 17);
		panelSeleccion.add(lblEstrategiaDeAsignacin);
		lblEstrategiaDeAsignacin.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		txtTLiberacion = new JTextField();
		txtTLiberacion.setBounds(389, 193, 96, 19);
		panelSeleccion.add(txtTLiberacion);
		txtTLiberacion.setColumns(10);
		
		JLabel lblTamaoDeMemoria = new JLabel("Tama\u00F1o de memoria f\u00EDsica:");
		lblTamaoDeMemoria.setBounds(92, 101, 222, 13);
		panelSeleccion.add(lblTamaoDeMemoria);
		lblTamaoDeMemoria.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 0, 579, 2);
		panelSeleccion.add(separator);
		
		JComboBox<String> cBoxEstrategia = new JComboBox<String>();
		cBoxEstrategia.setBounds(389, 144, 96, 21);
		panelSeleccion.add(cBoxEstrategia);
		
		JPanel panelInforme = new JPanel();
		frame.getContentPane().add(panelInforme, "name_1273546277124400");
		panelInforme.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 62, 658, 321);
		panelInforme.add(tabbedPane);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		tabbedPane.addTab("Tiempos", null, scrollPane_1, null);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		tabbedPane.addTab("Reg. Eventos", null, scrollPane_2, null);
		
		JTextArea textAreaEventos = new JTextArea();
		scrollPane_2.setViewportView(textAreaEventos);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		tabbedPane.addTab("Reg. Particiones", null, scrollPane_3, null);
		
		JTextArea textAreaTiempos = new JTextArea();
		textAreaTiempos.setText("");
		textAreaTiempos.setEditable(false);
		scrollPane_1.setViewportView(textAreaTiempos);
		
		JTextArea textAreaParticiones = new JTextArea();
		scrollPane_3.setViewportView(textAreaParticiones);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Gráficos Particiones", null, panel, null);
		panel.setLayout(null);
		
		layeredPane = new JLayeredPane();
		layeredPane.setBounds(10, 10, 633, 241);
		panel.add(layeredPane);
		layeredPane.setLayout(new CardLayout(0, 0));
		
		JComboBox<Integer> cBoxCiclos = new JComboBox<Integer>();
		cBoxCiclos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) layeredPane.getLayout();
				cl.show(layeredPane, "P" + String.valueOf(cBoxCiclos.getSelectedItem()));
				try {
					cicloElegido = (int) cBoxCiclos.getSelectedItem();
				} 
				catch (NullPointerException exc) {
					//En realidad no existe ningún peligro, pero al haber una referencia al
					//comboBox, en el momento de su limpieza puede arrojar una excepción.
				}
			}
		});
		cBoxCiclos.setBounds(326, 261, 48, 21);
		panel.add(cBoxCiclos);
		
		JButton btnAnterior = new JButton("<");
		btnAnterior.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnAnterior.setBackground(Color.LIGHT_GRAY);
		btnAnterior.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cicloElegido > 1) {
					cicloElegido --;
					CardLayout cl = (CardLayout) layeredPane.getLayout();
					cl.show(layeredPane, "P" + String.valueOf(cicloElegido));
					cBoxCiclos.setSelectedItem(cicloElegido);
				}
			}
		});
		btnAnterior.setBounds(10, 261, 85, 21);
		panel.add(btnAnterior);
		
		JButton btnSiguiente = new JButton(">");
		btnSiguiente.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnSiguiente.setBackground(Color.LIGHT_GRAY);
		btnSiguiente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cicloElegido < particionesTot) {
					cicloElegido ++;
					CardLayout cl = (CardLayout) layeredPane.getLayout();
					cl.show(layeredPane, "P" + String.valueOf(cicloElegido));
					cBoxCiclos.setSelectedItem(cicloElegido);
				}
			}
		});
		btnSiguiente.setBounds(558, 261, 85, 21);
		panel.add(btnSiguiente);
		
		JLabel lblIrACiclo = new JLabel("Ir al ciclo:");
		lblIrACiclo.setFont(new Font("Tahoma", Font.BOLD, 10));
		lblIrACiclo.setBounds(266, 263, 68, 17);
		panel.add(lblIrACiclo);
		
		JButton btnEvaluarNuevaTanda = new JButton("Evaluar una nueva tanda de procesos");
		btnEvaluarNuevaTanda.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnEvaluarNuevaTanda.setBackground(Color.LIGHT_GRAY);
		btnEvaluarNuevaTanda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtTLiberacion.setText("");
				txtTSeleccion.setText("");
				txtTCarga.setText("");
				txtTamañoMem.setText("");
				tf_fileDisplay.setText("");
				textAreaTiempos.setText("");
				textAreaEventos.setText("");
				textAreaParticiones.setText("");
				
				cicloElegido = 1;
				particionesTot = 0;
				layeredPane.removeAll();
				layeredPane.revalidate();
				layeredPane.repaint();
				cBoxCiclos.removeAllItems();
				
				controladora.vaciarEstructuras();
				
				btnEditar.setEnabled(false);
				panelSeleccion.setVisible(true);
				panelEdicion.setVisible(false);
				panelInforme.setVisible(false);
			}
		});
		btnEvaluarNuevaTanda.setBounds(10, 393, 247, 21);
		panelInforme.add(btnEvaluarNuevaTanda);
		
		JButton btnFinalizar = new JButton("Finalizar");
		btnFinalizar.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnFinalizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnFinalizar.setBackground(Color.LIGHT_GRAY);
		btnFinalizar.setBounds(583, 393, 85, 21);
		panelInforme.add(btnFinalizar);
		
		JLabel lblResultados = new JLabel("Resultados de la simulaci\u00F3n:");
		lblResultados.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblResultados.setBounds(10, 20, 225, 28);
		panelInforme.add(lblResultados);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setBounds(10, 46, 177, 6);
		panelInforme.add(separator_3);
		
		JButton btnEjecutar = new JButton("Ejecutar");
		btnEjecutar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> tiempos = new ArrayList<String>();
				ArrayList<String> eventos = new ArrayList<String>();
				ArrayList<String> particiones = new ArrayList<String>();
				ArrayList<ArrayList<Particion>> regParticiones = new ArrayList<ArrayList<Particion>>();
				Map<String, String> datos = new HashMap<String, String>();
				
				datos.put("tamañoMem", txtTamañoMem.getText());
				datos.put("estrategia", cBoxEstrategia.getSelectedItem().toString());
				datos.put("tLiberacion", txtTLiberacion.getText());
				datos.put("tSeleccion", txtTSeleccion.getText());
				datos.put("tCargaProm", txtTCarga.getText());
				controladora.administrarProcesos(tf_fileDisplay.getText(), datos);
				
				tiempos = controladora.obtenerTiempos();
				for (int i = 0; i < tiempos.size(); i++) {
					textAreaTiempos.append(tiempos.get(i) + " \n");
				}
				eventos = controladora.obtenerEventos();
				for (int i = 0; i < eventos.size(); i++) {
					textAreaEventos.append(eventos.get(i) + " \n");
				}
				particiones = controladora.obtenerParticiones();
				for (int i = 0; i < particiones.size(); i++) {
					textAreaParticiones.append(particiones.get(i) + " \n");
				}
				regParticiones = controladora.obtenerParticionesTotales();
				for (int i = 0; i < regParticiones.size(); i++) {
					particionesTot ++;
					cBoxCiclos.addItem(i+1);
					dibujarParticion(regParticiones.get(i), i+1, Integer.parseInt(txtTamañoMem.getText()));
				}
				CardLayout cl = (CardLayout) layeredPane.getLayout();
				cl.show(layeredPane, "P" + String.valueOf(1));
				panelSeleccion.setVisible(false);
				panelEdicion.setVisible(false);
				panelInforme.setVisible(true);
			}
		});
		btnEjecutar.setBounds(292, 370, 92, 21);
		panelSeleccion.add(btnEjecutar);
		btnEjecutar.setBackground(Color.LIGHT_GRAY);
		
		JButton btnSeleccionar = new JButton("Seleccionar");
		btnSeleccionar.setBounds(373, 21, 110, 21);
		panelSeleccion.add(btnSeleccionar);
		btnSeleccionar.setBackground(Color.LIGHT_GRAY);
		
		tf_fileDisplay = new JTextField();
		tf_fileDisplay.setBounds(75, 22, 288, 19);
		panelSeleccion.add(tf_fileDisplay);
		tf_fileDisplay.setEditable(false);
		tf_fileDisplay.setHorizontalAlignment(SwingConstants.LEFT);
		tf_fileDisplay.setColumns(10);
		
		JLabel lblMb = new JLabel("MB");
		lblMb.setBounds(486, 102, 24, 13);
		panelSeleccion.add(lblMb);
		
		JLabel lblCiclos = new JLabel("ciclo/s");
		lblCiclos.setBounds(486, 196, 46, 13);
		panelSeleccion.add(lblCiclos);
		
		JLabel label = new JLabel("ciclo/s");
		label.setBounds(486, 243, 46, 13);
		panelSeleccion.add(label);
		
		JLabel label_1 = new JLabel("ciclo/s");
		label_1.setBounds(486, 291, 46, 13);
		panelSeleccion.add(label_1);
		
		JSeparator separator_4 = new JSeparator();
		separator_4.setBounds(32, 52, 610, 10);
		panelSeleccion.add(separator_4);
		
		JSeparator separator_5 = new JSeparator();
		separator_5.setBounds(236, 350, 208, 10);
		panelSeleccion.add(separator_5);
		
		btnSeleccionar.addActionListener((a) -> getFile() );
		cBoxEstrategia.addItem("first-fit");
		cBoxEstrategia.addItem("best-fit");
		cBoxEstrategia.addItem("next-fit");
		cBoxEstrategia.addItem("worst-fit");
	}
	
	private void cargarTabla(String filePath) {
		ArrayList<String> procesos = new ArrayList<String>();
		procesos = controladora.obtenerProcesos(filePath);
		model.setRowCount(0); //primero se limpia el contenido existente
		
		for (int i = 0; i < procesos.size(); i++) {
			String[] proceso = procesos.get(i).split(",");
			model.addRow(new Object[]{proceso[0], proceso[1], proceso[2], proceso[3]});
		}
	}
	
	private void getFile() {
		JFileChooser jfc = new JFileChooser("C:\\Users\\User\\Desktop\\Facu\\Sistemas Operativos\\Archivos");

		FileNameExtensionFilter filter = new FileNameExtensionFilter("txt", "TXT");
		jfc.setFileFilter(filter);
		
		int returnValue = jfc.showOpenDialog(null);
		
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = jfc.getSelectedFile();
			filePath = selectedFile.getAbsolutePath();
			tf_fileDisplay.setText(filePath);
			cargarTabla(filePath);
			if ((filePath.substring(filePath.length() - 3)).equals("txt")) {
				btnEditar.setEnabled(true);
			}
		}
	}
	
	private void dibujarParticion(ArrayList<Particion> particiones, int ciclo, int memoria) {
		Panel panel = new Panel(particiones, memoria);
		layeredPane.add(panel, "P" + String.valueOf(ciclo));
		panel.setLayout(null);
		JLabel lblPanel = new JLabel("Ciclo " + String.valueOf(ciclo));
		lblPanel.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblPanel.setBounds(3, 10, 129, 39);
		panel.add(lblPanel);
	}
}
