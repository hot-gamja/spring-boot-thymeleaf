/**
 * shell.js — 3-state theme toggle (system/light/dark) + mobile drawer
 */
(function () {
  'use strict';

  // ---- Theme Toggle (3-state: system → light → dark → system) ----
  var CYCLE = ['system', 'light', 'dark'];
  var toggle = document.getElementById('theme-toggle');
  var iconSystem = document.getElementById('icon-system');
  var iconLight = document.getElementById('icon-light');
  var iconDark = document.getElementById('icon-dark');
  var mql = window.matchMedia('(prefers-color-scheme: dark)');

  function getTheme() {
    var t = localStorage.getItem('theme');
    return (t === 'light' || t === 'dark') ? t : 'system';
  }

  function applyTheme(theme) {
    var systemDark = mql.matches;
    var isDark = (theme === 'dark') || (theme === 'system' && systemDark);
    document.documentElement.classList.toggle('dark', isDark);
  }

  function updateIcon(theme) {
    if (!iconSystem) return;
    iconSystem.classList.toggle('hidden', theme !== 'system');
    iconLight.classList.toggle('hidden', theme !== 'light');
    iconDark.classList.toggle('hidden', theme !== 'dark');
    if (toggle) toggle.setAttribute('aria-label', 'Theme: ' + theme);
  }

  function setTheme(theme) {
    localStorage.setItem('theme', theme);
    applyTheme(theme);
    updateIcon(theme);
  }

  // Init: show correct icon on load
  var current = getTheme();
  applyTheme(current);
  updateIcon(current);

  // Click: cycle system → light → dark → system
  if (toggle) {
    toggle.addEventListener('click', function () {
      var cur = getTheme();
      var next = CYCLE[(CYCLE.indexOf(cur) + 1) % CYCLE.length];
      setTheme(next);
    });
  }

  // OS preference change: only react when theme is 'system'
  mql.addEventListener('change', function () {
    if (getTheme() === 'system') {
      applyTheme('system');
    }
  });

  // ---- Mobile Drawer ----
  var openBtn = document.getElementById('drawer-open');
  var closeBtn = document.getElementById('drawer-close');
  var overlay = document.getElementById('drawer-overlay');
  var drawer = document.getElementById('mobile-drawer');

  if (!openBtn || !drawer) return;

  function openDrawer() {
    drawer.classList.remove('translate-x-full');
    drawer.classList.add('translate-x-0');
    drawer.setAttribute('aria-hidden', 'false');
    overlay.classList.remove('opacity-0', 'pointer-events-none');
    overlay.classList.add('opacity-100');
    overlay.setAttribute('aria-hidden', 'false');
    openBtn.setAttribute('aria-expanded', 'true');
    document.body.style.overflow = 'hidden';
    // Focus first link
    var first = drawer.querySelector('a');
    if (first) first.focus();
  }

  function closeDrawer() {
    drawer.classList.add('translate-x-full');
    drawer.classList.remove('translate-x-0');
    drawer.setAttribute('aria-hidden', 'true');
    overlay.classList.add('opacity-0', 'pointer-events-none');
    overlay.classList.remove('opacity-100');
    overlay.setAttribute('aria-hidden', 'true');
    openBtn.setAttribute('aria-expanded', 'false');
    document.body.style.overflow = '';
    openBtn.focus();
  }

  openBtn.addEventListener('click', openDrawer);
  if (closeBtn) closeBtn.addEventListener('click', closeDrawer);
  if (overlay) overlay.addEventListener('click', closeDrawer);

  // ESC key closes drawer
  document.addEventListener('keydown', function (e) {
    if (e.key === 'Escape' && drawer.getAttribute('aria-hidden') === 'false') {
      closeDrawer();
    }
  });
})();
