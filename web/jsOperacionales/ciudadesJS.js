$(document).ready(function () {
    //alert('Conexion entre capas ');
    //recuperarDATOS();
    limpiar_deshabilitar_CamposC();
    //Cargar_Combo_Ciudad();
    //Cargar_Combo_Pais();
    Cargar_Tabla_Ciudad();
    //alerta();
    // iniciar();
});
var opcion;
function armarJSON(banderaOperacion) {
    datosJSON = {
        //"cadena": "Juan"
        "operacion": banderaOperacion,
        "ciudad": $('#txtCiudad').val(),
        "id": $('#txtId').val(),
        //"idpais": $('#txtIdpais').val(),
        "buscador": $('#txtBuscador').val()
                //"comboCiudad": $('#combo_ciudad').val(),
                //"comboPais": $('#combo_pais').val()
    };
}
function alerta() {
    document.getElementById("titulo").innerHTML = "Aviso";
    document.getElementById("texto").innerHTML = "Nos conectamos correctamente a la segunda capa";
    $('#miAlerta').show('.fade');
}
function agregarC() {
    opcion = 1;
    ABM();
}
function modificarC() {
    opcion = 2;
    ABM();
    Cargar_Tabla_Ciudad();
}
function eliminarC() {
    opcion = 3;
    ABM();

}
function listarSegunFiltroC() {
    opcion = 4;
    ABM();
    //alert('Capturando el comboCiudad ' + datosJSON.comboCiudad);
}
function limpiar_deshabilitar_CamposC() {
    /*Limpiar campos*/
    $('#txtId').val("");
    $('#txtCiudad').val("");
    $('#txtIdpais').val("");
    $('#txtBuscador').val("");


    /*Deshabilitar componentes*/
    $('#btGuardar').attr('disabled', true);
    $('#btModificar').attr('disabled', true);
    $('#btEliminar').attr('disabled', true);
    $('#btCancelar').attr('disabled', true);
    $('#btBuscar').attr('disabled', true);
    $('#btNuevo').attr('disabled', false);
    $('#txtId').attr('disabled', true);
    $('#txtCiudad').attr('disabled', true);
    $('#txtIdpais').attr('disabled', true);
    $('#txtBuscador').attr('disabled', true);
    $('#combo_pais').attr('disabled', true);
}
function limpiar_habilitar_CamposC() {
    /*Limpiar campos*/
//    $('#txtId').val("");
//    $('#txtCiudad').val("");
//    $('#txtIdpais').val("");
    /*Habilitar componentes*/
    $('#btGuardar').attr('disabled', false);
    $('#btCancelar').attr('disabled', false);
    $('#btModificar').attr('disabled', false);
    $('#btBuscar').attr('disabled', false);
    $('#txtId').attr('disabled', true);
    $('#txtCiudad').attr('disabled', false);
    $('#txtCiudad').focus();
//    $('#txtIdpais').attr('disabled', false);
    $('#combo_pais').attr('disabled', false);
}
function campo_ciudadC(e) {
    tecla = (document.all) ? e.keyCode : e.which;
    if (tecla == 13)
        $('#txtCiudad').focus();

}
function campo_IDpaisC(e) {
    tecla = (document.all) ? e.keyCode : e.which;
    if (tecla == 13)
        $('#txtIdpais').focus();

}
function boton_guardarC(e) {
    tecla = (document.all) ? e.keyCode : e.which;
    if (tecla == 13)
        $('#btGuardar').focus();
}
function dialogo_confirmacionC() {
    var mensaje = confirm('GUARDAR ?');
    if (mensaje == true) {
        agregarC();
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
        url: '/AgendaMedica_V00.2/CiudadSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {

            if (opcion == 4) { // opcion 4 era listar
                $.each(resp, function (indice, valor) {
                    $('#txtCiudad').val(valor.descripcion);
                    $('#txtIdpais').val(valor.id_pais);
                    //alert(valor.descripcion);
                    $('#combo_ciudad> option[value=' + valor.id + ']').attr('selected', 'selected');
                    $('#combo_pais> option[value=' + valor.id_pais + ']').attr('selected', 'selected');
                });
//                $('#btNuevo').attr('disabled', true);
//                $('#btModificar').attr('disabled', false);
//                $('#btEliminar').attr('disabled', false);
                $('#btNuevo').attr('disabled', false);
                $('#btModificar').attr('disabled', false);
                $('#btEliminar').attr('disabled', false);
                $('#btCancelar').attr('disabled', false);
            } else {
                if (opcion == 2) { // opcion 2 era modificar
                    javascript:location.reload();
                }
                if (opcion == 3) { //opcion 3 era eliminar
                    javascript:location.reload();
                }
            }

        },
        error: function (resp) {
//            $("#error").show();
//            alert('No existe cargo cape');
        }

    });
}

function Cargar_Tabla_Ciudad() {
    armarJSON(6);
    $.ajax({
        url: '/AgendaMedica_V00.2/CiudadSRV',
        data: datosJSON,
        type: 'POST',
        success: function (json_ciudad) {
            //alert(json_ciudad);
            $.each(json_ciudad, function (indice, ciudad) {
                $("#tabla_ciudad").append($("<tr>").append($("<td>" + ciudad.id + "</td>" +
                        "<td>" + ciudad.descripcion + "</td>")));
            });
        },
        error: function () {
            alert('No se pudo obtener ultimo valor en la tabla...!!!');
        }
    });
}
/* Para la función RecuperarPais(e)
 * se analiza la tecla presionada, en este caso la tecla ENTER
 * este analisis se logra al enviar mediante la palabra 'event'
 * desde el html hasta nuestra función en javascript.
 * Se recibe un valor en la variable 'e' que es el valor que produce 
 * al presionar la tecla enter, se declara la una variable 'tecla'
 * se le asigna con una condicional abreviada */
function RecuperarPais(e) {
//    document.getElementById("txtIdpais");
    tecla = (document.all) ? e.keyCode : e.which;
    if (tecla == 13) {
        Cargar_Tabla_Pais();
        $('#myModal').modal();
    } else {
        if (tecla == 113) {
            Cargar_Tabla_Pais();
            $('#myModal').modal();
        }
    }
//   alert(e);
//    var x = event;
//    if (x == 13) {
//        Cargar_Tabla_Pais();
//        $('#myModal').modal();
//    }
}

/* Esta seccion voy a abordar la seleccion del click en una tabla(Intento de experimento)*/
function iniciar() {
    $("#tabla_ciudad_body tr td").click(clickTabla);
}

function clickTabla() {
    var x = $(this).parent("tr");
    x.css("background-color", "red");
}
/*****************************************************************************************/






