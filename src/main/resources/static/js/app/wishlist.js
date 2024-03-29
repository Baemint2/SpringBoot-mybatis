document.addEventListener("DOMContentLoaded", function () {
    const wishlistToggleBtn = document.getElementById('wishlist-toggle');

    wishlistToggleBtn.addEventListener('click', function() {
        const productId = this.getAttribute('data-product-id');
        // 찜하기 상태 토글 AJAX 요청
        fetch(`/wishlist/toggle?productId=${productId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                // 필요에 따라 추가 헤더 설정
            },
            // 요청과 함께 CSRF 토큰을 보내야 할 경우 여기에 추가
        })
            .then(response => response.json())
            .then(data => {
                console.log(data)
                // 응답 데이터에서 찜하기 상태 가져오기
                const isLiked = data.isLiked;

                // 버튼 상태 업데이트
                updateWishlistButton(wishlistToggleBtn, isLiked);
            })
            .catch(error => console.error('Error:', error));
    });
    function updateWishlistButton(button, isLiked) {
        button.setAttribute('data-liked', isLiked);
        if(isLiked) {
            button.innerHTML = 'Remove from Wishlist';
        } else {
            button.innerHTML = 'Add to Wishlist';
        }
    }
})