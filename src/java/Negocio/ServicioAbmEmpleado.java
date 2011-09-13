/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Negocio;

import datos.Empleado;
import datos.ManejadorDatos;
import java.util.Date;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author oscarribera
 */
@WebService(serviceName = "ServicioAbmEmpleado")
public class ServicioAbmEmpleado {

    private ManejadorDatos manejadorDatos = ManejadorDatos.getInstance();

    @WebMethod(operationName = "addEmpleado")
    public String addEmpleado(@WebParam(name = "ci") Integer ci, @WebParam(name = "nombre") String nombre, @WebParam(name = "fechaIngreso") Date fechaIngreso) {
        String resultado;
        Empleado empleado = manejadorDatos.getById(Empleado.class, ci);

        if (empleado == null) {
            empleado = new Empleado();
            empleado.setCi(ci);
            empleado.setNombre(nombre);
            empleado.setFechaIngreso(fechaIngreso);
            manejadorDatos.save(empleado);

            resultado = "Empleado guardado : " + ci;
        } else {
            resultado = "Empleado duplicado : " + ci;
        }
        return resultado;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "modificarEmpleado")
    public String modificarEmpleado(@WebParam(name = "ci") Integer ci, @WebParam(name = "nombre") String nombre, @WebParam(name = "fechaIngreso") Date fechaIngreso) {
        String resultado;
        Empleado empleado = manejadorDatos.getById(Empleado.class, ci);

        if (empleado == null) {
            resultado = "Empleado inexistente : " + ci;
        } else {
            empleado.setNombre(nombre);
            empleado.setFechaIngreso(fechaIngreso);
            manejadorDatos.save(empleado);

            resultado = "Empleado modificado : " + ci;
        }
        return resultado;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "eliminarEmpleado")
    public String eliminarEmpleado(@WebParam(name = "ci") Integer ci) {
        String resultado;
        Empleado empleado = manejadorDatos.getById(Empleado.class, ci);

        if (empleado == null) {
            resultado = "Empleado inexistente : " + ci;
        } else {
            if (empleado.getEdiciones().isEmpty()) {
                //eliminar todas las inscripciones
                ServicioAbmInscripcion controlInscripcion = new ServicioAbmInscripcion(); //Esta linea modificada
                controlInscripcion.eliminarByEmpleado(empleado);
                manejadorDatos.delete(empleado);
                resultado = "Empleado eliminado : " + ci;
            } else {
                resultado = "El empleado tiene [" + empleado.getEdiciones().size() + "] ediciones de curso registradas como docente : " + ci;
            }
        }
        return resultado;
    }

    //OPERACIONES PARA EL FUNCIONAMIENTO DE SERVICIO ABMEMPLEADO
    public List<Object[]> getEmpleados() {
        return Utils.toArrayList(manejadorDatos.list(Empleado.class));
    }
}
