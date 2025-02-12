/**
 * 
 */

//< !--頭像JS部分 -->
function subMemberid(memberid) {
    console.log('進入傳輸表單的function')
    var form = $('<form>', {
        'method': 'POST',
        'action': '/memberShow'
    });

    var input = $('<input>', {
        'type': 'hidden',
        'name': 'memberid',
        'value': memberid
    });

    form.append(input);
    $('body').append(form);
    form.submit();  // 提交表單
}
//顯示選擇的圖片預覽
function previewImage() {
    const file = document.getElementById('upload').files[0];
    const reader = new FileReader();

    reader.onloadend = function () {
        const imgSrc = reader.result || '../img/caseImg.jpg';  // 默认图片路径
        document.getElementById('icon').src = imgSrc;
    }

    if (file) {
        reader.readAsDataURL(file);
    } else {
        // 如果没有选择文件，设置为默认图片
        document.getElementById('icon').src = '../img/caseImg.jpg'; // 默认图片路径
    }
}

// 提交圖片新表单
function submitImage(picurl) {
    var form = document.getElementById('updateIcon'); // 获取图片表单
    var formData = new FormData(form); // 将图片表单数据转为 FormData 对象

    const base64Image = document.getElementById("icon").src;  // 获取 Base64 字符串

    formData.append('picurl', base64Image);  // 添加 base64 图片数据到 FormData 中

    // 假设 URL 为 /memberHome/12345
    const path = window.location.pathname;
    const memberid = path.split('/').pop();  // 这会获取到路径中的最后一个部分 103

    // 动态构建 URL，替换 {memberid} 为实际的 memberid
    var url = `/api/members/memberHome/${memberid}/update`;  // 加上 /api/members 路径前缀

    // 通过 Fetch 请求将 Base64 字符串传递到后端
    fetch(url, {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (response.ok) {
                alert(picurl + ' 更新成功！');
            } else {
                alert('更新失敗，請稍後再試！');
            }
        })
        .catch(error => {
            console.error("錯誤:", error);
            alert('更新失敗，請稍後再試！');
        });
}

//獲取 memberid
const path = window.location.pathname;
const memberid = path.split('/').pop(); // 從 URL 獲取 memberid

$('#memberShow').click(function (event) {
    console.log('我被點到了');
    event.preventDefault();
    //var memberid = memberid;
    //const memberid = $(this).attr('alt');
    console.log(memberid);
    subMemberid(memberid);  // 提交會員 ID
})





// 確保 memberid 是有效的
if (!memberid || isNaN(memberid)) {
    console.error("Invalid memberid:", memberid);
    alert("Invalid member ID");
} else {
    // 設置連結的 href 屬性
    const link1 = document.getElementById("link1");
    const link2 = document.getElementById("link2");
    const link3 = document.getElementById("link3");
    const link4 = document.getElementById("link4");

    link1.href = `/memberProjectP1/${memberid}`;
    link2.href = `/memberlike/${memberid}`;
    // link3.href 的設置是根據需求而定，你可以取消註解並設置一個正確的 URL
    link3.href = `http://localhost:8080/order_edit`;
    link4.href = `/OrderProjectP1/${memberid}`;
}



