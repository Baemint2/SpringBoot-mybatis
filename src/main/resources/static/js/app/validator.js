export const validator = {
    init: function () {
        const _this = this;
        document.getElementById('username')?.addEventListener('blur', function () {
            _this.checkDuplicate('username', this.value, 'username-error', '이미 사용중인 사용자명입니다.');
        });
        document.getElementById('email')?.addEventListener('blur', function ()  {
            _this.checkDuplicate('email', this.value, 'email-error', '이미 사용중인 이메일입니다.');
        });
        document.getElementById('nickname')?.addEventListener('blur', function ()  {
            _this.checkDuplicate('nickname', this.value, 'nickname-error', '이미 사용중인 닉네임입니다.');
        });
        document.getElementById('confirmPassword')?.addEventListener('blur', () => {
            _this.passwordMatch();
        });
    },
    checkDuplicate: function (field, value, errorElementId, errorMessage) {
        fetch(`/api/v1/member/${field}/check?${field}=${value}`)
            .then(response => response.json())
            .then(data => {
                const isDuplicate = data[`is${field.charAt(0).toUpperCase() + field.slice(1)}Duplicate`];
                const errorElement = document.getElementById(errorElementId)
                if (isDuplicate) {
                    errorElement.textContent = errorMessage;
                    errorElement.style.display = 'block';
                } else {
                    errorElement.style.display = 'none';
                }
            })
            .catch(error => console.log('Error: ', error));
    },
    passwordMatch: function () {
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirmPassword').value;
        const errorMessage = document.getElementById('confirmPassword-error');
        if(password !== confirmPassword) {
            errorMessage.style.display = 'block';
            errorMessage.textContent = "비밀번호가 일치하지 않습니다."
        } else  {
            errorMessage.style.display = 'none';
        }
    }
}