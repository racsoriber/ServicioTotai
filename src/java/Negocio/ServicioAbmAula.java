/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Negocio;

import java.util.List;
import datos.Aula;
import datos.AulaDto;
import datos.ManejadorDatos;
import java.util.ArrayList;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author oscarribera
 */
@WebService(serviceName = "ServicioAbmAula")
public class ServicioAbmAula {

    private ManejadorDatos manejadorDatos = ManejadorDatos.getInstance();

    /**
     * Web service operation
     */
    @WebMethod(operationName = "addAula")
    public String addAula(@WebParam(name = "id") String id, @WebParam(name = "capacidad") Integer capacidad) {
        String resultado;
        Aula aula = manejadorDatos.getById(Aula.class, id);
        if (aula == null) {
            aula = new Aula();
            aula.setId(id);
            aula.setCapacidad(capacidad);
            manejadorDatos.save(aula);

            resultado = "Aula guardada exitosamente : " + id;
        } else {
            resultado = "Aula duplicada : " + id;
        }
        System.out.println("Resultado addAula:" + resultado);
        return resultado;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "modificarAula")
    public String modificarAula(@WebParam(name = "id") String id, @WebParam(name = "capacidad") Integer capacidad) {
        String resultado;
        Aula aula = manejadorDatos.getById(Aula.class, id);
        if (aula == null) {
            resultado = "Aula inexistente: " + id;
        } else {
            aula.setCapacidad(capacidad);
            manejadorDatos.save(aula);
            resultado = "Aula guardada : " + id;
        }
        return resultado;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "delAula")
    public String delAula(@WebParam(name = "id") String id) {
        String resultado;
        Aula aula = manejadorDatos.getById(Aula.class, id);
        if (aula.getEdiciones().isEmpty()) {
            manejadorDatos.delete(aula);
            resultado = "Aula eliminada : " + id;
        } else {
            resultado = "El aula no se puede eliminar, tiene [" + aula.getEdiciones().size() + "] ediciones activas";
        }
        return resultado;
    }
    //return Utils.toArrayList(manejadorDatos.list(Aula.class));

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getAulas")
    public java.util.List<String> getAulas() {
        java.util.List<String> result = new ArrayList<String>();
        for (Aula aula : manejadorDatos.list(Aula.class)) {
            result.add(aula.getId() + "," + aula.getCapacidad());
        }
        return result;
    }
}
