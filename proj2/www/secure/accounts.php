<?php
session_start();
require_once '../utils/function.php';
if (!isset($_SESSION['on'])) {
	redirect('../login.php');
	exit();
}
require '../theme/menu.php';
?>
<div class="container">
	<hgroup>
		<h1>Local Accounts</h1>
		<h2>Renew your Linux accounts.</h2>
	</hgroup>
	<table id="accounts">
		<tr>
			<th>Account Name</th>
			<th>Expiration Date</th>
			<th>State</th>
		</tr>
		<?php
		require_once '../utils/db.php';
		$query = "SELECT passwd.username as username, expdate, expflag FROM passwd JOIN useraccounts ON passwd.username = useraccounts.account WHERE useraccounts.username = '" . $_SESSION['username'] . "'";
		$result = $db->query($query);
		for($idx = 0; $row = $result->fetchArray(); $idx++) {
			if ($idx % 2 == 0) {
				echo '<tr class="alt">';
			} else {
				echo '<tr>';
			}
			echo '
				<td>' . $row['username'] . '</td>
				<td>' . date("d/m/y", $row['expdate']) . '</td>
			';
			if ($row['expflag'] || time() > $row['expdate']) {
				echo '<td>Suspended</td>';
			} else {
				echo '<td>Active</td>';
			}
			echo '</tr>';
		}
		?>
	</table>
	<br>
	<?php
	$result = $db->query($query);
	$rowsRENEW = 0;
	$rowsDELETE = 0;
	$listRENEW = '<select name = "renewList">';
	$listDELETE = '<select name = "deleteList">';
	while ($row = $result->fetchArray()) {
		if ($row['expflag'] || time() > $row['expdate']) {
			$rowsRENEW++;
			$listRENEW .= '<option value = "' . $row['username'] . '">' . $row['username'] . '</option>';
		}
		$rowsDELETE++;
		$listDELETE .= '<option value = "' . $row['username'] . '">' . $row['username'] . '</option>';
	}
	$listRENEW .= '</select>';
	$listDELETE .= '</select>';
	if ($rowsRENEW > 0) {
		echo '
			<form>
			' . $listRENEW . '
				<input type="submit" name="renew" id="renew" value="Renew" formmethod="post" formaction="renew_account.php"/>
			</form>
		';
	}
	if ($rowsDELETE > 0) {
		echo '
			<form>
			' . $listDELETE . '
				<input type="submit" name="delete" id="delete" value="Delete" formmethod="post" formaction="delete_account.php"/>
			</form>
		';
	}
	?>
</div>
<?php require '../theme/footer.php'?>
