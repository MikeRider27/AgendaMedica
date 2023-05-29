$(document).ready(function () {
    //alert('Conexion entre capas ');
    //recuperarDATOS();
    limpiar_deshabilitar_Campos_Cargos();
    Cargar_Tabla();
    //ComboTcargo();
});
var opcion;
function armarJSON(banderaOperacion) {
    datosJSON = {
        //"cadena": "Juan"
        "id": $('#txtId').val(),
        "operacion": banderaOperacion,
        "id_persona": $('#txtid_persona').val(),
        "nombre": $('#txtNombre').val(),
        "ci": $('#txtci').val(),
        "seguro": $('#txtnroseguro').val()
    };
}


function agregar() {
    opcion = 2;
    ABM();
}
function modificar() {
    opcion = 3;
    ABM();
}
function eliminar() {
    opcion = 4;
    ABM();
}
function listarSegunFiltro() {
    opcion = 5;
    ABM();
}
function limpiar_deshabilitar_Campos_Cargos() {
    $('#txtId').val("");
    $('#txtid_persona').val("");
    $('#txtNombre').val("");
    $('#txtci').val("");
    $('#txtnroseguro').val("");


    $('#txtId').attr('disabled', true);
    $('#txtid_persona').attr('disabled', true);
    $('#txtNombre').attr('disabled', true);
    $('#txtci').attr('disabled', true);
    $('#txtnroseguro').attr('disabled', true);


    $('#btGuardar').attr('disabled', true);
    $('#btModificar').attr('disabled', true);
    $('#btEliminar').attr('disabled', true);
    $('#btCancelar').attr('disabled', true);
    $('#btBuscar').attr('disabled', true);
    $('#btNuevo').attr('disabled', false);

}
function limpiar_habilitar_Campos_Campos_Cargos() {
//    $('#txtId').val("");
//    $('#txtCargo').val("");
    $('#btGuardar').attr('disabled', false);
    $('#btCancelar').attr('disabled', false);
    $('#btGuardar').attr('disabled', false);
    $('#btModificar').attr('disabled', false);
    $('#btEliminar').attr('disabled', false);
    $('#combo_tcargo').attr('disabled', false);
//    $('#btBuscar').attr('disabled', false);
    $('#txtId').attr('disabled', true);
    $('#txtid_persona').attr('disabled', false);
    $('#txtid_persona').focus();
    //$('#txtNombre').attr('disabled', false);
    //$('#txtci').attr('disabled', false);
    $('#txtnroseguro').attr('disabled', false);
    //$('#txtesp').attr('disabled', false);

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
        limpiar_deshabilitar_Campos_Cargos();
        document.getElementById("titulo").innerHTML = "Aviso";
        document.getElementById("texto").innerHTML = "Se rechazo la opcion !";
        $('#miAlerta').show('.fade');
    }
}


function ABM() {
    armarJSON(opcion);
    $.ajax({
        url: '/AgendaMedica_V00.2/ClienteSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
            $("#msmAviso").dialog("open");
            if (opcion == 5) {
                $.each(resp, function (indice, valor) {
                    $('#combo_tcargo> option[value=' + valor.id_tcargo + ']').attr('selected', 'selected');
                    $('#txtid_persona').val(valor.id_persona);
                    $('#txtid_espe').val(valor.id_espec);
                    $('#txtid_subc').val(valor.id_subcargo);
                });
                $('#btNuevo').attr('disabled', false);
                $('#btModificar').attr('disabled', false);
                $('#btEliminar').attr('disabled', false);
            } else {
                if (opcion == 3) {
                    javascript:location.reload();
                }
                if (opcion == 4) {
                    javascript:location.reload();
                }
            }

        },
        error: function (resp) {
            $("#error").show();
            alert('No existe esas opciones');
        }

    });
}

function Cargar_Tabla() {
    armarJSON(1);
    $.ajax({
        url: '/AgendaMedica_V00.2/ClienteSRV',
        data: datosJSON,
        type: 'POST',
        success: function (json_usuario) {
            //alert(json_usuario);
            $.each(json_usuario, function (indice, usu) {
                $("#tabla_cargos").append($("<tr>").append($("<td>" + usu.id + "</td>" +
                        "<td>" + usu.id_persona + "</td>" +
                        "<td>" + usu.nro_seguro + "</td>" +
                        "<td>" + usu.nombre_persona + "</td>" +
                        "<td>" + usu.ci_persona + "</td>")));

            });
        },
        error: function () {
            alert('No se pudo obtener ultimo valor en la tabla...!!!');
        }
    });
}
function ComboTcargo() {
    armarJSON(5);
    $.ajax({
        url: '/AgendaMedica_V00.2/CargoSRV',
        data: datosJSON,
        type: 'POST',
        success: function (json) {
            $.each(json, function (indice, valor) {
                $("#combo_tcargo").append("<option value= \"" + valor.id + "\"> " + valor.id + " - " + valor.cargo + "</option>");
            });
        },
        error: function () {
            alert('No se pudo obtener ultimo valor en el combo cargo...!!!');
        }
    });
}

function validarCamposOriginal(e) {
    tecla = (document.all) ? e.keyCode : e.which;
    var valor;
    if (tecla == 13) {
        //alert('presionaste enter');
        valor = document.getElementById("txtid_persona").value;
        //alert('hay ' + valor);
        if (valor == null || valor.length == 0 || /^\s+$/.test(valor)) {
            alert('No debe quedar vacio el campo Id persona');
            $('#txtid_persona').focus();
        }
    }
}

function resolverEspecialidad() {
    //alert('pulsaste una tecla ');
    var valor = document.getElementById("txtid_espe").value;
    if (!isNaN(valor) == true) {
        //alert('Ingresaste un numero' + valor);
        if (valor == 0) {
            $('#txtid_subc').attr('disabled', false);
            document.getElementById('txtid_subc').value = "";
            document.getElementById('txtsub').value = "";
        } else {
            $('#txtid_subc').attr('disabled', true);
            document.getElementById('txtid_subc').value = "0";
            recuperarSubcargo_Validado();
        }
    } else {
        //alert('Ingresaste un texto' + valor);
        document.getElementById('txtid_espe').value = "";
    }
}
function resolverSubcargo() {
    //alert('pulsaste una tecla ');
    var valor = document.getElementById("txtid_subc").value;
    if (!isNaN(valor) == true) {
        //alert('Ingresaste un numero' + valor);
        if (valor == 0) {
            $('#txtid_espe').attr('disabled', false);
            document.getElementById('txtid_espe').value = "";
            document.getElementById('txtesp').value = "";
        } else {
            $('#txtid_espe').attr('disabled', true);
            document.getElementById('txtid_espe').value = "0";
            recuperarEspecialidad_Validado();
        }
    } else {
        //alert('Ingresaste un texto' + valor);
        document.getElementById('txtid_subc').value = "";
    }
}





