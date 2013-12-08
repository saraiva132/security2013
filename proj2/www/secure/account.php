<?php
include '../utils/function.php';
if(empty($_SESSION)) {
   session_start();
}
if(!$_SESSION['on']) {
   redirect('../login.php');
   exit();
}
?>
<?php include '../theme/menu.php'?>
<div class="container">
	<hgroup>
		<?php
		include '../utils/function.php';
		if ($_SESSION['has_pin']) {
			echo '<h2>Account: ' . $_SESSION['account'] . '</h2>';
			echo '<h2>Pin: ' . $_SESSION['pin'] . '</h2>';
			$_SESSION['has_pin'] = FALSE;
			$_SESSION['account'] = '';
			$_SESSION['pin'] = '';
		} else {
			echo '<form>';
			echo '	<div class="field_container">';
			echo '		<label for="account">Account Name</label>';
			echo '		<input type="text" name="account" required>';
			echo '	</div>';
			echo '	<p>';
			echo '		<input type="submit" name="submit" id="submit" value="Create" formmethod="post" formaction="secure/pin_process.php"/>';
			echo '	</p>';
			echo '</form>';
		}
		?>
	</hgroup>
</div>
<?php include '../theme/footer.php'?>
