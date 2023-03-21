<?php
header('Access-Control-Allow-Origin: *');
header("Access-Control-Allow-Headers: *");
$conexion = new mysqli('localhost', 'root', '', 'examapp');
$resultado = $conexion->query('SELECT * FROM persons');


// Ejecutar la consulta
// Verificar si hay resultados
if ($resultado->num_rows > 0) {
    // Crear un arreglo vacío para almacenar los resultados
    $arreglo_resultados = array();

    // Recorrer los resultados y agregarlos al arreglo
    while($fila = $resultado->fetch_assoc()) {
        $arreglo_resultados[] = $fila;
    }

    // Convertir el arreglo a formato JSON
    $json_resultados = json_encode($arreglo_resultados);

    // Mostrar el resultado en formato JSON
    echo $json_resultados;
} else {
    echo "No se encontraron resultados";
}

// Cerrar la conexión
//$con->close();


?>