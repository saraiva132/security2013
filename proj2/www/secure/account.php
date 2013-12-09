<?php
session_start();
include '../utils/function.php';
if(!isset($_SESSION['on'])) {
   redirect('../login.php');
   exit();
}
include '../theme/menu.php'
?>
<div class="container">
	<?php
	if (isset($_SESSION['has_pin'])) {
		echo '<section id="intro">';
		echo '	<hgroup>';
		echo '		<h1>Temporary Account Created</h1>';
		echo '		<h2>You can start using your new account!</h2>';
		echo '	</hgroup>';
		echo '</section>';
		echo '<h2>Account: ' . $_SESSION['account'] . '</h2>';
		echo '<h2>Pin: ' . $_SESSION['pin'] . '</h2>';
		unset($_SESSION['has_pin']);
		unset($_SESSION['account']);
		unset($_SESSION['pin']);
	} else {
		echo '<section id="intro">';
		echo '	<hgroup>';
		echo '		<h1>New Temporary Account</h1>';
		echo '		<h2>Create a new Linux account at this host.</h2>';
		echo '	</hgroup>';
		echo '</section>';
		echo '<form>';
		echo '	<div class="field_container">';
		echo '		<label for="account">Name</label>';
		echo '		<input type="text" name="account" required>';
		echo '	</div>';
		echo '	<p>';
		echo '		<input type="submit" name="submit" id="submit" value="Create" formmethod="post" formaction="secure/pin_process.php"/>';
		echo '	</p>';
		echo '</form>';
	}
	?>
</div>
<?php include '../theme/footer.php'?>
