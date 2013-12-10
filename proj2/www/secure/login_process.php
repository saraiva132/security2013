<?php
session_start();
include '../utils/db.php';
include '../utils/function.php';
if (isset($_SESSION['on']) || !isset($_POST['username'])) {
   redirect('../index.php');
   exit();
}
$query = "SELECT * FROM users WHERE name = '" . $_POST['username'] . "' and pass = '" . salt_hash($_POST['password']) . "' and serial = '" . $_SERVER['SSL_CLIENT_M_SERIAL'] . "'";
$result = $db->query($query);
$rows = 0;
while($row = $result->fetchArray()) {
	$rows++;
}
if ($rows == 1) {
	$_SESSION['on'] = TRUE;
	$_SESSION['username'] = $_POST['username'];
    redirect('../index.php');
} else {
	redirect('../login.php');
}
?>
