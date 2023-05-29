$(document).ready(function () {
    (function ($) {

        $('#filtrar_cargo').keyup(function () {

            var rex = new RegExp($(this).val(), 'i');
            $('.buscar tr').hide();
            $('.buscar tr').filter(function () {
                return rex.test($(this).text());
            }).show();

        });

    }(jQuery));

});
function armarDatosJSON(bandera, id) {
    datosJSON = {
        "opcion": bandera,
        "identificador": id
    };
}
/* Capturar fila hecho por el profe*/
function capturarFilaCargosR(id) {
    v_id = 0;
    v_fecha = "";
    v_idfuncionario = 0;
    v_nombrefuncionario = "";
    v_idreserva = 0;
    v_nombrecliente = "";
    v_apellidocliente = "";
    v_cicliente = "";
    v_especialidad = "";
    v_nombremedico = "";
    // $('#' + id + ' tr').click(function () {
    $('#' + id + ' tbody tr').click(function () {
        v_id = $(this).find("td").eq(0).html();
        v_fecha = $(this).find("td").eq(1).html();
        v_idfuncionario = $(this).find("td").eq(2).html();
        v_nombrefuncionario = $(this).find("td").eq(3).html();
        v_idreserva = $(this).find("td").eq(4).html();
        v_nombrecliente = $(this).find("td").eq(5).html();
        v_apellidocliente = $(this).find("td").eq(6).html();
        v_cicliente = $(this).find("td").eq(7).html();
        v_especialidad = $(this).find("td").eq(8).html();
        v_nombremedico = $(this).find("td").eq(9).html();

        $('#txtId').val(v_id);
        $('#txtfecha').val(v_fecha);
        $('#txtid_funcionario').val(v_idfuncionario);
        $('#txtnombre_funcionario').val(v_nombrefuncionario);
        $('#txtid_reserva').val(v_idreserva);
        $('#txtnombre_cliente').val(v_nombrecliente);
        $('#txtapellido_cliente').val(v_apellidocliente);
        $('#txtci_cliente').val(v_cicliente);
        $('#txtespecialidad').val(v_especialidad);
        $('#txtnombre_medico').val(v_nombremedico);

        listarSegunFiltro();
        return false;
    });
//    $("#tabla_cargos tbody tr").on('click', function () {
//        console.log('clic');
//        return false;
//    });
}

function cargarBuscadorConsulta(opcion) {
    //alert(opcion);
    switch (opcion) {
        case 'funcionario' :
            casoActivo = 'funcionario';
            armarDatosJSON(13, 5);
            cargarTablaFuncionario();
            break;
        case 'reserva' :
            casoActivo = 'reserva';
            armarDatosJSON(19, 5);
            cargarTablaReserva();
            break;
        case 'medico' :
            casoActivo = 'medico';
            armarDatosJSON(17, 5);
            cargarTablaMedico();
            break;
        default :
            casoActivo = '';
            alert('no se seteo ningun dato');
            break;
            alert('llega');

    }
}
function capturarFila(id) {
    v_recuperado = 0;
    // $('#' + id + ' tr').click(function () {
    $('#' + id + ' tbody tr').click(function () {
        v_recuperado = $(this).find("td").eq(0).html();
        switch (casoActivo) {
            case 'funcionario' :
                $('#txtid_funcionario').val(v_recuperado);
                $('#myModal').modal('toggle');
                recuperarFuncionario();
                break;
            case 'reserva' :
                $('#txtid_reserva').val(v_recuperado);
                $('#myModal').modal('toggle');
                recuperarReserva();
                //resolverEspecialidad();
                break;
            case 'medico':
                $('#txtid_medico').val(v_recuperado);
                $('#myModal').modal('toggle');
                //recuperarMedico();
                // resolverSubcargo();
                break;
            default :
                casoActivo = '';
                alert('no se seteo ningun dato');
        }
    });
}

function cargarTablaFuncionario() {
    $.ajax({
        url: '/AgendaMedica_V00.2/buscadorSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
            $('#tabla_buscador_body').empty();
            $.each(resp, function (indice, json) {
                $("#tabla_buscador").append($("<tr>").append($("<td>" + json.id + "</td>" +
                        "<td>" + json.nombre_persona + "</td>" +
                        "<td>" + json.ci_persona + "</td>")));
            });

        },
        error: function (resp) {
            alert('No existe Dato que mostrar');
        }

    });
    $('#myModal').modal();
}
function cargarTablaReserva() {
    $.ajax({
        url: '/AgendaMedica_V00.2/buscadorSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
            $('#tabla_buscador_body').empty();
            $.each(resp, function (indice, json) {
                $("#tabla_buscador").append($("<tr>").append($("<td>" + json.id + "</td>" +
                        "<td>" + json.desc_cliente + "</td>" +
                        "<td>" + json.ape_cliente + "</td>" +
                        "<td>" + json.ci + "</td>" +
                        "<td>" + json.desc_medico + "</td>" +
                        "<td>" + json.desc_espec + "</td>")));
            });

        },
        error: function (resp) {
            alert('No existe Dato que mostrar');
        }

    });
    $('#myModal').modal();
}
function cargarTablaMedico() {
    $.ajax({
        url: '/AgendaMedica_V00.2/buscadorSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
            $('#tabla_buscador_body').empty();
            $.each(resp, function (indice, json) {
                $("#tabla_buscador").append($("<tr>").append($("<td>" + json.id + "</td>" +
                        "<td>" + json.nombre_persona + "</td>" +
                        "<td>" + json.ci_persona + "</td>")));
            });

        },
        error: function (resp) {
            alert('No existe Dato que mostrar');
        }

    });
    $('#myModal').modal();
}

