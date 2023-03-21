<?php
header('Access-Control-Allow-Origin: *');
header("Access-Control-Allow-Headers: *");
$conexion = new mysqli('localhost', 'root', '', 'examapp');

$postdata = file_get_contents("php://input");
$request = json_decode($postdata);

$id = $request->IdPerson;
$name = $request->Name;
$phone = $request->Phone;

$latitud = $request->Latitud;
$longitud = $request->Longitud;

$sql_query = "UPDATE persons SET Name='$name', Phone='$phone', Latitud='$latitud', Longitud='$longitud' WHERE IdPerson='$id'";

$result = $conexion->query($sql_query);

if ($result) {
    $resultado = array('estado' => true);
    echo json_encode($resultado);
} else {
    $resultado = array('estado' => false);
    echo json_encode($resultado);
}
?>