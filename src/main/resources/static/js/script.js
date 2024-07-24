const container = document.getElementById("container");
const registerBtn = document.getElementById("register");
const loginBtn = document.getElementById("login");

registerBtn.addEventListener("click", () => {
  container.classList.add("active");
});

loginBtn.addEventListener("click", () => {
  container.classList.remove("active");
});

function validatePassword() {
  const password = document.getElementById('password').value;
  const strengthElement = document.getElementById('password-strength');
  const strength = checkPasswordStrength(password);

  if (strength === 'Strong') {
    strengthElement.textContent = 'Strong password';
    strengthElement.classList.add('valid');
    return true;
  } else {
    strengthElement.textContent = 'Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one number, and one special character.';
    strengthElement.classList.remove('valid');
    return false;
  }
}

function checkPasswordStrength(password) {
  const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
  if (regex.test(password)) {
    return 'Strong';
  } else {
    return 'Weak';
  }
}
