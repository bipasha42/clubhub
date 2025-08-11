function followClub(button) {
        const originalHTML = button.innerHTML;
        button.innerHTML = '<span class="loading-animation"></span> Following...';
        button.disabled = true;

        // Simulate API call
        setTimeout(() => {
            button.innerHTML = '<i class="fas fa-check me-2"></i>Following';
            button.className = 'btn btn-modern btn-following flex-fill';
            button.onclick = function() { unfollowClub(this); };
            button.disabled = false;
        }, 1000);
    }

    function unfollowClub(button) {
        const originalHTML = button.innerHTML;
        button.innerHTML = '<span class="loading-animation"></span> Unfollowing...';
        button.disabled = true;

        // Simulate API call
        setTimeout(() => {
            button.innerHTML = '<i class="fas fa-plus me-2"></i>Follow';
            button.className = 'btn btn-modern btn-follow flex-fill';
            button.onclick = function() { followClub(this); };
            button.disabled = false;
        }, 1000);
    }

    // Add intersection observer for scroll animations
    document.addEventListener('DOMContentLoaded', function() {
        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.style.animationPlayState = 'running';
                }
            });
        });

        document.querySelectorAll('.stagger-animation').forEach(el => {
            el.style.animationPlayState = 'paused';
            observer.observe(el);
        });
    });