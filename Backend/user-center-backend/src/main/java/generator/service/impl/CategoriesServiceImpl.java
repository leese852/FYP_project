package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.Categories;
import generator.service.CategoriesService;
import generator.mapper.CategoriesMapper;
import org.springframework.stereotype.Service;

/**
* @author leese
* @description 针对表【categories】的数据库操作Service实现
* @createDate 2026-01-22 17:36:28
*/
@Service
public class CategoriesServiceImpl extends ServiceImpl<CategoriesMapper, Categories>
    implements CategoriesService{

}




