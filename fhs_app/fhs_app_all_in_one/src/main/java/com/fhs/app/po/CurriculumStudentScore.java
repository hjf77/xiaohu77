package com.fhs.app.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.core.base.dox.BaseDO;
import lombok.Data;

@Data
@TableName("t_school_curriculum_student_score")
public class CurriculumStudentScore extends BaseDO<CurriculumStudentScore> {
    @TableId
    private String id;

    private String studentId;
    private String curriculumId;
    private Double midtermScore;
    private Double terminalScore;
    private Double attendanceScore;
    private Double workScore;
    private Double resultScore;
    private String status;
}
