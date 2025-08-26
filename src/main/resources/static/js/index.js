// ===== Trackly Shared JS =====

// Focus interaction on form controls
document.querySelectorAll('.form-control').forEach(el => {
el.addEventListener('focus', () => el.closest('.mb-3, .mb-2')?.classList.add('is-focused'));
el.addEventListener('blur',  () => el.closest('.mb-3, .mb-2')?.classList.remove('is-focused'));
});

// Simple confirm-password check (register page)
// Keeps server-side validation authoritative.
(function initConfirmPasswordCheck(){
const pwd = document.getElementById('password');
const cpw = document.getElementById('confirmPassword');
const form = document.querySelector('form[data-confirm-password]');
if (!form || !pwd || !cpw) return;
form.addEventListener('submit', (e) => {
if (pwd.value !== cpw.value) {
e.preventDefault();
cpw.setCustomValidity('Passwords do not match');
cpw.reportValidity();
setTimeout(()=> cpw.setCustomValidity(''), 1200);
}
});
})();
