const admin = {
    init: function () {
        const _this = this;
        document.getElementById("btn-memberList")?.addEventListener("click", function () {
            _this.memberList();
        });
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
                        <td>${member.member_id}</td>
                        <td>${member.username}</td>
                        <td>${member.email}</td>
                        <td>${this.formatDate(member.created_at)}</td>
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