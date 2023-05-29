$(document).ready(function () {
    //alert('Conexion entre capas ');
    //recuperarDATOS();
    limpiar_deshabilitar_Campos_Cargos();
    Cargar_Tabla_Consulta();
    //Cargar_Combos();
    calendario();
    ObtenerFechaActual();
});
var opcion;
function armarJSON(banderaOperacion) {
    datosJSON = {
        //"cadena": "Juan"
        "operacion": banderaOperacion,
        "id": $('#txtId').val(),
        // "fecha": $('#txtfecha').val(),
        "idfuncionario": $('#txtid_funcionario').val(),
        "idreserva": $('#txtid_reserva').val(),
        "obs": $('#txtobs').val()
    };
}



function limpiar_deshabilitar_Campos_Cargos() {
    $('#txtId').val("");
    $('#txtfecha').val("");
    $('#txtid_funcionario').val("");
    $('#txtnombre_funcionario').val("");
    $('#txtid_cliente').val("");
    $('#txtnombre_cliente').val("");
    $('#txtapellido_cliente').val("");
    $('#txtci_cliente').val("");
    $('#txtid_medico').val("");
    $('#txtnombre_medico').val("");
    $('#txtid_reserva').val("");
    $('#txtespecialidad').val("");
    $('#txtobs').val("");


    $('#btGuardar').attr('disabled', true);
    $('#btModificar').attr('disabled', true);
    $('#btEliminar').attr('disabled', true);
    $('#btCancelar').attr('disabled', false);
    $('#btBuscar').attr('disabled', true);
    $('#btNuevo').attr('disabled', false);

    $('#txtId').attr('disabled', true);
    $('#txtfecha').attr('disabled', true);
    $('#txtid_funcionario').attr('disabled', true);
    $('#txtnombre_funcionario').attr('disabled', true);
    $('#txtid_cliente').attr('disabled', true);
    $('#txtnombre_cliente').attr('disabled', true);
    $('#txtapellido_cliente').attr('disabled', true);
    $('#txtci_cliente').attr('disabled', true);
    $('#txtid_medico').attr('disabled', true);
    $('#txtnombre_medico').attr('disabled', true);
    $('#txtid_reserva').attr('disabled', true);
    $('#txtespecialidad').attr('disabled', true);
    $('#txtobs').attr('disabled', true);
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

    $('#txtid_funcionario').attr('disabled', false);
    $('#txtid_reserva').attr('disabled', false);
    $('#txtid_reserva').focus();
    $('#txtobs').attr('disabled', false);
}



function ABM() {
    armarJSON(opcion);
    $.ajax({
        url: '/AgendaMedica_V00.2/ConsultaSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
//            $("#msmAviso").dialog("open");
            if (opcion == 2) {
                $.each(resp, function (indice, valor) {
                    $('#txtobs').val(valor.obs);
                    //alert('El dato recuperado de la bd es  ' + valor.fecha);
                });
                $('#btNuevo').attr('disabled', false);
                $('#btModificar').attr('disabled', false);
                $('#btEliminar').attr('disabled', false);
            } else {
                if (opcion == 5) {
                    javascript:location.reload();
                }
                if (opcion == 6) {
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
function agregarCON() {
    opcion = 4;
    ABM();
}
function modificar() {
    opcion = 5;
    ABM();
}
function eliminar() {
    opcion = 6;
    ABM();
}
function listarSegunFiltro() {
    opcion = 2;
    ABM();
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
function Cargar_Tabla_Consulta() {
    armarJSON(1);
    $.ajax({
        url: '/AgendaMedica_V00.2/ConsultaSRV',
        data: datosJSON,
        type: 'POST',
        success: function (json) {
            $.each(json, function (indice, valor) {
                $("#tabla_cargos").append($("<tr>").append($("<td>" + valor.id + "</td>" +
                        "<td>" + valor.fecha + "</td>" +
                        "<td>" + valor.id_funcionario + "</td>" +
                        "<td>" + valor.desc_funcionario + "</td>" +
                        "<td>" + valor.id_reserva + "</td>" +
                        "<td>" + valor.nombre_cliente + "</td>" +
                        "<td>" + valor.apellido_cliente + "</td>" +
                        "<td>" + valor.ci_cliente + "</td>" +
                        "<td>" + valor.especialidad + "</td>" +
                        "<td>" + valor.medico + "</td>")));

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
        url: '/AgendaMedica_V00.2/EstadoConsultaSRV',
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
function ObtenerFechaActual() {
    armarJSON(3);
    $.ajax({
        url: '/AgendaMedica_V00.2/ConsultaSRV',
        data: datosJSON,
        type: 'POST',
        success: function (json) {
            //alert(json);
            $("#txtfecha").val(json);
//            $.each(json, function (indice, valor) {
//                $("#txtfecha").val(valor.fechavista);
//            });
        },
        error: function () {
            alert('No se pudo obtener la fecha');
        }
    });
}
function calendario() {
    $('.datepicker').datepicker({todayBtn: "linked", language: "es", autoclose: true, format: 'dd/mm/yyyy'});
    //$('#txthora').timepicker({'timeFormat': 'H:i:s'});
}




