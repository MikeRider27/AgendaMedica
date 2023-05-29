$(document).ready(function () {

    (function ($) {

        $('#filtrar').keyup(function () {

            var rex = new RegExp($(this).val(), 'i');
            $('.buscar tr').hide();
            $('.buscar tr').filter(function () {
                return rex.test($(this).text());
            }).show();

        });

    }(jQuery));
    (function ($) {

        $('#filtrar_pais').keyup(function () {

            var rex = new RegExp($(this).val(), 'i');
            $('.buscar_pais tr').hide();
            $('.buscar_pais tr').filter(function () {
                return rex.test($(this).text());
            }).show();

        });

    }(jQuery));

});
/* Capturar fila hecho por el profe*/
function capturarFilaC(id) {

    v_id = 0;
    v_ciudad = "";
    // $('#' + id + ' tr').click(function () {
    $('#' + id + ' tbody tr').click(function () {
        v_id = $(this).find("td").eq(0).html();
        v_ciudad = $(this).find("td").eq(1).html();
        $('#txtId').val(v_id);
        $('#txtCiudad').val(v_ciudad);
        $('#btEliminar').attr('disabled', false);
        $('#btCancelar').attr('disabled', false)
        //listarSegunFiltroC();
    });
}
/***********************************/
/*
 *  #tabla_ciudad tbody tr
 * 
 */