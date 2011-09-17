/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Negocio;

import datos.Aula;
import datos.Curso;
import datos.Dia;
import datos.Edicion;
import datos.Empleado;
import datos.Inscripcion;
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
@WebService(serviceName = "ServicioAbmEdicion")
public class ServicioAbmEdicion {

    private ManejadorDatos manejadorDatos = ManejadorDatos.getInstance();

    @WebMethod(operationName = "addEdicion")
    public String addEdicion(@WebParam(name = "id") String id, @WebParam(name = "cursoId") String cursoId, @WebParam(name = "docenteId") Integer docenteId, @WebParam(name = "aulaId") String aulaId, @WebParam(name = "fechaInicio") Date fechaInicio, @WebParam(name = "fechaFin") Date fechaFin, @WebParam(name = "diasClase") Object[] diasClase) {
        String resultado;
        Edicion edicion = manejadorDatos.getById(Edicion.class, id);
        if (edicion == null) {
            Curso curso = manejadorDatos.getById(Curso.class, cursoId);
            Empleado docente = manejadorDatos.getById(Empleado.class, docenteId);
            Aula aula = manejadorDatos.getById(Aula.class, aulaId);
            edicion = new Edicion();
            edicion.setId(id);
            edicion.setFechaInicio(fechaInicio);
            edicion.setFechaFin(fechaFin);
            edicion.setAula(aula);
            edicion.setCurso(curso);
            edicion.setDocente(docente);
            manejadorDatos.save(edicion);
            setDiasClase(edicion, diasClase);
            manejadorDatos.save(edicion);
            resultado = "Edicion guardada : " + id;
        } else {
            resultado = "Edicion duplicada : " + id;
        }
        return resultado;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "modificarEdicion")
    public String modificarEdicion(@WebParam(name = "id") String id, @WebParam(name = "cursoId") String cursoId, @WebParam(name = "docenteId") Integer docenteId, @WebParam(name = "aulaId") String aulaId, @WebParam(name = "fechaInicio") Date fechaInicio, @WebParam(name = "fechaFin") Date fechaFin, @WebParam(name = "diasClase") Object[] diasClase) {
        String resultado;
        Edicion edicion = manejadorDatos.getById(Edicion.class, id);
        if (edicion == null) {
            resultado = "Edicion inexistente : " + id;
        } else {
            Curso curso = manejadorDatos.getById(Curso.class, cursoId);
            Empleado docente = manejadorDatos.getById(Empleado.class, docenteId);
            Aula aula = manejadorDatos.getById(Aula.class, aulaId);
            edicion.setFechaInicio(fechaInicio);
            edicion.setFechaFin(fechaFin);
            edicion.setAula(aula);
            edicion.setCurso(curso);
            edicion.setDocente(docente);
            setDiasClase(edicion, diasClase);
            manejadorDatos.save(edicion);
            resultado = "Edicion modificada : " + id;
        }
        return resultado;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "delEdicion")
    public String delEdicion(@WebParam(name = "id") String id) {
        String resultado;
        Edicion edicion = manejadorDatos.getById(Edicion.class, id);
        if (edicion == null) {
            resultado = "Edicion inexistente : " + id;
        } else {
            List<Inscripcion> inscritos = manejadorDatos.list(Inscripcion.class, " WHERE t.inscripcionPK.idEdicion = '" + id + "'");

            if (inscritos.isEmpty()) {
                edicion.getDiasClase().clear();
                manejadorDatos.delete(edicion);
                resultado = "Edicion eliminada : " + id;
            } else {
                resultado = "La edicion tiene [" + inscritos.size() + "] inscritos y no puede ser eliminada : " + id;
            }
        }
        return resultado;
    }

    //Operaciones Comunes para el funcionamiento AbmEdicion
    public List<Object[]> getEdiciones() {
        List<Edicion> ediciones = manejadorDatos.list(Edicion.class);
        return Utils.toArrayList(ediciones);
    }

    public List<Object[]> getDiasClase(String id) {
        Edicion edicion = manejadorDatos.getById(Edicion.class, id);
        return Utils.toArrayList(edicion.getDiasClase());
    }

    private void setDiasClase(Edicion edicion, Object[] diasClase) {
        edicion.getDiasClase().clear();
        for (Object item : diasClase) {
            Dia dia = manejadorDatos.getById(Dia.class, item.toString());
            edicion.getDiasClase().add(dia);
        }
    }

    public List<Object[]> getDocentes(String edicion) {
        List<Empleado> docentes;
        if (edicion.isEmpty()) {
            docentes = manejadorDatos.list(Empleado.class);
        } else {
            docentes = manejadorDatos.list("SELECT e FROM Empleado e WHERE e.ci NOT IN (SELECT i.inscripcionPK.idAlumno from Inscripcion i "
                    + "WHERE i.inscripcionPK.idEdicion = '" + edicion + "')");
        }
        return Utils.toArrayList(docentes);
    }
}
