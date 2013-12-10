<?php
session_start();
include '../utils/db.php';
include '../utils/function.php';
if (!isset($_SESSION['on']) || !isset($_POST['renew'])) {
	redirect('accounts.php');
	exit();
}
$days = config_days();
$length = config_length();
$pin = genpin($length);
$expdate = time() + 24 * 60 * 60 * $days;
$query = "UPDATE passwd SET password = '" . md5($pin) . "', expflag = 0, expdate =" . $expdate . ", retrycount = 0  WHERE username = '" . $_POST['renewList'] . "'";
$db->exec($query);
$_SESSION['pin'] = $pin;
$_SESSION['account'] = $_POST['renewList'];
$_SESSION['has_pin'] = TRUE;
$_SESSION['account_state'] = 'Renewed';
redirect('../secure/account.php');
?>
