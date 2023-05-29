<%-- 
    Document   : logueo
    Created on : 27-feb-2017, 13:50:22
    Author     : juan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Loguearse</title>
        <script type="text/javascript" src="Recursos/js/jquery.js" ></script>
        <script type="text/javascript" src="Recursos/js/jquery-ui.min.js" ></script>
        <script type="text/javascript" src="Recursos/js/bootstrap.min.js" ></script>
        <link rel="stylesheet" href="Recursos/css/bootstrap.css">
        <script type="text/javascript" src="jsEventos/LogueoEVT.js" ></script>
    </head>
    <body>

        <div class="jumbotron container">
            <h1>Acceso al Sistema</h1>
            <form action="logueoSRV" method="post" class="form-inline">
                <div class="input-group">
                    <span class="input-group-addon" id="basic-addon1">Usuarios</span>
                    <input type="text" name="usuario" id="usuario" class="form-control">
                </div>

                <div class="input-group">
                    <span class="input-group-addon" id="basic-addon1">Usuarios</span>
                    <input type="password" name="pass" id="clave" class="form-control">
                </div>


                <input type="submit" value="Iniciar Sesion" class="btn btn-primary">

            </form>
        </div>

    </body>
</html>
