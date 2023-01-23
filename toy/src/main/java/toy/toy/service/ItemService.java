package toy.toy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import toy.toy.domain.Cart_Item;
import toy.toy.domain.Item;
import toy.toy.repository.ItemRepository;

import java.io.File;
import java.io.IOException;
import java.lang.module.FindException;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final CartService cartService;
    private final CartFinderService cartFinderService;

    public List<Item> allItem() {
        return itemRepository.findAll();
    }

    public Item itemView(Long id) {
        return itemRepository.findById(id).orElseThrow(() ->
                new FindException("찾을 수 없습니다."));
    }

    public void saveItem(Item item, MultipartFile file) throws IOException {
        String path = System.getProperty("user.dir") + "/src/main/resources/static/files";
        UUID uuid = UUID.randomUUID();
        String filename = uuid + "_" + file.getOriginalFilename();
        File save = new File(path, filename);
        file.transferTo(save);

        item.setFilename(filename);
        item.setFilepath("/files/" + filename);
        itemRepository.save(item);
    }

    @Transactional
    public void itemModify(Item item, long id, MultipartFile file) throws IOException {
        String path = System.getProperty("user.dir") + "/src/main/resources/static/files";
        UUID uuid = UUID.randomUUID();
        String filename = uuid + "_" + file.getOriginalFilename();
        File save = new File(path, filename);
        file.transferTo(save);

        Item oldItem = itemRepository.findById(id).orElseThrow(()
        -> new FindException("찾을 수 없는 아이템입니다."));
        oldItem.setFilename(filename);
        oldItem.setFilepath("/files/" + filename);
        oldItem.setName(item.getText());
        oldItem.setPrice(item.getPrice());
        oldItem.setStock(item.getStock());
        itemRepository.save(oldItem);
    }

    @Transactional
    public void itemDelete(long id) {
        List<Cart_Item> items = cartFinderService.findCart_itemByItemId(id);
        for(Cart_Item item : items){
            cartService.deleteCart_item(item.getId());
        }
        itemRepository.deleteById(id);
    }

    public List<Item> itemBySeller(long id) {
        if(itemRepository.findItemByUserIdOrderByCountDesc(id) == null){
            return null;
        }else{
            return itemRepository.findItemByUserIdOrderByCountDesc(id);
        }
    }
}
