function toggleModal(modalId, show) {
    const modal = document.getElementById(modalId);

    if(show) {
        clearAddressForm();
    }
    modal.style.display = show ? 'block' : 'none';
}

function returnToAddressManager() {
    toggleModal("addAddressModal", false);
    toggleModal("modifyAddressModal", false);
    toggleModal("addressModal", true);
}

function clearAddressForm() {
   document.getElementById("addressNickname").value = "";
   document.getElementById("saZipcode").value = "";
   document.getElementById("saStreet").value = "";
   document.getElementById("saDetail").value = "";
   document.getElementById("saMobile").value = "";
   document.getElementById("saDefault").checked = false;
}

const address = {
    currentAddressId: null,

    init: function () {
        this.bindModalEventListeners();
        this.bindSaveAddressListener();

        document.getElementById("cancelAddModal").addEventListener("click", returnToAddressManager);
        document.getElementById("cancelModifyModal").addEventListener("click", returnToAddressManager);

    },

        bindModalEventListeners: function () {
            const btnAddressManager = document.getElementById("btnAddressManager");
            const btnClose = document.querySelectorAll('.close');
            const btnCancel = document.querySelectorAll('.cancel-address-modal');
            const btnOpenModal = document.querySelector('.open');

            btnAddressManager?.addEventListener("click", function () {
                toggleModal("addressModal", true);
                address.loadAddresses();
            });

            btnClose.forEach(function (element) {
                element.addEventListener('click', function () {
                    toggleModal(element.closest('.modal').id, false);
                });
            });

            btnCancel.forEach(function (element) {
                element.addEventListener('click', function () {
                    toggleModal(element.closest('.modal').id, false);
                });
            });

            btnOpenModal?.addEventListener("click", function () {
                toggleModal("addressModal", false);
                toggleModal("addAddressModal", true);
            });

},
    bindSaveAddressListener: function () {
        document.getElementById("addAddressButton").addEventListener('click', this.submitAddress.bind(this));
        document.getElementById("updateAddressButton").addEventListener("click", () => this.addressUpdate(this.currentAddressId));
        document.getElementById("removeAddressButton").addEventListener("click", () => this.addressDelete(this.currentAddressId));
    },

    submitAddress: function () {
        const addressData = {
            saRecipientName: document.getElementById("addressNickname").value,
            saZipcode: document.getElementById("saZipcode").value,
            saStreet: document.getElementById("saStreet").value,
            saDetail: document.getElementById("saDetail").value,
            saMobile: document.getElementById("saMobile").value,
            saDefault: document.getElementById("saDefault").checked ? "Y" : "N"
        }
        console.log(addressData);
        fetch("/api/v1/address", {
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
                    toggleModal("addAddressModal", false)
                    this.loadAddresses();
                })
                .catch((error) => {
                    console.error('Error:', error);
                    alert("배송지 저장에 실패했습니다.");
                })
    },

      loadAddress: function (addressId) {
        this.currentAddressId = addressId;
        console.log("마리의 addressId : ", this.currentAddressId);
          fetch(`/api/v1/address/${addressId}`)
              .then(response => response.json())
              .then(address => {
                  document.getElementById("modifyAddressNickname").value = address.saRecipientName;
                  document.getElementById("modifyZipcode").value = address.saZipcode;
                  document.getElementById("modifyStreetAddress").value = address.saStreet;
                  document.getElementById("modifyDetailAddress").value = address.saDetail;
                  document.getElementById("modifyAddressMobile").value = address.saMobile;
                  document.getElementById("modifyDefaultAddress").checked = address.saDefault === "Y";
                  toggleModal("modifyAddressModal", true);
              })
              .catch(error => console.error('Error:', error));
      },

      loadAddresses: function () {
        const userId = document.querySelector(".userId").value
        console.log(userId);
        fetch(`/api/v1/address/${userId}/addresses`, {method: "GET"})
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error('Something went wrong on API server!');
                }
            })
            .then(addresses => {
                this.displayAddresses(addresses)
                toggleModal("addressModal", true)})
            .catch(error => {
                alert("배송지 정보를 불러오는데 실패했습니다.")
            });
    },
    addressDelete: function (addressId) {
        fetch(`/api/v1/address/${addressId}`, {
            method: 'DELETE',
            headers: {'Content-Type': 'application/json'},
        }).then(response => {
            if (!response.ok) {
                throw new Error("네트워크 에러가 발생했습니다.")
            }
            alert("배송지가 삭제되었습니다.")
            toggleModal("modifyAddressModal", false);
            this.loadAddresses();
        }).catch(error =>
            alert(`오류가 발생했습니다. ${error}`));
    },
    addressUpdate: function (addressId) {
        const updateAddressData = {
            saRecipientName: document.getElementById("modifyAddressNickname").value,
            saZipcode: document.getElementById("modifyZipcode").value,
            saStreet: document.getElementById("modifyStreetAddress").value,
            saDetail: document.getElementById("modifyDetailAddress").value,
            saMobile: document.getElementById("modifyAddressMobile").value,
            saDefault: document.getElementById("modifyDefaultAddress").checked ? "Y" : "N"
        }
        fetch(`/api/v1/address/${addressId}`, {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(updateAddressData)
        }).then(response => {
            console.log(response);
            if (!response.ok) {

                throw new Error("네트워크 에러가 발생했습니다.")

            }
            alert("배송지가 수정되었습니다.")
            toggleModal("modifyAddressModal", false);
            this.loadAddresses();
        }).catch(error =>
            alert(`오류가 발생했습니다. ${error}`));
    },

    displayAddresses: function (addresses) {
        const addressList = document.getElementById("addressList");
        addressList.textContent = "";

        addresses.forEach(address => {
            const addressDiv = document.createElement("div");
            addressDiv.className = "addressContainer";

            const recipientP = document.createElement("p");
            recipientP.textContent = `받는 분 : ${address.saRecipientName}`;
            addressDiv.appendChild(recipientP);

            const mobileP = document.createElement("p");
            mobileP.textContent = `전화번호 : ${address.saMobile}` ;
            addressDiv.appendChild(mobileP);

            const zipcodeP = document.createElement("p");
            zipcodeP.textContent = `우편번호: ${address.saZipcode}`;
            addressDiv.appendChild(zipcodeP);

            const addressP = document.createElement("p");
            addressP.textContent = `주소: ${address.saStreet}, ${address.saDetail}`;
            addressDiv.appendChild(addressP);

            const defaultAddressP = document.createElement("p");
            defaultAddressP.textContent = `기본 주소: ${address.saDefault === 'Y' ? '예' : '아니오'}`;
            addressDiv.appendChild(defaultAddressP);

            const modifyBtn = document.createElement("button");
            modifyBtn.textContent = "배송지 수정";
            modifyBtn.type = "button";
            modifyBtn.addEventListener('click', () => {
                toggleModal("addressModal", false);
                this.loadAddress(address.addressId);
            });
            addressDiv.appendChild(modifyBtn);
            addressList.appendChild(addressDiv);
        })
    }
}

window.execPostCode = function (isModify) {
    new daum.Postcode({
        oncomplete: function (data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            const roadAddr = data.roadAddress; // 도로명 주소 변수
            if(isModify) {
                document.getElementById('modifyZipcode').value = data.zonecode;
                document.getElementById("modifyStreetAddress").value = roadAddr;
            } else {
                document.getElementById('modifyZipcode').value = data.zonecode;
                document.getElementById("modifyStreetAddress").value = roadAddr;
            }
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

            console.log(data)

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            console.log(document.getElementById('saZipcode').value = data.zonecode);
            console.log(document.getElementById("saStreet").value = roadAddr);

        }
    }).open();
}

document.addEventListener("DOMContentLoaded", function () {
    address.init();

});