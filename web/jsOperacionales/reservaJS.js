$(document).ready(function () {
    //alert('Conexion entre capas ');
    //recuperarDATOS();
    limpiar_deshabilitar_Campos_Cargos();
    Cargar_Tabla_Reserva();
    Cargar_Combos();
    calendario();

});
var opcion;
function armarJSON(banderaOperacion) {
    datosJSON = {
        //"cadena": "Juan"
        "operacion": banderaOperacion,
        "id": $('#txtId').val(),
        "fecha": $('#txtfecha').val(),
        "hora": $('#txthora').val(),
        "comboturno": $('#combo_turno').val(),
        "idfuncionario": $('#txtid_funcionario').val(),
        "nombrefuncionario": $('#txtnombre_funcionario').val(),
        "idcliente": $('#txtid_cliente').val(),
        "nombrecliente": $('#txtnombre_cliente').val(),
        "ci": $('#txtci_cliente').val(),
        "combotiporeserva": $('#combo_tiporeserva').val(),
        "comboestadoreserva": $('#combo_estadoreserva').val(),
        "combodia": $('#combo_dia').val(),
        "idmedico": $('#txtid_medico').val(),
        "nombremedico": $('#txtnombre_medico').val(),
        "comboespecialidad": $('#combo_especialidad').val(),
        "cupo": 5,
        "descripcion": 5,
        "cupoactual": $('#txtactual_cupo').val(),
        "cuporestante": $('#txtrestante_cupo').val()
    };
}


