(function () {
  'use strict';

  // ---- Client-side form validation (the server validates again) ----
  var form = document.getElementById('authForm');
  if (form) {
    var errorBox = form.parentNode.querySelector('.alert.error');

    var showError = function (msg) {
      if (!errorBox) { return; }
      errorBox.textContent = msg;
      errorBox.hidden = false;
    };

    form.addEventListener('submit', function (e) {
      var missing = false;
      form.querySelectorAll('input[required]').forEach(function (input) {
        var empty = input.value.trim() === '';
        input.classList.toggle('invalid', empty);
        if (empty) { missing = true; }
      });
      if (missing) {
        e.preventDefault();
        showError('Please fill in all fields.');
        return;
      }
      var pw = form.querySelector('input[name=password]');
      var confirm = form.querySelector('input[name=confirm]');
      if (confirm) {
        if (pw.value.length < 8) {
          e.preventDefault(); pw.classList.add('invalid');
          showError('Password must be at least 8 characters.');
        } else if (pw.value !== confirm.value) {
          e.preventDefault(); confirm.classList.add('invalid');
          showError('Passwords do not match.');
        }
      }
    });
  }

  // ---- Session countdown on the dashboard ----
  var clock = document.getElementById('countdown');
  if (clock) {
    var left = parseInt(clock.getAttribute('data-seconds'), 10) || 0;
    var tick = function () {
      if (left <= 0) { window.location.href = window.location.pathname; return; }
      var m = Math.floor(left / 60), s = left % 60;
      clock.textContent = (m < 10 ? '0' : '') + m + ':' + (s < 10 ? '0' : '') + s;
      left--;
    };
    tick();
    setInterval(tick, 1000);
  }
})();
