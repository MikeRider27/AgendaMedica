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
/* Capturar fila hecho por el profe*/
function capturarFilaCargos(id) {
    v_id = 0;
    v_nombre = "";
    v_ci = "";
    v_tipo = "";
    v_subcargo = "";
    v_especialidad = "";


    // $('#' + id + ' tr').click(function () {
    $('#' + id + ' tbody tr').click(function () {
        v_id = $(this).find("td").eq(0).html();
        v_nombre = $(this).find("td").eq(1).html();
        v_ci = $(this).find("td").eq(2).html();
        v_tipo = $(this).find("td").eq(3).html();
        v_subcargo = $(this).find("td").eq(4).html();
        v_especialidad = $(this).find("td").eq(5).html();

        $('#txtId').val(v_id);
        $('#txtNombre').val(v_nombre);
        $('#txtci').val(v_ci);
        $('#txtesp').val(v_especialidad);
        $('#txtsub').val(v_subcargo);
        //$('#txtsub').val(v_subc);

        listarSegunFiltro();
        return false;
    });
}
function armarDatosJSON(bandera, id) {
    datosJSON = {
        "opcion": bandera,
        "identificador": id
    };
}

/* Recordar que el numero 5 dentro de armarJSON es el señuelo.*/
/* El valo 5 se reemplaza por el valor capturado*/
/* El caso activo se utiliza en la funcion capturar fila*/
function cargarBuscadorF(opcion) {
    //alert(opcion);
    switch (opcion) {
        case 'persona' :
            casoActivo = 'persona';
            armarDatosJSON(3, 5);
            cargarTablaPersona();
            break;
        case 'especial' :
            casoActivo = 'especial';
            armarDatosJSON(9, 5);
            cargarTablaEspecialidades();
            break;
        case 'subcargos' :
            casoActivo = 'subcargos';
            armarDatosJSON(11, 5);
            cargarTablaSubcargo()
            break;
        default :
            casoActivo = '';
            alert('no se seteo ningun dato');
            break;
            alert('llega');

    }
}
function cargarTablaPersona() {
    $.ajax({
        url: '/AgendaMedica_V00.2/buscadorSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
            $('#tabla_buscador_body').empty();
            $.each(resp, function (indice, json) {
                $("#tabla_buscador").append($("<tr>").append($("<td>" + json.id + "</td>" +
                        "<td>" + json.nombre1 + "</td>" +
                        "<td>" + json.ci + "</td>")));
            });

        },
        error: function (resp) {
            alert('No existe Dato que mostrar');
        }

    });
    $('#myModal').modal();
}
function cargarTablaEspecialidades() {
    $.ajax({
        url: '/AgendaMedica_V00.2/buscadorSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
            $('#tabla_buscador_body').empty();
            $.each(resp, function (indice, json) {
                $("#tabla_buscador").append($("<tr>").append($("<td>" + json.id + "</td>" +
                        "<td>" + json.descripcion + "</td>")));
            });

        },
        error: function (resp) {
            alert('No existe Dato que mostrar');
        }

    });
    $('#myModal').modal();
}
function cargarTablaSubcargo() {
    $.ajax({
        url: '/AgendaMedica_V00.2/buscadorSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
            $('#tabla_buscador_body').empty();
            $.each(resp, function (indice, json) {
                $("#tabla_buscador").append($("<tr>").append($("<td>" + json.id + "</td>" +
                        "<td>" + json.descripcion + "</td>")));
            });

        },
        error: function (resp) {
            alert('No existe Dato que mostrar');
        }

    });
    $('#myModal').modal();
}

