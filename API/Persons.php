<?php

// Conexión a la base de datos
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "examapp";

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Obtener los datos enviados desde la aplicación
$nombre = $_POST["nombre"];
$telefono = $_POST["telefono"];
$base64 = $_POST["base64"];
$latitud = $_POST["latitud"];
$longitud = $_POST["longitud"];

// Insertar los datos en la tabla de la base de datos
$sql = "INSERT INTO persons (Name, Phone, Image, Longitud, Latitud) VALUES ('$nombre', '$telefono', '$base64', '$longitud', '$latitud')";
if ($conn->query($sql) === TRUE) {
    $response["success"] = 1;
    $response["message"] = "Datos guardados correctamente";
} else {
    $response["success"] = 0;
    $response["message"] = "Error al guardar los datos:" . $conn->error;
}

// Salida como JSON
header('Content-Type: application/json');
echo json_encode($response);

// Cierre de la conexión a la base de datos
$conn->close();

?>

