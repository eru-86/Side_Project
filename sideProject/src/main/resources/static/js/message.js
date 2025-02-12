/**
 * 
 */

document.querySelectorAll('.collapsible-header').forEach(header => {
	header.addEventListener('click', () => {
		const contentRow = header.nextElementSibling;//後面的下一個 <tr> 元素
		if (contentRow.style.display === '' || contentRow.style.display === 'none') {
			contentRow.style.display = 'table-row'; // 顯示行
        } else {
        	contentRow.style.display = 'none'; // 隱藏行
        }
    });
});

    // 預設隱藏內容行
document.querySelectorAll('.collapsible-content').forEach(row => {
	row.style.display = 'none';
});

document.addEventListener("DOMContentLoaded", () => {
    const deleteButtons = document.querySelectorAll("#del");
    const replyButtons = document.querySelectorAll("#back");

    

    // 回信
    replyButtons.forEach(button => {
        button.addEventListener("click", () => {
            const senderId = button.getAttribute("data-id");
            const content = prompt("請輸入回信內容：");
            if (content) {
                fetch(`/api/messages/submit`, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({
                        receiverId: senderId,
                        content: content,
                    }),
                }).then(response => {
                    if (response.ok) {
                        alert("回信已發送！");
                    } else {
                        alert("回信失敗！");
                    }
                });
            }
        });
    });
});
