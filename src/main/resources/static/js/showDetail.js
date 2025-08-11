document.addEventListener('DOMContentLoaded', function() {
        function setupShowMore(itemClass, buttonId, initialCount) {
            const items = document.querySelectorAll(itemClass);
            const showMoreBtn = document.getElementById(buttonId);

            if (!showMoreBtn) return;

            for (let i = initialCount; i < items.length; i++) {
                items[i].style.display = 'none';
            }

            if (items.length <= initialCount) {
                showMoreBtn.style.display = 'none';
            }

            showMoreBtn.addEventListener('click', function() {
                for (let i = initialCount; i < items.length; i++) {
                    items[i].style.display = 'block';
                    items[i].style.animation = 'fadeIn 0.5s ease-in-out';
                }
                showMoreBtn.style.display = 'none';
            });
        }

        setupShowMore('.post-item', 'show-all-posts-btn', 5);
        setupShowMore('.member-item', 'show-all-members-btn', 6);
    });

    // Add fade-in animation
    const style = document.createElement('style');
    style.textContent = `
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(20px); }
            to { opacity: 1; transform: translateY(0); }
        }
    `;
    document.head.appendChild(style);