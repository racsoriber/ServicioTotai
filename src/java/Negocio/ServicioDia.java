/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Negocio;

import datos.Dia;
import datos.DiaEnum;
import datos.ManejadorDatos;
import javax.jws.WebService;
import javax.jws.WebMethod;
import java.util.List;

/**
 *
 * @author oscarribera
 */
@WebService(serviceName = "ServicioDia")
public class ServicioDia {

    private ManejadorDatos manejadorDatos = ManejadorDatos.getInstance();

    @WebMethod(operationName = "getDias")
    public List<Object[]> getDias() {
        init();
        List<Dia> dias = manejadorDatos.list(Dia.class);
        return Utils.toArrayList(dias);
    }
      public void init(){        
        List<Dia> dias = manejadorDatos.list(Dia.class);

        if(dias.isEmpty()){
            for(DiaEnum diaEnum:DiaEnum.values()){
                Dia dia = new Dia(diaEnum.name());
                manejadorDatos.save(dia);
            }
        }
    }
}
