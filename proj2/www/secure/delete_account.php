<?php
include '../utils/db.php';
$query = "DELETE FROM useraccounts WHERE username = '" . $_SESSION['username'] . "'";
$db->exec($query);
$query = "DELETE FROM passwd WHERE name = '" . $_SESSION['username'] . "'";
$db->exec($query);
redirect('../secure/account.php');
?>
