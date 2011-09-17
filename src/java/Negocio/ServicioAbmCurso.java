/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Negocio;

import datos.Curso;
import datos.ManejadorDatos;
import java.util.Collections;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author oscarribera
 */
@WebService(serviceName = "ServicioAbmCurso")
public class ServicioAbmCurso {

    private ManejadorDatos manejadorDatos = ManejadorDatos.getInstance();

    /**
     * Web service operation
     */
    @WebMethod(operationName = "addCurso")
    public String addCurso(@WebParam(name = "codigo") String codigo, @WebParam(name = "titulo") String titulo, @WebParam(name = "tema") String tema, @WebParam(name = "hora") Integer hora, @WebParam(name = "requisitos") Object[] requisitos) {
        String resultado;
        Curso curso = (Curso) manejadorDatos.getById(Curso.class, codigo);
        if (curso == null) {
            curso = new Curso();
            curso.setCodigo(codigo);
            curso.setTitulo(titulo);
            curso.setTema(tema);
            curso.setDuracionHoras(hora);
            setRequisitos(curso, requisitos);

            manejadorDatos.save(curso);
            resultado = "Curso guardado : " + codigo;
        } else {
            resultado = "Curso duplicado : " + codigo;
        }
        return resultado;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "modificarCurso")
    public String modificarCurso(@WebParam(name = "codigo") String codigo, @WebParam(name = "titulo") String titulo, @WebParam(name = "tema") String tema, @WebParam(name = "hora") Integer hora, @WebParam(name = "requisitos") Object[] requisitos) {
        String resultado;
        Curso curso = (Curso) manejadorDatos.getById(Curso.class, codigo);
        if (curso == null) {
            resultado = "No existe este Curso a Modificar : " + codigo;
        } else {
            curso.setTitulo(titulo);
            curso.setTema(tema);
            curso.setDuracionHoras(hora);
            setRequisitos(curso, requisitos);
            manejadorDatos.save(curso);
            resultado = "Curso Modificado exitosamente" + codigo;
        }
        return resultado;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "delCurso")
    public String delCurso(@WebParam(name = "codigo") String codigo) {
        String resultado;
        Curso curso = (Curso) manejadorDatos.getById(Curso.class, codigo);
        if (curso == null) {
            resultado = "No existe este Curso a Eliminar : " + codigo;
        } else {
            curso.getRequisitos().clear();
            manejadorDatos.delete(curso);
            resultado = "Curso eliminado exitosamente" + codigo;
        }
        return resultado;
    }

    //Estos son Metodos para el funcionamiento de la Servicio Curso
    public List<Object[]> getCursos() {
        List<Curso> cursos = manejadorDatos.list(Curso.class);
        return Utils.toArrayList(cursos);
    }

    public List<Object[]> getRequisitos(String codigo) {
        Curso curso = manejadorDatos.getById(Curso.class, codigo);
        List<Object[]> resultado;
        if (curso == null) {
            resultado = Collections.EMPTY_LIST;
        } else {
            resultado = Utils.toArrayList(curso.getRequisitos());
        }
        return resultado;
    }

    public List<Object[]> filtrarCursos(Object[] arrayCodigos) {
        StringBuilder buffer = new StringBuilder();
        if (arrayCodigos.length > 0) {
            buffer.append(" WHERE t.codigo NOT IN (");

            for (int i = 0; i < arrayCodigos.length; i++) {
                buffer.append("'" + arrayCodigos[i] + "'");
                if ((i + 1) < arrayCodigos.length) {
                    buffer.append(",");
                }
            }
            buffer.append(")");
        }
        return Utils.toArrayList(manejadorDatos.list(Curso.class, buffer.toString()));
    }

    public void setRequisitos(Curso curso, Object[] requisitos) {
        curso.getRequisitos().clear();
        for (Object item : requisitos) {
            Curso requisito = manejadorDatos.getById(Curso.class, item.toString());
            curso.getRequisitos().add(requisito);
        }
    }
}
