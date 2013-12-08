<?php
include '../utils/db.php';
include '../utils/function.php';
if(empty($_SESSION)) {
	session_start();
}
if (!empty($_SESSION) || !isset($_POST['submit'])) {
   redirect('../index.php');
   exit();
}
$query = "SELECT * FROM users WHERE name = '" . $_POST['username'] . "'";
$result = $db->query($query);
$rows = 0;
while($row = $result->fetchArray()) {
	$rows++;
}
$query = "SELECT * FROM users WHERE serial = '" . $_SERVER['SSL_CLIENT_M_SERIAL'] . "'";
$result2 = $db->query($query);
$rows2 = 0;
while($row = $result->fetchArray()) {
	$rows2++;
}
if ($rows > 0 || $rows2 > 0) {
	echo $rows;
	echo $rows2;
    redirect('../register.php');
} else {
    $query = "INSERT INTO users (name, pass, serial) VALUES ('" . $_POST['username'] . "', '" . $_POST['password'] . "', '" . $_SERVER['SSL_CLIENT_M_SERIAL'] . "')";
    $db->exec($query);
    redirect('../index.php');
}
?>
