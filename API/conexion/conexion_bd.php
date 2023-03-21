<?php
//$con = new mysqli('localhost', 'root', '', 'examapp');
$con =mysqli_connect("host=localhost port=5432 dbname=examapp user=root password=* options='--client_encoding=UTF8'");
/* if ($db_handle) {

     echo 'Connection attempt succeeded.';
    
     } else {
    
     echo 'Connection attempt failed.';
    
     }
    
     pg_close($db_handle);*/
?>