function capturarFila(id) {
    v_recuperado = 0;
    // $('#' + id + ' tr').click(function () {
    $('#' + id + ' tbody tr').click(function () {
        v_recuperado = $(this).find("td").eq(0).html();
        switch (casoActivo) {
            case 'persona' :
                $('#txtid_persona').val(v_recuperado);
                $('#myModal').modal('toggle');
                recuperarPersona();
                break;
            case 'especial' :
                $('#txtid_espe').val(v_recuperado);
                $('#myModal').modal('toggle');
                recuperarEspecialidad();
                resolverEspecialidad();
                break;
            case 'subcargos':
                $('#txtid_subc').val(v_recuperado);
                $('#myModal').modal('toggle');
                recuperarSubcargo();
                resolverSubcargo();
                break;
            default :
                casoActivo = '';
                alert('no se seteo ningun dato');
        }
        return false;
    });
}
function recuperarPersona() {
    armarDatosJSON(8, v_recuperado);
    //alert('El valor recuperado es = ' + v_recuperado);
    $.ajax({
        url: '/AgendaMedica_V00.2/buscadorSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
            $.each(resp, function (indice, json) {
                //alert(json.nombre_usuario);
                $('#txtNombre').val(json.nombre1);
                $('#txtci').val(json.ci);
            });
        },
        error: function (resp) {
            alert('No existe Dato que mostrar en recuperar la descripcion de persona');
        }

    });
}
function recuperarEspecialidad() {
    armarDatosJSON(10, v_recuperado);
    //alert('El valor recuperado es = ' + v_recuperado);
    $.ajax({
        url: '/AgendaMedica_V00.2/buscadorSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
            $.each(resp, function (indice, json) {
                $('#txtesp').val(json.descripcion);
            });
        },
        error: function (resp) {
            alert('No existe Dato que mostrar en recuperar la descripcion de especialidad');
        }

    });
}
function recuperarSubcargo() {
    armarDatosJSON(12, v_recuperado);
    //alert('El valor recuperado es = ' + v_recuperado);
    $.ajax({
        url: '/AgendaMedica_V00.2/buscadorSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
            $.each(resp, function (indice, json) {
                $('#txtsub').val(json.descripcion);
            });
        },
        error: function (resp) {
            alert('No existe Dato que mostrar en recuperar la descripcion de especialidad');
        }

    });
}
function recuperarPersona_enter(e) {
    tecla = (document.all) ? e.keyCode : e.which;
    if (tecla == 13) {
        var idrecuperado = $('#txtid_persona').val();
        armarDatosJSON(8, idrecuperado);
        $.ajax({
            url: '/AgendaMedica_V00.2/buscadorSRV',
            //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
            data: datosJSON,
            type: 'POST',
            success: function (resp) {
                $.each(resp, function (indice, json) {
                    $('#txtNombre').val(json.nombre1);
                    $('#txtci').val(json.ci);
                });
            },
            error: function (resp) {
                alert('No existe Dato que mostrar');
            }

        });
    }
}
function recuperarEspecialidad_enter(e) {
    tecla = (document.all) ? e.keyCode : e.which;
    if (tecla == 13) {
        var idrecuperado = $('#txtid_espe').val();
        armarDatosJSON(10, idrecuperado);
        $.ajax({
            url: '/AgendaMedica_V00.2/buscadorSRV',
            //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
            data: datosJSON,
            type: 'POST',
            success: function (resp) {
                $.each(resp, function (indice, json) {
                    $('#txtesp').val(json.descripcion);
                });
            },
            error: function (resp) {
                alert('No existe la especialidad');
                $('#txtid_espe').focus();
            }

        });
    }
}
function recuperarSubcargo_enter(e) {
    tecla = (document.all) ? e.keyCode : e.which;
    if (tecla == 13) {
        var idrecuperado = $('#txtid_subc').val();
        armarDatosJSON(12, idrecuperado);
        $.ajax({
            url: '/AgendaMedica_V00.2/buscadorSRV',
            //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
            data: datosJSON,
            type: 'POST',
            success: function (resp) {
                $.each(resp, function (indice, json) {
                    $('#txtsub').val(json.descripcion);
                });
            },
            error: function (resp) {
                alert('No existe la especialidad');
                $('#txtid_espe').focus();
            }

        });
    }
}

function recuperarSubcargo_Validado() {
    var idrecuperado = $('#txtid_subc').val();
    armarDatosJSON(12, idrecuperado);
    $.ajax({
        url: '/AgendaMedica_V00.2/buscadorSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
            $.each(resp, function (indice, json) {
                $('#txtsub').val(json.descripcion);
            });
        },
        error: function (resp) {
            alert('No existe la especialidad');
        }

    });

}
function recuperarEspecialidad_Validado() {
    var idrecuperado = $('#txtid_espe').val();
    armarDatosJSON(10, idrecuperado);
    $.ajax({
        url: '/AgendaMedica_V00.2/buscadorSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
            $.each(resp, function (indice, json) {
                $('#txtesp').val(json.descripcion);
            });
        },
        error: function (resp) {
            alert('No existe la especialidad');
        }

    });

}