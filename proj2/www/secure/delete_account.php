<?php
session_start();
include '../utils/db.php';
include '../utils/function.php';
if (!isset($_SESSION['on']) || !isset($_POST['delete'])) {
	redirect('accounts.php');
	exit();
}
$query = "DELETE FROM useraccounts WHERE account = '" . $_POST['deleteList'] . "'";
$db->exec($query);
$query = "DELETE FROM passwd WHERE username = '" . $_POST['deleteList'] . "'";
$db->exec($query);
redirect('../secure/accounts.php');
?>
