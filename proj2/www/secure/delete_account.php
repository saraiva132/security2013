<?php
session_start();
require_once '../utils/db.php';
require_once '../utils/function.php';
if (!isset($_SESSION['on'])) {
	redirect('../login.php');
	exit();
}
if (!isset($_POST['delete'])) {
	redirect('../secure/accounts.php');
	exit();
}
$query = "DELETE FROM useraccounts WHERE account = :deleteList";
$stmt = $db->prepare($query);
$stmt->bindValue(':deleteList', $_POST['deleteList']);
$stmt->execute();
$query = "DELETE FROM passwd WHERE username = :deleteList";
$stmt = $db->prepare($query);
$stmt->bindValue(':deleteList', $_POST['deleteList']);
$stmt->execute();
$query = "DELETE FROM geopermissions WHERE account = :deleteList";
$stmt = $db->prepare($query);
$stmt->bindValue(':deleteList', $_POST['deleteList']);
$stmt->execute();
//$pid = shell_exec('ps aux | grep ' . $_POST['deleteList'] . ' | cut -d \' \' -f3 | head -n1');
//shell_exec('kill ' . $pid);
//shell_exec('rm -r /home/' . $_POST['deleteList'] . '/');
redirect('../secure/accounts.php');
?>
