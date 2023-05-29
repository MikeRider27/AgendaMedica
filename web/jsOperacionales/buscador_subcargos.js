$(document).ready(function () {

    (function ($) {

        $('#filtrar_subcargos').keyup(function () {

            var rex = new RegExp($(this).val(), 'i');
            $('.buscar_subcargos tr').hide();
            $('.buscar_subcargos tr').filter(function () {
                return rex.test($(this).text());
            }).show();

        });

    }(jQuery));
});
/* Capturar fila hecho por el profe*/
function capturarFila(id) {

    v_id = 0;
    v_ciudad = "";
    // $('#' + id + ' tr').click(function () {
    $('#' + id + ' tbody tr').click(function () {
        v_id = $(this).find("td").eq(0).html();
        v_ciudad = $(this).find("td").eq(1).html();
        $('#txtId').val(v_id);
        $('#txtSubcargo').val(v_ciudad);
        //listarSegunFiltroS();
    });
}
/***********************************/
/*
 *  #tabla_ciudad tbody tr
 * 
 */