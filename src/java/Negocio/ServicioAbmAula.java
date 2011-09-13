/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Negocio;

import java.util.List;
import datos.Aula;
import datos.ManejadorDatos;
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
        return resultado;
    }

    //Metodos Utiles para el Negocio (No son Servicios propiamente dicho)
    public List<Object[]> getAulas() {
        return Utils.toArrayList(manejadorDatos.list(Aula.class));
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
}
