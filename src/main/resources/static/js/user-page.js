window.addEventListener("scroll", function() {
    var navbar1 = document.querySelector(".navbar1");
    var navbar = document.querySelector(".navbar");
    var scrollPosition = window.scrollY;

    if (scrollPosition > 0) {
        navbar1.style.display = "none"; // Hide navbar1 when scrolled
        navbar.style.top = "0"; // Bring main navbar to top
    } else {
        navbar1.style.display = "flex"; // Show navbar1 when at top
        navbar.style.top = "auto"; // Reset main navbar position
    }
});
