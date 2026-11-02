$(document).ready(function () {
    //alert('Conexion entre capas ');
    //recuperarDATOS();
    limpiar_deshabilitar_Campos_Cargos();
    Cargar_Tabla_Usuario();
    Cargar_Combo_Rol();
});
var opcion;
function armarJSON(banderaOperacion) {
    datosJSON = {
        //"cadena": "Juan"
        "operacion": banderaOperacion,
        "nombre": $('#txtCargo').val(),
        "clave": $('#txtclave2').val(),
        "id": $('#txtId').val(),
        "comborol": $('#combo_rol').val()
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
                banderita = 1;
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
            javascript:location.reload();
        } else {
            limpiar_deshabilitar_Campos_Cargos();
//            $("#tabla_cargos_body").empty();
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
        url: '/AgendaMedica_V00.2/UsuarioSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
            $("#msmAviso").dialog("open");
            if (opcion == 4) {
                $.each(resp, function (indice, valor) {
                    //$('#txtCargo').val(valor.cargo);
                    $('#combo_rol> option[value=' + valor.id_rol + ']').attr('selected', 'selected');
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

function Cargar_Tabla_Usuario() {
    armarJSON(5);
    $.ajax({
        url: '/AgendaMedica_V00.2/UsuarioSRV',
        data: datosJSON,
        type: 'POST',
        success: function (json_usuario) {
            //alert(json_usuario);
            $.each(json_usuario, function (indice, usu) {
                $("#tabla_cargos").append($("<tr>").append($("<td>" + usu.id_usuario + "</td>" +
                        "<td>" + usu.nombre_usuario + "</td>" +
                        "<td>" + usu.clave_usuario + "</td>" +
                        "<td>" + usu.rol_descripcion + "</td>")));

            });
        },
        error: function () {
            alert('No se pudo obtener ultimo valor en la tabla...!!!');
        }
    });
}

function Cargar_Combo_Rol() {
    armarJSON(6);
    $.ajax({
        url: '/AgendaMedica_V00.2/UsuarioSRV',
        data: datosJSON,
        type: 'POST',
        success: function (json_ciudad) {
            $.each(json_ciudad, function (indice, rol) {
                $("#combo_rol").append("<option value= \"" + rol.id + "\"> " + rol.id + " - " + rol.descripcion + "</option>");
            });
        },
        error: function () {
            alert('No se pudo obtener ultimo valor en el combo...!!!');
        }
    });
}




