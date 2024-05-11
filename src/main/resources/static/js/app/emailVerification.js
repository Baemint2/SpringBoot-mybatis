import { stateManager } from "./stateManager.js";

export const emailVerify = {
    init: function () {
        const _this = this;
        document.querySelectorAll(".emailValidateBtn").forEach(btn => {
            btn.addEventListener("click", () => {
                const email = document.querySelector(".email").value;
                const action = btn.getAttribute('data-action');
                _this.handleVerificationCode(email, action);
            })
        });
        document.getElementById('checkVerificationCode')?.addEventListener('click', () => {
            _this.checkVerifyCode();
        });
    },
    handleVerificationCode: function (email, action) {
        let apiUrl = "/api/v1/email/send-verification-code";
        let requestBody = {email: email, action: action};
        console.log(email);

        if(action === "verify-user") {
            apiUrl = "/api/v1/member/verify-user";
            requestBody = {
                email: email,
                nickname: document.querySelector(".nickname").value,
                username: document.querySelector(".username")?.value,
           }
        }
        fetch(apiUrl, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(requestBody)
        }).then(response => {
            if(!response.ok) {
                throw response
            }
            return response.json();
        })
            .then(data => {
                console.log(data)
                if (data.message === "인증 코드가 발송되었습니다.") {
                    document.getElementById('verificationCodeDiv').style.display = 'block';
                    alert(data.message); // 사용자에게 인증 코드 발송 메시지를 표시
                } else {
                    alert(data.error); // 일치하는 회원 정보가 없다는 메시지를 경고 창으로 표시
                }
        }).catch((error) => {
            error.json().then(errorData => {
                Object.keys(errorData).forEach(function(field) {
                    const errorElement = document.getElementById(`${field}-error`);
                    if (errorElement) {
                        errorElement.textContent = errorData[field];
                        errorElement.style.display = 'block';
                    } else {
                        alert(errorData.error);
                    }
                })
            });
        });
    },

    checkVerifyCode: function () {
        const data = {
            email: document.querySelector(".email").value,
            verificationCode: document.querySelector(".verificationCode").value,
        }
        fetch("/api/v1/email/verify-code", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(data)
        })
        .then(response => response.json())
            .then(data => {
                if(data.message === "인증 성공") {
                    stateManager.setIsVerified(true);
                    console.log(data.message);
                    document.getElementById("checkVerificationCode").disabled = true;
                } else {
                    alert(data.error);
                    stateManager.setIsVerified(false);
                    console.log(data.message);
                }
            })
            .catch(error => {
                console.error("Error:", error);
                alert("인증번호 검증에 실패했습니다.");
            })
    }


}