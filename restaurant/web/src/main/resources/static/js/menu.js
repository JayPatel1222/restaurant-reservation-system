document.addEventListener('DOMContentLoaded', function () {
    const menuNameInput = document.getElementById('menu-name');
    const toggleActive = document.getElementById('toggle-active');
    const menuToggle = document.getElementById('menu-toggle');
    const badge = document.getElementById('toggle-badge');

    // Show toggle if menu is already set (edit mode)
    if (menuNameInput.value.trim() !== '') {
        toggleActive.style.setProperty('display', 'flex', 'important');
    }

    // Sync badge with checkbox state on change
    menuToggle.addEventListener('change', function () {
        badge.textContent = this.checked ? 'Live' : 'Not Live';
        badge.className = this.checked ? 'badge bg-success' : 'badge bg-secondary';
    });

    // Modal add button
    document.getElementById('modal-add-btn').addEventListener('click', function (e) {
        e.preventDefault();

        const selected = document.querySelector('#menuModal input[name="menuId"]:checked');

        if (selected) {
            const menuId = selected.value;
            const menuName = selected.closest('li')
                .querySelector('span')?.textContent || '';

            document.querySelector('input[name="menu.id"]').value = menuId;
            menuNameInput.value = menuName === 'No Menu' ? '' : menuName;

            if (menuNameInput.value !== '' && menuNameInput.value !== 'No Menu selected') {
                toggleActive.style.setProperty('display', 'flex', 'important');

                const oldInput = document.getElementById('menu-toggle');
                const freshInput = oldInput.cloneNode(true);
                oldInput.parentNode.replaceChild(freshInput, oldInput);
                freshInput.checked = false;

                freshInput.addEventListener('change', function () {
                    badge.textContent = this.checked ? 'Live' : 'Not Live';
                    badge.className = this.checked ? 'badge bg-success' : 'badge bg-secondary';
                });

            } else {
                toggleActive.style.setProperty('display', 'none', 'important');
                document.getElementById('menu-toggle').checked = false;
                badge.textContent = 'Not Live';
                badge.className = 'badge bg-secondary';
            }

            const menuSelectBtn = document.getElementById('menu-select-btn');
            menuSelectBtn.innerText = 'Change Menu';
            menuSelectBtn.style.color = 'black';
        }

        document.getElementById('close-modal-btn').click();
    });
});