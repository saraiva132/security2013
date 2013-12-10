<?php
session_start();
include '../utils/db.php';
include '../utils/function.php';
if (isset($_SESSION['on']) || !isset($_POST['username'])) {
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
while($row = $result2->fetchArray()) {
	$rows2++;
}
if ($rows > 0 || $rows2 > 0) {
    redirect('../register.php');
    exit();
} else {
    $query = "INSERT INTO users (name, pass, serial) VALUES ('" . $_POST['username'] . "', '" . salt_hash($_POST['password']) . "', '" . $_SERVER['SSL_CLIENT_M_SERIAL'] . "')";
    $db->exec($query);
    redirect('../index.php');
}
?>
