package hospitalbasesdedatos;

import java.sql.*;

// 1. Define la clase abstracta Persona con atributos comunes para pacientes y doctores.
abstract class Persona {
    protected String nombre;
    protected int edad;

    // Constructor y metodos necesarios

    public Persona(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
    }
}


// 2. Implementa la clase Paciente que hereda de Persona con atributos adicionales como historial medico.
class Paciente extends Persona {
    private String historialMedico;
    private int doctorCabecera;
    private Date fechaIngreso;

    public Paciente(String nombre, int edad, String historialMedico, int doctorCabecera, Date fechaIngreso) {
        super( nombre, edad);
        this.historialMedico = historialMedico;
        this.doctorCabecera = doctorCabecera;
        this.fechaIngreso = fechaIngreso;
    }

    public String getNombre() {
        return nombre;
    }

    public int getEdad() {
        return edad;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public String getHistorialMedico() {
        return historialMedico;
    }

    public int getDoctorCabecera() {
        return doctorCabecera;
    }
}


// 3. Implementa la clase Doctor que hereda de Persona con atributos como especialidad.
class Doctor extends Persona {
    private String especialidad;

    public Doctor(String nombre, int edad, String especialidad) {
        super(nombre, edad);
        this.especialidad = especialidad;
    }

}

class Hospital {
    public void agregarPaciente(Paciente paciente) {
        // Agregar el paciente a la base de datos
        String consulta = "INSERT INTO pacientes (nombre, edad, historial_medico, doctor, fecha_ingreso) VALUES ('" + paciente.getNombre() + "', " + paciente.getEdad() + ", '" + paciente.getHistorialMedico() + "', " + paciente.getDoctorCabecera() + ", '" + paciente.getFechaIngreso() + "')";
        DBHelper.ejecutarConsulta(consulta);
    }
    // elimine un paciente indicando su nombre
    public void eliminarPaciente(String nombre) {
        // Eliminar el paciente de la base de datos
        String consulta = "DELETE FROM pacientes WHERE nombre = '" + nombre + "'";
        DBHelper.ejecutarConsulta(consulta);
    }

    //metodo para asignar un doctor de cabecera a un paciente indicando el nombre del doctor y el nombre del paciente
    public void asignarDoctorCabecera(String nombreDoctor, String nombrePaciente) {
        System.out.println(nombreDoctor);
        String consulta = "UPDATE pacientes SET doctor = (SELECT id FROM doctores WHERE nombre = '"+nombreDoctor+"') WHERE nombre = '"+nombrePaciente+"'";
        DBHelper.ejecutarConsulta(consulta);

    }

    public void listarPacientes() {
        String consulta = "SELECT * FROM pacientes";
        ResultSet resultado = DBHelper.ejecutarConsultaConResultado(consulta);

        if (resultado != null) {
            try {
                System.out.println("Lista de Pacientes:");
                System.out.printf("%-10s %-15s %-5s %-20s %-12s %-10s\n", "ID", "Nombre", "Edad", "Historial M�dico", "Fecha Ingreso", "Doctor");

                while (resultado.next()) {
                    int id = resultado.getInt("id");
                    String nombre = resultado.getString("nombre");
                    int edad = resultado.getInt("edad");
                    String historialMedico = resultado.getString("historial_medico");
                    Date fechaIngreso = resultado.getDate("fecha_ingreso");
                    int idDoctor = resultado.getInt("doctor");

                    System.out.printf("%-10d %-15s %-5d %-20s %-12s %-10d\n", id, nombre, edad, historialMedico, fechaIngreso, idDoctor);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void listarPacientesEntreDosFechas(Date fechaDesde, Date fechaHasta) {
        String consulta = "SELECT * FROM pacientes WHERE fecha_ingreso BETWEEN '"+fechaDesde+"' AND '"+fechaHasta+"';\n";
        ResultSet resultado = DBHelper.ejecutarConsultaConResultado(consulta);

        if (resultado != null) {
            try {
                System.out.println("Lista de Pacientes:");
                System.out.printf("%-10s %-15s %-5s %-20s %-12s %-10s\n", "ID", "Nombre", "Edad", "Historial M�dico", "Fecha Ingreso", "Doctor");

                while (resultado.next()) {
                    int id = resultado.getInt("id");
                    String nombre = resultado.getString("nombre");
                    int edad = resultado.getInt("edad");
                    String historialMedico = resultado.getString("historial_medico");
                    Date fechaIngreso = resultado.getDate("fecha_ingreso");
                    int idDoctor = resultado.getInt("doctor");

                    System.out.printf("%-10d %-15s %-5d %-20s %-12s %-10d\n", id, nombre, edad, historialMedico, fechaIngreso, idDoctor);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}


class DBHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/hospital_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Metodo para ejecutar una consulta sin devolver resultados
    public static void ejecutarConsulta(String consulta) {
        try {
            // Establecer la conexi�n con la base de datos
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Crear la declaracion
            try (PreparedStatement statement = connection.prepareStatement(consulta)) {
                // Ejecutar la consulta
                statement.executeUpdate();
            }

            // Cerrar la conexion
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Metodo para ejecutar una consulta y devolver un conjunto de resultados
    public static ResultSet ejecutarConsultaConResultado(String consulta) {
        try {
            // Establecer la conexion con la base de datos
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Crear la declaracion
            PreparedStatement statement = connection.prepareStatement(consulta);

            // Ejecutar la consulta y devolver el conjunto de resultados
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Desarrolla un metodo en la clase Hospital que elimine un paciente indicando su nombre

}

class HospitalBasesDeDatos {
    //main del programa
    public static void main(String[] args) {
        //creamos un objeto de la clase Hospital
        Hospital hospital = new Hospital();
        //agregar un paciente de ejemplo
        //Date fechaActual = new Date(2023 - 1900, 1 - 1, 15);
        //Paciente paciente = new Paciente("Perez", 25, "Ninguno", 1, fechaActual);
        //Paciente paciente2 = new Paciente("Carlitos", 25, "Ninguno", 1, fechaActual);
        //hospital.agregarPaciente(paciente);
        //hospital.agregarPaciente(paciente2);

        //eliminar un paciente de ejemplo
        //hospital.eliminarPaciente("Perez");

        //asignar un doctor de cabecera a un paciente de ejemplo
        //hospital.asignarDoctorCabecera("Carlos", "Carlitos");

        //listar todos los pacientes
        //hospital.listarPacientes();

        //listar pacientes entre dos fechas
        Date fechaDesde = new Date(2023 - 1900, 1 - 1, 1);
        Date fechaHasta = new Date(2023 - 1900, 1 - 1, 10);
        hospital.listarPacientesEntreDosFechas(fechaDesde, fechaHasta);
    }
}


