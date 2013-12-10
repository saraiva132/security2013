<?php
session_start();
include 'utils/function.php';
if(isset($_SESSION['on'])) {
   redirect('index.php');
   exit();
}
include 'theme/menu.php'
?>
<div class="container">
	<hgroup>
		<h1>Sign up</h1>
		<h2>Insert your citizen card into your card reader!</h2>
	</hgroup>
	<form>
		<div class=”field_container”>
			<label for="name">Username</label>
			<input type="text" name="username" pattern="[_a-zA-Z0-9]{4,16}" required>
		</div>
		<div class=”field_container”>
			<label for="password">Password</label>
			<input type="password" name="password" pattern="[a-zA-Z0-9]{6,16}" required>
		</div>
		<p>
			<input type="submit" name="submit" id="submit" value="Register" formmethod="post" formaction="secure/register_process.php"/>
		</p>
	</form>
</div>
<?php include 'theme/footer.php'?>
