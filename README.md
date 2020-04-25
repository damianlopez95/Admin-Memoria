# Admin-Memoria

Administrador de Memoria con Particiones Dinámicas: Proyecto integrador final para la materia "Sistemas Operativos". UNTDF. 3er Año. 2019.

Desarrollo de aplicación de escritorio en el lenguaje Java.

Se trata de programar un sistema que simule distintas estrategias de asignación de particiones dinámicas de memoria a una tanda de trabajos y calcule un conjunto de 
indicadores.

Estrategias de asignación de particiones implementadas: first-fit, best-fit, next-fit y worst-fit.

El sistema proporciona como salida una serie de gráficos que representan el estado de la memoria principal durante cada ciclo de ejecución,
imprimiendo la posición de cada proceso en partición, su espacio asignado y la duración de su estadía en memoria.

Además, la aplicación muestra y guarda un log en .txt con cada movimiento del sistema y el cálculo de los siguientes indicadores:

Tiempo de Selección de partición, Tiempo de Retorno para cada proceso, Tiempo de Carga Promedio, Tiempo de Liberación de Partición, Tiempo Medio de
Retorno e Indice de Fragmentación Externa.

Tecnologías:

- Lenguaje: Java
- IDE: Eclipse
- Interfaz: WindowBuilder (Eclipse)
- Gráficos: librería awt (Graphics)
- Persistencia: en archivos .txt

Patrones de diseño: MVC, Singleton, Strategy.