function recuperarFuncionario() {
    armarDatosJSON(14, v_recuperado);
    //alert('El valor recuperado es = ' + v_recuperado);
    $.ajax({
        url: '/AgendaMedica_V00.2/buscadorSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
            $.each(resp, function (indice, json) {
                //alert(json.nombre_usuario);
                $('#txtnombre_funcionario').val(json.nombre_persona);

            });
        },
        error: function (resp) {
            alert('No existe Dato que mostrar en recuperar funcionario');
        }

    });
}
function recuperarReserva() {
    armarDatosJSON(20, v_recuperado);
    //alert('El valor recuperado es = ' + v_recuperado);
    $.ajax({
        url: '/AgendaMedica_V00.2/buscadorSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
            $.each(resp, function (indice, json) {
                //alert(json.nombre_usuario);
                $('#txtid_reserva').val(json.id);
                $('#txtnombre_cliente').val(json.desc_cliente);
                $('#txtapellido_cliente').val(json.ape_cliente);
                $('#txtci_cliente').val(json.ci);
                $('#txtespecialidad').val(json.desc_espec);
                $('#txtnombre_medico').val(json.desc_medico);
            });
        },
        error: function (resp) {
            alert('No existe Dato que mostrar en recuperar cliente');
        }

    });
}
function recuperarMedico() {
    armarDatosJSON(18, v_recuperado);
    //alert('El valor recuperado es = ' + v_recuperado);
    $.ajax({
        url: '/AgendaMedica_V00.2/buscadorSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
            $.each(resp, function (indice, json) {
                //alert(json.nombre_usuario);
                $('#txtnombre_medico').val(json.nombre_persona);

            });
        },
        error: function (resp) {
            alert('No existe Dato que mostrar en recuperar medico');
        }

    });
}
function recuperarFuncionarioConsulta_enter(e) {
    tecla = (document.all) ? e.keyCode : e.which;
    if (tecla == 13) {
        var idrecuperado = $('#txtid_funcionario').val();
        armarDatosJSON(14, idrecuperado);
        $.ajax({
            url: '/AgendaMedica_V00.2/buscadorSRV',
            //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
            data: datosJSON,
            type: 'POST',
            success: function (resp) {
                $.each(resp, function (indice, json) {
                    $('#txtnombre_funcionario').val(json.nombre_persona);
                });
            },
            error: function (resp) {
                alert('No existe la especialidad');
                $('#txtid_espe').focus();
            }

        });
    }
}
function recuperarReserva_enter(e) {
    tecla = (document.all) ? e.keyCode : e.which;
    if (tecla == 13) {
        var idrecuperado = $('#txtid_reserva').val();
        armarDatosJSON(20, idrecuperado);
        $.ajax({
            url: '/AgendaMedica_V00.2/buscadorSRV',
            //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
            data: datosJSON,
            type: 'POST',
            success: function (resp) {
                $.each(resp, function (indice, json) {
                    $('#txtid_reserva').val(json.id);
                    $('#txtnombre_cliente').val(json.desc_cliente);
                    $('#txtapellido_cliente').val(json.ape_cliente);
                    $('#txtci_cliente').val(json.ci);
                    $('#txtespecialidad').val(json.desc_espec);
                    $('#txtnombre_medico').val(json.desc_medico);
                });
            },
            error: function (resp) {
                alert('No existe ');
                $('#txtid_espe').focus();
            }

        });
    }
}
function recuperarMedico_enter(e) {
    tecla = (document.all) ? e.keyCode : e.which;
    if (tecla == 13) {
        var idrecuperado = $('#txtid_medico').val();
        armarDatosJSON(18, idrecuperado);
        $.ajax({
            url: '/AgendaMedica_V00.2/buscadorSRV',
            //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
            data: datosJSON,
            type: 'POST',
            success: function (resp) {
                $.each(resp, function (indice, json) {
                    $('#txtnombre_medico').val(json.nombre_persona);
                });
            },
            error: function (resp) {
                alert('No existe la especialidad');
                $('#txtid_espe').focus();
            }

        });
    }
}