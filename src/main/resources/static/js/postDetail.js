function toggleLike(button) {
        const isLiked = button.classList.contains('active');
        const likeCountSpan = button.querySelector('.stats-counter');
        const currentCount = parseInt(likeCountSpan.textContent);

        // Add loading animation
        const originalHTML = button.innerHTML;
        button.innerHTML = '<span class="loading-animation"></span>Processing...';
        button.disabled = true;

        // Simulate API call
        setTimeout(() => {
            if (isLiked) {
                button.classList.remove('active');
                likeCountSpan.textContent = currentCount - 1;
            } else {
                button.classList.add('active');
                likeCountSpan.textContent = currentCount + 1;
                // Add heartbeat animation
                button.classList.add('heartbeat-animation');
                setTimeout(() => button.classList.remove('heartbeat-animation'), 600);
            }

            // Restore button content
            button.innerHTML = `
                <i class="fas fa-heart me-2"></i>
                <span class="stats-counter">${likeCountSpan.textContent}</span>
                <span class="ms-1">Likes</span>
            `;
            button.disabled = false;
        }, 800);
    }

    // Add entrance animation
    document.addEventListener('DOMContentLoaded', function() {
        const postCard = document.querySelector('.post-card');
        if (postCard) {
            postCard.style.opacity = '0';
            postCard.style.transform = 'translateY(30px)';

            setTimeout(() => {
                postCard.style.transition = 'all 0.6s ease';
                postCard.style.opacity = '1';
                postCard.style.transform = 'translateY(0)';
            }, 100);
        }
    });