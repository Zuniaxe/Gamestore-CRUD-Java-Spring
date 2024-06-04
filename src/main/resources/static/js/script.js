function toggleSidebar() {
    var sidebar = document.getElementById('sidebar');
    var sidebarToggle = document.querySelector('.sidebar-toggle');

    sidebar.classList.toggle('active');
    sidebar.classList.toggle('initial-hidden'); // Tambahkan kelas initial-hidden

    sidebarToggle.classList.toggle('change');
}
