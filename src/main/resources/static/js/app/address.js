const address = {
    init: function () {
        this.bindModalEventListeners();
        this.bindSaveAddressListener();
    },

        bindModalEventListeners: function () {
            const btnAddressManager = document.getElementById("btnAddressManager");
            const addressModal = document.getElementById("addressModal");
            const btnClose = document.querySelectorAll('.close');
            const btnCancel = document.querySelectorAll('.cancel-address-modal');
            const btnOpenModal = document.querySelector('.open');

            btnAddressManager.addEventListener("click", function () {
                addressModal.style.display = "block";
                address.loadAddresses();
            });

            btnClose.forEach(function (element) {
                element.addEventListener('click', function () {
                    element.closest('.modal').style.display = 'none';
                });
            });

            btnCancel.forEach(function (element) {
                element.addEventListener('click', function () {
                    element.closest('.modal').style.display = 'none';
                });
            });

            btnOpenModal.addEventListener("click", function () {
                document.getElementById("addAddressModal").style.display = 'block';
                addressModal.style.display = 'none';
            });

},
    bindSaveAddressListener: function () {
        const saveButtons = document.querySelectorAll('.save-address-modal');
        saveButtons.forEach(button => {
            button.addEventListener('click', () => {
                address.submitAddress();
            });
        });
    },

    submitAddress: function () {
        const addressData = {
            recipientName: document.getElementById("addressNickname").value,
            zipcode: document.getElementById("zipcode").value,
            streetaddress: document.getElementById("streetAddress").value,
            detailaddress: document.getElementById("detailAddress").value,
            mobile: document.getElementById("addressMobile").value,
            defaultAddress: document.getElementById("defaultAddress").checked ? "Y" : "N"
        }
        fetch("/api/v1/address/insert", {
            method: 'POST',
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(addressData)
        })
                .then(response => {
                    if (response.ok) {
                        return response.json();
                    } else {
                        throw new Error('Something went wrong on API server!');
                    }
                })
                .then(resData => {
                    console.log('Success:', resData);
                    alert("배송지가 성공적으로 저장되었습니다.");
                    document.getElementById("addAddressModal").style.display = 'none';
                    this.loadAddresses();
                })
                .catch((error) => {
                    console.error('Error:', error);
                    alert("배송지 저장에 실패했습니다.");
                })
    },

    loadAddresses: function () {
        const memberId = document.querySelector(".memberId").value
        console.log(memberId);
        fetch(`/api/v1/address/${memberId}/addresses`, {method: "GET"})
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error('Something went wrong on API server!');
                }
            })
            .then(addresses => this.displayAddresses(addresses))
            .catch(error => {
                alert("배송지 정보를 불러오는데 실패했습니다.")
            });
    },

    displayAddresses: function (addresses) {
        const addressList = document.getElementById("addressList");
        addressList.textContent = "";

        addresses.forEach(address => {
            console.log(address);
            const addressDiv = document.createElement("div");

            const recipientP = document.createElement("p");
            recipientP.textContent = `받는 분 : ${address.recipientName}`;
            addressDiv.appendChild(recipientP);

            const mobileP = document.createElement("p");
            mobileP.textContent = `전화번호 : ${address.mobile}` ;
            addressDiv.appendChild(mobileP);

            const zipcodeP = document.createElement("p");
            zipcodeP.textContent = `우편번호: ${address.zipcode}`;
            addressDiv.appendChild(zipcodeP);

            const addressP = document.createElement("p");
            addressP.textContent = `주소: ${address.streetaddress}, ${address.detailaddress}`;
            addressDiv.appendChild(addressP);

            const defaultAddressP = document.createElement("p");
            defaultAddressP.textContent = `기본 주소: ${address.defaultAddress ? '예' : '아니오'}`;
            addressDiv.appendChild(defaultAddressP);

            addressList.appendChild(addressDiv);
        })
    }
}

window.execPostCode = function () {
    new daum.Postcode({
        oncomplete: function (data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            const roadAddr = data.roadAddress; // 도로명 주소 변수
            let extraRoadAddr = ''; // 참고 항목 변수

            // 법정동명이 있을 경우 추가한다. (법정리는 제외)
            // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
            if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                extraRoadAddr += data.bname;
            }
            // 건물명이 있고, 공동주택일 경우 추가한다.
            if (data.buildingName !== '' && data.apartment === 'Y') {
                extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
            }
            // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
            if (extraRoadAddr !== '') {
                extraRoadAddr = ' (' + extraRoadAddr + ')';
            }

            globalAddressData = {
                zipcode: data.zonecode, // 우편번호
                streetAddress: roadAddr, // 도로명 주소
            };
            console.log(data)

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            console.log(document.getElementById('zipcode').value = data.zonecode);
            console.log(document.getElementById("streetAddress").value = roadAddr);

        }
    }).open();
}

document.addEventListener("DOMContentLoaded", function () {
    address.init();
});




