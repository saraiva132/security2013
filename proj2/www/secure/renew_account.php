<?php
include '../utils/db.php';
include '../utils/function.php';
$days = config_days();
$pin = genpin($length);
$expdate = time() + 24 * 60 * 60 * $days;
$query = "UPDATE passwd SET password = '" . $pin . "' expflag = 0, expdate ='" . $expdate . "', retrycount = 0  WHERE name = '" . $_POST['renewList'] . "'";
$db->exec($query);
$_SESSION['pin'] = $pin;
$_SESSION['account'] = $_POST['renewList'];
$_SESSION['has_pin'] = TRUE;
$_SESSION['account_state'] = 'Renewed';
redirect('../secure/account.php');
?>
