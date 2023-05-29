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
    v_ciudad = "";
    v_cupo = 0;
    // $('#' + id + ' tr').click(function () {
    $('#' + id + ' tbody tr').click(function () {
        v_id = $(this).find("td").eq(0).html();
        v_ciudad = $(this).find("td").eq(1).html();
        v_cupo = $(this).find("td").eq(2).html();
        $('#txtId').val(v_id);
        $('#txtCargo').val(v_ciudad);
        $('#txtcupo').val(v_cupo);
        //listarSegunFiltro();
    });
}
/***********************************/
/*
 *  #tabla_ciudad tbody tr
 * 
 */