// 頁面加載後執行
window.addEventListener('DOMContentLoaded', (event) => {
    const path = window.location.pathname;
    const pathParts = path.split('/');
    const memberid = pathParts[pathParts.length - 1];

    // 如果沒有 memberid 或 memberid 無效，顯示錯誤
    if (!memberid || isNaN(memberid)) {
        console.error("Invalid memberid:", memberid);
        alert("Invalid member ID");
        return;
    }



    const url = `/api/memberOrders/getOrdersByMemberid/${memberid}`; // 根據 memberid 獲取收藏的專案
    fetch(url)
        .then(response => response.json())
        .then(data => {
            //console.log(data); // 檢查 API 返回的資料
            //console.log(data.member); // 檢查 API 返回的資料
            //console.log(data[0].member.picurl); // 訪問陣列中的第一個元素的 member 屬性


            const container = document.getElementById('Card_out');
            container.innerHTML = '';  // 清空現有內容

            // 確保 member 資料存在且包含 picurl
            if (Array.isArray(data) && data.length > 0) {
                const filteredData = data.filter(orderData => orderData.id.memberid > 0);

                if (filteredData.length > 0) {
                    filteredData.forEach(orderData => {

                        // 使用變數抓取對應的資料
                        const order = orderData.order;  // order 資料
                        const member = orderData.member;  // member 資料

                        //console.log(orderData.member); // 檢查 API 返回的資料

                        const memberpicDiv = document.getElementById("icon_test");  // 替換為您的目標 div ID 

                        //const picurl = member.picurl;  // 假設圖片 URL 存在於 member.picurl

                        memberpicDiv.innerHTML = `
							<img  name="picurl" id="icon" src="${member.picurl || '../img/caseImg.jpg'}" width="100%" height="100%" alt="iconimage" onclick="document.getElementById('upload').click();" />                     			
						    `;
                        //<form id="updateIcon" th:action="@{/memberHome/{id}/update(id=${memberid})}" method="post" enctype="multipart/form-data">

                        //<input type="file" id="upload" accept="image/*" style="display:none;" onchange="previewImage()" />

                        //	<button type="button" id="icon_btn" onclick="submitImage('picurl')">更換圖片</button>
                    }
                    )
                }
            }

            if (Array.isArray(data) && data.length > 0) {
                const filteredData = data.filter(orderData => orderData.collected === true);

                if (filteredData.length > 0) {
                    filteredData.forEach(orderData => {

                        // 使用變數抓取對應的資料
                        const order = orderData.order;  // order 資料
                        const member = orderData.member;  // member 資料

                        //console.log(orderData.member); // 檢查 API 返回的資料

                        const memberpicDiv = document.getElementById("icon_test");  // 替換為您的目標 div ID 

                        //const picurl = member.picurl;  // 假設圖片 URL 存在於 member.picurl

                        memberpicDiv.innerHTML = `
							<img  name="picurl" id="icon" src="${member.picurl || '../img/caseImg.jpg'}" width="100%" height="100%" alt="iconimage" onclick="document.getElementById('upload').click();" />                    			
						    `;
                        //<form id="updateIcon" th:action="@{/memberHome/{id}/update(id=${memberid})}" method="post" enctype="multipart/form-data">

                        //<input type="file" id="upload" accept="image/*" style="display:none;" onchange="previewImage()" />

                        //	<button type="button" id="icon_btn" onclick="submitImage('picurl')">更換圖片</button>

                        const svgElement = `
                            <svg class="icon favorite-icon" style="fill: currentcolor; width: 24px; height: 24px; cursor: pointer;" 
                                id="favorite-icon-${order.orderid}" 
                                data-member-id="${member.memberid}" 
                                data-order-id="${order.orderid}" 
                                data-collected="${orderData.collected}">
                                <use href="#icon-favorite-heart"></use>
                            </svg>
                        `;

                        const collectedStatus = orderData.collected ? 'true' : 'false';

                        const card = document.createElement('div');
                        card.classList.add('Card');
                        card.innerHTML = `
						<div id="imgOut">
							<img src="${order.picurl || '../img/caseImg.jpg'}" data-order-id="${order.orderid}" style="width:150px;height:150px;">
						</div>
						    <h4>${order.name}</h4>
						    <p class="title">${order.intro.length > 10 ? order.intro.substring(0, 10) + '...' : order.intro}</p>
						    <div class="card-icon ${collectedStatus}">
						        ${svgElement}
						    </div>
						    <button class="Card_btn">了解詳細</button>
						`;

                        // 等待DOM渲染完成後綁定按鈕點擊事件
                        const cardButton = card.querySelector('.Card_btn');

                        // 綁定按鈕的點擊事件
                        cardButton.addEventListener('click', function () {
                            // 從圖片元素獲取 data-order-id 屬性
                            const orderid = card.querySelector('img').getAttribute('data-order-id');
                            // 使用 orderid 進行跳轉
                            window.location.href = `http://localhost:8080/order_main/${orderid}`; // 假設這是詳細頁面的URL
                        });


                        // 添加收藏狀態的事件監聽器
                        const favoriteIcon = card.querySelector(`#favorite-icon-${order.orderid}`);
                        favoriteIcon.style.fill = orderData.collected ? "red" : "gray";

                        favoriteIcon.addEventListener('click', function () {
                            const currentcollectedStatus = favoriteIcon.getAttribute('data-collected') === 'true';
                            const newcollectedStatus = !currentcollectedStatus;

                            const orderid = parseInt(favoriteIcon.getAttribute('data-order-id'), 10);
                            if (isNaN(orderid)) {
                                console.error("Invalid orderid:", favoriteIcon.getAttribute('data-order-id'));
                                return;
                            }

                            // 發送更新請求，更新收藏狀態
                            fetch(`/api/memberOrders/updateFavoriteStatus`, {
                                method: 'POST',
                                body: JSON.stringify({
                                    memberid: memberid,
                                    orderid: orderid,
                                    collected: newcollectedStatus
                                }),
                                headers: {
                                    'Content-Type': 'application/json'  // 确保请求发送的是JSON格式
                                }
                            })
                                .then(response => {
                                    // 確保回應狀態正常
                                    if (!response.ok) {
                                        throw new Error("Error updating favorite status: " + response.statusText);
                                    }
                                    return response.json();  // 解析回應為 JSON
                                })
                                .then(data => {
                                    if (data.success) {
                                        favoriteIcon.setAttribute('data-collected', newcollectedStatus);
                                        favoriteIcon.style.fill = newcollectedStatus ? "red" : "gray";
                                    } else {
                                        alert('更新收藏狀態失敗: ' + data.message);
                                    }
                                })
                                .catch(error => {
                                    console.error("Error updating favorite status:", error);
                                    alert('更新成功');
                                    location.reload();
                                });

                        });


                        container.appendChild(card);
                    });
                } else {
                    container.innerHTML = '<p>您還沒有收藏任何專案。</p>';
                }
            }
        })
        .catch(error => {
            console.error("Error fetching favorite orders:", error);
        });
});