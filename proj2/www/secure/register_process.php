<?php
session_start();
require_once '../utils/db.php';
require_once '../utils/function.php';
if (isset($_SESSION['on'])) {
   redirect('../index.php');
   exit();
}
if (!isset($_POST['username'])) {
   redirect('../register.php');
   exit();
}
$query = "SELECT * FROM users WHERE name = :username";
$stmt = $db->prepare($query);
$stmt->bindValue(':username', $_POST['username']);
$result = $stmt->execute();
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
	$salt = gensalt();
    $query = "INSERT INTO users (name, pass, salt, serial) VALUES (:username, :hash, '" . $salt . "', '" . $_SERVER['SSL_CLIENT_M_SERIAL'] . "')";
	$stmt = $db->prepare($query);
	$stmt->bindValue(':username', $_POST['username']);
	$stmt->bindValue(':hash', salt_hash($_POST['password'], $salt));
	$result = $stmt->execute();
    redirect('../login.php');
}
?>
