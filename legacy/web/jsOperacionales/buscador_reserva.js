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
    // $('#' + id + ' tr').click(function () {
    $('#' + id + ' tbody tr').click(function () {
        v_id = $(this).find("td").eq(0).html();
        $('#txtId').val(v_id);
        listarSegunFiltro();
        return false;
    });
//    $("#tabla_cargos tbody tr").on('click', function () {
//        console.log('clic');
//        return false;
//    });
}

function cargarBuscadorR(opcion) {
    //alert(opcion);
    switch (opcion) {
        case 'funcionario' :
            casoActivo = 'funcionario';
            armarDatosJSON(13, 5);
            cargarTablaFuncionario();
            break;
        case 'cliente' :
            casoActivo = 'cliente';
            armarDatosJSON(15, 5);
            cargarTablaCliente();
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
            case 'cliente' :
                $('#txtid_cliente').val(v_recuperado);
                $('#myModal').modal('toggle');
                recuperarCliente();
                //resolverEspecialidad();
                break;
            case 'medico':
                $('#txtid_medico').val(v_recuperado);
                $('#myModal').modal('toggle');
                recuperarMedico();
                // resolverSubcargo();
                break;
            default :
                casoActivo = '';
                alert('no se seteo ningun dato');
        }
        return false;
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
function cargarTablaCliente() {
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
function recuperarCliente() {
    armarDatosJSON(16, v_recuperado);
    //alert('El valor recuperado es = ' + v_recuperado);
    $.ajax({
        url: '/AgendaMedica_V00.2/buscadorSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
            $.each(resp, function (indice, json) {
                //alert(json.nombre_usuario);
                $('#txtnombre_cliente').val(json.nombre_persona);
                $('#txtci_cliente').val(json.ci_persona);
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
function recuperarFuncionario_enter(e) {
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
function recuperarCliente_enter(e) {
    tecla = (document.all) ? e.keyCode : e.which;
    if (tecla == 13) {
        var idrecuperado = $('#txtid_cliente').val();
        armarDatosJSON(16, idrecuperado);
        $.ajax({
            url: '/AgendaMedica_V00.2/buscadorSRV',
            //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
            data: datosJSON,
            type: 'POST',
            success: function (resp) {
                $.each(resp, function (indice, json) {
                    $('#txtnombre_cliente').val(json.nombre_persona);
                    $('#txtci_cliente').val(json.ci_persona);
                });
            },
            error: function (resp) {
                alert('No existe la especialidad');
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