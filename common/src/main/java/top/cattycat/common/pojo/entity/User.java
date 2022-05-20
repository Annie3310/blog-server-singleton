package top.cattycat.common.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author 王金义
 * @date 2022/1/7
 */
@Data
@Accessors(chain = true)
@TableName("user")
public class User {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long gId;
    private Date registrationTime;
    private String nickname;
    private Date lastLoginTime;
    private String avatar;
}
