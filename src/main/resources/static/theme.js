const themes = {
    theme1: 'image1.jpg',
    theme2: 'image2.jpg'
};

function applyTheme(theme) {
    document.body.style.backgroundImage = `url('${themes[theme]}')`;
    localStorage.setItem('selectedTheme', theme);

    // Update dropdown on all pages
    const dropdowns = document.querySelectorAll('.theme-dropdown');
    dropdowns.forEach(dropdown => dropdown.value = theme);
}

// Initialize theme on page load
window.addEventListener('DOMContentLoaded', () => {
    const savedTheme = localStorage.getItem('selectedTheme') || 'theme1';
    applyTheme(savedTheme);

    // Set up dropdown
    const dropdown = document.querySelector('.theme-dropdown');
    if(dropdown) {
        dropdown.value = savedTheme;
        dropdown.addEventListener('change', (e) => {
            applyTheme(e.target.value);
        });
    }
});