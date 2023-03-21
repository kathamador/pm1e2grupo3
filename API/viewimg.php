<?php
// Conectar a la base de datos MySQL
$conexion = mysqli_connect("localhost", "root", "", "examapp");

// Obtener la imagen desde la base de datos MySQL
$sql = "SELECT Image FROM persons";
$resultado = mysqli_query($conexion, $sql);

// Crear un array de objetos con la información de las imágenes
$imagenes = array();
while ($fila = mysqli_fetch_assoc($resultado)) {
    $imagen_base64 = $fila['Image'];
    $imagen = base64_decode($imagen_base64);
    $imagenes[] = array("imagen" => base64_encode($imagen));
}

// Codificar el array en formato JSON y enviarlo como respuesta
header('Content-Type: application/json');
echo json_encode($imagenes);

// Cerrar la conexión a la base de datos MySQL
mysqli_close($conexion);
?>
