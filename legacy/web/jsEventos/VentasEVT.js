$(document).ready(function () {
    Limpiar();
    BotonesOFF();
    CamposOFF();
});

function BotonesOFF() {
    $('#btnuevo').attr('disabled', false);
    $('#btgrabar').attr('disabled', true);
    $('#btbuscar').attr('disabled', false);
    $('#btanular').attr('disabled', true);
    $('#btcancelar').attr('disabled', true);
}
function BotonesON() {
    $('#btnuevo').attr('disabled', true);
    $('#btgrabar').attr('disabled', false);
    $('#btbuscar').attr('disabled', true);
    $('#btanular').attr('disabled', true);
    $('#btcancelar').attr('disabled', false);
}

function Limpiar() {
    $('#id').val("");
    $('#id_usuario').val("");
    $('#usuario').val("");
    $('#id_cliente').val("");
    $('#cliente').val("");
    $('#osbv').val("");
    $('#codigoInsumo').val("");
    $('#descripcion').val("");
    $('#precio').val("");
    $('#cantidad').val("");
    $('#tabla_detalle').empty();
}
function CamposOFF() {
    $('#id').attr('disabled', true);
    $('#id_usuario').attr('disabled', true);
    $('#id_cliente').attr('disabled', true);
    $('#osbv').attr('disabled', true);
    $('#codigoInsumo').attr('disabled', true);
    $('#descripcion').attr('disabled', true);
    $('#precio').attr('disabled', true);
    $('#cantidad').attr('disabled', true);
}
function CamposON() {
    $('#id').attr('disabled', true);
    $('#id_usuario').attr('disabled', false);
    $('#id_cliente').attr('disabled', false);
    $('#osbv').attr('disabled', false);
    $('#codigoInsumo').attr('disabled', false);
    $('#descripcion').attr('disabled', true);
    $('#precio').attr('disabled', true);
    $('#cantidad').attr('disabled', false);
}

function BotonNuevo() {
    CamposON();
    BotonesON();
    $('#id_usuario').focus();
    Limpiar();
}
function BotonCancelar() {
    CamposOFF();
    BotonesOFF();
    Limpiar();
    HoraLocal();
}
function BotonBuscar() {
    $('#id').attr('disabled', false);
    $('#id').focus();
}


