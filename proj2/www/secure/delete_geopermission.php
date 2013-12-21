<?php
session_start();
require_once '../utils/db.php';
require_once '../utils/function.php';
if (!isset($_SESSION['on'])) {
	redirect('../login.php');
	exit();
}
if (!isset($_POST['deletegeo'])) {
	redirect('../secure/permissions.php');
	exit();
}
$fields = explode(', ',$_POST['deleteGeoList']);
$account = $fields[0];
$country = $fields[1];
$city = $fields[2];
$query = "DELETE FROM geopermissions WHERE account = :account and country = :country and city = :city";
$stmt = $db->prepare($query);
$stmt->bindValue(':account', $account);
$stmt->bindValue(':country', $country);
$stmt->bindValue(':city', $city);
$stmt->execute();
$_SESSION['error'] = TRUE;
$_SESSION['error_log'] = 'Deleted permission (' . $account . ', ' . $country . ', ' . $city . ').';
redirect('../secure/permissions.php');
?>
