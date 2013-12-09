<?php include 'theme/menu.php'?>
<div class="container">
	<br>
	<br>
	<section id="intro">
		<hgroup>
			<h1>Create your Linux Temporary Accounts.</h1>
			<?php
			if (isset($_SESSION['on'])) {
				echo '<h2>Welcome again ' . $_SESSION['username'] . '!</h2>';
			} else {
				echo '<h2>Authenticate with your Citizen Card.</h2>';
			}
			?>
		</hgroup>
	</section>
</div>
<?php include 'theme/footer.php'?>
