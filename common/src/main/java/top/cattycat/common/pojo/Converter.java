package top.cattycat.common.pojo;

import org.springframework.stereotype.Component;
import top.cattycat.common.pojo.entity.Label;
import top.cattycat.common.pojo.vo.LabelVO;

import java.util.List;

/**
 * @author 王金义
 * @date 2021/8/30
 */
@Component
public class Converter {

    private static final String WHITE = "#FFFFFF";
    private static final String BLACK = "#000000";
    /**
     * RPG 之和的一半
     */
    private static final Integer HALF_OF_SUM_OF_RPG = (255 + 255 + 255) /2;

    public static LabelVO labelVO(Label label) {
        final String color = label.getColor();
        return LabelVO.builder()
                .id(label.getId())
                .name(label.getName())
                .color(color)
                .fontColor(getFontColor(color))
                .description(label.getDescription())
                .build();
    }


    public static List<LabelVO> setFontColor(List<LabelVO> labelVOS) {
        for (LabelVO labelVO : labelVOS) {
            labelVO.setFontColor(getFontColor(labelVO.getColor()));
        }
        return labelVOS;
    }

    /**
     * 颜色的 RGB 值取平均值, 如果平均值 < (255 * 3 / 2) 则颜色偏暗, 取白色, 反之
     * @param color 标签颜色
     * @return 文字颜色
     */
    private static String getFontColor(String color) {
        String replace = color.replace("#", "");
        String hex01 = replace.substring(0, 2);
        String hex23 = replace.substring(2, 4);
        String hex45 = replace.substring(4, 6);
        int decimal01 = Integer.parseInt(hex01, 16);
        int decimal23 = Integer.parseInt(hex23, 16);
        int decimal45 = Integer.parseInt(hex45, 16);
        if (decimal01 + decimal23 + decimal45 < HALF_OF_SUM_OF_RPG) {
            return WHITE;
        } else {
            return BLACK;
        }
    }
}
