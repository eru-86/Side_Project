console.log("memberid:", memberid);  // 輸出 memberid，確認它是否正確
    //const memberid = document.querySelector('meta[name="memberid"]').getAttribute('content');

    //console.log('memberid: ' + memberid);
    
    // 顯示選擇的圖片預覽
    function previewImage() {
        const file = document.getElementById('upload').files[0];
        const reader = new FileReader();

        reader.onloadend = function () {
            document.getElementById('icon').src = reader.result;
        }

        if (file) {
            reader.readAsDataURL(file);
        }
    }

    // 提交圖片更新
    // 提交图片更新
function submitImage() {
    var form = document.getElementById('updateIcon');
    var formData = new FormData(form);

    const base64Image = document.getElementById("icon").src;
    formData.append('picurl', base64Image);
	
    console.log("picurl:", base64Image);
    
    // 从meta标签获取 memberid
    console.log("Member ID:", memberid);  // 确认输出

    var url = `/memberHome/${memberid}/update`;  // 使用 memberid 生成 URL

    fetch(url, {
        method: 'POST',
        body: formData
    })
    .then(response => {
        if (response.ok) {
            alert('圖片更新成功！');
        } else {
            alert('更新失敗，請稍後再試！');
        }
    })
    .catch(error => {
        console.error("錯誤:", error);
        alert('更新失敗，請稍後再試！');
    });
}


    // 提交其他字段更新
    function submitField(fieldName) {
        var form = document.getElementById('updateForm');
        var formData = new FormData(form);

        // 从meta标签获取 memberid
        const memberid = document.querySelector('meta[name="memberid"]').getAttribute('content');
        const url = `/memberHome/${memberid}/update`;  // 使用 memberid 生成 URL

        fetch(url, {
            method: 'POST',
            body: formData
        })
        .then(response => {
            if (response.ok) {
                alert(fieldName + ' 更新成功！');
            } else {
                alert('更新失敗，請稍後再試！');
            }
        })
        .catch(error => {
            console.error("錯誤:", error);
            alert('更新失敗，請稍後再試！');
        });
    }

    // 設置其他頁面功能鏈接
    
	// const link1 = document.getElementById("link1");
	//     const link2 = document.getElementById("link2");
	//     const link3 = document.getElementById("link3");
	//     const link4 = document.getElementById("link4");

	//     link1.href = `/memberProjectP1/${memberid}`;
	//     link2.href = `/memberlike/${memberid}`;
	//     // link3.href 的設置是根據需求而定，你可以取消註解並設置一個正確的 URL
	//     link3.href = `http://localhost:8080/order_edit`;
	//     link4.href = `/OrderProjectP1/${memberid}`;
