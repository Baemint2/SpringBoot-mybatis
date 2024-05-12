const admin = {
    init: function () {
        const _this = this;
        document.getElementById("btn-memberList")?.addEventListener("click", function () {
            _this.memberList();
        });
        document.getElementById("btn-register")?.addEventListener("click", function () {
            _this.memberResiter();
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
    memberList: function () {
        fetch("/admin/memberInfo")
            .then(response => {
                if(!response.ok) {
                    throw new Error("네트워크 오류입니다.")
                } else {
                    return response.json();
                }
            }).then(data => {
            const membersList = document.getElementById('membersList');
            membersList.innerHTML = '';
            data.forEach(member => {
                const row = `
                    <tr>
                        <td>${member.memberId}</td>
                        <td>${member.username}</td>
                        <td>${member.email}</td>
                        <td>${this.formatDate(member.createdAt)}</td>
                        <td>${member.role}</td>
                        <td><button class="btn-remove" data-username="${member.username}">탈퇴</button></td>
                    </tr>
                `;
                membersList.innerHTML += row;
            });
            document.querySelectorAll('.btn-remove').forEach(button => {
                button.addEventListener('click', (e) => {
                    const username = e.target.getAttribute('data-username');
                    if (confirm(`${username} 회원을 정말 탈퇴시키겠습니까?`)) {
                        this.removeMember(username);
                    }
                });
            });
        }).catch(error => {
                console.error('알 수 없는 오류가 발생했습니다:', error);
            });
    },
    memberResiter: function () {
        const data = {
            username: document.getElementById("admin-username").value,
            nickname: document.getElementById("admin-nickname").value,
            email: document.getElementById("admin-email").value,
            mobile: document.getElementById("admin-mobile").value,
            role: document.querySelector("input[name='role']:checked")?.value || "BUYER"
        }

        const formData = new FormData();
        formData.append('member', new Blob([JSON.stringify(data)], {type: "application/json"}));


        const file = document.getElementById("file-input").files[0];
        formData.append('file', file); // 'files' 대신 'file' 사용

        fetch("/api/v1/admin/register-member", {
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
    removeMember: function (username) {
        fetch(`/api/v1/admin/member/${username}`, {
            method: "DELETE"
        }).then(response => {
            if(!response.ok) {
                throw new Error("회원 탈퇴 처리 중 오류가 발생했습니다.");
            }
            alert("회원 탈퇴가 정상적으로 처리되었습니다.");
            this.memberList();
        })
            .catch(error => {
                console.error("오류: ", error);
            })
    }
}

document.addEventListener('DOMContentLoaded', function () {
    admin.init();
})