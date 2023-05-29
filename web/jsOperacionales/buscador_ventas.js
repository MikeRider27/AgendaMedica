$(document).ready(function () {
    (function ($) {
        $('#filtrar').keyup(function () {
            var rex = new RegExp($(this).val(), 'i');
            $('.abuscar tr').hide();
            $('.abuscar tr').filter(function () {
                return rex.test($(this).text());
            }).show();
        });
    }(jQuery));
    HoraLocal();
    eventos();
});

var casoActivo = '';

function armarJSON(bandera, id) {
    datosJSON = {
        "opcion": bandera,
        "identificador": id
    };
}
function eventos() {
    $('#fecha').attr('disabled', true);
    $('#id').attr('disabled', true);
    $('#id_usuario').attr('disabled', false);
    $('#usuario').attr('disabled', true);
    $('#cliente').attr('disabled', true);
}
/* Recordar que el numero 5 dentro de armarJSON es el señuelo.*/
function cargarBuscador(opcion) {
    //alert(opcion);
    switch (opcion) {
        case 'producto' :
            casoActivo = 'producto';
            armarJSON(21, 5);
            cargarTablaProducto();
            break;
        case 'clientes' :
            casoActivo = 'cliente';
            armarJSON(15, 5);
            cargarTablaClientes();
            break;
        case 'usuarios' :
            casoActivo = 'usuarios';
            armarJSON(13, 5);
            cargarTablaUsuarios();
            break;
        case 'pedidos' :
            casoActivo = 'pedidos';
            armarJSON(23, 5);
            cargarTablaBusquedaPedidos();
            break;
        case 'servicio' :
            casoActivo = 'servicio';
            armarJSON(26, 5);
            cargarServicios();
            break;
        case 'ventas' :
            casoActivo = 'ventas';
            armarJSON(27, 5);
            cargarTablaBusquedaVentas();
            break;

        default :
            casoActivo = '';
            alert('no se seteo ningun dato');
            break;
            alert('llega');

    }
}
function cargarTablaBusquedaVentas() {
    $.ajax({
        url: '/AgendaMedica_V00.2/buscadorSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
            $('#tabla_buscador_body').empty();
            $.each(resp, function (indice, json) {
                $("#tabla_buscador").append($("<tr>").append($("<td>" + json.id_pedido + "</td>" +
                        "<td>" + json.id_funcionario + "</td>" + "<td>" + json.nombrefuncionario + "</td>" +
                        "<td>" + json.id_cliente + "</td>" + "<td>" + json.nombrecliente + "</td>" +
                        "<td>" + json.obs + "</td>" + "<td>" + json.fecha + "</td>"))
                        );
            });

        },
        error: function (resp) {
            alert('No existe Dato que mostrar');
        }

    });
    $('#myModal').modal();
}
function cargarServicios() {
    $.ajax({
        url: '/AgendaMedica_V00.2/buscadorSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
            $('#tabla_buscador_body').empty();
            $.each(resp, function (indice, json) {
                $("#tabla_buscador").append($("<tr>").append($("<td>" + json.id_producto + "</td>" +
                        "<td>" + json.descripcion_producto + "</td>" +
                        "<td>" + json.precioventa + "</td>"))
                        );
            });

        },
        error: function (resp) {
            alert('No existe Dato que mostrar');
        }

    });
    $('#myModal').modal();
}
function cargarTablaBusquedaPedidos() {
    $.ajax({
        url: '/AgendaMedica_V00.2/buscadorSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
            $('#tabla_buscador_body').empty();
            $.each(resp, function (indice, json) {
                $("#tabla_buscador").append($("<tr>").append($("<td>" + json.id_pedido + "</td>" +
                        "<td>" + json.id_funcionario + "</td>" + "<td>" + json.nombrefuncionario + "</td>" +
                        "<td>" + json.id_cliente + "</td>" + "<td>" + json.nombrecliente + "</td>" +
                        "<td>" + json.obs + "</td>" + "<td>" + json.fecha + "</td>"))
                        );
            });

        },
        error: function (resp) {
            alert('No existe Dato que mostrar');
        }

    });
    $('#myModal').modal();
}
function cargarTablaProducto() {
    $.ajax({
        url: '/AgendaMedica_V00.2/buscadorSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
            $('#tabla_buscador_body').empty();
            $.each(resp, function (indice, json) {
                $("#tabla_buscador").append($("<tr>").append($("<td>" + json.id_producto + "</td>" +
                        "<td>" + json.descripcion_producto + "</td>"))
                        );
            });

        },
        error: function (resp) {
            alert('No existe Dato que mostrar');
        }

    });
    $('#myModal').modal();
}

