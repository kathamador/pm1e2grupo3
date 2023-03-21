<?php
header('Content-Type: application/json');

// Database credentials
$dbhost = 'localhost';
$dbuser = 'root';
$dbpass = '';
$dbname = 'examapp';

// Get the JSON data sent from the app
$data = json_decode(file_get_contents('php://input'), true);

// Create connection to database
$con = mysqli_connect($dbhost, $dbuser, $dbpass, $dbname);

if (!$con) {
    die('Could not connect: ' . mysqli_error());
}

// Escape special characters in the JSON data
$id = mysqli_real_escape_string($con, $data['IdPerson']);

// Delete data from the table
$sql = "DELETE FROM persons WHERE IdPerson='$id'";
if (mysqli_query($con, $sql)) {
    $response = array('success' => true);
} else {
    $response = array('success' => false, 'message' => 'Error deleting data: ' . mysqli_error($con));
}

// Close database connection
mysqli_close($con);

// Send response back to the app in JSON format
echo json_encode($response);
?>