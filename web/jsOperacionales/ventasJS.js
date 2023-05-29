/*
 * Proyecto de Juan
 */

var acu = 0;
var tindex = 0;
var factura;
var jsonCabDet;

function armarJSON(bandera, id) {
    datosJSON = {
        "opcion": bandera,
        "identificador": id
    };
}

function crearJsonCabDetMaestro() {
    var listaArticulos = [];
    $("#tabla_detalle  tr").each(function () {
        //push => Agrega un nuevo elemento al Array [listaArticulos]
        listaArticulos.push(
                {id_producto: $(this).find("td").eq(0).html(), cantidad: $(this).find("td").eq(2).html()}
        );
    });

    factura = {
        "fecha": $("#fecha").val(),
        "obs": $("#osbv").val(),
        "id_funcionario": $("#id_usuario").val(),
        "id_cliente": $("#id_cliente").val(),
        "detalle": listaArticulos
    };
    // El método JSON.stringify() convierte un valor dado en javascript a una cadena  JSON,
//    alert('Fecha = ' + factura.fecha + ', ' + 'Observacion = ' + factura.observacion +
//            ', Codigousuario= ' + factura.codigoUsuario + ', CodigoCliente= ' + factura.codigoCliente +
//            ', detapresu ' + factura.detalle[0].id_producto);
    jsonCabDet = JSON.stringify(factura);

//    allClientes();
}
function AgregarPedidoV() {
//    alert('Agregar pedido');
    crearJsonCabDetMaestro();
    $.ajax({
        url: "/AgendaMedica_V00.2/VentaSRV",
        data: jsonCabDet,
        type: 'POST',
        success: function () {

        },
        error: function () {
            //alert('No se pudo obtener ultimo valor...!!!');
        }
    });
    Limpiar();
    CamposOFF();
    BotonesOFF();
}
var bandera, id;
function AnularPedido() {
    id = $('#id').val();
    bandera = 29;
    armarJSON(bandera, id);
    $.ajax({
        url: "/AgendaMedica_V00.2/buscadorSRV",
        data: datosJSON,
        type: 'POST',
        success: function () {

        },
        error: function () {
            //alert('No se pudo obtener ultimo valor...!!!');
        }
    });
    BotonCancelar();
}

function agregarFila() {
    //alert('Llega');

    var cod = $('#codigoInsumo').val();
    var des = $('#descripcion').val();
    var cant = $('#cantidad').val();
    var precio = $('#precio').val();
    var calculo = cant * precio;
    acu = acu + calculo;
    $('#montoTotal').val(acu);
    tindex++;
    $('#tablaDetalleInsumos').append("<tr id=\'prod" + tindex + "\'>\n\
            <td style=' width: 5%;'>" + cod + "</td>\n\
            <td style=' width: 60%;'>" + des + "</td>\n\
            <td style=' width: 5%;'>" + cant + "</td>\n\
            <td style=' width: 25%;'>" + calculo + "</td>\n\
            <td style=' width: 5%;'><img onclick=\"$(\'#prod" + tindex + "\').remove();updateMonto(" + calculo + "," + tindex + ")\" src='../Recursos/img/update.png'/></td>\n\
            <td style=' width: 5%;'><img onclick=\"$(\'#prod" + tindex + "\').remove();updateMonto(" + calculo + "," + tindex + ")\" src='../Recursos/img/delete.png'/></td>\n\
      </tr>");
}


function updateMonto(monto, ind) {
    var montoActual = $('#montoTotal').val();
    var calculo = montoActual - monto;
    $('#montoTotal').val(calculo);
    // alert(ind);
    if (ind === 1) {
        acu = 0;
        tindex = 0;
        $('#montoTotal').val(null);
    }
    calculo = 0;
    montoActual = 0;

}



function limpiarCamposInsumos() {
    $('#codigoInsumo').val(null);
    $('#descripcion').val(null);
    $('#cantidad').val(null);
    $('#precio').val(null);
    $('#codigoInsumo').focus();
}

