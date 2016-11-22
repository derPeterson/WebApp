// Shake Funktion wenn login failed
function handleLoginRequest(xhr, status, args) {
	if (!args.loggedIn) {
		$("#login-panel").effect("shake", {times:4}, 500);
	}
}				

// Validation of SignUp Form
function handleSignUpRequest(xhr, status, args) {
	if(args.usernameValid != undefined) {
		if (!args.usernameValid) {
			$("[id='signup-form:username']").removeClass("valid").addClass("invalid");
		} else {
			$("[id='signup-form:username']").removeClass("invalid").addClass("valid");
		}
	} else {
		$("[id='signup-form:username']").removeClass("invalid").removeClass("valid");
	}
	
	if(args.emailValid != undefined) {
		if (!args.emailValid) {
			$("[id='signup-form:email']").removeClass("valid").addClass("invalid");
			$("[id='signup-form:emailToConfirm']").removeClass("valid").addClass("invalid");
		} else {
			$("[id='signup-form:email']").removeClass("invalid").addClass("valid");
			$("[id='signup-form:emailToConfirm']").removeClass("invalid").addClass("valid");
		}
	} else {
		$("[id='signup-form:email']").removeClass("valid").removeClass("invalid");
		$("[id='signup-form:emailToConfirm']").removeClass("valid").removeClass("invalid");
	}
	
	if(args.passwordValid != undefined) {
		if (!args.passwordValid) {
			$("[id='signup-form:password']").removeClass("valid").addClass("invalid");
		} else {
			$("[id='signup-form:password']").removeClass("invalid").addClass("valid");
		}
	} else {
		$("[id='signup-form:password']").removeClass("valid").removeClass("invalid");
	}
}

// Validation of ResetPassword Form
function handleResetPasswordRequest(xhr, status, args) {
	if(args.usernameOrEmailValid != undefined) {
		if (!args.usernameOrEmailValid) {
			$("[id='lost-password-form:usernameOrEmail']").removeClass("valid").addClass("invalid");
		} else {
			$("[id='lost-password-form:usernameOrEmail']").removeClass("invalid").addClass("valid");
		}
	} else {
		$("[id='lost-password-form:usernameOrEmail']").removeClass("invalid").removeClass("valid");
	}

	if(args.restPassword != undefined) {
		if (!args.restPassword) {
			$("[id='lost-password-form:resetPasswordMessage']").removeClass("success-message").addClass("error-message");
		} else {
			$("[id='lost-password-form:resetPasswordMessage']").removeClass("error-message").addClass("success-message");
		}
	} else {
		$("[id='lost-password-form:resetPasswordMessage']").removeClass("success-message").addClass("error-message");
	}
}

//Validation of ResetPassword Form
function handleChangePasswordRequest(xhr, status, args) {
	if(args.passwordValid != undefined) {
		if (!args.passwordValid) {
			$("[id='change-password-form:newPassword']").removeClass("valid").addClass("invalid");
			$("[id='change-password-form:newPasswordToConfirm']").removeClass("valid").addClass("invalid");
		} else {
			$("[id='change-password-form:newPassword']").removeClass("invalid").addClass("valid");
			$("[id='change-password-form:newPasswordToConfirm']").removeClass("invalid").addClass("valid");
		}
	} else {
		$("[id='change-password-form:newPassword']").removeClass("valid").removeClass("invalid");
		$("[id='change-password-form:newPasswordToConfirm']").removeClass("valid").removeClass("invalid");
	}
	
	if(args.changePassword != undefined) {
		if (!args.changePassword) {
			$("[id='change-password-form:changePasswordMessage']").removeClass("success-message").addClass("error-message");
		} else {
			$("[id='change-password-form:changePasswordMessage']").removeClass("error-message").addClass("success-message");
		}
	} else {
		$("[id='change-password-form:changePasswordMessage']").removeClass("success-message").addClass("error-message");
	}
}
