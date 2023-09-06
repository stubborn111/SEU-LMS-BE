package edu.seu.lms.backend.seulmsbe.file.service.impl;

import edu.seu.lms.backend.seulmsbe.file.entity.File;
import edu.seu.lms.backend.seulmsbe.file.mapper.FileMapper;
import edu.seu.lms.backend.seulmsbe.file.service.IFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author szh
 * @since 2023-09-04
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements IFileService {

}
