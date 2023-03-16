package com.po;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class FileHelper implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long fid;

    private String fileUrl;

    public FileHelper(String fileUrl){
        this.fileUrl = fileUrl;
    }
}