function cargarTablaUsuarios() {
    //alert('llaga a cargar');
    $.ajax({
        url: '/AgendaMedica_V00.2/buscadorSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
            $('#tabla_buscador_body').empty();
            $.each(resp, function (indice, json) {
                $("#tabla_buscador").append($("<tr>").append($("<td>" + json.id + "</td>" +
                        "<td>" + json.nombre_persona + "</td>"))
                        );
            });

        },
        error: function (resp) {
            alert('No existe Dato que mostrar');
        }

    });
    $('#myModal').modal();
}

function cargarTablaClientes() {
    //alert('llaga a cargar');
    $.ajax({
        url: '/AgendaMedica_V00.2/buscadorSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
            $('#tabla_buscador_body').empty();
            $.each(resp, function (indice, json) {
                $("#tabla_buscador").append($("<tr>").append($("<td>" + json.id + "</td>" +
                        "<td>" + json.nombre_persona + "</td>"))
                        );
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
            case 'producto' :
                $('#codigoInsumo').val(v_recuperado);
                $('#myModal').modal('toggle');
                recuperarProducto();
                break;
            case 'cliente' :
                $('#id_cliente').val(v_recuperado);
                $('#myModal').modal('toggle');
//                var c = recupararClienteSegunID(v_recuperado, 'buscador');
//                alert(c);
                recuperarCliente();
                break;
            case 'usuarios':
                $('#id_usuario').val(v_recuperado);
                $('#myModal').modal('toggle');
                recuperarUsuario();
                break;
            case 'pedidos':
                $('#id').val(v_recuperado);
                var v_idusuario = $(this).find("td").eq(1).html();
                var v_nomusuario = $(this).find("td").eq(2).html();
                var v_idcliente = $(this).find("td").eq(3).html();
                var v_nomcliente = $(this).find("td").eq(4).html();
                var v_obs = $(this).find("td").eq(5).html();
                var v_fecha = $(this).find("td").eq(6).html();
                $('#id_usuario').val(v_idusuario);
                $('#usuario').val(v_nomusuario);
                $('#cliente').val(v_nomcliente);
                $('#osbv').val(v_obs);
                $('#fecha').val(v_fecha);
                $('#id_usuario').val(v_idusuario);
                $('#id_cliente').val(v_idcliente);
                $('#myModal').modal('toggle');
                recuperarPedido();
                $('#btanular').attr('disabled', false);
                $('#btcancelar').attr('disabled', false);
                break;
            case 'servicio' :
                $('#codigoInsumo').val(v_recuperado);
                var descripcion = $(this).find("td").eq(1).html();
                var precio = $(this).find("td").eq(2).html();
                $('#descripcion').val(descripcion);
                $('#precio').val(precio);
                $('#myModal').modal('toggle');
                break;
            case 'ventas' :
                $('#id').val(v_recuperado);
                var v_idusuario = $(this).find("td").eq(1).html();
                var v_nomusuario = $(this).find("td").eq(2).html();
                var v_idcliente = $(this).find("td").eq(3).html();
                var v_nomcliente = $(this).find("td").eq(4).html();
                var v_obs = $(this).find("td").eq(5).html();
                var v_fecha = $(this).find("td").eq(6).html();
                $('#id_usuario').val(v_idusuario);
                $('#usuario').val(v_nomusuario);
                $('#cliente').val(v_nomcliente);
                $('#osbv').val(v_obs);
                $('#fecha').val(v_fecha);
                $('#id_usuario').val(v_idusuario);
                $('#id_cliente').val(v_idcliente);
                $('#myModal').modal('toggle');
                recuperarVenta();
                $('#btanular').attr('disabled', false);
                $('#btcancelar').attr('disabled', false);
                break;
            default :
                casoActivo = '';
                alert('no se seteo ningun dato');
        }
    });
}
function recuperarUsuario_enter(e) {
    tecla = (document.all) ? e.keyCode : e.which;
    if (tecla == 13) {
        var idrecuperado = $('#id_usuario').val();
        armarJSON(14, idrecuperado);
        $.ajax({
            url: '/AgendaMedica_V00.2/buscadorSRV',
            //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
            data: datosJSON,
            type: 'POST',
            success: function (resp) {
                $.each(resp, function (indice, json) {
                    //alert(json.nombre_usuario);
                    $('#usuario').val(json.nombre_persona);
                });

            },
            error: function (resp) {
                alert('No existe Dato que mostrar');
            }

        });
    }
}

function recuperarCliente_enter(e) {
    tecla = (document.all) ? e.keyCode : e.which;
    if (tecla == 13) {
        var idrecuperado = $('#id_cliente').val();
        armarJSON(16, idrecuperado);
        $.ajax({
            url: '/AgendaMedica_V00.2/buscadorSRV',
            //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
            data: datosJSON,
            type: 'POST',
            success: function (resp) {
                $.each(resp, function (indice, json) {
                    //alert(json.nombre_usuario);
                    $('#cliente').val(json.nombre_persona);
                });

            },
            error: function (resp) {
                alert('No existe Dato que mostrar');
            }

        });
    }
}

function recuperarProducto_enter(e) {
    tecla = (document.all) ? e.keyCode : e.which;
    if (tecla == 13) {
        var idrecuperado = $('#codigoInsumo').val();
        armarJSON(22, idrecuperado);
        $.ajax({
            url: '/AgendaMedica_V00.2/buscadorSRV',
            //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
            data: datosJSON,
            type: 'POST',
            success: function (resp) {
                $.each(resp, function (indice, json) {
                    //alert(json.nombre_usuario);
                    $('#descripcion').val(json.descripcion_producto);
                    $('#precio').val(json.precioventa);
                });

            },
            error: function (resp) {
                alert('No existe Dato que mostrar');
            }

        });
    }
}
function recuperarVenta(){
    armarJSON(28, v_recuperado);
    $.ajax({
        url: '/AgendaMedica_V00.2/buscadorSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
            $('#tabla_detalle').empty();
            $.each(resp, function (indice, json) {
                $("#tablaDetalleInsumos").append($("<tr>").append($("<td>" + json.id_producto + "</td>" +
                        "<td>" + json.pro_descrip + "</td>" + "<td>" + json.pd_cant + "</td>" +
                        "<td>" + json.total + "</td>")));
            });

        },
        error: function (resp) {
            alert('No existe Dato que mostrar');
        }

    });
}
function recuperarPedido() {
    armarJSON(24, v_recuperado);
    $.ajax({
        url: '/AgendaMedica_V00.2/buscadorSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
            $('#tabla_detalle').empty();
            $.each(resp, function (indice, json) {
                $("#tablaDetalleInsumos").append($("<tr>").append($("<td>" + json.id_producto + "</td>" +
                        "<td>" + json.pro_descrip + "</td>" + "<td>" + json.pd_cant + "</td>" +
                        "<td>" + json.total + "</td>")));
            });

        },
        error: function (resp) {
            alert('No existe Dato que mostrar');
        }

    });
}
function recuperarUsuario() {
    //alert('valor recuperado ' + v_recuperado);
    armarJSON(14, v_recuperado);
    $.ajax({
        url: '/AgendaMedica_V00.2/buscadorSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
            $.each(resp, function (indice, json) {
                //alert(json.nombre_usuario);
                $('#usuario').val(json.nombre_persona);
            });

        },
        error: function (resp) {
            alert('No existe Dato que mostrar');
        }

    });
}

function recuperarCliente() {
    armarJSON(16, v_recuperado);
    $.ajax({
        url: '/AgendaMedica_V00.2/buscadorSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
            $.each(resp, function (indice, json) {
                //alert(json.nombre_usuario);
                $('#cliente').val(json.nombre_persona);
            });

        },
        error: function (resp) {
            alert('No existe Dato que mostrar');
        }

    });
}

function recuperarProducto() {
    armarJSON(22, v_recuperado);
    $.ajax({
        url: '/AgendaMedica_V00.2/buscadorSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
            $.each(resp, function (indice, json) {
                //alert(json.nombre_usuario);
                $('#descripcion').val(json.descripcion_producto);
                $('#precio').val(json.precioventa);
            });

        },
        error: function (resp) {
            alert('No existe Dato que mostrar');
        }

    });
}

function HoraLocal() {
    armarJSON(1, 5);
    $.ajax({
        url: '/AgendaMedica_V00.2/HoraLocalSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
            $('#fecha').val(resp);
        },
        error: function (resp) {
            alert('No se recupero la fecha');
        }

    });
}
