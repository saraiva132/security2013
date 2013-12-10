<?php
session_start();
include '../utils/function.php';
if (!isset($_SESSION['on'])) {
   redirect('../login.php');
   exit();
}
include '../theme/menu.php'
?>
<div class="container">
	<?php
	if (isset($_SESSION['has_pin'])) {
		echo '
		<section id="intro">
			<hgroup>
				<h1>Temporary Account ' . $_SESSION['account_state'] . '</h1>
				<h2>You can start using your new account!</h2>
			</hgroup>
		</section>
		<h2>Account: ' . $_SESSION['account'] . '</h2>
		<h2>Pin: ' . $_SESSION['pin'] . '</h2>
		';
		unset($_SESSION['has_pin']);
		unset($_SESSION['account']);
		unset($_SESSION['pin']);
		unset($_SESSION['account_state']);
	} else {
		echo '
		<section id="intro">
			<hgroup>
				<h1>New Temporary Account</h1>
				<h2>Create a new Linux account at this host.</h2>
			</hgroup>
		</section>
		<form>
			<div class="field_container">
			 	<label for="account">Name</label>
				<input type="text" name="account" required>
			</div>
			<p>
				<input type="submit" name="create" id="create" value="create" formmethod="post" formaction="pin_process.php"/>
			</p>
		</form>
		';
	}
	?>
</div>
<?php include '../theme/footer.php'?>
