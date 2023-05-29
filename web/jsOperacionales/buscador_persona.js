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
    v_nombre1 = "";
    v_nombre2 = "";
    v_ape1 = "";
    v_ape2 = "";
    v_ci="";
    v_tel = "";
    v_dir = "";
    v_fecha = "";
    v_email = "";
    // $('#' + id + ' tr').click(function () {
    $('#' + id + ' tbody tr').click(function () {
        v_id = $(this).find("td").eq(0).html();
        v_nombre1 = $(this).find("td").eq(1).html();
        v_nombre2 = $(this).find("td").eq(2).html();
        v_ape1 = $(this).find("td").eq(3).html();
        v_ape2 = $(this).find("td").eq(4).html();
        v_ci = $(this).find("td").eq(5).html();
        v_tel = $(this).find("td").eq(6).html();
        v_dir = $(this).find("td").eq(7).html();
        v_fecha = $(this).find("td").eq(8).html();
        v_email = $(this).find("td").eq(9).html();
        
        $('#txtId').val(v_id);
        $('#txtNombre1').val(v_nombre1);
        $('#txtNombre2').val(v_nombre2);
        $('#txtApe1').val(v_ape1);
        $('#txtApe2').val(v_ape2);
        $('#txtCI').val(v_ci);
        $('#txtTel').val(v_tel);
        $('#txtDir').val(v_dir);
        $('#txtCalendario').val(v_fecha);
        $('#txtEmail').val(v_email);
        listarSegunFiltro();
        return false;
    });
}
/***********************************/
/*
 *  #tabla_ciudad tbody tr
 * 
 */