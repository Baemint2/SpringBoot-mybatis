const admin = {
    init: function () {
        const _this = this;
        document.getElementById("btn-memberList")?.addEventListener("click", function () {
            _this.userList();
        });
        document.getElementById("btn-register")?.addEventListener("click", function () {
            _this.UserRegister();
        })
    },
    formatDate: function(dateString) {
        const date = new Date(dateString);
        return date.toLocaleString('ko-KR', {
            year: "numeric",
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
        });
    },
    userList: function () {
        fetch("/admin/userInfo")
            .then(response => {
                if(!response.ok) {
                    throw new Error("네트워크 오류입니다.")
                } else {
                    return response.json();
                }
            }).then(data => {
            const membersList = document.getElementById('membersList');
            membersList.innerHTML = '';
            data.forEach(user => {
                const row = `
                    <tr>
                        <td>${user.userId}</td>
                        <td>${user.userName}</td>
                        <td>${user.userEmail}</td>
                        <td>${this.formatDate(user.userCreatedAt)}</td>
                        <td>${user.userRole}</td>
                        <td><button class="btn-remove" data-username="${user.userName}">탈퇴</button></td>
                    </tr>
                `;
                membersList.innerHTML += row;
            });
            document.querySelectorAll('.btn-remove').forEach(button => {
                button.addEventListener('click', (e) => {
                    const username = e.target.getAttribute('data-username');
                    if (confirm(`${username} 회원을 정말 탈퇴시키겠습니까?`)) {
                        this.removeUser(username);
                    }
                });
            });
        }).catch(error => {
                console.error('알 수 없는 오류가 발생했습니다:', error);
            });
    },
    UserRegister: function () {
        const data = {
            userName: document.getElementById("admin-username").value,
            userNickname: document.getElementById("admin-nickname").value,
            userEmail: document.getElementById("admin-email").value,
            userMobile: document.getElementById("admin-mobile").value,
            userRole: document.querySelector("input[name='role']:checked")?.value || "BUYER"
        }

        const formData = new FormData();
        formData.append('user', new Blob([JSON.stringify(data)], {type: "application/json"}));


        const file = document.getElementById("file-input").files[0];
        formData.append('file', file); // 'files' 대신 'file' 사용

        fetch("/api/v1/admin/register-user", {
            method: "POST",
            body: formData
        }).then(response => {
            if (response.ok) {
                alert("회원등록이 완료되었습니다.")
                location.href = "/admin";
            } else {
                throw new Error("회원등록 처리 중 문제가 발생했습니다.")
            }
        }).catch(error => {
            alert("알 수 없는 오류가 발생했습니다.");
            console.log(error);
        })
    },
    removeUser: function (userName) {
        fetch(`/api/v1/admin/user/${userName}`, {
            method: "DELETE"
        }).then(response => {
            if(!response.ok) {
                throw new Error("회원 탈퇴 처리 중 오류가 발생했습니다.");
            }
            alert("회원 탈퇴가 정상적으로 처리되었습니다.");
            this.userList();
        })
            .catch(error => {
                console.error("오류: ", error);
            })
    }
}

document.addEventListener('DOMContentLoaded', function () {
    admin.init();
})