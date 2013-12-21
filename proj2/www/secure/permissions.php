<?php
session_start();
require_once '../utils/function.php';
if (!isset($_SESSION['on'])) {
	redirect('../login.php');
	exit();
}
require_once '../theme/menu.php';
?>
<div class="container">
	<hgroup>
		<h1>Geographic Permissions</h1>
		<h2>Grant access to your Linux accounts.</h2>
	</hgroup>
	<table id="accounts">
		<tr>
			<th>Account Name</th>
			<th>Country</th>
			<th>City</th>
		</tr>
		<?php
		require_once '../utils/db.php';
		$query = "SELECT geopermissions.account as account, country, city FROM geopermissions JOIN useraccounts ON geopermissions.account = useraccounts.account WHERE useraccounts.username = '" . $_SESSION['username'] . "'";
		$result = $db->query($query);
		for($idx = 0; $row = $result->fetchArray(); $idx++) {
			if ($idx % 2 == 0) {
				echo '<tr class="alt">';
			} else {
				echo '<tr>';
			}
			echo '
					<td>' . $row['account'] . '</td>
					<td>' . $row['country'] . '</td>
					<td>' . $row['city'] . '</td>
				</tr>
			';
		}
		?>
	</table>
	<br>
	<?php
	$result = $db->query($query);
	$rowsGEO = 0;
	$listGEO = '<select name = "geoList">';
	while ($row = $result->fetchArray()) {
		$rowsGEO++;
		$value = $row['account'] . ', ' . $row['country'];
		if (strcmp($row['city'],'') != 0) {
			$value .= ', ' . $row['city'];
		}
		$listGEO .= '<option value = "' . $value . '">' . $value . '</option>';
	}
	$listGEO .= '</select>';
	if ($rowsGEO > 0) {
		echo '
			<h3>Remove Permission</h3>
			<form>
			' . $listGEO . '
				<input type="submit" name="delete" id="delete" value="Delete" formmethod="post" formaction="delete_geopermission.php"/>
			</form>
		';
	}
	if (isset($_SESSION['error'])) {
		echo '<p>' . $_SESSION['error_log'] . '</p>';
		unset($_SESSION['error']);
		unset($_SESSION['error_log']);
	}
	$query = "SELECT passwd.username as account FROM passwd JOIN useraccounts ON passwd.username = useraccounts.account WHERE useraccounts.username = '" . $_SESSION['username'] . "'";
	$result = $db->query($query);
	$listACCOUNT = '<select name = "accountList">';
	for($idx = 0; $row = $result->fetchArray(); $idx++) {
		$listACCOUNT .= '<option value = "' . $row['account'] . '">' . $row['account'] . '</option>';
	}
	$listACCOUNT .= '</select>';
	if ($idx > 0) {
		echo '
			<h3>Add Permission</h3>
			<form>
				<label for="account">Account</label>
				' . $listACCOUNT . '
				<div class="field_container">
					<label for="account">Country</label>
					<input type="text" name="country" pattern="[A-Z]{2}" required>
				</div>
				<div class="field_container">
					<label for="account">City</label>
					<input type="text" name="city" pattern="[ a-zA-Z]{4,24}" required>
				</div>
				<p>
					<input type="submit" name="addgeo" id="addgeo" value="Add" formmethod="post" formaction="add_geopermission.php"/>
				</p>
			</form>
		';
	}
	?>
</div>
<?php require_once '../theme/footer.php'?>
