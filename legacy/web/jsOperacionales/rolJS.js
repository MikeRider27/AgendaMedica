$(document).ready(function () {
    //alert('Conexion entre capas ');
    //recuperarDATOS();
    limpiar_deshabilitar_Campos_Cargos();
    Cargar_Tabla_Roles();
});
var opcion;
function armarJSON(banderaOperacion) {
    datosJSON = {
        //"cadena": "Juan"
        "operacion": banderaOperacion,
        "rol": $('#txtCargo').val(),
        "id": $('#txtId').val()
    };
}
function agregar() {
    opcion = 1;
    ABM();
}
function modificar() {
    opcion = 2;
    ABM();
}
function eliminar() {
    opcion = 3;
    ABM();
}
function listarSegunFiltro() {
    opcion = 4;
    ABM();
}
function limpiar_deshabilitar_Campos_Cargos() {
    $('#txtId').val("");
    $('#txtCargo').val("");
    $('#btGuardar').attr('disabled', true);
    $('#btModificar').attr('disabled', true);
    $('#btEliminar').attr('disabled', true);
    $('#btCancelar').attr('disabled', true);
    $('#btBuscar').attr('disabled', true);
    $('#btNuevo').attr('disabled', false);
    $('#txtId').attr('disabled', true);
    $('#txtCargo').attr('disabled', true);
}
function limpiar_habilitar_Campos_Campos_Cargos() {
//    $('#txtId').val("");
//    $('#txtCargo').val("");
    $('#btGuardar').attr('disabled', false);
    $('#btCancelar').attr('disabled', false);
    $('#btGuardar').attr('disabled', false);
    $('#btModificar').attr('disabled', false);
    $('#btEliminar').attr('disabled', false);
//    $('#btBuscar').attr('disabled', false);
    $('#txtId').attr('disabled', true);
//    $('#txtId').focus();
    $('#txtCargo').attr('disabled', false);
    $('#txtCargo').focus();
}
function campo_cargo(e) {
    tecla = (document.all) ? e.keyCode : e.which;
    if (tecla == 13)
        $('#txtCargo').focus();
}
function boton_guardar(e) {
    tecla = (document.all) ? e.keyCode : e.which;
    if (tecla == 13)
        $('#btGuardar').focus();
}
function dialogo_confirmacion() {
    var mensaje = confirm('GUARDAR ?');
    if (mensaje == true) {
        agregar();
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
        url: '/AgendaMedica_V00.2/RolSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
            $("#msmAviso").dialog("open");
            if (opcion == 4) {
                $.each(resp, function (indice, valor) {
                    $('#txtCargo').val(valor.vpais_descripcion);
                });
                $('#btNuevo').attr('disabled', false);
                $('#btModificar').attr('disabled', false);
                $('#btEliminar').attr('disabled', false);
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
            $("#error").show();
            alert('No existe cargo');
        }

    });
}

function Cargar_Tabla_Roles() {
    armarJSON(5);
    $.ajax({
        url: '/AgendaMedica_V00.2/RolSRV',
        data: datosJSON,
        type: 'POST',
        success: function (json_ciudad) {
            $.each(json_ciudad, function (indice, rol) {
                $("#tabla_cargos").append($("<tr>").append($("<td>" + rol.id + "</td>" +
                        "<td>" + rol.descripcion + "</td>")));
            });
        },
        error: function () {
            alert('No se pudo obtener ultimo valor en la tabla...!!!');
        }
    });
}




