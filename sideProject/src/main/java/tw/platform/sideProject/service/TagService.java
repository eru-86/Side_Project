package tw.platform.sideProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tw.platform.sideProject.model.Tag;
import tw.platform.sideProject.repository.TagRepository;

import java.util.List;

@Service
public class TagService {

	@Autowired
    private TagRepository tagRepository;

    public String addTags(List<Tag> tags) {
    	
    	// 新增技能 (Tag)
        Tag javaTag = new Tag();
//        javaTag.setTagidm(5);
//        javaTag.setTagido(5);
        javaTag.setTagname("JavaScript");
        tagRepository.save(javaTag);

//        Tag springTag = new Tag();
//        springTag.setTagidm(6);
//        springTag.setTagido(6);
//        springTag.setTagname("Python");
//        tagRepository.save(springTag);

        return "標籤新增成功";
    }

    // 更新標籤資料
    @Transactional
    public Tag updateTag(int tagId, Tag tagDetails) {
        return tagRepository.findById(tagId).map(tag -> {
            tag.setTagname(tagDetails.getTagname());
            tag.setTagid(tagDetails.getTagid());
//            tag.setTagido(tagDetails.getTagido());
            return tagRepository.save(tag);
        }).orElseThrow(() -> new RuntimeException("標籤ID不存在 (ID: " + tagId + ")"));
    }

    // 刪除標籤
    @Transactional
    public void deleteTag(int tagId) {
        if (tagRepository.existsById(tagId)) {
            tagRepository.deleteById(tagId);
        } else {
            throw new RuntimeException("標籤ID不存在 (ID: " + tagId + ")");
        }
    }
    
    //取得標籤名字(order)
    @Transactional
    public List<String> getTagNames() {
      return tagRepository.findAllTagname();
    }
    
    //根據orderid取得標籤名字(order)
    @Transactional
    public List<String> getTagByOrderId(Long orderId) {
        return tagRepository.findTagNamesByOrderId(orderId);
    }
}
