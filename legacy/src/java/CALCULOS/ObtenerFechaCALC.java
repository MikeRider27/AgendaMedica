package CALCULOS;

import java.util.Calendar;

public class ObtenerFechaCALC {

    String dia, mes, ano, fecha, fechaUSA, fechaUSA2;
    Calendar objeto = Calendar.getInstance();

    public String PrepararFecha() {
        dia = Integer.toString(objeto.get(Calendar.DATE));
        mes = Integer.toString(objeto.get(Calendar.MONTH) + 1);
        ano = Integer.toString(objeto.get(Calendar.YEAR));
        fecha = dia + "/" + mes + "/" + ano;
        fechaUSA = ano + "/" + mes + "/" + dia;
        return fecha;
    }

    public String PrepararFechaUSA() {
        dia = Integer.toString(objeto.get(Calendar.DATE));
        mes = Integer.toString(objeto.get(Calendar.MONTH) + 1);
        ano = Integer.toString(objeto.get(Calendar.YEAR));
        //fecha = dia + "/" + mes + "/" + ano;
        fechaUSA = ano + "-" + mes + "-" + dia;
        return fechaUSA;
    }

    public String PrepararFechaSeparador() {
        dia = Integer.toString(objeto.get(Calendar.DATE));
        mes = Integer.toString(objeto.get(Calendar.MONTH) + 1);
        ano = Integer.toString(objeto.get(Calendar.YEAR));
        fechaUSA2 = dia + "-" + mes + "-" + ano;
        return fechaUSA2;
    }

    public void MostrarFechaActual() {
        System.out.println(fecha);
    }
}
