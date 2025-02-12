/**
 * 
 */



// 刪除訊息
document.getElementById("del").addEventListener("click", function(e) {
    e.preventDefault(); // 防止表單提交
    const messageId = this.getAttribute('data-messageid');
    
    fetch(`/deleteMessage/${messageId}`, {
        method: 'DELETE',
    })
    .then(response => response.json())  // 這裡解析為 JSON 格式
    .then(data => {
        alert(data.message);  // 顯示後端返回的訊息
        setTimeout(() => {
            window.history.back();  // 或者使用 window.location.href
        }, 500);
    })
    .catch(error => {
        console.error('刪除失敗:', error);
    });
});