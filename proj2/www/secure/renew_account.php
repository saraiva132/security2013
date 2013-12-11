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
$salt = gensalt();
$query = "UPDATE passwd SET password = '" . salt_hash($pin) . "', salt = '" . $salt . "', expflag = 0, expdate =" . $expdate . ", retrycount = 0  WHERE username = :renewList";
$stmt = $db->prepare($query);
$stmt->bindValue(':renewList', $_POST['renewList']);
$result = $stmt->execute();
$_SESSION['pin'] = $pin;
$_SESSION['account'] = $_POST['renewList'];
$_SESSION['has_pin'] = TRUE;
$_SESSION['account_state'] = 'Renewed';
redirect('../secure/account.php');
?>
