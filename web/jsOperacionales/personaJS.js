$(document).ready(function () {
    //alert('Conexion entre capas ');
    //recuperarDATOS();
    limpiar_deshabilitar_Campos_Cargos();
    Cargar_Tabla_Persona();
    Cargar_Combos();
    calendario();
});

function limpiar_deshabilitar_Campos_Cargos() {
    $('#txtId').val("");
    $('#txtNombre1').val("");
    $('#txtNombre2').val("");
    $('#txtApe1').val("");
    $('#txtApe2').val("");
    $('#txtCI').val("");
    $('#txtTel').val("");
    $('#txtDir').val("");
    $('#txtCalendario').val("");
    $('#txtEmail').val("");

    $('#btGuardar').attr('disabled', true);
    $('#btModificar').attr('disabled', true);
    $('#btEliminar').attr('disabled', true);
    $('#btCancelar').attr('disabled', true);
    $('#btBuscar').attr('disabled', true);
    $('#btNuevo').attr('disabled', false);

    $('#txtId').attr('disabled', true);
    $('#txtNombre1').attr('disabled', true);
    $('#txtNombre2').attr('disabled', true);
    $('#txtApe1').attr('disabled', true);
    $('#txtApe2').attr('disabled', true);
    $('#txtCI').attr('disabled', true);
    $('#txtTel').attr('disabled', true);
    $('#txtDir').attr('disabled', true);
    $('#txtCalendario').attr('disabled', true);
    $('#txtEmail').attr('disabled', true);
    $('#combo_ciudad').attr('disabled', true);
    $('#combo_ecivil').attr('disabled', true);
    $('#combo_nacionalidad').attr('disabled', true);
    $('#combo_sexo').attr('disabled', true);
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
    $('#txtNombre1').attr('disabled', false);
    $('#txtNombre1').focus();
    $('#txtNombre2').attr('disabled', false);
    $('#txtApe1').attr('disabled', false);
    $('#txtApe2').attr('disabled', false);
    $('#txtCI').attr('disabled', false);
    $('#txtTel').attr('disabled', false);
    $('#txtDir').attr('disabled', false);
    $('#txtCalendario').attr('disabled', false);
    $('#txtEmail').attr('disabled', false);
    $('#combo_ciudad').attr('disabled', false);
    $('#combo_ecivil').attr('disabled', false);
    $('#combo_nacionalidad').attr('disabled', false);
    $('#combo_sexo').attr('disabled', false);
}

var opcion = "valor";
function armarJSON(banderaOperacion) {
    datosJSON = {
        //"cadena": "Juan"
        "operacion": banderaOperacion,
        "nombre1": $('#txtNombre1').val(),
        "nombre2": $('#txtNombre2').val(),
        "apellido1": $('#txtApe1').val(),
        "apellido2": $('#txtApe2').val(),
        "ci": $('#txtCI').val(),
        "id": $('#txtId').val(),
        "tel": $('#txtTel').val(),
        "dir": $('#txtDir').val(),
        "fechanac": $('#txtCalendario').val(),
        "email": $('#txtEmail').val(),
        "cbo_ecivil": $('#combo_ecivil').val(),
        "cbo_ciudad": $('#combo_ciudad').val(),
        "cbo_nac": $('#combo_nacionalidad').val(),
        "cbo_sexo": $('#combo_sexo').val()
    };
}