function agregar() {
    opcion = 3;
    ABM();
}
function modificar() {
    opcion = 4;
    ABM();
}
function eliminar() {
    opcion = 5;
    ABM();
}
function listarSegunFiltro() {
    opcion = 2;
    ABM();
}
function limpiar_deshabilitar_Campos_Cargos() {
    $('#txtId').val("");
    $('#txtfecha').val("");
    $('#txthora').val("");
    $('#txtid_funcionario').val("");
    $('#txtnombre_funcionario').val("");
    $('#txtid_cliente').val("");
    $('#txtnombre_cliente').val("");
    $('#txtci_cliente').val("");
    $('#txtid_medico').val("");
    $('#txtnombre_medico').val("");
    $('#txtrestante_cupo').val("");
    $('#txtactual_cupo').val("");

    $('#btGuardar').attr('disabled', true);
    $('#btModificar').attr('disabled', true);
    $('#btEliminar').attr('disabled', true);
    $('#btCancelar').attr('disabled', false);
    $('#btBuscar').attr('disabled', true);
    $('#btNuevo').attr('disabled', false);

    $('#txtId').attr('disabled', true);
    $('#txtfecha').attr('disabled', true);
    $('#txthora').attr('disabled', true);
    $('#txtid_funcionario').attr('disabled', true);
    $('#txtnombre_funcionario').attr('disabled', true);
    $('#txtid_cliente').attr('disabled', true);
    $('#txtnombre_cliente').attr('disabled', true);
    $('#txtci_cliente').attr('disabled', true);
    $('#txtid_medico').attr('disabled', true);
    $('#txtnombre_medico').attr('disabled', true);
    $('#txtrestante_cupo').attr('disabled', true);
    $('#combo_turno').attr('disabled', true);
    $('#combo_tiporeserva').attr('disabled', true);
    $('#combo_estadoreserva').attr('disabled', true);
    $('#combo_dia').attr('disabled', true);
    $('#combo_especialidad').attr('disabled', true);
    $('#txtactual_cupo').attr('disabled', true);
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


    $('#txtfecha').attr('disabled', false);

    $('#txthora').attr('disabled', false);
    $('#txtid_funcionario').attr('disabled', false);
    $('#txtid_funcionario').focus();
    $('#txtnombre_funcionario').attr('disabled', true);
    $('#txtid_cliente').attr('disabled', false);
    $('#txtnombre_cliente').attr('disabled', true);
    $('#txtci_cliente').attr('disabled', true);
    $('#txtid_medico').attr('disabled', false);
    $('#txtnombre_medico').attr('disabled', true);
    $('#combo_turno').attr('disabled', false);
    $('#combo_tiporeserva').attr('disabled', false);
    $('#combo_estadoreserva').attr('disabled', false);
    $('#combo_dia').attr('disabled', false);
    $('#combo_especialidad').attr('disabled', false);
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
//        limpiar_deshabilitar_Campos_Cargos();
//        $("#tabla_cargos_body").empty();
//        document.getElementById("titulo").innerHTML = "Aviso";
//        document.getElementById("texto").innerHTML = "Se guardo el registro ! Refresque la pagina";
//        $('#miAlerta').show('.fade');
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
        url: '/AgendaMedica_V00.2/ReservaSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
            $("#msmAviso").dialog("open");
            if (opcion == 2) {
                $.each(resp, function (indice, valor) {
                    $('#txtfecha').val(valor.fecha);
                    $('#txthora').val(valor.hora);
                    $('#txtid_funcionario').val(valor.id_funcionario_fun);
                    $('#txtnombre_funcionario').val(valor.desc_funcionario);
                    $('#txtid_cliente').val(valor.id_cliente);
                    $('#txtnombre_cliente').val(valor.desc_cliente);
                    $('#txtci_cliente').val(valor.ci);
                    $('#txtid_medico').val(valor.id_funcionario_med);
                    $('#txtnombre_medico').val(valor.desc_medico);
                    $('#txtactual_cupo').val(valor.cupoactual);
                    $('#txtrestante_cupo').val(valor.resta);
                    //alert('El dato recuperado de la bd es  ' + valor.fecha);
                    $('#combo_turno> option[value=' + valor.id_turno + ']').attr('selected', 'selected');
                    $('#combo_tiporeserva> option[value=' + valor.id_tiporeserva + ']').attr('selected', 'selected');
                    $('#combo_estadoreserva> option[value=' + valor.id_estadoreserva + ']').attr('selected', 'selected');
                    $('#combo_dia> option[value=' + valor.id_dia + ']').attr('selected', 'selected');
                    $('#combo_especialidad> option[value=' + valor.id_espec + ']').attr('selected', 'selected');
                });
                $('#btNuevo').attr('disabled', false);
                $('#btModificar').attr('disabled', false);
                $('#btEliminar').attr('disabled', false);
            } else {
                if (opcion == 4) {
//                    limpiar_deshabilitar_Campos_Cargos();
//                    $("#tabla_cargos_body").empty();
//                    document.getElementById("titulo").innerHTML = "Aviso";
//                    document.getElementById("texto").innerHTML = "Se modifico el registro ! Refresque la pagina";
//                    $('#miAlerta').show('.fade');
                    javascript:location.reload();
                }
                if (opcion == 5) {
//                    limpiar_deshabilitar_Campos_Cargos();
//                    $("#tabla_cargos_body").empty();
//                    document.getElementById("titulo").innerHTML = "Aviso";
//                    document.getElementById("texto").innerHTML = "Se elimino el registro !Refresque la pagina";
//                    $('#miAlerta').show('.fade');
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

function Cargar_Tabla_Reserva() {
    armarJSON(1);
    $.ajax({
        url: '/AgendaMedica_V00.2/ReservaSRV',
        data: datosJSON,
        type: 'POST',
        success: function (json) {
            $.each(json, function (indice, valor) {
                $("#tabla_cargos").append($("<tr>").append($("<td>" + valor.id + "</td>" +
                        "<td>" + valor.desc_dia + "</td>" +
                        "<td>" + valor.fecha + "</td>" +
                        "<td>" + valor.hora + "</td>" +
                        "<td>" + valor.id_cliente + "</td>" +
                        "<td>" + valor.desc_cliente + "</td>" +
                        "<td>" + valor.ci + "</td>" +
                        "<td>" + valor.desc_espec + "</td>" +
                        "<td>" + valor.desc_reserva + "</td>" +
                        "<td>" + valor.desc_estadoreserva + "</td>" +
                        "<td>" + valor.id_funcionario_med + "</td>" +
                        "<td>" + valor.desc_medico + "</td>" +
                        "<td>" + valor.id_funcionario_fun + "</td>" +
                        "<td>" + valor.desc_funcionario + "</td>")));

            });
        },
        error: function () {
            alert('No se pudo obtener ultimo valor en la tabla...!!!');
        }
    });
}

function Cargar_Combos() {
    ComboTurno();
    ComboTipoReserva();
    ComboEstadoReserva();
    ComboDia();
    ComboEspecialidad();
}
function ComboTurno() {
    armarJSON(5);
    $.ajax({
        url: '/AgendaMedica_V00.2/TurnoSRV',
        data: datosJSON,
        type: 'POST',
        success: function (json) {
            $.each(json, function (indice, valor) {
                $("#combo_turno").append("<option value= \"" + valor.id + "\"> " + valor.id + " - " + valor.descripcion + "</option>");

            });
        },
        error: function () {
            alert('No se pudo obtener ultimo valor en el combo ciudad...!!!');
        }
    });
}
function ComboTipoReserva() {
    armarJSON(5);
    $.ajax({
        url: '/AgendaMedica_V00.2/TreservaSRV',
        data: datosJSON,
        type: 'POST',
        success: function (json) {
            $.each(json, function (indice, valor) {
                $("#combo_tiporeserva").append("<option value= \"" + valor.id + "\"> " + valor.id + " - " + valor.descripcion + "</option>");

            });
        },
        error: function () {
            alert('No se pudo obtener ultimo valor en el combo ciudad...!!!');
        }
    });
}
function ComboEstadoReserva() {
    armarJSON(1);
    $.ajax({
        url: '/AgendaMedica_V00.2/EstadoReservaSRV',
        data: datosJSON,
        type: 'POST',
        success: function (json) {
            $.each(json, function (indice, valor) {
                $("#combo_estadoreserva").append("<option value= \"" + valor.id + "\"> " + valor.id + " - " + valor.descripcion + "</option>");

            });
        },
        error: function () {
            alert('No se pudo obtener ultimo valor en el combo ciudad...!!!');
        }
    });
}
function ComboDia() {
    armarJSON(1);
    $.ajax({
        url: '/AgendaMedica_V00.2/DiaSRV',
        data: datosJSON,
        type: 'POST',
        success: function (json) {
            $.each(json, function (indice, valor) {
                $("#combo_dia").append("<option value= \"" + valor.id + "\"> " + valor.id + " - " + valor.descripcion + "</option>");

            });
        },
        error: function () {
            alert('No se pudo obtener ultimo valor en el combo ciudad...!!!');
        }
    });
}
function ComboEspecialidad() {
    armarJSON(5);
    $.ajax({
        url: '/AgendaMedica_V00.2/EspecialidadSRV',
        data: datosJSON,
        type: 'POST',
        success: function (json) {
            $.each(json, function (indice, valor) {
                $("#combo_especialidad").append("<option value= \"" + valor.id + "\"> " + valor.id + " - " + valor.descripcion + "</option>");
                //$("#txtrestante_cupo").val(valor.cupo);
            });
        },
        error: function () {
            alert('No se pudo obtener ultimo valor en el combo ciudad...!!!');
        }
    });
}
function calendario() {
    $('.datepicker').datepicker({todayBtn: "linked", language: "es", autoclose: true, format: 'dd/mm/yyyy'});
    $('#txthora').timepicker({'timeFormat': 'H:i:s'});
}




