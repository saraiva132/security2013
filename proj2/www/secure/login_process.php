<?php
session_start();
require_once '../utils/db.php';
require_once '../utils/function.php';
if (isset($_SESSION['on']) || !isset($_POST['username'])) {
	redirect('../login.php');
	exit();
}
$query = "SELECT salt FROM users WHERE name = :username";
$stmt = $db->prepare($query);
$stmt->bindValue(':username', $_POST['username']);
$result = $stmt->execute();
$rows = 0;
while ($row = $result->fetchArray()) {
	$salt = $row['salt'];
	$rows++;
}
if ($rows < 1) {
	$_SESSION['error'] = TRUE;
	$_SESSION['error_log'] = 'Invalid username, password or citizen card.';
	redirect('../login.php');
	exit();
}
$query = "SELECT * FROM users WHERE name = :name and pass = :pass and serial = '" . $_SERVER['SSL_CLIENT_M_SERIAL'] . "'";
$stmt = $db->prepare($query);
$stmt->bindValue(':name', $_POST['username']);
$stmt->bindValue(':pass', salt_hash($_POST['password'], $salt));
$result = $stmt->execute();
$rows = 0;
while ($row = $result->fetchArray()) {
	$rows++;
}
if ($rows < 1) {
	$_SESSION['error'] = TRUE;
	$_SESSION['error_log'] = 'Invalid username, password or citizen card.';
	redirect('../login.php');
	exit();
}
$_SESSION['on'] = TRUE;
$_SESSION['username'] = $_POST['username'];
redirect('../index.php');
?>
