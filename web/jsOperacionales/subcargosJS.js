$(document).ready(function () {
    //alert('Conexion entre capas ');
    //recuperarDATOS();
    limpiar_deshabilitar_CamposS();
    Cargar_Tabla_Subcargo();
    //Cargar_Combo_Cargo();
});
var opcion;
function armarJSON(banderaOperacion) {
    datosJSON = {
        //"cadena": "Juan"
        "operacion": banderaOperacion,
        "subcargo": $('#txtSubcargo').val(),
        "id": $('#txtId').val()
        //"combocargo": $('#combo_cargo').val()
    };
}
function agregarS() {
    opcion = 1;
    ABM();
}
function modificarS() {
    opcion = 2;
    ABM();
}
function eliminarS() {
    opcion = 3;
    ABM();
}
function listarSegunFiltroS() {
    opcion = 4;
    ABM();
}
function limpiar_deshabilitar_CamposS() {
    /*Limpiar campos*/
    $('#txtId').val("");
    $('#txtSubcargo').val("");

    /*Deshabilitar componentes*/
//    $('#btGuardar').attr('disabled', true);
//    $('#btModificar').attr('disabled', true);
//    $('#btEliminar').attr('disabled', true);
//    $('#btCancelar').attr('disabled', true);
//    $('#btBuscar').attr('disabled', true);
//    $('#btNuevo').attr('disabled', false);
    $('#txtId').attr('disabled', true);
    $('#txtSubcargo').attr('disabled', true);
    $('#txtIdcargo').attr('disabled', true);
}
function limpiar_habilitar_CamposS() {
    /*Limpiar campos*/
//    $('#txtId').val("");
//    $('#txtSubcargo').val("");
//    $('#txtIdcargo').val("");
    /*Habilitar componentes*/
    $('#btGuardar').attr('disabled', false);
    $('#btCancelar').attr('disabled', false);
    $('#btBuscar').attr('disabled', false);
    $('#txtId').attr('disabled', true);
    $('#txtSubcargo').attr('disabled', false);
    $('#txtSubcargo').focus();
    $('#combo_cargo').attr('disabled', false);
}
function campo_ciudadS(e) {
    tecla = (document.all) ? e.keyCode : e.which;
    if (tecla == 13)
        $('#txtSubcargo').focus();

}
function campo_IDpaisS(e) {
    tecla = (document.all) ? e.keyCode : e.which;
    if (tecla == 13)
        $('#txtIdcargo').focus();

}
function boton_guardarS(e) {
    tecla = (document.all) ? e.keyCode : e.which;
    if (tecla == 13)
        $('#btGuardar').focus();
}
function dialogo_confirmacionS() {
    var mensaje = confirm('GUARDAR ?');
    if (mensaje == true) {
        agregarS();
        javascript:location.reload();
    } else {
        document.getElementById("titulo").innerHTML = "Aviso";
        document.getElementById("texto").innerHTML = "No se guardo el registro !";
        $('#miAlerta').show('.fade');
    }
}


function ABM() {
    armarJSON(opcion);
    $.ajax({
        url: '/AgendaMedica_V00.2/SubcargoSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
            $("#msmAviso").dialog("open");
            if (opcion == 4) {
                $.each(resp, function (indice, valor) {
                    $('#txtSubcargo').val(valor.descripcion);
                    $('#combo_cargo> option[value=' + valor.id + ']').attr('selected', 'selected');
                });
                $('#btNuevo').attr('disabled', false);
                $('#btModificar').attr('disabled', false);
                $('#btEliminar').attr('disabled', false);
                $('#btCancelar').attr('disabled', false);
            } else {
                if (opcion == 2) {
                    javascript:location.reload();
                }

                if (opcion == 3) {
                   javascript:location.reload();
                }

            }

        },
        error: function (resp) {
           // alert('No existe cargo macho');
        }

    });
}

function Cargar_Tabla_Subcargo() {
    armarJSON(5);
    $.ajax({
        url: '/AgendaMedica_V00.2/SubcargoSRV',
        data: datosJSON,
        type: 'POST',
        success: function (json_ciudad) {
            $.each(json_ciudad, function (indice, ciudad) {
                $("#tabla_subcargos").append($("<tr>").append($("<td>" + ciudad.id + "</td>" +
                        "<td>" + ciudad.descripcion + "</td>")));

            });
        },
        error: function () {
            alert('No se pudo obtener ultimo valor en la tabla...!!!');
        }
    });
}







