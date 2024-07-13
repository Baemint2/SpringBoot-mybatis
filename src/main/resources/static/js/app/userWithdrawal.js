export const withdrawal = {
    init: function () {
        const _this = this;
        _this.withdrawal();
    },
    withdrawal: function () {
        const btnWithdrawal = document.getElementById('btn-withdrawal');
        const modal = document.getElementById('modal-withdrawal-confirm');
        const originalModalContent = modal?.querySelector('.modal-content').innerHTML;

        btnWithdrawal?.addEventListener('click', function () {
            resetModal();
            modal.style.display = 'block';
        });

        function resetModal() {
            modal.querySelector('.modal-content').innerHTML = originalModalContent;
            bindModalEventListeners();
        }

        function bindModalEventListeners() {
            const btnClose = document.querySelectorAll('.close');
            const btnCancel = document.getElementById('cancel-withdrawal');
            const btnConfirm = document.getElementById('confirm-withdrawal')

            btnClose.forEach(function (element) {
                element.addEventListener('click', function () {
                    console.log("탈퇴 창 닫기")
                    modal.style.display = 'none';
                })
            });

            btnCancel.addEventListener('click', function () {
                console.log("탈퇴 취소");
                modal.style.display = 'none';
            });

            btnConfirm.addEventListener('click', function () {
                // 모달의 기존 내용을 숨기고 탈퇴 폼을 보여주는 로직
                const modalContent = modal.querySelector('.modal-content');
                modalContent.innerHTML = '';

                // 비밀번호 확인 폼을 모달 내에서 보여줍니다.
                const withdrawalForm = document.getElementById('password-confirmation-form').cloneNode(true);
                withdrawalForm.style.display = 'block';
                modalContent.appendChild(withdrawalForm);

                const closeButton = withdrawalForm.querySelector('.close');
                closeButton.addEventListener('click', function () {
                    modal.style.display = 'none';
                })
                const submitWithdrawal = document.getElementById('submit-withdrawal');

                submitWithdrawal.addEventListener("click", function () {
                    const username = document.getElementById('username').value;
                    const password = document.getElementById('password').value;
                    console.log(password);

                    fetch(`/api/v1/user/withdrawal`, {
                        method: "DELETE",
                        headers: {
                            "Content-Type": "application/json"
                        },
                        body: JSON.stringify({username: username, password: password})
                    }).then(response => {
                        if (!response.ok) {
                            throw new Error("비밀번호가 불일치합니다.");
                        }
                        alert("회원 탈퇴가 성공적으로 처리되었습니다.");
                        window.location.href = "/";
                    }).catch(error => {
                        document.getElementById("password-error").textContent = error.message;
                        document.getElementById("password-error").style.display = 'block';
                    });

                })
            })

        }
    }
}