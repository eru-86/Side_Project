package tw.platform.sideProject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.platform.sideProject.model.Tag;
import tw.platform.sideProject.service.TagService;

@RestController
@RequestMapping("/api/tags")
@CrossOrigin(origins = "*") // 避免 CORS 問題
public class TagController {

	@Autowired
	private TagService tagService;

	// 新增標籤
	@PostMapping("/addTag")
	public String addTag(@RequestBody List<Tag> tags) {
		return tagService.addTags(tags);
	}

	// 更新標籤資料
	@PostMapping("/updateTag/{tagId}")
	public String updateTag(@PathVariable int tagId, @RequestBody Tag tagDetails) {
		try {
			Tag updatedTag = tagService.updateTag(tagId, tagDetails);
			return "標籤更新成功: " + updatedTag.getTagname();
		} catch (RuntimeException e) {
			return e.getMessage();
		}
	}

	// 刪除標籤
	@DeleteMapping("/deleteTag/{tagId}")
	public String deleteTag(@PathVariable int tagId) {
		try {
			tagService.deleteTag(tagId);
			return "標籤刪除成功 (ID: " + tagId + ")";
		} catch (RuntimeException e) {
			return e.getMessage();
		}
	}

	// 取得標籤名字(order)
	@GetMapping("/getTagNames")
	public List<String> getTagNames() {
		return tagService.getTagNames(); // 返回所有的 tagname 列表
	}

	// 取得指定訂單的tag(order)
	@GetMapping("/getTag/{orderId}")
	public List<String> getTagByOrderId(@PathVariable Long orderId) {
		return tagService.getTagByOrderId(orderId); // 返回所有的 tagname 列表
	}

}
