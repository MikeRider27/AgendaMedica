$(document).ready(function () {
    //alert('Conexion entre capas ');
    //recuperarDATOS();
    limpiar_deshabilitar_Campos_Cargos();
    Cargar_Tabla_Cargos();
});
var opcion;
function armarJSON(banderaOperacion) {
    datosJSON = {
        //"cadena": "Juan"
        "operacion": banderaOperacion,
        "nombre": $('#txtCargo').val(),
        "clave": $('#txtclave2').val(),
        "id": $('#txtId').val()
    };
}

function ComprobarClave(opcion) {
    datosRegistro = {
        "clave1": $('#txtclave1').val(),
        "clave2": $('#txtclave2').val()
    };
    var clave1 = datosRegistro.clave1;
    var clave2 = datosRegistro.clave2;

    if (clave1 == clave2) {
        alert('Correcto');
        switch (opcion) {
            case 1://Agregar
                ABM();
                banderita=1;
                break;
            case 2://Modificar
                ABM();
                break;
        }
    } else {
        alert('Se ingreso mal la contraseña, verifique por favor');
    }
}

function agregar() {
    opcion = 1;
    ComprobarClave(opcion);
}
function modificar() {
    opcion = 2;
    ComprobarClave(opcion);
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
    $('#txtclave1').val("");
    $('#txtclave2').val("");
    $('#btGuardar').attr('disabled', true);
    $('#btModificar').attr('disabled', true);
    $('#btEliminar').attr('disabled', true);
    $('#btCancelar').attr('disabled', true);
    $('#btBuscar').attr('disabled', true);
    $('#btNuevo').attr('disabled', false);
    $('#txtId').attr('disabled', true);
    $('#txtCargo').attr('disabled', true);
    $('#txtclave1').attr('disabled', true);
    $('#txtclave2').attr('disabled', true);
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
    $('#txtclave1').attr('disabled', false);
    $('#txtclave2').attr('disabled', false);
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
//        alert('Se ha guardado el mensaje');
        if (banderita == 1) {
            limpiar_deshabilitar_Campos_Cargos();
            $("#tabla_cargos_body").empty();
            document.getElementById("titulo").innerHTML = "Aviso";
            document.getElementById("texto").innerHTML = "Se guardo el registro ! Refresque la pagina";
            $('#miAlerta').show('.fade');
        } else {
            limpiar_deshabilitar_Campos_Cargos();
            $("#tabla_cargos_body").empty();
            document.getElementById("titulo").innerHTML = "Aviso";
            document.getElementById("texto").innerHTML = "No se guardo el registro !";
            $('#miAlerta').show('.fade');
        }

    } else {
        limpiar_deshabilitar_Campos_Cargos();
        document.getElementById("titulo").innerHTML = "Aviso";
        document.getElementById("texto").innerHTML = "Se rechazo la opcion !";
        $('#miAlerta').show('.fade');
    }
}


function ABM() {
    armarJSON(opcion);
    $.ajax({
        url: '/AgendaMedica_V00.1/UsuarioSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
            $("#msmAviso").dialog("open");
            if (opcion == 4) {
                $.each(resp, function (indice, valor) {
                    //$('#txtCargo').val(valor.cargo);
                });
                $('#btNuevo').attr('disabled', false);
                $('#btModificar').attr('disabled', false);
                $('#btEliminar').attr('disabled', false);
            } else {
                if (opcion == 2) {
                    limpiar_deshabilitar_Campos_Cargos();
                    $("#tabla_cargos_body").empty();
                    document.getElementById("titulo").innerHTML = "Aviso";
                    document.getElementById("texto").innerHTML = "Se modifico el registro ! Refresque la pagina";
                    $('#miAlerta').show('.fade');
                }
                if (opcion == 3) {
                    limpiar_deshabilitar_Campos_Cargos();
                    $("#tabla_cargos_body").empty();
                    document.getElementById("titulo").innerHTML = "Aviso";
                    document.getElementById("texto").innerHTML = "Se elimino el registro !Refresque la pagina";
                    $('#miAlerta').show('.fade');
                }
            }

        },
        error: function (resp) {
            $("#error").show();
            alert('No existe cargo');
        }

    });
}

function Cargar_Tabla_Cargos() {
    armarJSON(5);
    $.ajax({
        url: '/AgendaMedica_V00.1/UsuarioSRV',
        data: datosJSON,
        type: 'POST',
        success: function (json_ciudad) {
            $.each(json_ciudad, function (indice, ciudad) {
                $("#tabla_cargos").append($("<tr>").append($("<td>" + ciudad.id_usuario + "</td>" +
                        "<td>" + ciudad.nombre_usuario + "</td>" +
                        "<td>" + ciudad.clave_usuario + "<td>")));
            });
        },
        error: function () {
            alert('No se pudo obtener ultimo valor en la tabla...!!!');
        }
    });
}




