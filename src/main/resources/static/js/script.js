document.addEventListener("DOMContentLoaded", () => {

    const sidebar = document.getElementById("sidebar");
    const overlay = document.getElementById("overlay");

    const openMenu = document.getElementById("openMenu");
    const closeMenu = document.getElementById("closeMenu");

    function abrirMenu() {
        sidebar.classList.add("open");
        overlay.classList.add("active");
        openMenu.setAttribute("aria-expanded", "true");
    }

    function fecharMenu() {
        sidebar.classList.remove("open");
        overlay.classList.remove("active");
        openMenu.setAttribute("aria-expanded", "false");
    }

    openMenu.addEventListener("click", abrirMenu);
    closeMenu.addEventListener("click", fecharMenu);
    overlay.addEventListener("click", fecharMenu);

    document.addEventListener("keydown", (event) => {
        if (event.key === "Escape") {
            fecharMenu();
        }
    });

});