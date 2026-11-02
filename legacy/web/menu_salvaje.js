$(document).ready(function () {
    MenuServelet();
});
var opcion;
function armarJSON(banderaOperacion) {
    datosJSON = {
        //"cadena": "Juan"
        "operacion": banderaOperacion,
        
    };
}


function MenuServelet() {
    armarJSON(1);
    $.ajax({
        url: '/AgendaMedica_V00.2/Menu_Salvaje',
        //url: "http://localhost:8084/LP3_NOCHE/usuarioSRV",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
            $("#msmAviso").dialog("open");
        },
        error: function (resp) {
            $("#error").show();
           
        }

    });
}