function ABM() {
    armarJSON(opcion);
//    alert("operacion enviada es " + datosJSON.operacion);
    $.ajax({
        url: '/AgendaMedica_V00.2/PersonaSRV',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
            // $("#msmAviso").dialog("open");
            if (opcion == 2) {
                $.each(resp, function (indice, valor) {
                    //$('#txtCargo').val(valor.cargo);
                    $('#combo_sexo> option[value=' + valor.id_sexo + ']').attr('selected', 'selected');
                    $('#combo_ciudad> option[value=' + valor.id_ciudad + ']').attr('selected', 'selected');
                    $('#combo_ecivil> option[value=' + valor.id_ecivil + ']').attr('selected', 'selected');
                    $('#combo_nacionalidad> option[value=' + valor.id_nac + ']').attr('selected', 'selected');
                });
                $('#btNuevo').attr('disabled', false);
                $('#btModificar').attr('disabled', false);
                $('#btEliminar').attr('disabled', false);
            } else {
                if (opcion == 8) { //Modificar
                    javascript:location.reload();
                }
                if (opcion == 9) { //Eliminar
                    recargaSalvaje();
//                    javascript:location.reload();
                }
                if (opcion == 1) { //Agregar

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
function recargaSalvaje() {
    console.log('Hasta aqui no se refresco la pagina');
    $("#tabla_cargos").load("http://localhost:8080/AgendaMedica_V00.2/vistas/personas.html #tabla_cargos");
    Cargar_Tabla_Persona();
    limpiar_deshabilitar_Campos_Cargos();
    console.log('Hasta se refresco la pagina');
    return false;
    
}
function agregarP() {
    opcion = 1;
    ABM();
}
function modificar() {
    opcion = 8;
    ABM();
}
function eliminar() {
    opcion = 9;
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
        agregarP();
//        alert('Se ha guardado el mensaje');
//        javascript:location.reload();

    } else {
        javascript:location.reload();
    }
}
function Cargar_Tabla_Persona() {
    armarJSON(7);
    $.ajax({
        url: '/AgendaMedica_V00.2/PersonaSRV',
        data: datosJSON,
        type: 'POST',
        success: function (json_usuario) {
            //alert(json_usuario);
            $.each(json_usuario, function (indice, usu) {
                $("#tabla_cargos").append($("<tr>").append($("<td>" + usu.id + "</td>" +
                        "<td>" + usu.nombre1 + "</td>" +
                        "<td>" + usu.nombre2 + "</td>" +
                        "<td>" + usu.ape1 + "</td>" +
                        "<td>" + usu.ape2 + "</td>" +
                        "<td>" + usu.ci + "</td>" +
                        "<td>" + usu.tel + "</td>" +
                        "<td>" + usu.dir + "</td>" +
                        "<td>" + usu.fecha + "</td>" +
                        "<td>" + usu.email + "</td>" +
                        "<td>" + usu.desc_nac + "</td>" +
                        "<td>" + usu.desc_ciudad + "</td>" +
                        "<td>" + usu.des_ecivil + "</td>" +
                        "<td>" + usu.desc_sex + "</td>")));

            });
        },
        error: function () {
            alert('No se pudo obtener ultimo valor en la tabla...!!!');
        }
    });
}

function Cargar_Combos() {
    ComboCiudad();
    ComboNacionalidad();
    ComboECivil();
    ComboSexo();
}
function ComboCiudad() {
    armarJSON(3);
    $.ajax({
        url: '/AgendaMedica_V00.2/PersonaSRV',
        data: datosJSON,
        type: 'POST',
        success: function (json) {
            $.each(json, function (indice, valor) {
                $("#combo_ciudad").append("<option value= \"" + valor.id + "\"> " + valor.id + " - " + valor.descripcion + "</option>");

            });
        },
        error: function () {
            alert('No se pudo obtener ultimo valor en el combo ciudad...!!!');
        }
    });
}
function ComboECivil() {
    armarJSON(4);
    $.ajax({
        url: '/AgendaMedica_V00.2/PersonaSRV',
        data: datosJSON,
        type: 'POST',
        success: function (json) {
            $.each(json, function (indice, valor) {
                $("#combo_ecivil").append("<option value= \"" + valor.id + "\"> " + valor.id + " - " + valor.descripcion + "</option>");

            });
        },
        error: function () {
            alert('No se pudo obtener ultimo valor en el combo ciudad...!!!');
        }
    });
}
function ComboNacionalidad() {
    armarJSON(5);
    $.ajax({
        url: '/AgendaMedica_V00.2/PersonaSRV',
        data: datosJSON,
        type: 'POST',
        success: function (json) {
            $.each(json, function (indice, valor) {
                $("#combo_nacionalidad").append("<option value= \"" + valor.vpais_id + "\"> " + valor.vpais_id + " - " + valor.vpais_descripcion + "</option>");

            });
        },
        error: function () {
            alert('No se pudo obtener ultimo valor en el combo ciudad...!!!');
        }
    });
}
function ComboSexo() {
    armarJSON(6);
    $.ajax({
        url: '/AgendaMedica_V00.2/PersonaSRV',
        data: datosJSON,
        type: 'POST',
        success: function (json) {
            $.each(json, function (indice, valor) {
                $("#combo_sexo").append("<option value= \"" + valor.id + "\"> " + valor.id + " - " + valor.descripcion + "</option>");

            });
        },
        error: function () {
            alert('No se pudo obtener ultimo valor en el combo ciudad...!!!');
        }
    });
}
function calendario() {
    $('.datepicker').datepicker({todayBtn: "linked", language: "es", autoclose: true, format: 'dd/mm/yyyy'});
}




