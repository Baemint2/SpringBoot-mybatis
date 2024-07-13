import {validator} from "./validator.js";
import {withdrawal} from "./userWithdrawal.js";
import {emailVerify} from "./emailVerification.js";


// let globalAddressData = {};

const user = {
    init: function () {
        const _this = this;
        document.getElementById("btn-signup")?.addEventListener("click", function () {
            _this.signup();
        });

        document.getElementById("btn-change-nickname")?.addEventListener("click", function () {
            _this.changeNickname();
        });
        document.getElementById("btn-update-address")?.addEventListener("click", function () {
            _this.changeAddress();
        });
        _this.changePassword();
        document.getElementById("btn-update-profile")?.addEventListener("click", function () {
            _this.changeProfileImage();
        })
        _this.setupProfileImageUpload();
        window.addEventListener("pageshow", this.handlePageShow);
    },

    signup: function () {
        const data = {
            username: document.getElementById("username").value,
            password: document.getElementById("password").value,
            confirmPassword: document.getElementById("confirmPassword").value,
            nickname: document.getElementById("nickname").value,
            email: document.getElementById("email").value,
            mobile: document.getElementById("mobile").value,
            role: document.querySelector("input[name='role']:checked")?.value || "BUYER"
        }

        const formData = new FormData();
        formData.append('user', new Blob([JSON.stringify(data)], {type: "application/json"}));


        const file = document.getElementById("file-input").files[0];
            formData.append('file', file); // 'files' 대신 'file' 사용

        fetch("/api/v1/user/signup", {
            method: "POST",
            body: formData
        }).then(response => {
            if (response.ok) {
                alert("회원가입이 완료되었습니다.")
                location.href = "/user/login";
            } else {
                throw new Error("회원가입 처리 중 문제가 발생했습니다.")
            }
        }).catch(error => {
            alert("알 수 없는 오류가 발생했습니다.");
            console.log(error);
        })
    },
    setupProfileImageUpload: function () {
      const profileImage = document.querySelector(".profile-img");
      const updateText = document.getElementById("update-img");
      const fileInput = document.getElementById("file-input");

        profileImage?.addEventListener('click', function() {
            fileInput.click();
        });
        updateText?.addEventListener('click', function() {
            fileInput.click();
        });

      fileInput?.addEventListener("change", function () {
          if(this.files && this.files[0]) {
              const reader = new FileReader();
              reader.onload = (e) =>
                  profileImage.src = e.target.result;
              reader.readAsDataURL(this.files[0]);
          }

      })

    },
    changeNickname: function () {
        const nicknameSpan = document.querySelector("#nickname-span");

        console.log(nicknameSpan);
        const currentNickname = nicknameSpan.textContent;
        const input = document.createElement("input");
        input.type = "text";
        input.value = currentNickname;
        input.id = 'nickname-input';

        nicknameSpan.replaceWith(input);

        input.focus();
        console.log(input)

        input.addEventListener('blur', function () {
            const newNickname = input.value;
            const originalNickname = nicknameSpan.textContent;
            const errorMessage = document.querySelector(".error-message"); // 에러 메시지를 표시할 요소의 ID가 "error-message"라고 가정

            if(newNickname === originalNickname) {
                input.replaceWith(nicknameSpan);
                errorMessage.style.display = 'none';
                return;
            }

            fetch('/api/v1/user/updateNickname', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `nickname=${encodeURIComponent(newNickname)}`
            })
                .then(response => {
                    if (!response.ok) {
                        return response.json().then(data => {
                            throw new Error(data.isNicknameDuplicate); // 서버에서 보낸 오류 메시지 추출
                        });
                    }
                    return response.text();
                })
                .then(() => {
                    const span = document.createElement('span');
                    span.textContent = newNickname;
                    console.log(span);
                    span.id = 'nickname-span';
                    input.replaceWith(span);
                    errorMessage.style.display = 'none';
                })
                .catch(error => {
                    console.error('Nickname update failed:', error);
                    // 여기에서 사용자 인터페이스에 에러 메시지를 표시하면 좋습니다.
                    errorMessage.textContent = error.message;
                    errorMessage.style.display = 'block';
                });
        });
    },
        changeAddress: function () {
        const data = {
            saZipcode: document.getElementById("saZipcode").value,
            saStreet: document.getElementById("saStreet").value,
            saDetail: document.getElementById("saDetail").value,
        }

        fetch("/api/v1/user/updateAddress", {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data)
        })
            .then(response => {
                if(response.ok) {
                    return response.json();
                }
                throw new Error("주소 업데이트에 실패했습니다.");
            })
            .then(data => {
                alert("주소가 성공적으로 업데이트 되었습니다.");
                console.log(data);
            })
            .catch(error => {
                console.error('Error:', error);
            })
    },
    changePassword: function () {
        const passwordForm = document.getElementById("passwordForm");
        if(passwordForm) {
            passwordForm.addEventListener("submit", function (e) {
                e.preventDefault();

                const data = {
                    username: document.querySelector(".username").value,
                    currentPassword: document.getElementById("modal-current-pass").value,
                    newPassword: document.getElementById("newPassword").value,
                    confirmPassword: document.getElementById("modal-confirm-pass").value
                }
                // 서버로 데이터 전송
                fetch("/api/v1/user/updatePassword", {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(data)
                })
                    .then(response => {
                        if(!response.ok) {
                            return response.json().then(data => Promise.reject(data));
                        }
                        alert("비밀번호가 성공적으로 변경 되었습니다.");
                        location.href = "/user/login";
                        return response.json();
                    })
                    .catch(error => {
                        console.log("Error:", error);
                        Object.entries(error).forEach(([key, value]) => {
                            const errorContainer = document.getElementById(`${key}-error`);
                            console.log(key)
                            console.log(value)
                            if(errorContainer) {
                                errorContainer.textContent = value;
                                errorContainer.style.display = "block";
                            }
                        })
                    })
            })
        }
    },
    changeProfileImage: function () {
        const username = document.getElementById("info-username").textContent;

        const formData = new FormData();
        formData.append('username', username);

        const file = document.getElementById("file-input").files[0];
        if (file) { // 파일이 선택되었는지 확인
            formData.append('file', file);
        }

        fetch("/api/v1/user/updateProfile", {
            method: "PUT",
            body: formData
        }).then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error('Failed to update profile image');
        }).then(data => {
            alert(data.message);
        }).catch(error => {
            console.error('Error:', error);
        });
    },
    handlePageShow: function (e) {
        if(e.persisted) {
            document.querySelectorAll('.form-control').forEach(function(input) {
                input.value = '';
            });
            document.getElementById("verificationCodeDiv").style.display = 'none';
            document.querySelectorAll('.error-message').forEach(function(element) {
                element.style.display = 'none';
            });
        }
    }
}

document.addEventListener("DOMContentLoaded", function () {
    user.init();
    validator.init();
    withdrawal.init();
    emailVerify.init();
})