<?php 

$servername = "localhost";
$username = "root";
$password = "";
$database = "onlinet";

$conn = new mysqli($servername, $username, $password, $database);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$name = array();

$sql = "SELECT fullname FROM instructor;";

$stmt = $conn->prepare($sql);

$stmt->execute();

$stmt->bind_result($fullname);

while($stmt->fetch()){
    $temp=[
    'fullname'=>$fullname];

    array_push($name, $temp);
}
echo json_encode($name);

?>
