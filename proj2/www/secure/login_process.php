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
$query = "SELECT * FROM users WHERE name = '" . $_POST['username'] . "' and pass = '" . $_POST['password'] . "' and serial = '" . $_SERVER['SSL_CLIENT_M_SERIAL'] . "'";
$result = $db->query($query);
$rows = 0;
while($row = $result->fetchArray()) {
	$rows++;
}
if ($rows == 1) {
	session_start();
	$_SESSION['on'] = TRUE;
	$_SESSION['has_pin'] = FALSE;
	$_SESSION['username'] = $_POST['username'];
    redirect('../index.php');
} else {
	redirect('../login.php');
}
?>
