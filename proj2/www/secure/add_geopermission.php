<?php
session_start();
require_once '../utils/db.php';
require_once '../utils/function.php';
if (!isset($_SESSION['on'])) {
	redirect('../login.php');
	exit();
}
if (!isset($_POST['addgeo'])) {
	redirect('../secure/permissions.php');
	exit();
}
$query = "INSERT INTO geopermissions (account, country, city) VALUES (:account, :country, :city)";
$stmt = $db->prepare($query);
$stmt->bindValue(':account', $_POST['addGeoList']);
$stmt->bindValue(':country', $_POST['country']);
if (strcmp($_POST['city'],'') != 0) {
	$city = $_POST['city'];
} else {
	$city = 'N/A';
}
$stmt->bindValue(':city', $city);
$stmt->execute();
$_SESSION['error'] = TRUE;
$_SESSION['error_log'] = 'New permission added (' . $_POST['addGeoList'] . ', ' . $_POST['country'] . ', ' . $city . ').';
redirect('../secure/permissions.php');
?>
