<?php
session_start();
include '../utils/db.php';
include '../utils/function.php';
if (!isset($_SESSION['on']) || !isset($_POST['delete'])) {
	redirect('accounts.php');
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
redirect('../secure/accounts.php');
?>
